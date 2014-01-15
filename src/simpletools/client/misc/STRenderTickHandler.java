package simpletools.client.misc;

import java.util.EnumSet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;

import org.lwjgl.opengl.GL11;

import simpletools.api.IAdditionalMeterData;
import simpletools.api.IAssembledTool;
import simpletools.client.TextureLocations;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class STRenderTickHandler implements ITickHandler
{
    private static final int SCALE = 4;
    
    private static final int BORDER_WIDTH = 2 * SCALE;
    private static final int TEXTURE = 64 * SCALE;
    
    
    private static final int OUTER_X = TEXTURE / 8;
    private static final int OUTER_Y = TEXTURE;
    
    private static final int INNER_X = 2 * BORDER_WIDTH;
    private static final int INNER_Y = TEXTURE - 2 * BORDER_WIDTH;
    
    
    public static final STRenderTickHandler INSTANCE = new STRenderTickHandler();
    Minecraft mc = Minecraft.getMinecraft();

    private STRenderTickHandler() { } 
    
    @Override
    public void tickStart(EnumSet<TickType> type, Object... tickData) { }
    
    @Override
    public void tickEnd(EnumSet<TickType> type, Object... tickData)
    {
        this.renderMeters();
    }
    
    @Override
    public EnumSet<TickType> ticks()
    {
        return EnumSet.of(TickType.RENDER, TickType.CLIENT);
    }
    
    @Override
    public String getLabel()
    {
        return "SimpleToolsRender";
    }
    
    public void drawString(String toDraw, double x, double y)
    {
        RenderHelper.disableStandardItemLighting();
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(toDraw, (int) x, (int) y, 0xFFFFFFFF);
    }
    
    public void renderMeters()
    {
        EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
        if (player != null)
        {
            int vertLocation = 0;
            int horizLocation = 2;
            ScaledResolution res = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
            InventoryPlayer inv = player.inventory;
            
            Gui gui;
            
            if (this.mc.currentScreen == null || this.mc.currentScreen instanceof GuiChat)
            {
                gui = this.mc.ingameGUI;
            }
            else
                gui = this.mc.currentScreen;
            
            //GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            GL11.glScalef(1.0F / SCALE, 1.0F / SCALE, 1);
            
            for (int i = 8; i >= 0; i--)
            {
                
                if (inv.getStackInSlot(i) != null && inv.getStackInSlot(i).getItem() instanceof IAdditionalMeterData)
                {
                    IAdditionalMeterData tool = (IAssembledTool) inv.getStackInSlot(i).getItem();
                    float[] energyArray = tool.getAdditionalDisplayData(inv.getStackInSlot(i));
                    
                    for (float energy : energyArray)
                    {
                        int x = SCALE * (res.getScaledWidth() - (15 * horizLocation));
                        int y = SCALE * (res.getScaledHeight() - (70 * ++vertLocation));
                        int height = (int) (INNER_Y * energy + BORDER_WIDTH);
                        
                        mc.renderEngine.bindTexture(TextureLocations.HUD_METER_FRAME);
                        gui.drawTexturedModalRect(x, y, 0, 0, OUTER_X, OUTER_Y);
                        mc.renderEngine.bindTexture(TextureLocations.HUD_METER_FILL);
                        gui.drawTexturedModalRect(x, y + (INNER_Y - height), 0, OUTER_Y - height - 2 * BORDER_WIDTH, OUTER_X, height + BORDER_WIDTH);

                    }
                }
                else if (inv.getStackInSlot(i) != null && inv.getStackInSlot(i).getItem().isDamageable())
                {
                    Item tool = inv.getStackInSlot(i).getItem();
                    
                    float damage = 1.0F - Math.min(1.0F, (tool.getDamage(inv.getStackInSlot(i)) * 1.0F / tool.getMaxDamage(inv.getStackInSlot(i))));
                    
                    int x = SCALE * (res.getScaledWidth() - (15 * horizLocation));
                    int y = SCALE * (res.getScaledHeight() - (70 * ++vertLocation));
                    int height = (int) (INNER_Y * damage + BORDER_WIDTH);
                    

                    mc.renderEngine.bindTexture(TextureLocations.HUD_METER_FRAME);
                    gui.drawTexturedModalRect(x, y, 0, 0, OUTER_X, OUTER_Y);
                    mc.renderEngine.bindTexture(TextureLocations.HUD_METER_FILL);
                    gui.drawTexturedModalRect(x, y + (INNER_Y - height), 0, OUTER_Y - height - 2 * BORDER_WIDTH, OUTER_X, height + BORDER_WIDTH);
                }
                horizLocation++;
                vertLocation = 0;
            }

            GL11.glScalef(SCALE, SCALE, 1);
        }
    }
}
