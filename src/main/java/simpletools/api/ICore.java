package simpletools.api;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * @author Alex_hawks
 */
public interface ICore
{
    /**
     * @param i
     *            The ItemStack, to provide the metadata to the method.
     * @return 0: Mechanical 1: Plasma
     */
    public int getCoreType(ItemStack i);
    
    /**
     * @param i
     *            The ItemStack, to provide the metadata to the method.
     * @return 0: Basic 1: Advanced 2: Elite 3: Ultimate
     */
    public int getCoreTier(ItemStack i);
    
    /**
     * @param i
     *            The ItemStack, to provide the metadata to the method.
     * @return The Unique Identifier of the core. POSITIVE VALUES ONLY!
     */
    public byte getCoreUID(ItemStack i);
    
    /**
     * @param i
     *            The ItemStack, to provide the metadata to the method.
     * @return What the core is powered by. f.e. "electric", "fuel", "plasma"
     */
    public String getCoreFinerType(ItemStack i);
    
    /**
     * @param i
     *            The ItemStack, to provide the metadata to the method.
     * @return The Item that is given when the tool is assembled
     */
    public Item getAssmebledToolItem(ItemStack i);
}
