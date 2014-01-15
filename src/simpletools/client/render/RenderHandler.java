package simpletools.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

import simpletools.api.SimpleToolsItems;
import simpletools.client.model.ModelPlasmaTorch;
import simpletools.common.SimpleTools;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderHandler implements ISimpleBlockRenderingHandler
{
    public ModelPlasmaTorch plasmaTorch = new ModelPlasmaTorch();
    public final int RENDER_ID;
    
    private static final ResourceLocation textureLocation = new ResourceLocation(SimpleTools.DOMAIN, "/blocks/plasmaTorch.png");
    
    public RenderHandler()
    {
        SimpleTools.STLogger.warning("Registered Render Handler");
        this.RENDER_ID = RenderingRegistry.getNextAvailableRenderId();
    }
    
    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer)
    {
        if (block.blockID == SimpleToolsItems.plasmaTorch.blockID)
        {
            GL11.glPushMatrix();
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, FMLClientHandler.instance().getClient().renderEngine.getTexture(textureLocation).getGlTextureId());
            GL11.glTranslatef(0.5F, 2.5F, 0.5F);
            GL11.glScalef(2F, -2F, -2F);
            this.plasmaTorch.render(null, 0, 0, 0, 0, 0, 0.0625F);
            GL11.glPopMatrix();
        }
    }
    
    @Override
    public boolean renderWorldBlock(IBlockAccess world, int X, int Y, int Z, Block block, int modelId,
            RenderBlocks renderer)
    {
        if (modelId == this.RENDER_ID)
        {
            double x = X + 0.5;
            double y = Y + 0.5;
            double z = Z + 0.5;
            Tessellator tessellator = Tessellator.instance;
            
            double minX = x - 0.125;
            double maxX = x + 0.125;
            double minY = y - 0.125;
            double maxY = y + 0.125;
            double minZ = z - 0.125;
            double maxZ = z + 0.125;
            
            double textureMinU = block.getBlockTextureFromSide(0).getMinU();
            double textureMaxU = block.getBlockTextureFromSide(0).getMaxU();
            double textureMinV = block.getBlockTextureFromSide(0).getMinV();
            double textureMaxV = block.getBlockTextureFromSide(0).getMaxV();
            
            tessellator.setBrightness(block.getMixedBrightnessForBlock(world, X, Y, Z));
            
            tessellator.addVertexWithUV(minX, minY, maxZ, textureMinU, textureMinV);
            tessellator.addVertexWithUV(maxX, minY, maxZ, textureMaxU, textureMinV);
            tessellator.addVertexWithUV(maxX, maxY, maxZ, textureMaxU, textureMaxV);
            tessellator.addVertexWithUV(minX, maxY, maxZ, textureMinU, textureMaxV);
            
            tessellator.addVertexWithUV(minX, minY, minZ, textureMinU, textureMinV);
            tessellator.addVertexWithUV(minX, minY, maxZ, textureMaxU, textureMinV);
            tessellator.addVertexWithUV(minX, maxY, maxZ, textureMaxU, textureMaxV);
            tessellator.addVertexWithUV(minX, maxY, minZ, textureMinU, textureMaxV);
            
            tessellator.addVertexWithUV(maxX, minY, minZ, textureMinU, textureMinV);
            tessellator.addVertexWithUV(minX, minY, minZ, textureMaxU, textureMinV);
            tessellator.addVertexWithUV(minX, maxY, minZ, textureMaxU, textureMaxV);
            tessellator.addVertexWithUV(maxX, maxY, minZ, textureMinU, textureMaxV);
            
            tessellator.addVertexWithUV(maxX, minY, maxZ, textureMinU, textureMinV);
            tessellator.addVertexWithUV(maxX, minY, minZ, textureMaxU, textureMinV);
            tessellator.addVertexWithUV(maxX, maxY, minZ, textureMaxU, textureMaxV);
            tessellator.addVertexWithUV(maxX, maxY, maxZ, textureMinU, textureMaxV);
            
            tessellator.addVertexWithUV(minX, maxY, maxZ, textureMinU, textureMinV);
            tessellator.addVertexWithUV(maxX, maxY, maxZ, textureMaxU, textureMinV);
            tessellator.addVertexWithUV(maxX, maxY, minZ, textureMaxU, textureMaxV);
            tessellator.addVertexWithUV(minX, maxY, minZ, textureMinU, textureMaxV);
            
            tessellator.addVertexWithUV(minX, minY, minZ, textureMinU, textureMinV);
            tessellator.addVertexWithUV(maxX, minY, minZ, textureMaxU, textureMinV);
            tessellator.addVertexWithUV(maxX, minY, maxZ, textureMaxU, textureMaxV);
            tessellator.addVertexWithUV(minX, minY, maxZ, textureMinU, textureMaxV);
        }
        return true;
    }
    
    @Override
    public boolean shouldRender3DInInventory()
    {
        return true;
    }
    
    @Override
    public int getRenderId()
    {
        return this.RENDER_ID;
    }
    
}
