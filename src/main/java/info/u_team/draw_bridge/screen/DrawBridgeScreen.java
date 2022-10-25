package info.u_team.draw_bridge.screen;

import java.util.stream.Collectors;

import com.mojang.blaze3d.vertex.PoseStack;

import info.u_team.draw_bridge.DrawBridgeMod;
import info.u_team.draw_bridge.container.DrawBridgeContainer;
import info.u_team.draw_bridge.tileentity.DrawBridgeTileEntity;
import info.u_team.u_team_core.gui.elements.ScalableButton;
import info.u_team.u_team_core.gui.elements.ScalableSlider;
import info.u_team.u_team_core.screen.UContainerMenuScreen;
import info.u_team.u_team_core.util.WidgetUtil;
import io.netty.buffer.Unpooled;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.StateSwitchingButton;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.block.state.StateHolder;

public class DrawBridgeScreen extends UContainerMenuScreen<DrawBridgeContainer> {
	
	private static final ResourceLocation BACKGROUND = new ResourceLocation(DrawBridgeMod.MODID, "textures/gui/draw_bridge.png");
	private static final ResourceLocation NEED_REDSTONE_TEXTURE = new ResourceLocation(DrawBridgeMod.MODID, "textures/gui/need_redstone_button.png");
	
	private final Component needRedstoneTextComponent;
	private final Component speedTextComponent;
	private final Component ticksTextComponent;
	private final Component cycleBlockStateTextComponent;
	private final Component blockStateTextComponent;
	private final Component noCycleBlockStateTextComponent;
	private final Component camouflageTextComponent;
	
	private ScalableSlider slider;
	private ScalableButton renderStateButton;
	
	public DrawBridgeScreen(DrawBridgeContainer container, Inventory playerInventory, Component title) {
		super(container, playerInventory, title, BACKGROUND, 212, 168);
		backgroundWidth = backgroundHeight = 512;
		setTextLocation(8, 6, 26, imageHeight - 94);
		
		final String langKey = "container.drawbridge.draw_bridge.";
		
		needRedstoneTextComponent = Component.translatable(langKey + "need_redstone");
		speedTextComponent = Component.translatable(langKey + "speed");
		ticksTextComponent = Component.translatable(langKey + "ticks");
		cycleBlockStateTextComponent = Component.translatable(langKey + "cycle_block_state");
		blockStateTextComponent = Component.translatable(langKey + "block_state");
		noCycleBlockStateTextComponent = Component.translatable(langKey + "no_cycle_block_state").withStyle(ChatFormatting.RED);
		camouflageTextComponent = Component.translatable(langKey + "camouflage");
	}
	
	@Override
	protected void init() {
		super.init();
		final DrawBridgeTileEntity drawBridge = menu.getBlockEntity();
		
		final StateSwitchingButton redstoneToggleButton = addRenderableWidget(new StateSwitchingButton(leftPos + 105, topPos + 35, 18, 18, drawBridge.isNeedRedstone()) {
			
			@Override
			public void onClick(double mouseX, double mouseY) {
				final boolean newState = !drawBridge.isNeedRedstone();
				setStateTriggered(newState);
				menu.getNeedRedstoneMessage().triggerMessage();
			}
			
			@Override
			public void renderToolTip(PoseStack matrixStack, int mouseX, int mouseY) {
				if (WidgetUtil.isHovered(this)) {
					renderTooltip(matrixStack, needRedstoneTextComponent, mouseX, mouseY);
				}
			}
		});
		redstoneToggleButton.initTextureValues(0, 0, 18, 18, NEED_REDSTONE_TEXTURE);
		
		slider = addRenderableWidget(new ScalableSlider(leftPos + 7, topPos + 57, 90, 13, speedTextComponent.plainCopy().append(" "), Component.literal(" ").append(ticksTextComponent.plainCopy()), 0, 100, drawBridge.getSpeed(), false, true, true, 0.75F) {
			
			@Override
			public void onRelease(double mouseX, double mouseY) {
				super.onRelease(mouseX, mouseY);
				menu.getSpeedMessage().triggerMessage(() -> new FriendlyByteBuf(Unpooled.buffer(1).writeByte(getValueInt())));
			}
		});
		
		renderStateButton = addRenderableWidget(new ScalableButton(leftPos + 150, topPos + 57, 54, 13, cycleBlockStateTextComponent, 0.5F));
		renderStateButton.setPressable(() -> {
			menu.getCamouflageBlockStateMessage().triggerMessage();
		});
		renderStateButton.setTooltip((button, matrixStack, mouseX, mouseY) -> {
			if (WidgetUtil.isHovered(button)) {
				if (drawBridge.hasRenderBlockState()) {
					if (drawBridge.getRenderBlockState().getBlock().getStateDefinition().getPossibleStates().size() > 1) {
						final String blockStateString = drawBridge.getRenderBlockState().getValues().entrySet().stream().map(StateHolder.PROPERTY_ENTRY_TO_STRING_FUNCTION).collect(Collectors.joining(","));
						renderTooltip(matrixStack, blockStateTextComponent.plainCopy().append(": ").append(Component.literal(blockStateString).withStyle(ChatFormatting.GREEN)), mouseX, mouseY);
					} else {
						renderTooltip(matrixStack, noCycleBlockStateTextComponent, mouseX, mouseY);
					}
				}
			}
		});
		
		updateRenderState();
	}
	
	@Override
	protected void renderLabels(PoseStack matrixStack, int mouseX, int mouseY) {
		super.renderLabels(matrixStack, mouseX, mouseY);
		font.draw(matrixStack, camouflageTextComponent, 148, 6, 4210752);
	}
	
	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int mouseButton) {
		if (slider != null) {
			slider.mouseReleased(mouseX, mouseY, mouseButton);
		}
		return super.mouseReleased(mouseX, mouseY, mouseButton);
	}
	
	@Override
	public void containerTick() {
		super.containerTick();
		updateRenderState();
	}
	
	private void updateRenderState() {
		if (renderStateButton != null) {
			final DrawBridgeTileEntity drawBridge = menu.getBlockEntity();
			renderStateButton.active = drawBridge.hasRenderBlockState() && drawBridge.getRenderBlockState().getBlock().getStateDefinition().getPossibleStates().size() > 1;
		}
	}
}
