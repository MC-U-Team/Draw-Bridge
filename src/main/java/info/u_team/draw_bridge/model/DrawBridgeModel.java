package info.u_team.draw_bridge.model;

import java.util.List;

import info.u_team.draw_bridge.tileentity.DrawBridgeTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.BakedModelWrapper;
import net.minecraftforge.client.model.data.ModelData;

public class DrawBridgeModel extends BakedModelWrapper<BakedModel> {
	
	public DrawBridgeModel(BakedModel originalModel) {
		super(originalModel);
	}
	
	@Override
	public List<BakedQuad> getQuads(BlockState state, Direction side, RandomSource random, ModelData data, RenderType renderType) {
		final BakedModel model = getModel(data);
		if (model != null) {
			return model.getQuads(data.get(DrawBridgeTileEntity.BLOCKSTATE_PROPERTY), side, random, data, renderType);
		}
		return super.getQuads(state, side, random, data, renderType);
	}
	
	@Override
	public TextureAtlasSprite getParticleIcon(ModelData data) {
		final BakedModel model = getModel(data);
		if (model != null) {
			return model.getParticleIcon(data);
		}
		return super.getParticleIcon(data);
	}
	
	private BakedModel getModel(ModelData data) {
		if (data.has(DrawBridgeTileEntity.BLOCKSTATE_PROPERTY)) {
			final BlockState renderBlockState = data.get(DrawBridgeTileEntity.BLOCKSTATE_PROPERTY);
			return Minecraft.getInstance().getBlockRenderer().getBlockModelShaper().getBlockModel(renderBlockState);
		}
		return null;
	}
	
}
