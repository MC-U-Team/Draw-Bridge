package info.u_team.draw_bridge.screen;

import com.mojang.blaze3d.matrix.MatrixStack;

import info.u_team.draw_bridge.DrawBridgeMod;
import info.u_team.draw_bridge.container.DrawBridgeContainer;
import info.u_team.draw_bridge.tileentity.DrawBridgeTileEntity;
import info.u_team.u_team_core.gui.elements.BetterFontSlider;
import info.u_team.u_team_core.screen.UContainerScreen;
import io.netty.buffer.Unpooled;
import net.minecraft.client.gui.widget.ToggleWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.*;

public class DrawBridgeScreen extends UContainerScreen<DrawBridgeContainer> {
	
	private static final ResourceLocation BACKGROUND = new ResourceLocation(DrawBridgeMod.MODID, "textures/gui/draw_bridge.png");
	
	private ToggleWidget button;
	private BetterFontSlider slider;
	
	public DrawBridgeScreen(DrawBridgeContainer container, PlayerInventory playerInventory, ITextComponent title) {
		super(container, playerInventory, title, BACKGROUND);
		ySize = 184;
	}
	
	@Override
	protected void init() {
		super.init();
		final DrawBridgeTileEntity drawBridge = container.getTileEntity();
		
		button = addButton(new ToggleWidget(guiLeft + 132, guiTop + 62, 20, 20, drawBridge.isNeedRedstone()) {
			
			@Override
			public void onClick(double mouseX, double mouseY) {
				final boolean newState = !drawBridge.isNeedRedstone();
				setStateTriggered(newState);
				container.getNeedRedstoneMessage().triggerMessage();
			}
			
			@Override
			public void renderToolTip(MatrixStack matrixStack, int mouseX, int mouseY) {
				if (isHovered()) {
					renderTooltip(matrixStack, new TranslationTextComponent("container.drawbridge.draw_bridge.need_redstone"), mouseX, mouseY);
				}
			}
		});
		button.initTextureValues(xSize, 0, 20, 20, BACKGROUND);
		
		slider = addButton(new BetterFontSlider(guiLeft + 7, guiTop + 62, 90, 20, new TranslationTextComponent("container.drawbridge.draw_bridge.speed").appendString(" "), new StringTextComponent(" ").append(new TranslationTextComponent("container.drawbridge.draw_bridge.ticks")), 0, 100, drawBridge.getSpeed(), false, true, 1, null) {
			
			@Override
			public void onRelease(double mouseX, double mouseY) {
				super.onRelease(mouseX, mouseY);
				container.getSpeedMessage().triggerMessage(() -> new PacketBuffer(Unpooled.buffer(1).writeByte(slider.getValueInt())));
			}
		});
	}
	
	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		renderBackground(matrixStack);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		buttons.forEach(button -> button.renderToolTip(matrixStack, mouseX, mouseY));
		renderHoveredTooltip(matrixStack, mouseX, mouseY);
	}
	
	@Override
	public void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int mouseX, int mouseY) {
		font.func_243246_a(matrixStack, title, 8, 6, 4210752);
		font.func_243246_a(matrixStack, playerInventory.getDisplayName(), 8.0F, ySize - 94, 4210752);
	}
	
	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int mouseButton) {
		if (slider != null) {
			slider.mouseReleased(mouseX, mouseY, mouseButton);
		}
		return super.mouseReleased(mouseX, mouseY, mouseButton);
	}
	
	@Override
	public void tick() {
		super.tick();
	}
	
}
