package simpletools.common.interfaces;

import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * @author Alex_hawks
 *
 */
public interface IAssembledTool 
{
	/**
	 * @param attachment The attachment for the tool
	 * @param core The Core for the tool
	 * @param storage The Storage, such as a battery, for the tool
	 * @return 	The ItemStack that should store all of the Tool's data, ENSURE that it has NBTTags on it.
	 * 			null if it failed...
	 */
	public ItemStack onCreate(ItemStack attachment, ItemStack core, ItemStack storage);
	
	/**
	 * @param assembledTool The assembled tool for which the core is wanted
	 * @return	The ItemStack of the associated core. stackSize of 1, MUST BE CORRECT TIER!
	 */
	public ItemStack getCore(ItemStack assembledTool);
	
	/**
	 * @param assembledTool The assembled tool for which the storage is wanted
	 * @return	The ItemStack of the Storage (eg. Battery). MUST be created storing 
	 * 			the amount of primary energy that was stored in the assembled tool
	 */
	public ItemStack getStorage(ItemStack assembledTool);
	
	/**
	 * @param assembledTool The assembled tool for which the storage is wanted
	 * @return	The ItemStack of the attachment on the tool. stackSize of 1, MUST BE CORRECT TIER AND TYPE!
	 * 			Also, the attachment stores the enchantments...
	 */
	public ItemStack getAttachment(ItemStack assembledTool);

	/**
	 * @param assembledTool The Assembled Tool for which the condition is wanted
	 * @return weather or not the tool can successfully complete an action
	 */
	boolean canDoWork(ItemStack assembledTool);

	/**
	 * @param assembledTool The Assembled Tool for which the condition is wanted
	 * @param entity the entity breaking the block, can usually be cast to an EntityPlayer, but make sure to check
	 * @return true if the block should yield it's drops, and the tool should be effective against it
	 */
	public boolean canBreakBlock(ItemStack assembledTool, World world, int blockID, int metadata, EntityLiving entity);

	/**
	 * @param assembledTool The Assembled Tool for which the condition is wanted
	 * @param entity the entity breaking the block, can usually be cast to an EntityPlayer, but make sure to check
	 * @return true if the block should yield it's drops, and the tool should be effective against it
	 */
	public boolean isEffectiveOnBlock(ItemStack assembledTool, World world, int blockID, int metadata, EntityLiving entity);
	
	/**
	 * @param assembledTool the Assembled Tool for which the current storage is wanted
	 * @return the current storage of the max storage, formatted for a short display
	 */
	public String getStoredForDisplay(ItemStack assembledTool);
}
