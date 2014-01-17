package simpletools.api;

import net.minecraft.item.ItemStack;

public interface IAdditionalMeterData
{
    /**
     * @param is
     *            the ItemStack for which the current storage is wanted
     * @return An array of floats. Each float should be between 0f and 1f, to
     *         indicate how full the bar should be. 1 bar will be drawn per
     *         float in the array.
     */
    public float[] getAdditionalDisplayData(ItemStack is);
}
