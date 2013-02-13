package simpletools.common.containers;

import simpletools.common.misc.SlotAttachment;
import simpletools.common.misc.SlotCore;
import simpletools.common.misc.SlotSTStorage;
import simpletools.common.tileentities.TileEntityTableAssembly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerTableAssembly extends Container implements IInventory
{
	public ContainerTableAssembly(InventoryPlayer inventory, TileEntityTableAssembly tileEntity)
	{
		this.addSlotToContainer(new SlotAttachment(tileEntity, 0, 11, 16));
		this.addSlotToContainer(new SlotCore(tileEntity, 1, 11, 32));
		this.addSlotToContainer(new SlotSTStorage(tileEntity, 2, 11, 56));
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
		return false;
	}

	@Override
	public int getSizeInventory()
	{
		return 4;
	}

	@Override
	public ItemStack getStackInSlot(int var1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemStack decrStackSize(int var1, int var2)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int var1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setInventorySlotContents(int var1, ItemStack var2)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getInvName()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getInventoryStackLimit()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void onInventoryChanged()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer var1)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void openChest()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closeChest()
	{
		// TODO Auto-generated method stub
		
	}
	
}
