package info.u_team.draw_bridge.block;

import info.u_team.draw_bridge.init.DrawBridgeBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.math.*;
import net.minecraft.world.IBlockReader;

public class DrawBridgeCamouflage extends DrawBridgeBlock {
	
	@Override
	public BlockItem getBlockItem() {
		return null;
	}
	
	@Override
	public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player) {
		return new ItemStack(DrawBridgeBlocks.DRAW_BRIDGE.get());
	}
	
}
