package info.u_team.draw_bridge.tileentity;

import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.EnumSkyBlock;
import net.minecraftforge.client.model.pipeline.*;

/**
 * @author MrTroble
 *
 */
public class SpecialDrawbridgeRender extends TileEntitySpecialRenderer<DBMDrawbridgeTile> {
	
	@Override
	public void render(DBMDrawbridgeTile te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		// if (te.hasRender()) {
		// GlStateManager.pushMatrix();
		// float brightness = te.getWorld().getLightFor(EnumSkyBlock.SKY, te.getPos());
		//
		// RenderUtil.setupLightmapCoords(te.getPos(), te.getWorld());
		// RenderUtil.bindBlockTexture();
		// RenderHelper.disableStandardItemLighting();
		// GlStateManager.disableLighting();
		// GlStateManager.enableNormalize();
		// GlStateManager.enableBlend();
		// GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		// GlStateManager.shadeModel(GL11.GL_SMOOTH);
		//
		// GlStateManager.translate(x, y, z);
		// GlStateManager.color(1, 1, 1);
		// ItemStack stack = te.getRender();
		// GlStateManager.enableLighting();
		// @SuppressWarnings("deprecation")
		// IBlockState state =
		// Block.getBlockFromItem(stack.getItem()).getStateFromMeta(stack.getMetadata());
		// state = state.getActualState(te.getWorld(), te.getPos());
		// IBakedModel model =
		// Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(state);
		// Tessellator tes = Tessellator.getInstance();
		// BufferBuilder build = tes.getBuffer();
		// build.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
		// for(EnumFacing face : EnumFacing.VALUES) {
		// List<BakedQuad> quads = model.getQuads(state, face, 0);
		// quads.forEach(quad -> {
		// LightUtil.putBakedQuad(this, quad);
		// });
		// }
		// tes.draw();
		// GlStateManager.disableNormalize();
		// GlStateManager.disableBlend();
		// GlStateManager.shadeModel(GL11.GL_FLAT);
		// RenderHelper.enableStandardItemLighting();
		// GlStateManager.popMatrix();
		// }
	}
	
}
