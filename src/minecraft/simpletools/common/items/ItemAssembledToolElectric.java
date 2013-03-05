package simpletools.common.items;

import java.util.List;
import java.util.Map;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentThorns;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import simpletools.common.SimpleTools;
import simpletools.common.interfaces.IAssembledTool;
import simpletools.common.interfaces.IAttachment;
import simpletools.common.interfaces.ICore;
import simpletools.common.misc.SimpleToolsCreativeTab;
import universalelectricity.core.electricity.ElectricInfo;
import universalelectricity.core.implement.IItemElectric;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemAssembledToolElectric extends ItemTool implements IItemElectric, IAssembledTool
{
	private final double joulesPerUse = 2000; //in Joules
	
	public ItemAssembledToolElectric(int itemID, String name)
	{
		super(itemID, 0, EnumToolMaterial.EMERALD, new Block[0]);
		this.setItemName(name);
		this.setMaxStackSize(1);
		this.setNoRepair();
		this.setMaxDamage(0);
		this.toolMaterial = null;
		this.setCreativeTab(SimpleToolsCreativeTab.INSTANCE);
		this.setTextureFile(SimpleTools.ITEM_TEXTURES);
	}
	
	@Override
	public double getMaxJoules(Object... data)
	{
		try
		{
			if (data[0] instanceof ItemStack)
			{
				ItemStack var2 = (ItemStack)data[0];
				return var2.stackTagCompound.getCompoundTag("SimpleTools").getDouble("maxEnergy");
			}
		} catch (Exception e) {}
		return 0;
	}
	
	@Override
	public double getVoltage(Object... data)
	{
		try
		{
			if (data[0] instanceof ItemStack)
			{
				ItemStack var2 = (ItemStack)data[0];
				NBTTagCompound info = var2.stackTagCompound.getCompoundTag("SimpleTools");
				ItemStack batteryStack = ItemStack.loadItemStackFromNBT((NBTTagCompound) info);
				if (batteryStack.getItem() instanceof IItemElectric)
					return ((IItemElectric) batteryStack.getItem()).getVoltage(batteryStack);
			}
		} catch (Exception e) {}
		return 0;
	}
	
	@Override
	public double getJoules(Object... data)
	{
		try
		{
			if (data[0] instanceof ItemStack)
			{
				ItemStack var2 = (ItemStack)data[0];
				return var2.stackTagCompound.getCompoundTag("SimpleTools").getDouble("electricity");
			}
		} catch (Exception e) {}
		return 0;
	}
	
	@Override
	public void setJoules(double joules, Object... data)
	{
		try
		{
			if (data[0] instanceof ItemStack)
			{
				ItemStack var2 = (ItemStack)data[0];
				var2.stackTagCompound.getCompoundTag("SimpleTools").setDouble("electricity", joules);
			}
		} catch (Exception e) {}
	}
	
	@Override
	public double onReceive(double amps, double voltage, ItemStack itemStack)
	{
		try
		{
			double rejectedElectricity = Math.max((this.getJoules(itemStack) + ElectricInfo.getJoules(amps, voltage, 1)) - this.getMaxJoules(itemStack), 0);
			this.setJoules(this.getJoules(itemStack) + ElectricInfo.getJoules(amps, voltage, 1) - rejectedElectricity, itemStack);
			return rejectedElectricity;
		} catch (Exception e) 
		{
			return ElectricInfo.getJoules(amps, voltage, 1);
		}
	}
	
	@Override
	public double onUse(double joulesNeeded, ItemStack itemStack)
	{
		double electricityToUse = Math.min(this.getJoules(itemStack), joulesNeeded);
		this.setJoules(this.getJoules(itemStack) - electricityToUse, itemStack);
		return electricityToUse;
	}
	
	@Override
	public boolean canReceiveElectricity()
	{
		return true;
	}
	
	@Override
	public boolean canProduceElectricity()
	{
		return false;
	}
	
	/**
	 * @param attachment The attachment, passed in as an ItemStack
	 * @param core The core, passed in as an ItemStack
	 * @param storage The battery, fuelTank, etc passed in as an ItemStack
	 * @return the resultant Item, as an ItemStack. null if it failed.
	 */
	@Override
	public ItemStack onCreate(ItemStack attachment, ItemStack core, ItemStack battery)
	{
		ItemStack returnStack = null;
		if (attachment.getItem() instanceof IAttachment && core.getItem() instanceof ICore)
		{
			IAttachment attachmentTemp = (IAttachment) attachment.getItem();
			ICore coreTemp = (ICore) core.getItem();
			
			if (coreTemp.getCoreType(core) == attachmentTemp.getToolAttachmentType(attachment))
			{
				if (coreTemp.getCoreTier(core) >= attachmentTemp.getMinimumTier(attachment))
				{
					int coreMeta = coreTemp.getCoreTier(core) * 1000;
					int attachMeta = attachmentTemp.getAttachmentUID(attachment);
					int meta = coreMeta + attachMeta;
					returnStack = new ItemStack(this, 1, meta);
					
					NBTTagCompound compound = new NBTTagCompound();
					compound.setCompoundTag("SimpleTools", new NBTTagCompound());
					compound.setCompoundTag("damageVsEntity", new NBTTagCompound());
					compound.setBoolean("useDamageVsEntityTag", true);
					
					compound.getCompoundTag("SimpleTools").setDouble("electricity", ((IItemElectric)battery.getItem()).getJoules(battery));
					compound.getCompoundTag("SimpleTools").setDouble("maxEnergy", ((IItemElectric)battery.getItem()).getMaxJoules(battery));
					compound.getCompoundTag("SimpleTools").setCompoundTag("attachment", attachment.writeToNBT(new NBTTagCompound()));
					compound.getCompoundTag("SimpleTools").setCompoundTag("battery", battery.writeToNBT(new NBTTagCompound()));
					
					compound.getCompoundTag("damageVsEntity").setInteger("", attachmentTemp.getDamageVsEntities(attachment).get(null));
					
					returnStack.setTagCompound(compound);
				}
			}
		}
		return returnStack;
	}
	
	@Override
	public float getStrVsBlock(ItemStack itemStack, Block block, int meta)
	{	
		ItemStack attachment = this.getAttachment(itemStack);
		String toolClass = ((IAttachment)attachment.getItem()).getToolType(attachment);
		int toolLevel = ((IAttachment)attachment.getItem()).getAttachmentTier(attachment);
		int blockLevel = MinecraftForge.getBlockHarvestLevel(block, meta, toolClass);
		
		if (toolLevel >= blockLevel)
			return (float) ((IAttachment)attachment.getItem()).getHarvestSpeed(attachment);
		else
			return 1f;
		
	}
	
	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player, List currentTips, boolean advancedToolTips)
	{
		ItemStack attachment = null, battery = null;
		try
		{
			NBTTagCompound batt = itemStack.getTagCompound().getCompoundTag("SimpleTools").getCompoundTag("battery");
			battery = ItemStack.loadItemStackFromNBT((NBTTagCompound) batt);
			
			NBTBase attach = itemStack.getTagCompound().getCompoundTag("SimpleTools").getCompoundTag("attachment");
			attachment = ItemStack.loadItemStackFromNBT((NBTTagCompound) attach);
		}
		catch (Exception e) {}
		
		currentTips.add("Energy: " + this.getJoules(itemStack) + " / " + this.getMaxJoules(itemStack));
		
		//if (advancedToolTips)
		{
			if (this.getCore(itemStack) != null)
				currentTips.add("Core Module: " + this.getCore(itemStack).getDisplayName());
			if (attachment != null)
				currentTips.add("Tool Module: " + attachment.getDisplayName());
			if (battery != null)
				currentTips.add("Batt Module: " + battery.getDisplayName());
		}
	}
	
	public boolean onItemUse(ItemStack i)
	{
		if (this.getJoules(i) >= this.joulesPerUse)
		{
			this.setJoules(this.getJoules(i) - this.joulesPerUse, i);
			return true;
		}
		return false;
	}
	
	@Override
	public int getItemEnchantability()
	{
		return 0;
	}
	
	@Override
	public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack)
	{
		return false;
	}
	
	@Override
	public boolean onBlockDestroyed(ItemStack par1ItemStack, World par2World, int id, int x, int y, int z, EntityLiving par7EntityLiving)
	{
		double blockHardness = (double)Block.blocksList[id].getBlockHardness(par2World, x, y, z);
		if (blockHardness != 0D && this.canBreakBlock(par1ItemStack, par2World, id, x, y, z, par7EntityLiving))
		{
			int fortune = this.getEnchantmentLvl(Enchantment.fortune, par1ItemStack);
			
			if(this.getEnchantmentLvl(Enchantment.silkTouch, par1ItemStack) >= 0)
			{
				this.dropBlockAsItem_do(par2World, x, y, z, new ItemStack(Block.blocksList[id], 1, par2World.getBlockMetadata(x, y, z)));
			}
			else
			{
				Block.blocksList[id].dropBlockAsItem(par2World, x, y, z, par2World.getBlockMetadata(x, y, z), fortune);
			}
			return this.onItemUse(par1ItemStack);
		}
		return true;
	}
	
	@Override
	public ItemStack getCore(ItemStack assembledTool) 
	{
		int tier = assembledTool.getItemDamage() / 1000;
		return new ItemStack(SimpleTools.coreMechElectric, 1, tier);
	}
	
	@Override
	public ItemStack getStorage(ItemStack assembledTool) 
	{
		ItemStack battery = null;
		try
		{
			NBTTagCompound batt = assembledTool.getTagCompound().getCompoundTag("SimpleTools").getCompoundTag("battery");
			battery = ItemStack.loadItemStackFromNBT((NBTTagCompound) batt);
			((IItemElectric)battery.getItem()).setJoules(this.getJoules(assembledTool), battery);
		}
		catch(Throwable e) {}
		return battery;
	}
	
	@Override
	public ItemStack getAttachment(ItemStack assembledTool) 
	{
		ItemStack attachment = null;
		try
		{
			NBTBase attach = assembledTool.getTagCompound().getCompoundTag("SimpleTools").getCompoundTag("attachment");
			attachment = ItemStack.loadItemStackFromNBT((NBTTagCompound) attach);
			if (!assembledTool.getEnchantmentTagList().equals(null))
			{
				attachment.setTagCompound(new NBTTagCompound());
				attachment.getTagCompound().setTag(assembledTool.getEnchantmentTagList().getName(), assembledTool.getEnchantmentTagList());
			}
		}
		catch(Throwable e) {}
		return attachment;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean requiresMultipleRenderPasses()
	{
		return true;
	}
	
	@Override
	public int getIconIndex(ItemStack assembledTool, int pass)
	{
		//	pass: 0 is attachment, 1 is core
		if (pass == 0)
		{
			try
			{
				int attachMeta = ((IAssembledTool)assembledTool.getItem()).getAttachment(assembledTool).getItemDamage();
				return ((IAssembledTool)assembledTool.getItem()).getAttachment(assembledTool).getItem().getIconFromDamage(attachMeta);
			}
			catch (Throwable e) {}
		}
		else if (pass == 1)
		{
			try
			{
				int coreMeta = ((IAssembledTool)assembledTool.getItem()).getAttachment(assembledTool).getItemDamage();
				return ((IAssembledTool)assembledTool.getItem()).getCore(assembledTool).getItem().getIconFromDamage(coreMeta);
			}
			catch (Throwable e) {}
		}
		return 255;
	}
	
	private boolean canBreakBlock(ItemStack par1ItemStack, World par2World, int par3, int par4, int par5, int par6, EntityLiving par7EntityLiving) 
	{
		Block block = Block.blocksList[par3];
		int metadata = par2World.getBlockMetadata(par4, par5, par6);
		ItemStack attachment = this.getAttachment(par1ItemStack);
		String toolClass = ((IAttachment)attachment.getItem()).getToolType(attachment);
		int toolLevel = ((IAttachment)attachment.getItem()).getAttachmentTier(attachment);
		int blockLevel = MinecraftForge.getBlockHarvestLevel(block, metadata, toolClass);
		return toolLevel >= blockLevel;
	}
	
	/**
	 * @param enchant The enchantment to check for.
	 * @param assembledTool The ItemStack to check on. 
	 * @return The level of the enchantment. -1 if the enchantment is not on the given tool
	 */
	public int getEnchantmentLvl(Enchantment enchant, ItemStack assembledTool)
	{
		int level = -1;
		if (assembledTool.getEnchantmentTagList() != null)
		{
			for (int i = 0; i < assembledTool.getEnchantmentTagList().tagCount(); i++)
			{
				if (assembledTool.getEnchantmentTagList().tagAt(i) instanceof NBTTagCompound)
				{
					if (((NBTTagCompound)assembledTool.getEnchantmentTagList().tagAt(i)).getShort("id") == enchant.effectId)
						level = ((NBTTagCompound)assembledTool.getEnchantmentTagList().tagAt(i)).getShort("lvl");
				}
			}
		}
		return level;
	}
	
	protected void dropBlockAsItem_do(World par1World, int par2, int par3, int par4, ItemStack par5ItemStack)
	{
		if (!par1World.isRemote && par1World.getGameRules().getGameRuleBooleanValue("doTileDrops"))
		{
			float var6 = 0.7F;
			double var7 = (double)(par1World.rand.nextFloat() * var6) + (double)(1.0F - var6) * 0.5D;
			double var9 = (double)(par1World.rand.nextFloat() * var6) + (double)(1.0F - var6) * 0.5D;
			double var11 = (double)(par1World.rand.nextFloat() * var6) + (double)(1.0F - var6) * 0.5D;
			EntityItem var13 = new EntityItem(par1World, (double)par2 + var7, (double)par3 + var9, (double)par4 + var11, par5ItemStack);
			var13.delayBeforeCanPickup = 10;
			par1World.spawnEntityInWorld(var13);
		}
	}
	
	@Override
	public boolean hitEntity(ItemStack par1ItemStack, EntityLiving par2EntityLiving, EntityLiving par3EntityLiving)
	{
		if (par1ItemStack.getTagCompound().hasKey("damageVsEntity"))
		{
			if (!par1ItemStack.getTagCompound().getCompoundTag("damageVsEntity").hasKey(par2EntityLiving.getEntityName()));
			{
				BiMap<Entity, Integer> damageMap = HashBiMap.create();
				if (damageMap.containsKey(par2EntityLiving))
				{
					int damage = damageMap.get(par2EntityLiving);
					par1ItemStack.getTagCompound().getCompoundTag("damageVsEntity").setInteger(par2EntityLiving.getEntityName(), damage);
				}
			}
		}
		if (this.onItemUse(par1ItemStack))
			return true;
		else
			return false;
	}
}
