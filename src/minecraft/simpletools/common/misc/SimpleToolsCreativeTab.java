package simpletools.common.misc;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class SimpleToolsCreativeTab extends CreativeTabs
{
	public static final SimpleToolsCreativeTab INSTANCE = new SimpleToolsCreativeTab("SimpleTools");
	private static ItemStack itemStack;
	
	public SimpleToolsCreativeTab(String label)
	{
		super(CreativeTabs.getNextID(), label);
	}
	
	public static void setItemStack(ItemStack newItemStack)
	{
		if (itemStack == null)
			itemStack = newItemStack;
	}
	
	@Override
	public ItemStack getIconItemStack()
	{
		if (itemStack == null) 
			return new ItemStack(Block.blocksList[this.getTabIconItemIndex()]);
		return itemStack;
	}

}
