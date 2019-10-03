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

public class DrawBridgeTileEntityRender extends TileEntityRenderer<DrawBridgeTileEntity> {
	
	private BlockRendererDispatcher blockRenderer = Minecraft.getInstance().getBlockRendererDispatcher();
	
	@Override
	public void render(DrawBridgeTileEntity tileEntityIn, double x, double y, double z, float partialTicks, int destroyStage) {
		BlockPos pos = tileEntityIn.getPos();
		World world = this.getWorld();
		Direction facing = tileEntityIn.getFacing();
		BlockState[] states = tileEntityIn.getBlocksToRender();
		if (states.length <= 0)
			return;
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
			
			GlStateManager.scaled(1.0001, 1.0001, 1.0001);
			
			BlockModelRenderer.enableCache();
			bufferbuilder.begin(7, DefaultVertexFormats.BLOCK);
			
			float off = tileEntityIn.getOffset();
			for (int i = 1; i <= tileEntityIn.getExtentState() + 1; i++) {
				int of = tileEntityIn.getExtentState() - i + 1;
				BlockPos tmppos = pos.offset(facing, i);
				bufferbuilder.setTranslation(x - tmppos.getX() + (facing.getXOffset() * (i - off)), y - tmppos.getY() + (facing.getYOffset() * (i - off)), z - tmppos.getZ() + (facing.getZOffset() * (i - off)));
				this.renderStateModel(tmppos, states[of], bufferbuilder, world, false);
			}
			
			GlStateManager.scaled(1, 1, 1);
			
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
