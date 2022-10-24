package info.u_team.draw_bridge.model;

import java.util.List;
import java.util.Random;

import info.u_team.draw_bridge.tileentity.DrawBridgeTileEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraftforge.client.model.BakedModelWrapper;
import net.minecraftforge.client.model.data.IModelData;

public class DrawBridgeModel extends BakedModelWrapper<BakedModel> {
	
	public DrawBridgeModel(BakedModel originalModel) {
		super(originalModel);
	}
	
	@Override
	public List<BakedQuad> getQuads(BlockState state, Direction side, Random rand, IModelData data) {
		final BakedModel model = getModel(data);
		if (model != null) {
			return model.getQuads(data.getData(DrawBridgeTileEntity.BLOCKSTATE_PROPERTY), side, rand, data);
		}
		return super.getQuads(state, side, rand, data);
	}
	
	@Override
	public TextureAtlasSprite getParticleTexture(IModelData data) {
		final BakedModel model = getModel(data);
		if (model != null) {
			return model.getParticleTexture(data);
		}
		return super.getParticleTexture(data);
	}
	
	private BakedModel getModel(IModelData data) {
		if (data.hasProperty(DrawBridgeTileEntity.BLOCKSTATE_PROPERTY)) {
			final BlockState renderBlockState = data.getData(DrawBridgeTileEntity.BLOCKSTATE_PROPERTY);
			return Minecraft.getInstance().getBlockRenderer().getBlockModelShaper().getBlockModel(renderBlockState);
		}
		return null;
	}
	
}
