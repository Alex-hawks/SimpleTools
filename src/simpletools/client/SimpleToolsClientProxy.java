package simpletools.client;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import simpletools.client.gui.GuiTableAssembly;
import simpletools.common.SimpleToolsCommonProxy;
import simpletools.common.tileentities.TileEntityTableAssembly;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SimpleToolsClientProxy extends SimpleToolsCommonProxy implements IGuiHandler
{
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
		if (tileEntity != null)
		{
			switch (ID)
			{
				case 0: return new GuiTableAssembly(player.inventory, (TileEntityTableAssembly)tileEntity);
			}
		}
		return null;
	}

}
