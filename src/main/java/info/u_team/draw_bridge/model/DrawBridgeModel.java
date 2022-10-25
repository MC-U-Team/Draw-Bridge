package info.u_team.draw_bridge.model;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import org.apache.logging.log4j.LogManager;

import info.u_team.draw_bridge.blockentity.DrawBridgeBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.ChunkRenderTypeSet;
import net.minecraftforge.client.model.BakedModelWrapper;
import net.minecraftforge.client.model.data.ModelData;

public class DrawBridgeModel extends BakedModelWrapper<BakedModel> {
	
	public DrawBridgeModel(BakedModel originalModel) {
		super(originalModel);
	}
	
	@Override
	public List<BakedQuad> getQuads(BlockState state, Direction side, RandomSource random, ModelData data, RenderType renderType) {
		return withCamouflageModel(data, (model, camouflageState) -> model.getQuads(camouflageState, side, random, data, renderType), () -> super.getQuads(state, side, random, data, renderType));
	}
	
	@Override
	public ChunkRenderTypeSet getRenderTypes(BlockState state, RandomSource random, ModelData data) {
		return withCamouflageModel(data, (model, camouflageState) -> model.getRenderTypes(camouflageState, random, data), () -> super.getRenderTypes(state, random, data));
	}
	
	@Override
	public TextureAtlasSprite getParticleIcon(ModelData data) {
		return withCamouflageModel(data, (model, camouflageState) -> model.getParticleIcon(data), super::getParticleIcon);
	}
	
	private <T> T withCamouflageModel(ModelData data, BiFunction<BakedModel, BlockState, T> function, Supplier<T> other) {
		if (data.has(DrawBridgeBlockEntity.BLOCKSTATE_PROPERTY)) {
			final BlockState state = data.get(DrawBridgeBlockEntity.BLOCKSTATE_PROPERTY);
			final BakedModel model = Minecraft.getInstance().getBlockRenderer().getBlockModelShaper().getBlockModel(state);
			try {
				return function.apply(model, state);
			} catch (final Exception ex) {
				LogManager.getLogger().warn("Failed to call camouflage block model method for camouflage blockstate " + state, ex);
			}
		}
		return other.get();
	}
}
