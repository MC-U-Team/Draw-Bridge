package info.u_team.draw_bridge.tileentity.render;

import java.util.Random;

import com.mojang.blaze3d.platform.GlStateManager;

import info.u_team.draw_bridge.tileentity.DrawBridgeTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.data.EmptyModelData;

public class DrawBridgeTileEntityRender extends TileEntityRenderer<DrawBridgeTileEntity> {
	
	@Override
	public void render(DrawBridgeTileEntity drawBridge, double x, double y, double z, float partialTicks, int destroyStage) {
		final BlockPos pos = drawBridge.getPos();
		final World world = this.getWorld();
		final Direction facing = drawBridge.getFacing();
		final BlockState[] states = drawBridge.getBlocksToRender();
		if (states.length <= 0) {
			return;
		}
		if ((drawBridge.getExtentState() < 10 && drawBridge.getExtentState() > 0) || drawBridge.isLast()) {
			final Tessellator tessellator = Tessellator.getInstance();
			final BufferBuilder bufferBuilder = tessellator.getBuffer();
			bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
			RenderHelper.disableStandardItemLighting();
			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
			GlStateManager.enableBlend();
			GlStateManager.disableCull();
			if (Minecraft.isAmbientOcclusionEnabled()) {
				GlStateManager.shadeModel(7425);
			} else {
				GlStateManager.shadeModel(7424);
			}
			
			GlStateManager.scaled(1.0001, 1.0001, 1.0001);
			
			BlockModelRenderer.enableCache();
			bufferBuilder.begin(7, DefaultVertexFormats.BLOCK);
			
			float off = drawBridge.getOffset();
			for (int i = 1; i <= drawBridge.getExtentState() + 1; i++) {
				final int of = drawBridge.getExtentState() - i + 1;
				final BlockPos tmpPos = pos.offset(facing, i);
				bufferBuilder.setTranslation(x - tmpPos.getX() + (facing.getXOffset() * (i - off)), y - tmpPos.getY() + (facing.getYOffset() * (i - off)), z - tmpPos.getZ() + (facing.getZOffset() * (i - off)));
				renderStateModel(tmpPos, states[of], bufferBuilder, world, false);
			}
			
			GlStateManager.scaled(1, 1, 1);
			
			bufferBuilder.setTranslation(0.0D, 0.0D, 0.0D);
			tessellator.draw();
			BlockModelRenderer.disableCache();
			RenderHelper.enableStandardItemLighting();
		}
	}
	
	@Override
	public boolean isGlobalRenderer(DrawBridgeTileEntity drawBridge) {
		return true;
	}
	
	private boolean renderStateModel(BlockPos pos, BlockState state, BufferBuilder buffer, World world, boolean checkSides) {
		final BlockRendererDispatcher blockRenderer = Minecraft.getInstance().getBlockRendererDispatcher();
		return blockRenderer.getBlockModelRenderer().renderModel(world, blockRenderer.getModelForState(state), state, pos, buffer, checkSides, new Random(), state.getPositionRandom(pos), EmptyModelData.INSTANCE);
	}
	
}
