package simpletools.common.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import simpletools.common.SimpleTools;
import simpletools.common.interfaces.IAttachment;
import simpletools.common.misc.SimpleToolsCreativeTab;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemAttachmentToolMotor extends Item implements IAttachment
{
	HashMap<String, Icon> icons = new HashMap<String, Icon>();

	public ItemAttachmentToolMotor(int itemID, String name)
	{
		super(itemID);
		this.setCreativeTab(SimpleToolsCreativeTab.INSTANCE);
		this.setUnlocalizedName(name);
		this.setMaxStackSize(16);
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
	public String getUnlocalizedName(ItemStack i)
	{
		return this.getUnlocalizedName() + "." + this.getToolType(i) + "." + this.getAttachmentTier(i);
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
		default:	return 1.0F;
		}
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
	
	@SideOnly(Side.CLIENT)
	public void func_94581_a(IconRegister iconRegister)
	{
		List<ItemStack> list = new ArrayList<ItemStack>();
		this.getSubItems(this.itemID, this.getCreativeTab(), list);

		if (list.size() > 0)
		{
			for (ItemStack itemStack : list)
			{
				this.icons.put(this.getToolType(itemStack) + "." + this.getAttachmentTier(itemStack), iconRegister.func_94245_a(this.getUnlocalizedName(itemStack).replace("item.", SimpleTools.TEXTURE_NAME_PREFIX)));
			}
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIconFromDamage(int par1)
	{
		ItemStack thisIS = new ItemStack(this, 1, par1);
		return this.icons.get(this.getToolType(thisIS) + "." + this.getAttachmentTier(thisIS));
	}

}
