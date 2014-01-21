package simpletools.client.gui;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;
import simpletools.client.TextureLocations;
import simpletools.common.containers.ContainerTablePlasma;
import simpletools.common.tileentities.TileEntityTablePlasma;

public class GuiTablePlasma extends GuiContainer
{
    private TileEntityTablePlasma tileEntity;
    private int containerWidth;
    private int containerHeight;

    public GuiTablePlasma(InventoryPlayer par1InventoryPlayer, TileEntityTablePlasma tileEntity)
    {        
        super(new ContainerTablePlasma(par1InventoryPlayer, tileEntity));
        this.tileEntity = tileEntity;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        this.fontRenderer.drawString(this.tileEntity.getInvName(), 48, 6, 4210752);
        this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2,
                4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(TextureLocations.GUI_TABLE_PLASMA);
        int plasma = (int) (this.tileEntity.getPlasma() / this.tileEntity.getMaxPlasma() * 50);
        int fuel = (int) (this.tileEntity.getFuel() / this.tileEntity.getMaxFuel() * 50);
        int energy = (int) (((float) this.tileEntity.getEnergy(null)) / this.tileEntity.getEnergyCapacity(null) * 50);
        
        int plasmaTop = 50 - plasma;
        int fuelTop = 50 - fuel;
        int energyTop = 50 - energy;
        
        this.containerWidth = (this.width - this.xSize) / 2;
        this.containerHeight = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(this.containerWidth, this.containerHeight, 0, 0, this.xSize, this.ySize);
        this.drawTexturedModalRect(this.containerWidth + 136, this.containerHeight + 18 + fuelTop, 176, 0 + fuelTop, 10, fuel);
        this.drawTexturedModalRect(this.containerWidth + 154, this.containerHeight + 18 + plasmaTop, 188, 0 + plasmaTop, 10, plasma);
        this.drawTexturedModalRect(this.containerWidth + 118, this.containerHeight + 18 + energyTop, 200, 0 + energyTop, 10, energy);
    }
}
