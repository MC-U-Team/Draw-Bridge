package info.u_team.draw_bridge.model;

import java.util.List;
import java.util.Random;

import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import info.u_team.draw_bridge.block.BlockDrawBridge;
import info.u_team.draw_bridge.init.DrawBridgeBlocks;
import info.u_team.draw_bridge.util.BlockStateUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BakedModelDrawBridge implements IBakedModel {
	
	private final IBakedModel defaultModel;
	
	public BakedModelDrawBridge(IBakedModel model) {
		defaultModel = model;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, Random rand) {
		return getModel(state).getQuads(state, side, rand);
	}
	
	private IBakedModel getModel(IBlockState state) {
		IBakedModel model = defaultModel;
		
		ItemStack stack = new ItemStack(Item.getItemById(state.get(BlockDrawBridge.ITEMSTACK)));
		IBlockState newBlockState = BlockStateUtil.getBlockState(stack);
			
		if (newBlockState != null && newBlockState.getBlock() != Blocks.AIR && newBlockState.getBlock() != DrawBridgeBlocks.draw_bridge) {
			model = Minecraft.getInstance().getBlockRendererDispatcher().getBlockModelShapes().getModel(newBlockState);
		}
		return model;
	}
	
	@Override
	public TextureAtlasSprite getParticleTexture() {
		return defaultModel.getParticleTexture();
	}
	
	@Override
	public boolean isAmbientOcclusion() {
		return defaultModel.isAmbientOcclusion();
	}
	
	@Override
	public boolean isGui3d() {
		return defaultModel.isGui3d();
	}
	
	@Override
	public boolean isBuiltInRenderer() {
		return defaultModel.isBuiltInRenderer();
	}
	
	@Override
	public ItemOverrideList getOverrides() {
		return defaultModel.getOverrides();
	}
	
	@Override
	public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
		return Pair.of(this, defaultModel.handlePerspective(cameraTransformType).getRight());
	}
	
}
