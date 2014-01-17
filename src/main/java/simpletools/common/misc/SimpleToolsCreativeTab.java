package simpletools.common.misc;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class SimpleToolsCreativeTab extends CreativeTabs
{
    public static final SimpleToolsCreativeTab INSTANCE = new SimpleToolsCreativeTab("SimpleTools");
    private ItemStack itemStack;
    
    public SimpleToolsCreativeTab(String label)
    {
        super(CreativeTabs.getNextID(), label);
    }
    
    public SimpleToolsCreativeTab(String label, ItemStack icon)
    {
        super(CreativeTabs.getNextID(), label);
        this.itemStack = icon;
    }
    
    public void setItemStack(ItemStack newItemStack)
    {
        if (this.itemStack == null)
        {
            this.itemStack = newItemStack;
        }
    }
    
    @Override
    public ItemStack getIconItemStack()
    {
        if (this.itemStack == null)
            return new ItemStack(Block.blocksList[this.getTabIconItemIndex()]);
        return this.itemStack;
    }
}
