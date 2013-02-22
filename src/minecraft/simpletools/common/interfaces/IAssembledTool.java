package simpletools.common.interfaces;

import net.minecraft.item.ItemStack;

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
}
