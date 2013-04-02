package simpletools.client.misc;

import java.util.EnumSet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import simpletools.common.interfaces.IAssembledTool;
import universalelectricity.core.electricity.ElectricityDisplay;
import universalelectricity.core.electricity.ElectricityDisplay.ElectricUnit;
import universalelectricity.core.item.IItemElectric;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.TickType;

public class STRenderTickHandler implements ITickHandler
{
    public static final STRenderTickHandler INSTANCE = new STRenderTickHandler();
    
    @Override
    public void tickStart(EnumSet<TickType> type, Object... tickData)
    {
        
    }
    
    @Override
    public void tickEnd(EnumSet<TickType> type, Object... tickData)
    {
        EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
        if (player != null
                && !(Loader.isModLoaded("" /* NEI */) || Minecraft
                        .getMinecraft().currentScreen != null))
        {
            int location = 0;
            if (Loader.isModLoaded("mmmPowersuits"))
            {
                location++;
            }
            for (int i = 0; i < 9; i++)
            {
                if (player.inventory.getStackInSlot(i) != null
                        && player.inventory.getStackInSlot(i).getItem() instanceof IAssembledTool)
                {
                    ItemStack tool = player.inventory.getStackInSlot(i);
                    String display = ((IAssembledTool) player.inventory
                            .getStackInSlot(i).getItem())
                            .getStoredForDisplay(tool);
                    this.drawString("Slot <>:  ".replaceAll("<>", i + 1 + "")
                            + display, 1, 1 + 10 * location++);
                }
                
                else if (player.inventory.getStackInSlot(i) != null
                        && player.inventory.getStackInSlot(i).getItem() instanceof IItemElectric)
                {
                    ItemStack itemStack = player.inventory.getStackInSlot(i);
                    IItemElectric item = (IItemElectric) itemStack.getItem();
                    String display = ElectricityDisplay.getDisplayShort(
                            item.getJoules(itemStack), ElectricUnit.JOULES)
                            + " / "
                            + ElectricityDisplay.getDisplayShort(
                                    item.getJoules(itemStack),
                                    ElectricUnit.JOULES);
                    this.drawString("Slot <>:  ".replaceAll("<>", i + 1 + "")
                            + display, 1, 1 + 10 * location++);
                }
            }
        }
    }
    
    @Override
    public EnumSet<TickType> ticks()
    {
        return EnumSet.of(TickType.RENDER);
    }
    
    @Override
    public String getLabel()
    {
        return "SimpleToolsRender";
    }
    
    public void drawString(String toDraw, double x, double y)
    {
        RenderHelper.disableStandardItemLighting();
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(toDraw,
                (int) x, (int) y, 0xFFFFFFFF);
    }
}
