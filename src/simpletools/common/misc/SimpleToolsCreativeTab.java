package simpletools.common.misc;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import simpletools.common.SimpleTools;
import simpletools.common.items.ItemAssembledToolElectric;
import universalelectricity.components.common.BasicComponents;
import universalelectricity.core.item.ElectricItemHelper;

public class SimpleToolsCreativeTab extends CreativeTabs
{
	public static final SimpleToolsCreativeTab INSTANCE = new SimpleToolsCreativeTab("SimpleTools");
	private static ItemStack itemStack;
	
	public SimpleToolsCreativeTab(String label)
	{
		super(CreativeTabs.getNextID(), label);
	}
	
	public SimpleToolsCreativeTab(String label, ItemStack icon)
	{
		super(CreativeTabs.getNextID(), label);
		this.itemStack = icon;
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
