package simpletools.common.interfaces;

import net.minecraft.item.ItemStack;
/**
 * @author Alex_hawks
 */
public interface IAttachment
{
	/**
	 * @param i The ItemStack, to provide the metadata to the method.
	 * @return 	0: pick
	 * 			1: axe
	 * 			2: shovel
	 * 			3: sword
	 * 			4: shears
	 * 			5: 
	 * 			6: 
	 * 			7: 
	 * 			8: 
	 * 			9: 
	 */
	public int getToolType(ItemStack i);
	
	/**
	 * @param i The ItemStack, to provide the metadata to the method.
	 * @return 0: Mechanical, 1: Plasma
	 */
	public int getToolAttachmentType(ItemStack i);
	
	/**
	 * @param i The ItemStack, to provide the metadata to the method.
	 * @return	0: Works with Basic, or above
	 * 			1: Works with Advanced, or above
	 * 			2: Works with Elite, or above
	 * 			3: Works only with Ultimate
	 */
	public int getMinimumTier(ItemStack i);
	
	/**
	 * @param i The ItemStack, to provide the metadata to the method.
	 * @return The Unique Identifier of the attachment. POSITIVE VALUES UNDER 1000 ONLY!
	 */
	public short getAttachmentUID(ItemStack i);
	
	/**
	 * @param i The ItemStack, to provide the metadata to the method.
	 * @return the attachment's tier
	 * 		0:	Stone
	 * 		1:	Iron
	 * 		2:	Diamond
	 * 		3:	(3, ignored, 12.0F, 4, ignored)
	 */
	public int getAttachmentTier(ItemStack i);
}
