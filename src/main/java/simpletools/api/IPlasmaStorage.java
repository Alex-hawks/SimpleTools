package simpletools.api;

import net.minecraft.item.ItemStack;

public interface IPlasmaStorage
{
    public int getPlasma(ItemStack stack);
    
    public int getMaxPlasma(ItemStack stack);
    
    public void setPlasma(ItemStack stack, int i);
    
    public boolean addPlasma(ItemStack stack, int i);
}
