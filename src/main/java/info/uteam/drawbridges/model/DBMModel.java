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

package info.uteam.drawbridges.model;

import java.util.List;
import java.util.function.Function;

import com.google.common.collect.ImmutableList;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.*;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.common.model.IModelState;

/**
 * @author MrTroble
 *
 */
public class DBMModel implements IModel {

	/* (non-Javadoc)
	 * @see net.minecraftforge.client.model.IModel#bake(net.minecraftforge.common.model.IModelState, net.minecraft.client.renderer.vertex.VertexFormat, java.util.function.Function)
	 */
	@Override
	public IBakedModel bake(IModelState state, VertexFormat format,
			Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
		return new DBMBakedModel();
	}
	
	static class DBMBakedModel implements IBakedModel {

		/* (non-Javadoc)
		 * @see net.minecraft.client.renderer.block.model.IBakedModel#getQuads(net.minecraft.block.state.IBlockState, net.minecraft.util.EnumFacing, long)
		 */
		@Override
		public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
			return ImmutableList.of();
		}

		/* (non-Javadoc)
		 * @see net.minecraft.client.renderer.block.model.IBakedModel#isAmbientOcclusion()
		 */
		@Override
		public boolean isAmbientOcclusion() {
			return false;
		}

		/* (non-Javadoc)
		 * @see net.minecraft.client.renderer.block.model.IBakedModel#isGui3d()
		 */
		@Override
		public boolean isGui3d() {
			return false;
		}

		/* (non-Javadoc)
		 * @see net.minecraft.client.renderer.block.model.IBakedModel#isBuiltInRenderer()
		 */
		@Override
		public boolean isBuiltInRenderer() {
			return false;
		}

		/* (non-Javadoc)
		 * @see net.minecraft.client.renderer.block.model.IBakedModel#getParticleTexture()
		 */
		@Override
		public TextureAtlasSprite getParticleTexture() {
			return null;
		}

		/* (non-Javadoc)
		 * @see net.minecraft.client.renderer.block.model.IBakedModel#getOverrides()
		 */
		@Override
		public ItemOverrideList getOverrides() {
			return ItemOverrideList.NONE;
		}
		
	}

}
