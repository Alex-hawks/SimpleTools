package simpletools.common.block;

import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import simpletools.common.SimpleTools;
import universalelectricity.prefab.BlockMachine;
import universalelectricity.prefab.tile.TileEntityAdvanced;

public class BlockTableAssembly extends BlockMachine
{

	public BlockTableAssembly(int par1)
	{
		super(par1, Material.iron);
		this.setRequiresSelfNotify();
		this.blockHardness = -1;
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
}
