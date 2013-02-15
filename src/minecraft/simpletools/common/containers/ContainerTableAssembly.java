package simpletools.common.containers;

import simpletools.common.misc.SlotAttachment;
import simpletools.common.misc.SlotCore;
import simpletools.common.misc.SlotOutput;
import simpletools.common.misc.SlotSTStorage;
import simpletools.common.tileentities.TileEntityTableAssembly;
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
		this.addSlotToContainer(new SlotOutput(tileEntity, 3, 120, 36));
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
}
