package simpletools.common.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import simpletools.client.SimpleToolsClientProxy;
import simpletools.common.SimpleTools;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockPlasmaTorch extends Block
{
    
    public BlockPlasmaTorch(int id)
    {
        super(id, Material.glass);
        this.blockHardness = -1;
        this.setUnlocalizedName("plasmaTorch");
        this.setCreativeTab(null);
        this.setLightValue(1.0f);
        this.setHardness(-1);
        this.setBlockBounds(0.375f, 0.375f, 0.375f, 0.625f, 0.625f, 0.625f);
    }
    
    @Override
    public int quantityDropped(Random par1Random)
    {
        return 0;
    }
    
    @Override
    public int idDropped(int par1, Random par2Random, int par3)
    {
        return 0;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderType()
    {
        return SimpleToolsClientProxy.plasmaTorch.getRenderId();
    }
    
    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }
    
    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }
    
    @Override
    public void registerIcons(IconRegister i)
    {
        this.blockIcon = i.registerIcon(this.getUnlocalizedName().replaceAll("tile.", SimpleTools.TEXTURE_NAME_PREFIX));
    }
}
