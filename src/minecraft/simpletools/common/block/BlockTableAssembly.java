package simpletools.common.block;

import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import simpletools.common.SimpleTools;
import simpletools.common.tileentities.TileEntityTableAssembly;
import universalelectricity.prefab.BlockMachine;
import universalelectricity.prefab.implement.ISneakUseWrench;
import universalelectricity.prefab.tile.TileEntityAdvanced;

public class BlockTableAssembly extends BlockMachine implements ISneakUseWrench
{

	public BlockTableAssembly(int par1)
	{
		super(par1, Material.iron);
		this.setRequiresSelfNotify();
		this.blockHardness = -1;
		this.setBlockName("tableAssembly");
	}
	
	@Override
	public String getTextureFile()
	{
		return SimpleTools.BLOCK_TEXTURES;
	}
	
	@Override
	public void onBlockPlacedBy(World par1World, int x, int y, int z, EntityLiving par5EntityLiving)
	{
		int metadata = par1World.getBlockMetadata(x, y, z);

		int angle = MathHelper.floor_double((par5EntityLiving.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
		int change = 3;

		switch (angle)
		{
			case 0:
				par1World.setBlockMetadata(x, y, z, 1);
				break;
			case 1:
				par1World.setBlockMetadata(x, y, z, 2);
				break;
			case 2:
				par1World.setBlockMetadata(x, y, z, 0);
				break;
			case 3:
				par1World.setBlockMetadata(x, y, z, 3);
				break;
		}

		((TileEntityAdvanced) par1World.getBlockTileEntity(x, y, z)).initiate();
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
				change = 3;
				break;
			case 3:
				change = 1;
				break;
			case 1:
				change = 2;
				break;
			case 2:
				change = 0;
				break;
		}

		par1World.setBlockMetadata(x, y, z, change);

		((TileEntityAdvanced) par1World.getBlockTileEntity(x, y, z)).initiate();

		return true;
	}
	
	@Override
	public boolean onMachineActivated(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer, int side, float hitX, float hitY, float hitZ)
	{
		if (!par1World.isRemote)
		{
			par5EntityPlayer.openGui(SimpleTools.INSTANCE, 0, par1World, x, y, z);
			return true;
		}
		return true;
	}
	
	@Override
	public boolean onSneakUseWrench(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer, int side, float hitX, float hitY, float hitZ)
	{
		this.dropBlockAsItem(par1World, x, y, z, par1World.getBlockMetadata(x, y, z), 0);
        par1World.setBlockWithNotify(x, y, z, 0);
        return true;
	}
	
	@Override
	public TileEntity createNewTileEntity(World var1)
	{
		return new TileEntityTableAssembly();
	}
}
