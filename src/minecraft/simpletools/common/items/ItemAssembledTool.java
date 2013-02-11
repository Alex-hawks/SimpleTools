package simpletools.common.items;

import net.minecraft.item.ItemStack;
import universalelectricity.prefab.ItemElectric;

public class ItemAssembledTool extends ItemElectric 
{
	private ItemStack attachment;
	private ItemStack core;
	private ItemElectric battery;
	private double energyStored = 0;
	private double maxEnergy;
	
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
	
	public void onCreate(ItemStack attachment, ItemStack core, ItemElectric battery)
	{
		this.attachment = attachment;
		this.core = core;
		this.battery = battery;
		this.energyStored = battery.getJoules();
		this.maxEnergy = battery.getMaxJoules();
	}
}
