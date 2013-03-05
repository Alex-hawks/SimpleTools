package simpletools.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import simpletools.common.SimpleTools;
import simpletools.common.containers.ContainerTableAssembly;
import simpletools.common.tileentities.TileEntityTableAssembly;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiTableAssembly extends GuiContainer
{
	private TileEntityTableAssembly tileEntity;
	private int containerWidth;
	private int containerHeight;

	public GuiTableAssembly(InventoryPlayer par1InventoryPlayer, TileEntityTableAssembly tileEntity)
	{
		super(new ContainerTableAssembly(par1InventoryPlayer, tileEntity));
		this.tileEntity = tileEntity; 
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
		int var1 = (this.width - this.xSize) / 2;
		int var2 = (this.height - this.ySize) / 2;
		this.controlList.clear();
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		this.fontRenderer.drawString(this.tileEntity.getInvName(), 22, 6, 4210752);
		this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
	{
		int var4 = this.mc.renderEngine.getTexture(SimpleTools.TEXTURE_PATH + "TableAssembly.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(var4);

		containerWidth = (this.width - this.xSize) / 2;
		containerHeight = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(containerWidth, containerHeight, 0, 0, this.xSize, this.ySize);
	}
}
