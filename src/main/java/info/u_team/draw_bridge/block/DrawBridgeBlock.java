package info.u_team.draw_bridge.block;

import info.u_team.draw_bridge.init.*;
import info.u_team.draw_bridge.tileentity.DrawBridgeTileEntity;
import info.u_team.u_team_core.block.UTileEntityBlock;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.World;

public class DrawBridgeBlock extends UTileEntityBlock {
	
	public static final EnumProperty<Direction> FACING = BlockStateProperties.FACING;
	
	public DrawBridgeBlock(String name) {
		super(name, DrawBridgeItemGroups.GROUP, Properties.create(Material.IRON).hardnessAndResistance(1.5F), () -> DrawBridgeTileEntityTypes.DRAW_BRIDGE);
		setDefaultState(getDefaultState().with(FACING, Direction.NORTH));
	}
	
	// Trigger drawbridge
	
	@Override
	public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
		if (world.isRemote) {
			return;
		}
		isTileEntityFromType(world, pos).map(DrawBridgeTileEntity.class::cast).ifPresent(DrawBridgeTileEntity::neighborChanged);
	}
	
	// Open gui
	
	@Override
	public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		return openContainer(world, pos, player, true);
	}
	
	// Drop items
	
	@SuppressWarnings("deprecation")
	@Override
	public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
		isTileEntityFromType(world, pos).map(DrawBridgeTileEntity.class::cast).ifPresent(drawBridge -> {
			drawBridge.getSlots().ifPresent(inventory -> InventoryHelper.dropInventoryItems(world, pos, inventory));
			drawBridge.getRenderSlot().ifPresent(inventory -> InventoryHelper.dropInventoryItems(world, pos, inventory));
			world.updateComparatorOutputLevel(pos, this);
		});
		super.onReplaced(state, world, pos, newState, isMoving);
	}
	
	// Facing stuff
	
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return getDefaultState().with(FACING, context.getNearestLookingDirection().getOpposite());
	}
	
	@Override
	public BlockState rotate(BlockState state, Rotation rotation) {
		return state.with(FACING, rotation.rotate(state.get(FACING)));
	}
	
	@Override
	public BlockState mirror(BlockState state, Mirror mirror) {
		return state.rotate(mirror.toRotation(state.get(FACING)));
	}
	
	@Override
	protected void fillStateContainer(Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}
}