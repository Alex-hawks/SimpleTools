package simpletools.common.block;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import simpletools.common.SimpleTools;
import simpletools.common.misc.SimpleToolsCreativeTab;
import simpletools.common.tileentities.TileEntityTableAssembly;
import calclavia.lib.prefab.block.BlockAdvanced;
import calclavia.lib.prefab.tile.TileAdvanced;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockTableAssembly extends BlockAdvanced implements ITileEntityProvider
{
    Icon top;
    Icon bottom;
    Icon side1;
    Icon side2;
    
    public BlockTableAssembly(int par1)
    {
        super(par1, Material.iron);
        this.blockHardness = -1;
        this.setUnlocalizedName("tableAssembly");
        this.setCreativeTab(SimpleToolsCreativeTab.INSTANCE);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int side, int meta)
    {
        switch (side)
        {
            case 0:
                return this.bottom;
            case 1:
                return this.top;
        }
        if (side == meta + 2 || ForgeDirection.getOrientation(side).getOpposite().ordinal() == meta + 2)
            return this.side1;
        else if ((side & 3) + 1 == meta
                || (ForgeDirection.getOrientation(side).getOpposite().ordinal() & 3) + 1 == meta)
            return this.side2;
        else if ((side & 3) - 1 == meta
                || (ForgeDirection.getOrientation(side).getOpposite().ordinal() & 3) - 1 == meta)
            return this.side2;
        return this.bottom;
    }
    
    @Override
    public void registerIcons(IconRegister par1IconRegister)
    {
        
        this.top = par1IconRegister.registerIcon(SimpleTools.DOMAIN + "tableAssembleTop");
        this.bottom = par1IconRegister.registerIcon(SimpleTools.DOMAIN + "tableAssembleBottom");
        this.side1 = par1IconRegister.registerIcon(SimpleTools.DOMAIN + "tableAssembleSide1");
        this.side2 = par1IconRegister.registerIcon(SimpleTools.DOMAIN + "tableAssembleSide2");
    }
    
    @Override
    public void onBlockPlacedBy(World par1World, int x, int y, int z, EntityLivingBase par5EntityLiving, ItemStack par6ItemStack)
    {
        int angle = MathHelper.floor_double(par5EntityLiving.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
        switch (angle)
        {
            case 0:
                par1World.setBlock(x, y, z, this.blockID, 1, 0x04);
                break;
            case 1:
                par1World.setBlock(x, y, z, this.blockID, 2, 0x04);
                break;
            case 2:
                par1World.setBlock(x, y, z, this.blockID, 0, 0x04);
                break;
            case 3:
                par1World.setBlock(x, y, z, this.blockID, 3, 0x04);
                break;
        }
        
        par1World.notifyBlocksOfNeighborChange(x, y, z, this.blockID);
    }
    
    @Override
    public boolean onUseWrench(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer, int side, float hitX, float hitY, float hitZ)
    {
        int metadata = par1World.getBlockMetadata(x, y, z);
        
        int change = 0;
        
        // Re-orient the block
        switch (metadata)
        {
            case 0:
                change = 1;
                break;
            case 1:
                change = 2;
                break;
            case 2:
                change = 5;
                break;
            case 3:
                change = 4;
                break;
            case 4:
                change = 0;
                break;
            case 5:
                change = 3;
                break;
        }
        
        par1World.setBlock(x, y, z, this.blockID, change, 0x04);
        par1World.markBlockForRenderUpdate(x, y, z);
        
        return true;
    }
    
    @Override
    public boolean onMachineActivated(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer, int side,
            float hitX, float hitY, float hitZ)
    {
        //if (!par1World.isRemote)
        //{
            par5EntityPlayer.openGui(SimpleTools.INSTANCE, 0, par1World, x, y, z);
            return true;
        //}
        //return true;
    }
    
    @Override
    public boolean onSneakUseWrench(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer, int side,
            float hitX, float hitY, float hitZ)
    {
        if (!par5EntityPlayer.capabilities.isCreativeMode)
        {
            this.dropBlockAsItem(par1World, x, y, z, par1World.getBlockMetadata(x, y, z), 0);
        }
        par1World.setBlock(x, y, z, 0, 0, 0x04);
        return true;
    }
    
    @Override
    public TileEntity createNewTileEntity(World world)
    {
        return new TileEntityTableAssembly();
    }
}
