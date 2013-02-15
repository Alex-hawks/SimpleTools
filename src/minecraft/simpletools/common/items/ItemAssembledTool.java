package simpletools.common.items;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import simpletools.common.interfaces.IAttachment;
import simpletools.common.interfaces.ICore;
import simpletools.common.misc.SimpleToolsCreativeTab;
import universalelectricity.core.electricity.ElectricInfo;
import universalelectricity.core.implement.IItemElectric;
import universalelectricity.prefab.ItemElectric;

public class ItemAssembledTool extends ItemTool implements IItemElectric
{
	
	public ItemAssembledTool(int itemID, String name)
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
				NBTTagCompound info = var2.stackTagCompound;
				return info.getDouble("maxEnergy");
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
				NBTTagCompound info = var2.stackTagCompound;
				return ((IItemElectric)Item.itemsList[info.getIntArray("battery")[0]]).getVoltage(data);
			}
		} catch (Exception e) {}
		return 0;
	}

	public double getMaxPlasma(Object... data)
	{
		try
		{
			if (data[0] instanceof ItemStack)
			{
				ItemStack var2 = (ItemStack)data[0];
				int[] core = var2.stackTagCompound.getIntArray("core");
				return ((ICore)Item.itemsList[core[0]]).getMaxAltFuel(new ItemStack(core[0], 1, core[1]));
			}
		} catch (Exception e) {}
		return 0;
	}

	public double getStoredPlasma(Object... data)
	{
		try
		{
			if (data[0] instanceof ItemStack)
			{
				ItemStack var2 = (ItemStack)data[0];
				int[] core = var2.stackTagCompound.getIntArray("core");
				if (((ICore)Item.itemsList[core[0]]).getCoreFinerType(new ItemStack(core[0], 1, core[1])) == "plasma")
				{
					return var2.stackTagCompound.getDouble("plasma");
				}
				else return 0;
			}
		} catch (Exception e) {}
		return 0;
	}
	
	public void setStoredPlasma(double plasma, Object... data)
	{
		try
		{
			if (data[0] instanceof ItemStack)
			{
				ItemStack var2 = (ItemStack)data[0];
				int[] core = var2.stackTagCompound.getIntArray("core");
				if (((ICore)Item.itemsList[core[0]]).getCoreFinerType(new ItemStack(core[0], 1, core[1])) == "plasma")
				{
					var2.stackTagCompound.setDouble("plasma", plasma);
				}
			}
		} catch (Exception e) {}
	}

	public double getMaxFuel(Object... data)
	{
		try
		{
			if (data[0] instanceof ItemStack)
			{
				ItemStack var2 = (ItemStack)data[0];
				int[] core = var2.stackTagCompound.getIntArray("core");
				return ((ICore)Item.itemsList[core[0]]).getMaxAltFuel(new ItemStack(core[0], 1, core[1]));
			}
		} catch (Exception e) {}
		return 0;
	}

	public double getStoredFuel(Object... data)
	{
		try
		{
			if (data[0] instanceof ItemStack)
			{
				ItemStack var2 = (ItemStack)data[0];
				int[] core = var2.stackTagCompound.getIntArray("core");
				if (((ICore)Item.itemsList[core[0]]).getCoreFinerType(new ItemStack(core[0], 1, core[1])) == "fuel")
				{
					return var2.stackTagCompound.getDouble("fuelStored");
				}
				else return 0;
			}
		} catch (Exception e) {}
		return 0;
	}
	
	public void setStoredFuel(double fuel, Object... data)
	{
		try
		{
			if (data[0] instanceof ItemStack)
			{
				ItemStack var2 = (ItemStack)data[0];
				int[] core = var2.stackTagCompound.getIntArray("core");
				if (((ICore)Item.itemsList[core[0]]).getCoreFinerType(new ItemStack(core[0], 1, core[1])) == "fuel")
				{
					var2.stackTagCompound.setDouble("fuel", fuel);
				}
			}
		} catch (Exception e) {}

	}

	/**
	 * @param attachment The attachment, passed in as an ItemStack
	 * @param core The core, passed in as an ItemStack
	 * @param storage The battery, fuelTank, etc passed in as an ItemStack
	 * @return the resultant Item, as an ItemStack. null if it failed.
	 */
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
					int coreMeta = ((int)coreTemp.getCoreUID(core)) * 1000;
					int attachMeta = attachmentTemp.getAttachmentUID(attachment);
					int meta = coreMeta + attachMeta;
					returnStack = new ItemStack(this, 1, meta);

					returnStack.setTagCompound(new NBTTagCompound());
					NBTTagCompound info = returnStack.stackTagCompound;
					//	all ItemStacks are stored as new int[] {itemID, item metadata}
					//	stackSize is not needed, as it is always one

					info.setIntArray("attachment", new int[] {attachment.itemID, attachment.getItemDamage()});
					info.setIntArray("core", new int[] {core.itemID, core.getItemDamage()});

					if (coreTemp.requiresElectricity(core) && storage.getItem() instanceof IItemElectric && ((IItemElectric)storage.getItem()).canProduceElectricity())
					{
						info.setIntArray("battery", new int[] {storage.itemID, storage.getItemDamage()});
						info.setDouble("electricity", ((IItemElectric)storage.getItem()).getJoules(storage));
						info.setDouble("maxEnergy", ((IItemElectric)storage.getItem()).getMaxJoules(storage));

						if (coreTemp.getCoreType(core) == 1)
						{
							info.setDouble("plasma", 0);
						}
					}
					if (coreTemp.getCoreFinerType(core) == "fuel")
					{
						info.setIntArray("fuelTank", new int[] {storage.itemID, storage.getItemDamage()});
						info.setDouble("fuelStored", 0);
					}
				}
			}
		}
		return returnStack;
	}

	@Override
	public double getJoules(Object... data)
	{
		try
		{
			if (data[0] instanceof ItemStack)
			{
				ItemStack var2 = (ItemStack)data[0];
				int[] core = var2.stackTagCompound.getIntArray("core");
				if (((ICore)Item.itemsList[core[0]]).requiresElectricity(new ItemStack(core[0], 1, core[1])))
				{
					return var2.stackTagCompound.getDouble("electricity");
				}
				else return 0;
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
				int[] core = var2.stackTagCompound.getIntArray("core");
				if (((ICore)Item.itemsList[core[0]]).requiresElectricity(new ItemStack(core[0], 1, core[1])))
				{
					var2.stackTagCompound.setDouble("electricity", joules);
				}
			}
		} catch (Exception e) {}
	}

	@Override
	public double onReceive(double amps, double voltage, ItemStack itemStack)
	{
		try
		{
			int[] core = itemStack.stackTagCompound.getIntArray("core");
			if (((ICore)Item.itemsList[core[0]]).requiresElectricity(new ItemStack(core[0], 1, core[1])))
			{
				double rejectedElectricity = Math.max((this.getJoules(itemStack) + ElectricInfo.getJoules(amps, voltage, 1)) - this.getMaxJoules(itemStack), 0);
				this.setJoules(this.getJoules(itemStack) + ElectricInfo.getJoules(amps, voltage, 1) - rejectedElectricity, itemStack);
				return rejectedElectricity;
			}
			return ElectricInfo.getJoules(amps, voltage, 1);
		} catch (Exception e) 
		{
			return ElectricInfo.getJoules(amps, voltage, 1);
		}
	}

	@Override
	public double onUse(double joulesNeeded, ItemStack itemStack)
	{
		int[] core = itemStack.stackTagCompound.getIntArray("core");
		if (((ICore)Item.itemsList[core[0]]).requiresElectricity(new ItemStack(core[0], 1, core[1])))
		{
			double electricityToUse = Math.min(this.getJoules(itemStack), joulesNeeded);
			this.setJoules(this.getJoules(itemStack) - electricityToUse, itemStack);
			return electricityToUse;
		}
		return 0d;
	}

	@Override
	public boolean canReceiveElectricity()
	{
/*
		int[] core = itemStack.stackTagCompound.getIntArray("core");
		if (((ICore)Item.itemsList[core[0]]).requiresElectricity(new ItemStack(core[0], 1, core[1])))
			return ((ICore)core.getItem()).getCoreFinerType(this.core) == "electric";
*/
		return false;
	}

	@Override
	public boolean canProduceElectricity()
	{
		return false;
	}

	@Override
	public float getStrVsBlock(ItemStack itemStack, Block block)
	{
		return 1f;
	}

	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player, List currentTips, boolean advancedToolTips)
	{
		ItemStack core = null, attachment = null, battery = null, fuelTank = null;
		try 
		{
			NBTTagCompound info = itemStack.stackTagCompound;
			int[] coreIntArray = info.getIntArray("core");
			core = new ItemStack(coreIntArray[0], 1, coreIntArray[1]);
			int[] attachmentIntArray = info.getIntArray("attachment");
			attachment = new ItemStack(attachmentIntArray[0], 1, attachmentIntArray[1]);
			int[] batteryIntArray = info.getIntArray("battery");
			battery = new ItemStack(batteryIntArray[0], 1, batteryIntArray[1]);
		} catch (Exception e)
		{
			try 
			{
				NBTTagCompound info = itemStack.stackTagCompound;
				int[] coreIntArray = info.getIntArray("core");
				core = new ItemStack(coreIntArray[0], 1, coreIntArray[1]);
				int[] attachmentIntArray = info.getIntArray("attachment");
				attachment = new ItemStack(attachmentIntArray[0], 1, attachmentIntArray[1]);
				int[] fuelTankIntArray = info.getIntArray("fuelTank");
				fuelTank = new ItemStack(fuelTankIntArray[0], 1, fuelTankIntArray[1]);
			} catch (Exception e2) {}
		}


		if (core != null && attachment != null && battery != null)
		{
			if (((ICore)core.getItem()).requiresElectricity(core) && battery != null)
			{
				currentTips.add("Energy: " + this.getJoules(itemStack) + " / " + this.getMaxJoules(itemStack));
			}
			else if (((ICore)core.getItem()).getCoreFinerType(core) == "plasma" && battery != null)
			{
				currentTips.add("Energy: " + this.getJoules(itemStack) + " / " + this.getMaxJoules(itemStack));
				currentTips.add("Plasma: " + this.getStoredPlasma(itemStack) + " / " + this.getMaxPlasma(itemStack));
			}
			else if (((ICore)core.getItem()).getCoreFinerType(core) == "fuel")
			{
				currentTips.add("Fuel: " + this.getStoredFuel(itemStack) + " / " + this.getMaxFuel(itemStack));
			}

		}
		else
		{
			currentTips.add("Creative Mode Tool");
		}
		

		if (advancedToolTips)
		{
			if (core != null)
				currentTips.add("Core Module: " + core.getDisplayName());
			if (attachment != null)
				currentTips.add("Tool Module: " + attachment.getDisplayName());
			if (battery != null)
				currentTips.add("Batt Module: " + battery.getDisplayName());
		}
	}

	public boolean onItemUse(ItemStack i)
	{
		ItemStack core = null, attachment = null, battery = null, fuelTank = null;
		try 
		{
			NBTTagCompound info = i.stackTagCompound;
			int[] coreIntArray = info.getIntArray("core");
			core = new ItemStack(coreIntArray[0], 1, coreIntArray[1]);
			int[] attachmentIntArray = info.getIntArray("attachment");
			attachment = new ItemStack(attachmentIntArray[0], 1, attachmentIntArray[1]);
			int[] batteryIntArray = info.getIntArray("battery");
			battery = new ItemStack(batteryIntArray[0], 1, batteryIntArray[1]);
		} catch (Exception e)
		{
			try 
			{
				NBTTagCompound info = i.stackTagCompound;
				int[] coreIntArray = info.getIntArray("core");
				core = new ItemStack(coreIntArray[0], 1, coreIntArray[1]);
				int[] attachmentIntArray = info.getIntArray("attachment");
				attachment = new ItemStack(attachmentIntArray[0], 1, attachmentIntArray[1]);
				int[] fuelTankIntArray = info.getIntArray("fuelTank");
				fuelTank = new ItemStack(fuelTankIntArray[0], 1, fuelTankIntArray[1]);
			} catch (Exception e2) {}
		}
		if (((ICore)core.getItem()).requiresElectricity(core))
		{
			if (this.getJoules(i) >= this.getPrimaryEnergyPerOperation(i))
			{
				this.setJoules(this.getJoules(i) - this.getPrimaryEnergyPerOperation(i), i);
				return true;
			}
		}
		else if (((ICore)core.getItem()).getCoreFinerType(i) == "plasma")
		{
			if (this.getJoules(i) >= this.getPrimaryEnergyPerOperation(i) && this.getStoredPlasma(i) >= this.getSecondaryEnergyPerOperation(i))
			{
				this.setJoules(this.getJoules(i) - this.getPrimaryEnergyPerOperation(i), i);
				this.setStoredPlasma(this.getStoredPlasma(i) - this.getSecondaryEnergyPerOperation(i), i);
				return true;
			}
		}
		else if (((ICore)core.getItem()).getCoreFinerType(i) == "fuel")
		{
			if (this.getStoredFuel(i) >= this.getPrimaryEnergyPerOperation(i))
			{
				this.setStoredFuel(this.getStoredFuel(i) - this.getPrimaryEnergyPerOperation(i), i);
				return true;
			}
		}
		return false;
	}

	private double getPrimaryEnergyPerOperation(ItemStack i)
	{
		try
		{
			ItemStack core = null;
			int[] coreIntArray = i.stackTagCompound.getIntArray("core");
			core = new ItemStack(coreIntArray[0], 1, coreIntArray[1]);
			return ((ICore)core.getItem()).getPrimaryEnergyPerOperation(i);
		} catch (Exception e)
		{
			return 0;
		}
	}

	private double getSecondaryEnergyPerOperation(ItemStack i)
	{
		try
		{
			ItemStack core = null;
			int[] coreIntArray = i.stackTagCompound.getIntArray("core");
			core = new ItemStack(coreIntArray[0], 1, coreIntArray[1]);
			if (((ICore)core.getItem()).usesAltFuel(i))
				return ((ICore)core.getItem()).getSecondaryEnergyPerOperation(i);
			else return 0;
		} catch (Exception e)
		{
			return 0;
		}
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

}
