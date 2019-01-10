package info.u_team.draw_bridge.gui;

import java.io.IOException;

import com.google.common.collect.Lists;

import info.u_team.draw_bridge.DrawBridgeConstants;
import info.u_team.draw_bridge.container.ContainerDrawBridge;
import info.u_team.draw_bridge.tileentity.TileEntityDrawBridge;
import info.u_team.u_team_core.gui.UGuiContainerTileEntity;
import net.minecraft.client.gui.*;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiSlider;
import net.minecraftforge.fml.relauncher.*;

@SideOnly(Side.CLIENT)
public class GuiDrawBridge extends UGuiContainerTileEntity {
	
	public static final ResourceLocation BACKGROUND = new ResourceLocation(DrawBridgeConstants.MODID, "textures/gui/drawbridge.png");
	
	private final TileEntityDrawBridge drawbridge;
	
	private GuiSlider speedSlider;
	private GuiButtonToggle toggleRedstone;
	
	public GuiDrawBridge(TileEntityDrawBridge tileentity, EntityPlayer entityplayer) {
		super(new ContainerDrawBridge(tileentity, entityplayer), BACKGROUND);
		drawbridge = tileentity;
		ySize = 180;
	}
	
	@Override
	public void initGui() {
		super.initGui();
		speedSlider = new GuiSlider(0, guiLeft + 7, guiTop + 60, 90, 20, I18n.format(DrawBridgeConstants.MODID + ":container.drawbridge.speed"), I18n.format(DrawBridgeConstants.MODID + ":container.drawbridge.ticks"), 0, 100, drawbridge.getSpeed(), false, true, (slider) -> {
			drawbridge.setSpeed(slider.getValueInt());
			drawbridge.syncClientToServer(drawbridge.getPos());
		});
		
		toggleRedstone = new GuiButtonToggle(1, guiLeft + 100, guiTop + 60, 20, 20, drawbridge.needsRedstone());
		toggleRedstone.initTextureValues(xSize, 0, 20, 20, BACKGROUND);
		
		addButton(toggleRedstone);
		addButton(speedSlider);
		// Slider must be added after, cause it has a bug to not reset the colors
		// (or toggle button is wrong with not clearing the color
	}
	
	@Override
	public void handleServerDataOnFirstArrival(NBTTagCompound compound) {
		speedSlider.setValue(drawbridge.getSpeed());
		speedSlider.updateSlider();
		toggleRedstone.setStateTriggered(drawbridge.needsRedstone());
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		super.actionPerformed(button);
		if (button.id == 1) {
			toggleRedstone.setStateTriggered(!toggleRedstone.isStateTriggered());
			drawbridge.setNeedsRedstone(toggleRedstone.isStateTriggered());
			drawbridge.syncClientToServer(drawbridge.getPos());
		}
	}
	
	@Override
	public void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		fontRenderer.drawString(I18n.format(DrawBridgeConstants.MODID + ":container.drawbridge"), 8, 6, 4210752);
		fontRenderer.drawString(I18n.format("container.inventory"), 8, ySize - 96 + 2, 4210752);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		renderHoveredToolTip(mouseX, mouseY);
		if (toggleRedstone.isMouseOver()) {
			drawHoveringText(Lists.newArrayList(I18n.format(DrawBridgeConstants.MODID + ":container.drawbridge.needsrs")), mouseX, mouseY, fontRenderer);
		}
	}
}
