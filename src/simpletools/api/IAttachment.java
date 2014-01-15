package simpletools.api;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import com.google.common.collect.BiMap;

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
     * @return A BiMap<Entity, Integer> of the Entity and the damage dealt.
     *         </br> The Assembled tool will do the out of energy penalties.
     *         </br> Also, DO NOT DAMAGE THE ENTITY IN THIS METHOD... The
     *         Assembled tool does that too. </br> You do need to calculate
     *         damage based on the attachment, and the entity. But NOT based on
     *         enchantments. </br> Also, map a value to Entity as that value
     *         will be used on anything not specifically listed. </br> If you
     *         want all entities to take the same base damage from the tool,
     *         just return the map with one entry. </br> THIS IS
     *         META-SENSETIVE!!! </br> Shovels do 1 more damage than their
     *         EnumToolMaterial says. </br> Pick-axes do 2 more damage than
     *         their EnumToolMaterial says. </br> Axes do 3 more damage than
     *         their EnumToolMaterial says. </br> Swords do 4 more damage than
     *         their EnumToolMaterial says.
     */
    public BiMap<Entity, Integer> getDamageVsEntities(ItemStack i);
    
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
