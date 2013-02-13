package simpletools.common.misc;

import simpletools.common.interfaces.ICore;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotCore extends Slot
{
	public SlotCore(IInventory par1iInventory, int par2, int par3, int par4)
	{
		super(par1iInventory, par2, par3, par4);
	}

	@Override
	public boolean isItemValid(ItemStack i)
	{
		return i.getItem() instanceof ICore;
	}

}
