package simpletools.common.misc;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.core.item.IItemElectric;

public class SlotSTStorage extends Slot
{
	public SlotSTStorage(IInventory par1iInventory, int par2, int par3, int par4)
	{
		super(par1iInventory, par2, par3, par4);
	}
	
	@Override
	public boolean isItemValid(ItemStack i)
	{
		if (i.getItem() instanceof IItemElectric)
		{
			IItemElectric batt = (IItemElectric) i.getItem();
			ElectricityPack test = ElectricityPack.getFromWatts(10, 20);
			double original = batt.getJoules(i);
			
			if (batt.getJoules(i) > 0)
			{
				if (batt.getProvideRequest(i).getWatts() > 0)
				{
					return true;
				}
				else
				{
					return false;
				}
			}
			else 
			{
				batt.onReceive(test, i);
				if (batt.getProvideRequest(i).getWatts() > 0)
				{
					batt.onProvide(test, i);
					return true;
				}
				else
				{
					batt.setJoules(original, i);
					return false;
				}
			}
		}
		else return false;
	}
}
