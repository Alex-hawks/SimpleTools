package simpletools.common.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import simpletools.common.SimpleTools;
import simpletools.common.interfaces.IAttachment;
import simpletools.common.interfaces.ICore;
import universalelectricity.core.electricity.ElectricInfo;
import universalelectricity.core.implement.IItemElectric;
import universalelectricity.prefab.ItemElectric;

public class ItemAssembledTool extends Item implements IItemElectric
{
	private String coreType;
	
	private ItemElectric battery;
	private double energyStored = 0;
	private double maxEnergy;
	
	private ItemStack fuelTank;
	private double fuelStored = 0;
	private double maxFuel;
	
	private double plasmaStored;
	
	public ItemAssembledTool(int itemID, String name)
	{
		super(itemID);
		this.setItemName(name);
		this.setMaxStackSize(1);
		this.setNoRepair();
	}

	@Override
	public double getMaxJoules(Object... data)
	{
		return maxEnergy;
	}

	@Override
	public double getVoltage(Object... data)
	{
		return battery.getVoltage(data);
	}
	
	/**
	 * @param attachment The attachment, passed in as an ItemStack
	 * @param core The core, passed in as an ItemStack
	 * @param batt The battery, passed in as an ItemElectric
	 * @return the resultant Item, as an ItemStack. null if it failed.
	 */
	public ItemStack onCreate(ItemStack attachment, ItemStack core, ItemElectric batt)
	{
		ItemStack returnStack = null;
		if (attachment.getItem() instanceof IAttachment && core.getItem() instanceof ICore)
		{
			IAttachment attachmentTemp = (IAttachment) attachment.getItem();
			ICore coreTemp = (ICore) attachment.getItem();
			
			if (coreTemp.getCoreType(core) == attachmentTemp.getToolAttachmentType(attachment))
			{
				if (coreTemp.getCoreTier(core) >= attachmentTemp.getMinimumTier(attachment))
				{
					int coreMeta = ((int)coreTemp.getCoreUID(core)) * 1000;
					int attachMeta = attachmentTemp.getAttachmentUID(attachment);
					int meta = coreMeta + attachMeta;
					returnStack = new ItemStack(this, 1, meta);
					this.battery = batt;
					this.energyStored = batt.getJoules();
					this.maxEnergy = batt.getMaxJoules();
					
					if (coreTemp.getCoreType(core) == 1)
						this.plasmaStored = 0;
				}
			}
		}
		return returnStack;
	}

	@Override
	public double getJoules(Object... data)
	{
		if (coreType == "electric")
			return this.energyStored;
		else return 0.0;
	}

	@Override
	public void setJoules(double joules, Object... data)
	{
		if (coreType == "electric")
			this.energyStored = joules;
	}

	@Override
	public double onReceive(double amps, double voltage, ItemStack itemStack)
	{
		double rejectedElectricity = Math.max((this.getJoules(itemStack) + ElectricInfo.getJoules(amps, voltage, 1)) - this.getMaxJoules(itemStack), 0);
		this.setJoules(this.getJoules(itemStack) + ElectricInfo.getJoules(amps, voltage, 1) - rejectedElectricity, itemStack);
		return rejectedElectricity;
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
}
