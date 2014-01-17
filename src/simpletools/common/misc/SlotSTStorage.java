package simpletools.common.misc;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import universalelectricity.api.CompatibilityModule;
import universalelectricity.api.item.IEnergyItem;

public class SlotSTStorage extends Slot
{
    public SlotSTStorage(IInventory par1iInventory, int par2, int par3, int par4)
    {
        super(par1iInventory, par2, par3, par4);
    }
    
    @Override
    public boolean isItemValid(ItemStack i)
    {
        // TODO CompatibilityModule.isHandler(handler)
        if (i.getItem() instanceof IEnergyItem)
        {
            IEnergyItem batt = (IEnergyItem) i.getItem();
            long test = 10;
            
            if (batt.getEnergy(i) > 10)
            {
                if (batt.discharge(i, test, false) > 0)
                    return true;
                else
                    return false;
            }
            else
            {
                batt.setEnergy(i, batt.getEnergyCapacity(i) + 10);
                if (batt.discharge(i, test, false) > 0)
                {
                    batt.setEnergy(i, batt.getEnergyCapacity(i) - 10);
                    return true;
                }
                else
                {
                    batt.setEnergy(i, batt.getEnergyCapacity(i) - 10);
                    return false;
                }
            }
        }
        else
            return false;
    }
}
