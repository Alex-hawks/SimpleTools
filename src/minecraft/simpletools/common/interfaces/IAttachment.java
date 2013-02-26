package simpletools.common.interfaces;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
/**
 * @author Alex_hawks
 */
public interface IAttachment
{
	/**
	 * @param i The ItemStack, to provide the metadata to the method.
	 * @return 	"pickaxe", "shovel", "axe", "sword", "shears"
	 */
	public String getToolType(ItemStack i);
	
	/**
	 * @param i The ItemStack, to provide the metadata to the method.
	 * @return 0: Mechanical, 1: Plasma
	 */
	public int getToolAttachmentType(ItemStack i);
	
	/**
	 * @param i The ItemStack, to provide the metadata to the method.
	 * @return	0: Works with Basic, or above
	 * </br>	1: Works with Advanced, or above
	 * </br>	2: Works with Elite, or above
	 * </br>	3: Works only with Ultimate
	 */
	public int getMinimumTier(ItemStack i);
	
	/**
	 * @param i The ItemStack, to provide the metadata to the method.
	 * @return The Unique Identifier of the attachment. POSITIVE VALUES UNDER 1000 ONLY! TIER SENSETIVE!!!
	 */
	public short getAttachmentUID(ItemStack i);
	
	/**
	 * @param i The ItemStack, to provide the metadata to the method.
	 * @return 	The attachment's tier. Provided below are the assumed EnumToolMaterial equivalents
	 * </br>	0:	(1, ignored, 04.0F, 1, ignored)
	 * </br>	1:	(2, ignored, 06.0F, 2, ignored)
	 * </br>	2:	(3, ignored, 08.0F, 3, ignored)
	 * </br>	3:	(4, ignored, 12.0F, 4, ignored)
	 */
	public int getAttachmentTier(ItemStack i);
	
	/**
	 * @param i The ItemStack, to provide the metadata to the method.
	 * @return 	The harvest speed of the Item, on its proper material.
	 * </br>	The harvest level is getAttachmentTier(i) + 1
	 */
	public double getHarvestSpeed(ItemStack i);
	
	/**
	 * @param i			The ItemStack, to provide the metadata to the method.
	 * @param entity	The entity being attacked, best to ensure it is an instance of EntityLiving first.
	 * @return			The damage to be dealt to the entity. The Assembled tool will do the out of energy penalties.
	 * </br>			Also, DO NOT DAMAGE THE ENTITY IN THIS METHOD... The Assembled tool does that too.
	 * </br>			You do need to calculate damage based on the attachment, and the entity. But NOT based on enchantments.
	 * </br>			Shovels do 1 more damage than their EnumToolMaterial says.
	 * </br>			Pick-axes do 2 more damage than their EnumToolMaterial says.
	 * </br>			Axes do 3 more damage than their EnumToolMaterial says.
	 * </br>			Swords do 4 more damage than their EnumToolMaterial says.
	 */
	public int getDamageVsEntity(ItemStack i, Entity entity);
}
