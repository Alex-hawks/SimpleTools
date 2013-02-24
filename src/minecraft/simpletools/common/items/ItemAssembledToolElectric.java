package simpletools.common.items;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.*;
import net.minecraft.world.World;
import simpletools.common.SimpleTools;
import simpletools.common.interfaces.IAssembledTool;
import simpletools.common.interfaces.IAttachment;
import simpletools.common.interfaces.ICore;
import simpletools.common.misc.SimpleToolsCreativeTab;
import universalelectricity.core.electricity.ElectricInfo;
import universalelectricity.core.implement.IItemElectric;
import universalelectricity.core.implement.IVoltage;
import universalelectricity.prefab.ItemElectric;

public class ItemAssembledToolElectric extends ItemTool implements IItemElectric, IAssembledTool
{
	private final double joulesPerUse = 2000; //in Joules
	
	public ItemAssembledToolElectric(int itemID, String name)
	{
		super(itemID, 0, EnumToolMaterial.EMERALD, new Block[0]);
		this.damageVsEntity = 1;
		this.setItemName(name);
		this.setMaxStackSize(1);
		this.setNoRepair();
		this.setIconIndex(0);
		this.setCreativeTab(SimpleToolsCreativeTab.INSTANCE);
	}

	@Override
	public double getMaxJoules(Object... data)
	{
		try
		{
			if (data[0] instanceof ItemStack)
			{
				ItemStack var2 = (ItemStack)data[0];
				NBTBase info = var2.stackTagCompound.getTagList("SimpleTools").tagAt(3);
				if (info instanceof NBTTagDouble)
					return ((NBTTagDouble)info).data;
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
				NBTBase info = var2.stackTagCompound.getTagList("SimpleTools").tagAt(1);
				if (info instanceof NBTTagCompound)
				{
					ItemStack batteryStack = ItemStack.loadItemStackFromNBT((NBTTagCompound) info);
					if (batteryStack.getItem() instanceof IItemElectric)
						return ((IItemElectric) batteryStack.getItem()).getVoltage(batteryStack);
				}
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
				NBTBase info = var2.stackTagCompound.getTagList("SimpleTools").tagAt(2);
				if (info instanceof NBTTagDouble)
					return ((NBTTagDouble)info).data;
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
				NBTBase info = var2.stackTagCompound.getTagList("SimpleTools").tagAt(2);
				if (info instanceof NBTTagDouble)
					((NBTTagDouble)info).data = joules;
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
					
					if (!returnStack.hasTagCompound())
						returnStack.setTagCompound(new NBTTagCompound());
					if (!returnStack.stackTagCompound.hasKey("SimpleTools"))
						returnStack.stackTagCompound.setTag("SimpleTools", new NBTTagList("SimpleTools"));
					
					NBTTagList info = returnStack.stackTagCompound.getTagList("SimpleTools");
					
					//	all ItemStacks are stored as new int[] {itemID, item metadata}
					//	stackSize is not needed, as it is always one
					
					NBTTagCompound attach = attachment.writeToNBT(new NBTTagCompound("attachment"));
					NBTTagCompound batt = battery.writeToNBT(new NBTTagCompound("battery"));
					NBTTagDouble joules = new NBTTagDouble("electricity", ((IItemElectric)battery.getItem()).getJoules(battery));
					NBTTagDouble maxJoules = new NBTTagDouble("maxEnergy", ((IItemElectric)battery.getItem()).getMaxJoules(battery));
					
					info.appendTag(attach);
					info.appendTag(batt);
					info.appendTag(joules);
					info.appendTag(maxJoules);
				}
			}
		}
		return returnStack;
	}

	@Override
	public float getStrVsBlock(ItemStack itemStack, Block block)
	{
		return 1f;
	}

	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player, List currentTips, boolean advancedToolTips)
	{
		ItemStack attachment = null, battery = null;
		try
		{
			NBTBase batt = itemStack.getTagCompound().getTagList("SimpleTools").tagAt(1);
			if (batt instanceof NBTTagCompound)
				battery = ItemStack.loadItemStackFromNBT((NBTTagCompound) batt);
			
			NBTBase attach = itemStack.getTagCompound().getTagList("SimpleTools").tagAt(0);
			if (attach instanceof NBTTagIntArray)
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
	public boolean onBlockDestroyed(ItemStack par1ItemStack, World par2World, int par3, int par4, int par5, int par6, EntityLiving par7EntityLiving)
	{
		double blockHardness = (double)Block.blocksList[par3].getBlockHardness(par2World, par4, par5, par6);
		if (blockHardness != 0D)
		{
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

}
