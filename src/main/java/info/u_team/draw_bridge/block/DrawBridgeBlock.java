package info.u_team.draw_bridge.block;

import info.u_team.draw_bridge.init.*;
import info.u_team.draw_bridge.tileentity.DrawBridgeTileEntity;
import info.u_team.draw_bridge.util.InventoryStackHandler;
import info.u_team.u_team_core.block.UTileEntityBlock;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.shapes.*;
import net.minecraft.world.*;

public class DrawBridgeBlock extends UTileEntityBlock {
	
	public static final EnumProperty<Direction> FACING = BlockStateProperties.FACING;
	
	public DrawBridgeBlock() {
		super(DrawBridgeItemGroups.GROUP, Properties.create(Material.IRON).hardnessAndResistance(1.5F).notSolid().variableOpacity(), DrawBridgeTileEntityTypes.DRAW_BRIDGE);
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
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		return openContainer(world, pos, player, true);
	}
	
	// Drop items
	
	@SuppressWarnings("deprecation")
	@Override
	public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
		isTileEntityFromType(world, pos).map(DrawBridgeTileEntity.class::cast).ifPresent(drawBridge -> {
			drawBridge.getSlots().map(InventoryStackHandler::getInventory).ifPresent(inventory -> InventoryHelper.dropInventoryItems(world, pos, inventory));
			drawBridge.getRenderSlot().map(InventoryStackHandler::getInventory).ifPresent(inventory -> InventoryHelper.dropInventoryItems(world, pos, inventory));
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
	
	@SuppressWarnings("deprecation")
	@Override
	public BlockState mirror(BlockState state, Mirror mirror) {
		return state.rotate(mirror.toRotation(state.get(FACING)));
	}
	
	@Override
	protected void fillStateContainer(Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}
	
	// Simulate camouflage block
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
		return isTileEntityFromType(world, pos).map(DrawBridgeTileEntity.class::cast).filter(DrawBridgeTileEntity::hasRenderBlockState).map(drawBridge -> {
			return drawBridge.getRenderBlockState().getShape(world, pos, context);
		}).orElse(VoxelShapes.fullCube());
	}
	
	@Override
	public SoundType getSoundType(BlockState state, IWorldReader world, BlockPos pos, Entity entity) {
		return isTileEntityFromType(world, pos).map(DrawBridgeTileEntity.class::cast).filter(DrawBridgeTileEntity::hasRenderBlockState).map(drawBridge -> {
			return drawBridge.getRenderBlockState().getSoundType(world, pos, entity);
		}).orElse(super.getSoundType(state, world, pos, entity));
	}
	
	@Override
	public int getLightValue(BlockState state, IBlockReader world, BlockPos pos) {
		return isTileEntityFromType(world, pos).map(DrawBridgeTileEntity.class::cast).filter(DrawBridgeTileEntity::hasRenderBlockState).map(drawBridge -> {
			return drawBridge.getRenderBlockState().getLightValue(world, pos);
		}).orElse(super.getLightValue(state, world, pos));
	}
}
