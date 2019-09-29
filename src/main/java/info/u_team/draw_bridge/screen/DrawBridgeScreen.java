package info.u_team.draw_bridge.screen;

import info.u_team.draw_bridge.DrawBridgeMod;
import info.u_team.draw_bridge.container.DrawBridgeContainer;
import info.u_team.draw_bridge.tileentity.DrawBridgeTileEntity;
import info.u_team.u_team_core.gui.UContainerScreen;
import info.u_team.u_team_core.gui.elements.BetterFontSlider;
import net.minecraft.client.gui.widget.ToggleWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.*;

@OnlyIn(Dist.CLIENT)
public class DrawBridgeScreen extends UContainerScreen<DrawBridgeContainer> {
	
	private static final ResourceLocation BACKGROUND = new ResourceLocation(DrawBridgeMod.MODID, "textures/gui/draw_bridge.png");
	
	private BetterFontSlider slider;
	
	public DrawBridgeScreen(DrawBridgeContainer container, PlayerInventory playerInventory, ITextComponent title) {
		super(container, playerInventory, title, BACKGROUND);
		ySize = 184;
	}
	
	@Override
	protected void init() {
		super.init();
		final DrawBridgeTileEntity drawBridge = container.getTileEntity();
		
		final ToggleWidget button = addButton(new ToggleWidget(guiLeft + 132, guiTop + 62, 20, 20, drawBridge.isNeedRedstone()) {
			
			@Override
			public void onClick(double mouseX, double mouseY) {
				final boolean newState = !drawBridge.isNeedRedstone();
				drawBridge.setNeedRedstone(newState);
				setStateTriggered(newState);
				drawBridge.sendDataToServer();
			}
			
			@Override
			public void renderToolTip(int mouseX, int mouseY) {
				if (isHovered()) {
					renderTooltip("Need redstone power?", mouseX, mouseY);
				}
			}
		});
		button.initTextureValues(xSize, 0, 20, 20, BACKGROUND);
		
		addButton(slider = new BetterFontSlider(guiLeft + 7, guiTop + 62, 90, 20, "Speed: ", " Ticks", 0, 100, drawBridge.getSpeed(), false, true, 1, null) {
			
			@Override
			public void onRelease(double mouseX, double mouseY) {
				super.onRelease(mouseX, mouseY);
				drawBridge.setSpeed(slider.getValueInt());
				drawBridge.sendDataToServer();
			}
		});
	}
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		renderBackground();
		super.render(mouseX, mouseY, partialTicks);
		buttons.forEach(button -> button.renderToolTip(mouseX, mouseY));
		renderHoveredToolTip(mouseX, mouseY);
	}
	
	@Override
	public void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		font.drawString(title.getFormattedText(), 8, 6, 4210752);
		font.drawString(playerInventory.getDisplayName().getFormattedText(), 8.0F, ySize - 94, 4210752);
	}
	
	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int mouseButton) {
		if (slider != null) {
			slider.mouseReleased(mouseX, mouseY, mouseButton);
		}
		return super.mouseReleased(mouseX, mouseY, mouseButton);
	}
	
}
