/*-*****************************************************************************
 * Copyright 2018 U-Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/

package info.uteam.drawbridges.tiles;

import java.util.List;

import info.uteam.drawbridges.DBMConstants;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;

/**
 * @author MrTroble
 *
 */
public class SpecialDrawbridgeRender extends TileEntitySpecialRenderer<DBMDrawbridgeTile> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer#render(net
	 * .minecraft.tileentity.TileEntity, double, double, double, float, int, float)
	 */
	@Override
	public void render(DBMDrawbridgeTile te, double x, double y, double z, float partialTicks, int destroyStage,
			float alpha) {
		if (te.hasRender()) {
			GlStateManager.pushMatrix();
			GlStateManager.translate(x, y, z);
			GlStateManager.color(1, 1, 1);
			IBlockState state = te.getRender().getActualState(te.getWorld(), te.getPos());
			DBMConstants.LOGGER.info(state);
            IBakedModel model = Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(state);
            state = state.getBlock().getExtendedState(state, te.getWorld(), te.getPos());
            Minecraft.getMinecraft().getBlockRendererDispatcher().renderBlock(state, te.getPos(), te.getWorld(), Tessellator.getInstance().getBuffer());
			GlStateManager.popMatrix();
		}
	}
	
	private void renderModel(IBakedModel model, int color) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(7, DefaultVertexFormats.ITEM);
		
		for (EnumFacing enumfacing : EnumFacing.values()) {
			this.renderQuads(bufferbuilder, model.getQuads((IBlockState) null, enumfacing, 0L), color);
		}
		
		this.renderQuads(bufferbuilder, model.getQuads((IBlockState) null, (EnumFacing) null, 0L), color);
		tessellator.draw();
}
	

	private void renderQuads(BufferBuilder renderer, List<BakedQuad> quads, int color) {
		boolean flag = color == 1;
		int i = 0;
		
		for (int j = quads.size(); i < j; ++i) {
			BakedQuad bakedquad = quads.get(i);
			int k = color;
			
			if (flag && bakedquad.hasTintIndex()) {								
				k = k | -16777216;
			}
			
			net.minecraftforge.client.model.pipeline.LightUtil.renderQuadColor(renderer, bakedquad, k);
		}
}

}
