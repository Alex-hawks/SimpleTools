package simpletools.common.items.battery;

import net.minecraft.item.ItemStack;
import simpletools.common.SimpleTools;
import universalelectricity.api.UniversalClass;
import universalelectricity.api.item.ItemElectric;

@UniversalClass
public class Battery extends ItemElectric
{
    public static final long[] CAPACITY = new long[] {10_000_000L, 50_000_000L, 100_000_000L, 500_000_000L};
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