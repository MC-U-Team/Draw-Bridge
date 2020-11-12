package info.u_team.draw_bridge.model;

import java.util.*;

import info.u_team.draw_bridge.tileentity.DrawBridgeTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Direction;
import net.minecraftforge.client.model.BakedModelWrapper;
import net.minecraftforge.client.model.data.IModelData;

public class DrawBridgeModel extends BakedModelWrapper<IBakedModel> {
	
	public DrawBridgeModel(IBakedModel originalModel) {
		super(originalModel);
	}
	
	@Override
	public List<BakedQuad> getQuads(BlockState state, Direction side, Random rand, IModelData data) {
		final IBakedModel model = getModel(data);
		if (model != null) {
			return model.getQuads(data.getData(DrawBridgeTileEntity.BLOCKSTATE_PROPERTY), side, rand, data);
		}
		return super.getQuads(state, side, rand, data);
	}
	
	@Override
	public TextureAtlasSprite getParticleTexture(IModelData data) {
		final IBakedModel model = getModel(data);
		if (model != null) {
			return model.getParticleTexture(data);
		}
		return super.getParticleTexture(data);
	}
	
	private IBakedModel getModel(IModelData data) {
		if (data.hasProperty(DrawBridgeTileEntity.BLOCKSTATE_PROPERTY)) {
			final BlockState renderBlockState = data.getData(DrawBridgeTileEntity.BLOCKSTATE_PROPERTY);
			return Minecraft.getInstance().getBlockRendererDispatcher().getBlockModelShapes().getModel(renderBlockState);
		}
		return null;
	}
	
}
