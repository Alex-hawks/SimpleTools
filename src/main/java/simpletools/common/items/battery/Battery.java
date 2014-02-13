package simpletools.common.items.battery;

import net.minecraft.item.ItemStack;
import simpletools.common.SimpleTools;
import universalelectricity.api.UniversalClass;
import universalelectricity.api.item.ItemElectric;

@UniversalClass
public class Battery extends ItemElectric
{
    public static final long[] CAPACITY = new long[] {10000000L, 50000000L, 100000000L, 500000000L};
    private final int tier;
    
    public Battery(int id, int tier)
    {
        super(id);
        this.tier = tier;
        this.setUnlocalizedName("stBattery." + this.tier);
        this.setTextureName(SimpleTools.DOMAIN + "battery." + this.tier);
    }

    @Override
    public long getEnergyCapacity(ItemStack theItem)
    {
        return CAPACITY[this.tier - 1];
    }
}