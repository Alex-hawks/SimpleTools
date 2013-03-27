package simpletools.common.items;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import simpletools.common.SimpleTools;
import simpletools.common.interfaces.IAttachment;
import simpletools.common.misc.SimpleToolsCreativeTab;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class ItemAttachmentPlasma extends Item implements IAttachment
{
	private Icon[] icons = new Icon[40];

	public ItemAttachmentPlasma(int itemID, String name)
	{
		super(itemID);
		this.setCreativeTab(SimpleToolsCreativeTab.INSTANCE);
		this.setUnlocalizedName(name);
		this.setMaxStackSize(16);
	}
	
	@Override
	public String getToolType(ItemStack i)
	{
		return "";
	}
	
	@Override
	public int getToolAttachmentType(ItemStack i)
	{
		return 1;
	}
	
	@Override
	public int getMinimumTier(ItemStack i)
	{
		switch (i.getItemDamage())
		{
			case 0: return 1;
		}
		return 0;
	}
	
	@Override
	public short getAttachmentUID(ItemStack i)
	{
		return ((Integer) (i.getItemDamage() + 40)).shortValue();
	}
	
	@Override
	public int getAttachmentTier(ItemStack i)
	{
		return 0;
	}
	
	@Override
	public double getHarvestSpeed(ItemStack i)
	{
		return 0;
	}
	
	@Override
	public BiMap<Entity, Integer> getDamageVsEntities(ItemStack i)
	{
		BiMap<Entity, Integer> toReturn = HashBiMap.create();
		toReturn.put(null, 3);
		return toReturn;
	}
	
	@Override
	public boolean canRightClick(ItemStack i, Object targetData, EntityPlayer player)
	{
		if (i.getItemDamage() == 0 && targetData instanceof Object[])
		{
			Object[] target = (Object[]) targetData;
			int x = (int) target[1];
			int y = (int) target[2];
			int z = (int) target[3];
			int side = (int) target[4];
			if ((World) target[0] != null)
			{
				World world = (World) target[0];
				
				if (world.getBlockId(x, y, z) == SimpleTools.plasmaTorch.blockID)
				{
					return true;
				}
				
				x += ForgeDirection.getOrientation(side).offsetX;
				y += ForgeDirection.getOrientation(side).offsetY;
				z += ForgeDirection.getOrientation(side).offsetZ;
				if (world.getBlockId(x, y, z) == 0 || world.getBlockId(x, y, z) == SimpleTools.plasmaTorch.blockID)
					return true;
			}
		}
		return false;
	}
	
	@Override
	public byte onRightClick(ItemStack i, Object targetData, EntityPlayer player)
	{
		if (i.getItemDamage() == 0 && targetData instanceof Object[])
		{
			Object[] target = (Object[]) targetData;
			int x = (int) target[1];
			int y = (int) target[2];
			int z = (int) target[3];
			int side = (int) target[4];
			if ((World) target[0] != null)
			{
				World world = (World) target[0];
				
				if (world.getBlockId(x, y, z) == SimpleTools.plasmaTorch.blockID && player != null && !player.isSneaking())
				{
					world.setBlock(x, y, z, 0, 0, 0x02);
					return 1;
				}
				
				x += ForgeDirection.getOrientation(side).offsetX;
				y += ForgeDirection.getOrientation(side).offsetY;
				z += ForgeDirection.getOrientation(side).offsetZ;
				if (world.getBlockId(x, y, z) == 0)
				{
					world.setBlock(x, y, z, SimpleTools.plasmaTorch.blockID, 0, 0x02);
					return -1;
				}
				if (world.getBlockId(x, y, z) == SimpleTools.plasmaTorch.blockID)
				{
					world.setBlock(x, y, z, 0, 0, 0x02);
					return 1;
				}
			}
		}
		return 0;
	}
	
}
