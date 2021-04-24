package info.u_team.draw_bridge.screen;

import java.util.stream.Collectors;

import com.mojang.blaze3d.matrix.MatrixStack;

import info.u_team.draw_bridge.DrawBridgeMod;
import info.u_team.draw_bridge.container.DrawBridgeContainer;
import info.u_team.draw_bridge.tileentity.DrawBridgeTileEntity;
import info.u_team.draw_bridge.util.DrawBridgeCamouflageRenderTypes;
import info.u_team.u_team_core.gui.elements.ScalableButton;
import info.u_team.u_team_core.gui.elements.ScalableSlider;
import info.u_team.u_team_core.screen.UBasicContainerScreen;
import info.u_team.u_team_core.util.WidgetUtil;
import io.netty.buffer.Unpooled;
import net.minecraft.block.Block;
import net.minecraft.client.gui.widget.ToggleWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;
import net.minecraft.state.StateHolder;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

public class DrawBridgeScreen extends UBasicContainerScreen<DrawBridgeContainer> {
	
	private static final ResourceLocation BACKGROUND = new ResourceLocation(DrawBridgeMod.MODID, "textures/gui/draw_bridge.png");
	private static final ResourceLocation NEED_REDSTONE_TEXTURE = new ResourceLocation(DrawBridgeMod.MODID, "textures/gui/need_redstone_button.png");
	
	private final ITextComponent needRedstoneTextComponent;
	private final ITextComponent speedTextComponent;
	private final ITextComponent ticksTextComponent;
	private final ITextComponent renderTypeTextComponent;
	private final ITextComponent cycleBlockStateTextComponent;
	private final ITextComponent blockStateTextComponent;
	private final ITextComponent noCycleBlockStateTextComponent;
	private final ITextComponent camouflageTextComponent;
	
	private ScalableSlider slider;
	private ScalableButton renderStateButton;
	
	private Block currentBlock;
	
	public DrawBridgeScreen(DrawBridgeContainer container, PlayerInventory playerInventory, ITextComponent title) {
		super(container, playerInventory, title, BACKGROUND, 212, 168);
		backgroundWidth = backgroundHeight = 512;
		setTextLocation(8, 6, 26, ySize - 94);
		
		final String langKey = "container.drawbridge.draw_bridge.";
		
		needRedstoneTextComponent = new TranslationTextComponent(langKey + "need_redstone");
		speedTextComponent = new TranslationTextComponent(langKey + "speed");
		ticksTextComponent = new TranslationTextComponent(langKey + "ticks");
		renderTypeTextComponent = new TranslationTextComponent(langKey + "render_type");
		cycleBlockStateTextComponent = new TranslationTextComponent(langKey + "cycle_block_state");
		blockStateTextComponent = new TranslationTextComponent(langKey + "block_state");
		noCycleBlockStateTextComponent = new TranslationTextComponent(langKey + "no_cycle_block_state").mergeStyle(TextFormatting.RED);
		camouflageTextComponent = new TranslationTextComponent(langKey + "camouflage");
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
					renderTooltip(matrixStack, needRedstoneTextComponent, mouseX, mouseY);
				}
			}
		});
		redstoneToggleButton.initTextureValues(0, 0, 18, 18, NEED_REDSTONE_TEXTURE);
		
		slider = addButton(new ScalableSlider(guiLeft + 7, guiTop + 57, 90, 13, speedTextComponent.copyRaw().appendString(" "), new StringTextComponent(" ").appendSibling(ticksTextComponent.copyRaw()), 0, 100, drawBridge.getSpeed(), false, true, true, 0.75F) {
			
			@Override
			public void onRelease(double mouseX, double mouseY) {
				super.onRelease(mouseX, mouseY);
				container.getSpeedMessage().triggerMessage(() -> new PacketBuffer(Unpooled.buffer(1).writeByte(slider.getValueInt())));
			}
		});
		
		final ScalableButton renderTypeButton = addButton(new ScalableButton(guiLeft + 150, guiTop + 17, 54, 13, ITextComponent.getTextComponentOrEmpty(null), 0.5F) {
			
			@Override
			public ITextComponent getMessage() {
				return DrawBridgeCamouflageRenderTypes.getType(currentBlock).getTextComponent();
			}
		});
		renderTypeButton.setPressable(() -> {
			container.getCamouflageTypeMessage().triggerMessage();
		});
		renderTypeButton.setTooltip((button, matrixStack, mouseX, mouseY) -> {
			if (WidgetUtil.isHovered(button)) {
				renderTooltip(matrixStack, renderTypeTextComponent, mouseX, mouseY);
			}
		});
		
		updateCurrentBlock();
		
		renderStateButton = addButton(new ScalableButton(guiLeft + 150, guiTop + 57, 54, 13, cycleBlockStateTextComponent, 0.5F));
		renderStateButton.setPressable(() -> {
			container.getCamouflageBlockStateMessage().triggerMessage();
		});
		renderStateButton.setTooltip((button, matrixStack, mouseX, mouseY) -> {
			if (WidgetUtil.isHovered(button)) {
				if (drawBridge.hasRenderBlockState()) {
					if (drawBridge.getRenderBlockState().getBlock().getStateContainer().getValidStates().size() > 1) {
						final String blockStateString = drawBridge.getRenderBlockState().getValues().entrySet().stream().map(StateHolder.printableFunction).collect(Collectors.joining(","));
						renderTooltip(matrixStack, blockStateTextComponent.copyRaw().appendString(": ").appendSibling(new StringTextComponent(blockStateString).mergeStyle(TextFormatting.GREEN)), mouseX, mouseY);
					} else {
						renderTooltip(matrixStack, noCycleBlockStateTextComponent, mouseX, mouseY);
					}
				}
			}
		});
		
		updateRenderState();
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(matrixStack, mouseX, mouseY);
		font.drawText(matrixStack, camouflageTextComponent, 148, 6, 4210752);
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
		final DrawBridgeTileEntity drawBridge = container.getTileEntity();
		currentBlock = drawBridge.getWorld().getBlockState(drawBridge.getPos()).getBlock();
	}
	
	private void updateRenderState() {
		if (renderStateButton != null) {
			final DrawBridgeTileEntity drawBridge = container.getTileEntity();
			renderStateButton.active = drawBridge.hasRenderBlockState() && drawBridge.getRenderBlockState().getBlock().getStateContainer().getValidStates().size() > 1;
		}
	}
}
