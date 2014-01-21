package simpletools.common.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import simpletools.api.IPlasmaStorage;
import simpletools.common.tileentities.TileEntityTablePlasma;
import calclavia.lib.prefab.slot.SlotEnergyItem;
import calclavia.lib.prefab.slot.SlotSpecific;

public class ContainerTablePlasma extends Container
{
    private TileEntityTablePlasma tileEntity;
    
    public ContainerTablePlasma(InventoryPlayer inventory, TileEntityTablePlasma tileEntity)
    {
        this.tileEntity = tileEntity;
        // The Slot for the Battery
        this.addSlotToContainer(new SlotEnergyItem(tileEntity, 0, 16, 22));
        // The Slot for the Tool
        this.addSlotToContainer(new SlotSpecific(tileEntity, 1, 90, 36, IPlasmaStorage.class));
        // The slot for the deuterieum / tritium / water
        this.addSlotToContainer(new Slot(tileEntity, 2, 16, 50));
        this.addSlotToContainer(new Slot(tileEntity, 3, 40, 50));
        
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
    }
    
    @Override
    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return this.tileEntity.isUseableByPlayer(entityplayer);
    }
    
    @Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
    {
        return null;
    }
}
