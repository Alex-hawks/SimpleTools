package simpletools.common.misc;

import universalelectricity.core.implement.IItemElectric;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotSTStorage extends Slot
{
	public SlotSTStorage(IInventory par1iInventory, int par2, int par3, int par4)
	{
		super(par1iInventory, par2, par3, par4);
	}
	
	@Override
	public boolean isItemValid(ItemStack i)
	{
		if (i.getItem() instanceof IItemElectric && ((IItemElectric)i.getItem()).canProduceElectricity())
			return true;
		return false;
	}

}
