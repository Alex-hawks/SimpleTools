package simpletools.common.containers;

import simpletools.common.interfaces.ICore;
import simpletools.common.misc.SlotAttachment;
import simpletools.common.misc.SlotCore;
import simpletools.common.misc.SlotOutput;
import simpletools.common.misc.SlotSTStorage;
import simpletools.common.tileentities.TileEntityTableAssembly;
import universalelectricity.core.implement.IItemElectric;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerTableAssembly extends Container
{
	private TileEntityTableAssembly tileEntity;

	public ContainerTableAssembly(InventoryPlayer inventory, TileEntityTableAssembly tileEntity)
	{
		this.tileEntity = tileEntity;
		this.addSlotToContainer(new SlotCore(tileEntity, 1, 39, 36)); 
		this.addSlotToContainer(new SlotAttachment(tileEntity, 0, 16, 22, tileEntity.getStackInSlot(1)));
		this.addSlotToContainer(new SlotSTStorage(tileEntity, 2, 16, 50, tileEntity.getStackInSlot(1)));
		this.addSlotToContainer(new Slot(tileEntity, 3, 120, 36));
		int var3;

		for (var3 = 0; var3 < 3; ++var3)
		{
			for (int var4 = 0; var4 < 9; ++var4)
			{
				this.addSlotToContainer(new Slot(inventory, var4 + var3 * 9 + 9, 8 + var4 * 18, 84 + var3 * 18));
			}
		}

		for (var3 = 0; var3 < 9; ++var3)
		{
			this.addSlotToContainer(new Slot(inventory, var3, 8 + var3 * 18, 142));
		}

		tileEntity.openChest();
	}

	@Override
	public boolean canInteractWith(EntityPlayer var1)
	{
		return tileEntity.isUseableByPlayer(var1);
	}

	@Override
	public void onCraftGuiClosed(EntityPlayer entityplayer)
	{
		super.onCraftGuiClosed(entityplayer);
		tileEntity.closeChest();
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
	{
		return null;
/*		Slot var3 = (Slot)this.inventorySlots.get(par2);
		ItemStack var4 = null;
		
		if (var3 != null && var3.getHasStack())
		{
			ItemStack var5 = var3.getStack();
			var4 = var5.copy();

			if (par2 > 4)
			{
				if (var5.getItem() instanceof ICore)
				{
					if (!this.mergeItemStack(var5, 2, 4, false)) { return null; }
				}
				else if (var5.getItem() instanceof IItemElectric && ((ICore)this.tileEntity.getStackInSlot(1).getItem()).getCoreFinerType(this.tileEntity.getStackInSlot(1)).equals("electric"))
				{
					if (((IItemElectric) var5.getItem()).canProduceElectricity())
					{
						if (!this.mergeItemStack(var5, 2, 2, false)) { return null; }
					}
					else
					{
						 return null;
					}
				}
				else if
				
			}
			else if (!this.mergeItemStack(var5, 5, 38, false)) { return null; }

			if (var5.stackSize == 0)
			{
				var3.putStack((ItemStack) null);
			}
			else
			{
				var3.onSlotChanged();
			}

			if (var5.stackSize == var4.stackSize) { return null; }

			var3.onPickupFromSlot(par1EntityPlayer, var5);
*/
	}
}
