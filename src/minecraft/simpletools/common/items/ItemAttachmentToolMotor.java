package simpletools.common.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import simpletools.common.interfaces.IAttachment;
import simpletools.common.misc.SimpleToolsCreativeTab;

public class ItemAttachmentToolMotor extends Item implements IAttachment
{
	public ItemAttachmentToolMotor(int itemID, String name)
	{
		super(itemID);
		this.setCreativeTab(SimpleToolsCreativeTab.INSTANCE);
		this.setItemName(name);
		this.setMaxDamage(39);
		this.setMaxStackSize(16);
	}

	@Override
	public int getToolType(ItemStack i)
	{
		return i.getItemDamage() % 10;
	}

	@Override
	public int getToolAttachmentType(ItemStack i)
	{
		return 0;
	}

	@Override
	public int getMinimumTier(ItemStack i)
	{
		return i.getItemDamage() / 10;
	}

	@Override
	public short getAttachmentUID(ItemStack i)
	{
		return ((Integer)i.getItemDamage()).shortValue();
	}

}
