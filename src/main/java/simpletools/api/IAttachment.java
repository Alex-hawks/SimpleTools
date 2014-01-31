package simpletools.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * @author Alex_hawks
 */
public interface IAttachment
{
    /**
     * @param i
     *            The ItemStack, to provide the metadata to the method.
     * @return "pickaxe", "shovel", "axe", "sword", "shears"
     */
    public String getToolType(ItemStack i);
    
    /**
     * @param i
     *            The ItemStack, to provide the metadata to the method.
     * @return 0: Mechanical, 1: Plasma
     */
    public int getToolAttachmentType(ItemStack i);
    
    /**
     * @param i
     *            The ItemStack, to provide the metadata to the method.
     * @return 0: Works with Basic, or above </br> 1: Works with Advanced, or
     *         above </br> 2: Works with Elite, or above </br> 3: Works only
     *         with Ultimate
     */
    public int getMinimumTier(ItemStack i);
    
    /**
     * @param i
     *            The ItemStack, to provide the metadata to the method.
     * @return The Unique Identifier of the attachment. POSITIVE VALUES UNDER
     *         1000 ONLY! TIER SENSETIVE!!!
     */
    public short getAttachmentUID(ItemStack i);
    
    /**
     * @param i
     *            The ItemStack, to provide the metadata to the method.
     * @return The attachment's tier. Provided below are the assumed
     *         EnumToolMaterial equivalents </br> 0: (1, ignored, 04.0F, 1,
     *         ignored) </br> 1: (2, ignored, 06.0F, 2, ignored) </br> 2: (3,
     *         ignored, 08.0F, 3, ignored) </br> 3: (4, ignored, 12.0F, 4,
     *         ignored)
     */
    public int getAttachmentTier(ItemStack i);
    
    /**
     * @param i
     *            The ItemStack, to provide the metadata to the method.
     * @return The harvest speed of the Item, on its proper material. </br> The
     *         harvest level is getAttachmentTier(i) + 1
     */
    public float getHarvestSpeed(ItemStack i);
    
    /**
     * @param i
     *            The ItemStack, to provide the metadata to the method.
     * @return 
     *            The damage to be dealt
     */
    public double getDamageVsEntities(ItemStack i);
    
    /**
     * Called to check if the attachment can interact with this object
     * 
     * @param i
     *            The ItemStack, to provide the metadata to the method.
     * @param target
     *            The target of the right click. There is no one safe cast, so
     *            check first. </br> However, you can probably rely on it being
     *            an entity, or an Object[] { world, x, y, z, (int) side, hitX,
     *            hitY, hitZ } </br> BUT IT CAN BE NULL!!!
     * @param player
     *            The player that used the item. Also, can be, and probably is,
     *            null. There are some time that it won't be though.
     * @return false if nothing happens.
     */
    public boolean canRightClick(ItemStack i, Object target, EntityPlayer player);
    
    /**
     * Called when the player right clicks with the enclosing tool
     * 
     * @param i
     *            The ItemStack, to provide the metadata to the method.
     * @param target
     *            The target of the right click. There is no one safe cast, so
     *            check first. </br> However, you can probably rely on it being
     *            an entity, or an Object[] { world, x, y, z, (int) side, hitX,
     *            hitY, hitZ } </br> BUT IT CAN BE NULL!!!
     * @param player
     *            The player that used the item. Also, can be, and probably is,
     *            null. There are some time that it won't be though.
     * @return Handle it yourself.
     */
    public byte onRightClick(ItemStack i, Object target, EntityPlayer player);
}
