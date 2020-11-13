package info.u_team.draw_bridge.screen;

import java.util.stream.Collectors;

import com.mojang.blaze3d.matrix.MatrixStack;

import info.u_team.draw_bridge.DrawBridgeMod;
import info.u_team.draw_bridge.container.DrawBridgeContainer;
import info.u_team.draw_bridge.tileentity.DrawBridgeTileEntity;
import info.u_team.draw_bridge.util.DrawBridgeCamouflageRenderTypes;
import info.u_team.u_team_core.gui.elements.*;
import info.u_team.u_team_core.screen.UBasicContainerScreen;
import io.netty.buffer.Unpooled;
import net.minecraft.block.Block;
import net.minecraft.client.gui.widget.ToggleWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;
import net.minecraft.state.StateHolder;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.*;

public class DrawBridgeScreen extends UBasicContainerScreen<DrawBridgeContainer> {
	
	private static final ResourceLocation BACKGROUND = new ResourceLocation(DrawBridgeMod.MODID, "textures/gui/draw_bridge.png");
	private static final ResourceLocation NEED_REDSTONE_TEXTURE = new ResourceLocation(DrawBridgeMod.MODID, "textures/gui/need_redstone_button.png");
	
	private BetterFontSlider slider;
	private BetterButton renderStateButton;
	
	private Block currentBlock;
	
	public DrawBridgeScreen(DrawBridgeContainer container, PlayerInventory playerInventory, ITextComponent title) {
		super(container, playerInventory, title, BACKGROUND, 212, 168);
		backgroundWidth = backgroundHeight = 512;
		setTextLocation(8, 6, 26, ySize - 94);
	}
	
	@Override
	protected void init() {
		super.init();
		final DrawBridgeTileEntity drawBridge = container.getTileEntity();
		
		final ToggleWidget redstoneToggleButton = addButton(new ToggleWidget(guiLeft + 105, guiTop + 35, 18, 18, drawBridge.isNeedRedstone()) {
			
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
		redstoneToggleButton.initTextureValues(0, 0, 18, 18, NEED_REDSTONE_TEXTURE);
		
		slider = addButton(new BetterFontSlider(guiLeft + 7, guiTop + 57, 90, 13, new TranslationTextComponent("container.drawbridge.draw_bridge.speed").appendString(" "), new StringTextComponent(" ").append(new TranslationTextComponent("container.drawbridge.draw_bridge.ticks")), 0, 100, drawBridge.getSpeed(), false, true, 0.75F, null) {
			
			@Override
			public void onRelease(double mouseX, double mouseY) {
				super.onRelease(mouseX, mouseY);
				container.getSpeedMessage().triggerMessage(() -> new PacketBuffer(Unpooled.buffer(1).writeByte(slider.getValueInt())));
			}
		});
		
		final BetterButton renderTypeButton = addButton(new BetterButton(guiLeft + 150, guiTop + 17, 54, 13, 0.5F, ITextComponent.getTextComponentOrEmpty(null)) {
			
			@Override
			public ITextComponent getMessage() {
				return DrawBridgeCamouflageRenderTypes.getType(currentBlock).getTextComponent();
			}
			
			@Override
			public void renderToolTip(MatrixStack matrixStack, int mouseX, int mouseY) {
				if (isHovered()) {
					renderTooltip(matrixStack, new TranslationTextComponent("container.drawbridge.draw_bridge.render_type"), mouseX, mouseY);
				}
			}
		});
		renderTypeButton.setPressable(() -> {
			container.getCamouflageTypeMessage().triggerMessage();
		});
		updateCurrentBlock();
		
		renderStateButton = addButton(new BetterButton(guiLeft + 150, guiTop + 57, 54, 13, 0.5F, new TranslationTextComponent("container.drawbridge.draw_bridge.cycle_block_state")) {
			
			@Override
			public void renderToolTip(MatrixStack matrixStack, int mouseX, int mouseY) {
				if (isHovered()) {
					final DrawBridgeTileEntity tileEntity = container.getTileEntity();
					if (tileEntity.hasRenderBlockState()) {
						final String blockStateString = tileEntity.getRenderBlockState().getValues().entrySet().stream().map(StateHolder.field_235890_a_).collect(Collectors.joining(","));
						renderTooltip(matrixStack, new TranslationTextComponent("container.drawbridge.draw_bridge.block_state").appendString(": " + blockStateString), mouseX, mouseY);
					}
				}
			}
		});
		renderStateButton.setPressable(() -> {
			container.getCamouflageBlockStateMessage().triggerMessage();
		});
		updateRenderState();
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(matrixStack, mouseX, mouseY);
		font.func_243248_b(matrixStack, new TranslationTextComponent("container.drawbridge.draw_bridge.camouflage"), 148, 6, 4210752);
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
		updateCurrentBlock();
		updateRenderState();
	}
	
	private void updateCurrentBlock() {
		currentBlock = container.getTileEntity().getWorld().getBlockState(container.getTileEntity().getPos()).getBlock();
	}
	
	private void updateRenderState() {
		if (renderStateButton != null) {
			renderStateButton.active = container.getTileEntity().hasRenderBlockState();
		}
	}
}
