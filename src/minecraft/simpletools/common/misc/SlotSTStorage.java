package simpletools.common.misc;

import simpletools.common.interfaces.IAttachment;
import simpletools.common.interfaces.ICore;
import simpletools.common.tileentities.TileEntityTableAssembly;
import universalelectricity.core.implement.IItemElectric;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotSTStorage extends Slot
{
	private ICore core;
	private ItemStack coreIS;
	
	public SlotSTStorage(IInventory par1iInventory, int par2, int par3, int par4)
	{
		this(par1iInventory, par2, par3, par4, null);
	}
	
	public SlotSTStorage(IInventory par1iInventory, int par2, int par3, int par4, ItemStack core)
	{
		super(par1iInventory, par2, par3, par4);
		if (core != null && core.getItem() instanceof ICore)
		{
			this.coreIS = core;
			this.core = (ICore) core.getItem();
		}
	}

	@Override
	public boolean isItemValid(ItemStack i)
	{
		if (core != null && (i.getItem() instanceof IItemElectric /*|| i.getItem() can store fuel*/))
		{
			if (i.getItem() instanceof IItemElectric && ((IItemElectric)i.getItem()).canProduceElectricity())
				return true;
			else return false;
		}
		else return false;
	}
}
