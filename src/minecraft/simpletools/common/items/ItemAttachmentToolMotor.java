package simpletools.common.items;

import java.util.List;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import simpletools.common.SimpleTools;
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
		this.setIconIndex(32);
		this.setTextureFile(SimpleTools.ITEM_TEXTURES);
	}
	
	@Override
	public String getToolType(ItemStack i)
	{
		switch (i.getItemDamage() % 10)
		{
		case 0: return "pickaxe";
		case 1: return "shovel";
		case 2: return "axe";
		case 3: return "sword";
		case 4: return "shears";
		case 5:
		case 6:
		case 7:
		case 8:
		case 9:
		default: return "";
		}
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
	public String getItemNameIS(ItemStack i)
	{
		return this.getItemName() + "." + this.getToolType(i) + "." + this.getAttachmentTier(i);
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

	@Override
	public double getHarvestSpeed(ItemStack i) 
	{
		switch (this.getAttachmentTier(i))
		{
		case 0:		return 4.0F;
		case 1:		return 6.0F;
		case 2:		return 8.0F;
		case 3:		return 12.0F;
		default:	System.out.println("Default Strength");
					return 1.0F;
		}
	}

	public int getDamageVsEntity(ItemStack i, Entity entity) 
	{
		String type = this.getToolType(i);
		int toolBonus = 0;
		if (type.equals("pickaxe"))
			toolBonus = 2;
		else if (type.equals("shovel"))
			toolBonus = 1;
		else if (type.equals("axe"))
			toolBonus = 3;
		else if (type.equals("sword"))
			toolBonus = 4;
		else if (type.equals("shears"))
			toolBonus = -(this.getAttachmentTier(i) + 1); // shears will hit for 0
		return this.getAttachmentTier(i) + toolBonus + 1;
	}

	@Override
	public BiMap<Entity, Integer> getDamageVsEntities(ItemStack i)
	{
		BiMap<Entity, Integer> toReturn = HashBiMap.create();
		
		String type = this.getToolType(i);
		int toolBonus = 0;
		if (type.equals("pickaxe"))
			toolBonus = 2;
		else if (type.equals("shovel"))
			toolBonus = 1;
		else if (type.equals("axe"))
			toolBonus = 3;
		else if (type.equals("sword"))
			toolBonus = 4;
		else if (type.equals("shears"))
			toolBonus = -(this.getAttachmentTier(i) + 1); // shears will hit for 0
		toReturn.put(null, this.getAttachmentTier(i) + toolBonus + 1);
		return toReturn;
	}
	
	@Override
    public int getIconFromDamage(int meta)
    {
        return this.iconIndex + meta;
    }
}
