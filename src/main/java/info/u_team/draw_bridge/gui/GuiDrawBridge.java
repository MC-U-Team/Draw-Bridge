package info.u_team.draw_bridge.gui;

import java.io.IOException;

import com.google.common.collect.Lists;

import info.u_team.draw_bridge.DrawBridgeConstants;
import info.u_team.draw_bridge.container.ContainerDrawBridge;
import info.u_team.draw_bridge.tileentity.TileEntityDrawBridge;
import info.u_team.u_team_core.gui.UGuiContainer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiButtonToggle;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiSlider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiDrawBridge extends UGuiContainer {
	
	public static final ResourceLocation BACKGROUND = new ResourceLocation(DrawBridgeConstants.MODID, "textures/gui/drawbridge.png");
	
	private final TileEntityDrawBridge drawbridge;
	
	private GuiSlider speedSlider;
	private GuiButtonToggle toggle;
	
	private int fixValueSpeedSlider, fixValueRedstone;
	
	public GuiDrawBridge(TileEntityDrawBridge tileentity, EntityPlayer entityplayer) {
		super(new ContainerDrawBridge(tileentity, entityplayer), BACKGROUND);
		drawbridge = tileentity;
		ySize = 180;
	}
	
	@Override
	public void initGui() {
		super.initGui();
		fixValueSpeedSlider = fixValueRedstone = 0;
		toggle = new GuiButtonToggle(1, guiLeft + 100, guiTop + 60, 20, 20, drawbridge.needsRedstone());
		toggle.initTextureValues(xSize, 0, 20, 20, BACKGROUND);
		buttonList.add(toggle);
		speedSlider = new GuiSlider(0, guiLeft + 7, guiTop + 60, 90, 20, I18n.format(DrawBridgeConstants.MODID + ":container.drawbridge.speed"), I18n.format(DrawBridgeConstants.MODID + ":container.drawbridge.ticks"), 0, 100, drawbridge.getSpeed(), false, true, (slider) -> {
			drawbridge.setSpeed(slider.getValueInt());
			drawbridge.syncClientToServer(drawbridge.getPos());
		});
		buttonList.add(speedSlider);
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		super.actionPerformed(button);
		if (button.id == 1) {
			toggle.setStateTriggered(!toggle.isStateTriggered());
			drawbridge.setNeedsRedstone(toggle.isStateTriggered());
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
		if (toggle.isMouseOver()) {
			drawHoveringText(Lists.newArrayList("Needs Redstone to activate?"), mouseX, mouseY, fontRenderer);
		}
		// Dumb but how should we know exactly when it is ready? Maybe the
		// handleServerData should call a method on the container which then can call
		// the gui to update its states
		if (fixValueSpeedSlider < 5 && speedSlider.getValueInt() == 0) {
			speedSlider.setValue(drawbridge.getSpeed());
			if (speedSlider.getValueInt() > 0) {
				speedSlider.updateSlider();
			}
			fixValueSpeedSlider++;
		}
		if (fixValueRedstone < 5 && !toggle.isStateTriggered()) {
			toggle.setStateTriggered(drawbridge.needsRedstone());
			fixValueRedstone++;
		}
	}
}
