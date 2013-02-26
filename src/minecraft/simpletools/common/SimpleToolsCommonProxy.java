package simpletools.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import simpletools.common.containers.ContainerTableAssembly;
import simpletools.common.tileentities.TileEntityTableAssembly;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.registry.GameRegistry;

public class SimpleToolsCommonProxy implements IGuiHandler
{
	public void init()
	{
		GameRegistry.registerTileEntity(TileEntityTableAssembly.class, "TileEntityTableAssembly");
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
		if (tileEntity != null)
		{
			switch (ID)
			{
				case 0: return new ContainerTableAssembly(player.inventory, (TileEntityTableAssembly)tileEntity);
			}
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		return null;
	}
}
