package info.u_team.draw_bridge.model;

import java.util.*;

import info.u_team.draw_bridge.tileentity.DrawBridgeTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.*;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IEnviromentBlockReader;
import net.minecraftforge.api.distmarker.*;
import net.minecraftforge.client.model.BakedModelWrapper;
import net.minecraftforge.client.model.data.IModelData;

@OnlyIn(Dist.CLIENT)
public class DrawBridgeModel extends BakedModelWrapper<IBakedModel> {
	
	public DrawBridgeModel(IBakedModel originalModel) {
		super(originalModel);
	}
	
	@Override
	public List<BakedQuad> getQuads(BlockState state, Direction side, Random rand, IModelData extraData) {
		if (extraData.hasProperty(DrawBridgeTileEntity.BLOCKSTATE_PROPERTY)) {
			final BlockState renderBlockState = extraData.getData(DrawBridgeTileEntity.BLOCKSTATE_PROPERTY);
			return Minecraft.getInstance().getBlockRendererDispatcher().getBlockModelShapes().getModel(renderBlockState).getQuads(renderBlockState, side, rand, extraData);
		}
		return super.getQuads(state, side, rand, extraData);
	}
	
	@Override
	public IModelData getModelData(IEnviromentBlockReader world, BlockPos pos, BlockState state, IModelData tileData) {
		return super.getModelData(world, pos, state, tileData);
	}
	
}
