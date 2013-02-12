package simpletools.common.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import simpletools.common.interfaces.ICore;
import simpletools.common.misc.SimpleToolsCreativeTab;

public class ItemCoreMotor extends Item implements ICore
{

	public ItemCoreMotor(int itemID, String name)
	{
		super(itemID);
		this.setCreativeTab(SimpleToolsCreativeTab.INSTANCE);
		this.setItemName(name);
		this.setMaxDamage(4);
		this.setMaxStackSize(16);
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
		return 0;
	}

}
