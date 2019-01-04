package info.u_team.draw_bridge.model;

import java.util.List;

import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import info.u_team.draw_bridge.block.BlockDrawBridge;
import info.u_team.draw_bridge.init.DrawBridgeBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fml.relauncher.*;

@SideOnly(Side.CLIENT)
public class BakedModelDrawBridge implements IBakedModel {
	
	private final IBakedModel defaultModel;
	
	public BakedModelDrawBridge(IBakedModel model) {
		defaultModel = model;
	}
	
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
		return getModel(state).getQuads(state, side, rand);
	}
	
	@SuppressWarnings("deprecation")
	private IBakedModel getModel(IBlockState state) {
		IBakedModel model = defaultModel;
		
		if (state instanceof IExtendedBlockState) {
			IExtendedBlockState extended = (IExtendedBlockState) state;
			ItemStack stack = extended.getValue(BlockDrawBridge.ITEMSTACK);
			
			if (stack != null && !stack.isEmpty()) {
				Block block = Block.getBlockFromItem(stack.getItem());
				IBlockState newBlockState = block.getStateFromMeta(stack.getMetadata());
				
				if (newBlockState != Blocks.AIR.getDefaultState() && newBlockState.getBlock() != DrawBridgeBlocks.draw_bridge) {
					model = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelForState(newBlockState);
				}
			}
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
