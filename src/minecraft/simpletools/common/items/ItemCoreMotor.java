package simpletools.common.items;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import simpletools.common.SimpleTools;
import simpletools.common.interfaces.ICore;
import simpletools.common.misc.SimpleToolsCreativeTab;

public class ItemCoreMotor extends Item implements ICore
{

	public ItemCoreMotor(int itemID, String name)
	{
		super(itemID);
		this.setCreativeTab(SimpleToolsCreativeTab.INSTANCE);
		this.setItemName(name);
		this.setMaxStackSize(16);
		this.setIconIndex(0);
		this.setTextureFile(SimpleTools.ITEM_TEXTURES);
	}

	@Override
	public int getCoreType(ItemStack i)
	{
		return 0;
	}

	@Override
	public int getCoreTier(ItemStack i)
	{
		return i.getItemDamage();
	}

	@Override
	public byte getCoreUID(ItemStack i)
	{
		return (byte) i.getItemDamage();
	}

	@Override
	public String getCoreFinerType(ItemStack i)
	{
		return "electric";
	}

	@Override
	public String getItemNameIS(ItemStack itemStack)
	{
		return this.getItemName() + "." + getCoreTier(itemStack);
	}

	@Override
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		for (int i = 0; i < 4; i++)
			par3List.add(new ItemStack(par1, 1, i));
	}
	
	@Override
	public boolean isDamageable()
	{
		return false;
	}

	@Override
	public Item getAssmebledToolItem(ItemStack i) 
	{
		return SimpleTools.assembledToolElectric;
	}
	
	@Override
    public int getIconFromDamage(int meta)
    {
        return this.iconIndex + meta;
    }
}
