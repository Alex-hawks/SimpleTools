package simpletools.common.items;

import net.minecraft.item.Item;
import simpletools.common.misc.SimpleToolsCreativeTab;

public class ItemAttachmentToolMotor extends Item
{
	public ItemAttachmentToolMotor(int itemID, String name)
	{
		super(itemID);
		this.setCreativeTab(SimpleToolsCreativeTab.INSTANCE);
		this.setItemName(name);
		this.setMaxDamage(4);
		this.setMaxStackSize(16);
	}

}
