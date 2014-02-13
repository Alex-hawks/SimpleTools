package simpletools.client;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import simpletools.client.gui.GuiTableAssembly;
import simpletools.client.gui.GuiTablePlasma;
import simpletools.client.misc.STRenderTickHandler;
import simpletools.client.render.RenderHandler;
import simpletools.common.SimpleToolsCommonProxy;
import simpletools.common.tileentities.TileEntityTableAssembly;
import simpletools.common.tileentities.TileEntityTablePlasma;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SimpleToolsClientProxy extends SimpleToolsCommonProxy implements IGuiHandler
{
    public static final ISimpleBlockRenderingHandler plasmaTorch = new RenderHandler();
    
    @Override
    public void init()
    {
        super.init();
        
        RenderingRegistry.registerBlockHandler(plasmaTorch);
        
        TickRegistry.registerTickHandler(STRenderTickHandler.INSTANCE, Side.CLIENT);
    }
    
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
        if (tileEntity != null)
        {
            switch (ID)
            {
                case 0:
                    return new GuiTableAssembly(player.inventory, (TileEntityTableAssembly) tileEntity);
                case 1:
                    return new GuiTablePlasma(player.inventory, (TileEntityTablePlasma) tileEntity);
            }
        }
        return null;
    }
    
}
