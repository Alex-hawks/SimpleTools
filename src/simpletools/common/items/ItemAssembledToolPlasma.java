package simpletools.common.items;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

import org.lwjgl.input.Keyboard;

import simpletools.common.SimpleTools;
import simpletools.common.interfaces.IAssembledTool;
import simpletools.common.interfaces.IAttachment;
import simpletools.common.interfaces.ICore;
import simpletools.common.interfaces.IPlasmaStorage;
import universalelectricity.core.electricity.ElectricityDisplay;
import universalelectricity.core.electricity.ElectricityDisplay.ElectricUnit;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.core.item.IItemElectric;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemAssembledToolPlasma extends Item implements IItemElectric, IAssembledTool, IPlasmaStorage
{
	private static final double JOULES_PER_USE = 2500;
	private static final int PLASMA_PER_USE = 1;
	
	public ItemAssembledToolPlasma(int par1, String name)
	{
		super(par1);
		this.setUnlocalizedName(name);
		this.setMaxStackSize(1);
		this.setNoRepair();
		this.setMaxDamage(0);
		this.setCreativeTab(null);
	}
	
	@Override
	public ItemStack onCreate(ItemStack attachment, ItemStack core, ItemStack storage)
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
					
					compound.getCompoundTag("SimpleTools").setDouble("electricity", ((IItemElectric)storage.getItem()).getJoules(storage));
					compound.getCompoundTag("SimpleTools").setDouble("maxEnergy", ((IItemElectric)storage.getItem()).getMaxJoules(storage));
					compound.getCompoundTag("SimpleTools").setInteger("plasma", Integer.MAX_VALUE);
					compound.getCompoundTag("SimpleTools").setInteger("maxPlasma", (coreTemp.getCoreTier(core) + 1) * 125);
					compound.getCompoundTag("SimpleTools").setCompoundTag("attachment", attachment.writeToNBT(new NBTTagCompound()));
					compound.getCompoundTag("SimpleTools").setCompoundTag("battery", storage.writeToNBT(new NBTTagCompound()));
					
					compound.getCompoundTag("damageVsEntity").setInteger("", attachmentTemp.getDamageVsEntities(attachment).get(null));
					
					returnStack.setTagCompound(compound);
				}
			}
		}
		return returnStack;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	@SuppressWarnings({ "unchecked", "rawtypes" })
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
		
		currentTips.add("Energy: " + ElectricityDisplay.getDisplayShort(this.getJoules(itemStack), ElectricUnit.JOULES) 
				+ " / " + ElectricityDisplay.getDisplayShort(this.getMaxJoules(itemStack), ElectricUnit.JOULES));
		currentTips.add("Plasma: " + this.getPlasma(itemStack) + " / " + this.getMaxPlasma(itemStack));
		
		if (Keyboard.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindSneak.keyCode))
		{
			if (this.getCore(itemStack) != null)
				currentTips.add("Core Module: " + this.getCore(itemStack).getDisplayName());
			if (attachment != null)
				currentTips.add("Tool Module: " + attachment.getDisplayName());
			if (battery != null)
				currentTips.add("Batt Module: " + battery.getDisplayName());
		}
		else
		{
			currentTips.add("Hold Sneak for more information");
		}	
	}

	
	@Override
	public ItemStack getCore(ItemStack assembledTool) 
	{
		int tier = assembledTool.getItemDamage() / 1000;
		return new ItemStack(SimpleTools.corePlasma, 1, tier);
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
	public boolean canDoWork(ItemStack assembledTool)
	{
		return this.getJoules(assembledTool) >= JOULES_PER_USE && this.getPlasma(assembledTool) >= PLASMA_PER_USE;
	}
	
	@Override
	public boolean canBreakBlock(ItemStack assembledTool, World world, int blockID, int metadata, EntityLiving entity)
	{
		return false;
	}
	
	@Override
	public boolean isEffectiveOnBlock(ItemStack assembledTool, World world, int blockID, int metadata, EntityLiving entity)
	{
		return false;
	}
	
	@Override
	public String getStoredForDisplay(ItemStack assembledTool)
	{
		String toReturn = "";
		
		String currEnergy = ElectricityDisplay.getDisplayShort(this.getJoules(assembledTool), ElectricityDisplay.ElectricUnit.JOULES);
		String maxEnergy = ElectricityDisplay.getDisplayShort(this.getMaxJoules(assembledTool), ElectricityDisplay.ElectricUnit.JOULES);
		String currPlasma = this.getPlasma(assembledTool) + "";
		String maxPlasma = this.getMaxPlasma(assembledTool) + "";
		
		if (this.getJoules(assembledTool) != Double.POSITIVE_INFINITY)
			toReturn += currEnergy + " of " + maxEnergy + "  /  ";
		else
			toReturn += "Infinite  /  ";
		
		if (this.getPlasma(assembledTool) != Integer.MAX_VALUE)
			toReturn += currPlasma + " of " + maxPlasma;
		else 
			toReturn += "Infinite";
		
		return toReturn;
	}
	
	@Override
	public double getJoules(ItemStack itemStack)
	{
		try
		{
			return itemStack.stackTagCompound.getCompoundTag("SimpleTools").getDouble("electricity");
		}
		catch (Exception e) {return 0D;}
	}
	
	@Override
	public void setJoules(double joules, ItemStack itemStack)
	{
		itemStack.stackTagCompound.getCompoundTag("SimpleTools").setDouble("electricity", joules);
	}
	
	@Override
	public double getMaxJoules(ItemStack itemStack)
	{
		try
		{
			return itemStack.stackTagCompound.getCompoundTag("SimpleTools").getDouble("maxEnergy");
		}
		catch (Exception e) {return 0D;}
	}
	
	@Override
	public double getVoltage(ItemStack itemStack)
	{
		try
		{
			NBTTagCompound info = itemStack.stackTagCompound.getCompoundTag("SimpleTools").getCompoundTag("battery");
			ItemStack batteryStack = ItemStack.loadItemStackFromNBT((NBTTagCompound) info);
			return ((IItemElectric) batteryStack.getItem()).getVoltage(batteryStack);
		}
		catch (Exception e) {return 35D;}
	}
	
	@Override
	public ElectricityPack onReceive(ElectricityPack electricityPack, ItemStack itemStack)
	{
		try
		{
			double rejectedElectricity = Math.max((this.getJoules(itemStack) + electricityPack.getWatts()) - this.getMaxJoules(itemStack), 0);
			double joulesToStore = electricityPack.getWatts() - rejectedElectricity;
			this.setJoules(this.getJoules(itemStack) + joulesToStore, itemStack);
			return ElectricityPack.getFromWatts(joulesToStore, this.getVoltage(itemStack));
		} catch (Exception e) 
		{
			return electricityPack;
		}
	}
	
	@Override
	public ElectricityPack onProvide(ElectricityPack electricityPack, ItemStack itemStack)
	{
		return new ElectricityPack(0,0);
	}
	
	@Override
	public ElectricityPack getReceiveRequest(ItemStack itemStack)
	{
		try
		{
			return ElectricityPack.getFromWatts(this.getMaxJoules(itemStack) - this.getJoules(itemStack), this.getVoltage(itemStack));
		}
		catch (Exception e) {return new ElectricityPack(0,0);}
	}
	
	@Override
	public ElectricityPack getProvideRequest(ItemStack itemStack)
	{
		return new ElectricityPack(0,0);
	}
	
	@Override
	public int getPlasma(ItemStack assembledTool)
	{
		try
		{
			return assembledTool.stackTagCompound.getCompoundTag("SimpleTools").getInteger("plasma");
		}
		catch (Exception e) {return 0;}
	}
	
	@Override
	public int getMaxPlasma(ItemStack assembledTool)
	{
		try
		{
			return assembledTool.stackTagCompound.getCompoundTag("SimpleTools").getInteger("maxPlasma");
		}
		catch (Exception e) {return 0;}
	}
	
	@Override
	public void setPlasma(ItemStack stack, int i)
	{
		try
		{
			stack.stackTagCompound.getCompoundTag("SimpleTools").setInteger("plasma", i);
		}
		catch (Exception e)
		{
			
		}
	}
	
	public boolean onItemUse(ItemStack i)
	{
		if (this.canDoWork(i))
		{
			this.setJoules(this.getJoules(i) - JOULES_PER_USE, i);
			return true;
		}
		return false;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean requiresMultipleRenderPasses()
	{
		return true;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(ItemStack stack, int pass)
	{
		//	pass: 0 is core, 1 is attachment
		if (pass == 0)
		{
			try
			{
				int coreMeta = ((IAssembledTool)stack.getItem()).getCore(stack).getItemDamage();
				return ((IAssembledTool)stack.getItem()).getCore(stack).getItem().getIconFromDamage(coreMeta);
			}
			catch (Throwable e) {}
		}
		else if (pass == 1)
		{
			try
			{
				int attachMeta = ((IAssembledTool)stack.getItem()).getAttachment(stack).getItemDamage();
				return ((IAssembledTool)stack.getItem()).getAttachment(stack).getItem().getIconFromDamage(attachMeta);
			}
			catch (Throwable e) {}
		}
		return null;
	}
	
	@Override
	public boolean itemInteractionForEntity(ItemStack itemstack, EntityLiving entity)
	{
		IAttachment attach = (IAttachment) this.getAttachment(itemstack).getItem();
		if (attach.canRightClick(this.getAttachment(itemstack), entity, null) && this.canDoWork(itemstack))
		{
			attach.onRightClick(this.getAttachment(itemstack), entity, null);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean addPlasma(ItemStack stack, int i)
	{
		if (this.getPlasma(stack) + i <= this.getMaxPlasma(stack))
		{
			this.setPlasma(stack, this.getPlasma(stack) + i);
			return true;
		}
		else 
			return false;
	}
	
	@Override
	public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float HitY, float hitZ)
	{
		IAttachment attach = (IAttachment) this.getAttachment(itemStack).getItem();
		boolean canUse = attach.canRightClick(this.getAttachment(itemStack), new Object[] {world, x, y, z, side, hitX, HitY, hitZ }, player) && this.canDoWork(itemStack);
		
		if (canUse && this.canDoWork(itemStack))
		{
			byte plasma = attach.onRightClick(this.getAttachment(itemStack), new Object[] {world, x, y, z, side, hitX, HitY, hitZ }, player);
			this.setJoules(this.getJoules(itemStack) - JOULES_PER_USE, itemStack);
			this.setPlasma(itemStack, this.getPlasma(itemStack) + plasma);
		}
		return canUse;
	}
	
}
