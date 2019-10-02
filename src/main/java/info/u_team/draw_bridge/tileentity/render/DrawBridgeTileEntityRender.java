package info.u_team.draw_bridge.tileentity.render;

import java.util.Random;

import com.mojang.blaze3d.platform.GlStateManager;

import info.u_team.draw_bridge.block.DrawBridgeBlock;
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

public class DrawBridgeTileEntityRender extends TileEntityRenderer<DrawBridgeTileEntity> {
	
	private BlockRendererDispatcher blockRenderer = Minecraft.getInstance().getBlockRendererDispatcher();
	
	@Override
	public void render(DrawBridgeTileEntity tileEntityIn, double x, double y, double z, float partialTicks, int destroyStage) {
		BlockPos pos = tileEntityIn.getPos();
		World world = this.getWorld();
		Direction facing = world.getBlockState(pos).get(DrawBridgeBlock.FACING);
		if (tileEntityIn.getExtentState() < 10 && tileEntityIn.getExtentState() > 0) {
			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder bufferbuilder = tessellator.getBuffer();
			this.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
			RenderHelper.disableStandardItemLighting();
			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
			GlStateManager.enableBlend();
			GlStateManager.disableCull();
			if (Minecraft.isAmbientOcclusionEnabled()) {
				GlStateManager.shadeModel(7425);
			} else {
				GlStateManager.shadeModel(7424);
			}
			
			BlockModelRenderer.enableCache();
			bufferbuilder.begin(7, DefaultVertexFormats.BLOCK);
			
			BlockState[] states = tileEntityIn.getBlocksToRender();
			for (int i = 0; i < states.length; i++) {
				int off = i + 1;
				BlockPos tmppos = pos.offset(facing, off);
				bufferbuilder.setTranslation(x - tmppos.getX() + (facing.getXOffset() * off) - tileEntityIn.getOffsetX(partialTicks), y - tmppos.getY() + (facing.getYOffset() * off) - tileEntityIn.getOffsetY(partialTicks), z - tmppos.getZ() + (facing.getZOffset() * off) - tileEntityIn.getOffsetZ(partialTicks));
				this.renderStateModel(tmppos, states[i], bufferbuilder, world, false);
			}
			
			bufferbuilder.setTranslation(0.0D, 0.0D, 0.0D);
			tessellator.draw();
			BlockModelRenderer.disableCache();
			RenderHelper.enableStandardItemLighting();
		}
	}
	
	@Override
	public boolean isGlobalRenderer(DrawBridgeTileEntity te) {
		return true;
	}
	
	@SuppressWarnings("deprecation")
	private boolean renderStateModel(BlockPos pos, BlockState state, BufferBuilder buffer, World world, boolean checkSides) {
		if (blockRenderer == null)
			blockRenderer = Minecraft.getInstance().getBlockRendererDispatcher();
		return this.blockRenderer.getBlockModelRenderer().renderModel(world, this.blockRenderer.getModelForState(state), state, pos, buffer, checkSides, new Random(), state.getPositionRandom(pos));
	}
	
}
