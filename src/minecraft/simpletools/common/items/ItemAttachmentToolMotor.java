package simpletools.common.items;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
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
	
	@Override
	public int getAttachmentTier(ItemStack i)
	{
		return i.getItemDamage() / 10;
	}
	
	@Override
	public String getItemNameIS(ItemStack par1ItemStack)
	{
		String type;
		String tier = par1ItemStack.getItemDamage() / 10 + "";
		switch (par1ItemStack.getItemDamage() % 10)
		{
			case 0:		type = "drill";
						break;
			case 1:		type = "chainsaw";
						break;
			case 2:		type = "shovel";
						break;
			case 3:		type = "sword";
						break;
			case 4:		type = "shears";
						break;
			case 5:
			case 6:
			case 7:
			case 8:
			case 9:
			default:	type = "unknown";
						break;
		}
		
		return this.getItemName() + "." + type + "." + tier;
	}

	@Override
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		for (int i = 0; i < 4; i++)
			for (int k = 0; k < 5; k++)
				par3List.add(new ItemStack(par1, 1, (i * 10) + k));
	}
	
	@Override
	public boolean isDamageable()
	{
		return false;
	}

}
