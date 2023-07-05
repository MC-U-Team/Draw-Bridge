package info.u_team.draw_bridge.block;

import java.util.function.Function;
import java.util.function.Supplier;

import info.u_team.draw_bridge.blockentity.DrawBridgeBlockEntity;
import info.u_team.draw_bridge.init.DrawBridgeBlockEntityTypes;
import info.u_team.u_team_core.block.UEntityBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class DrawBridgeBlock extends UEntityBlock {
	
	public static final EnumProperty<Direction> FACING = BlockStateProperties.FACING;
	
	public DrawBridgeBlock() {
		super(Properties.of().strength(1.5F).mapColor(MapColor.METAL).noOcclusion().dynamicShape().isRedstoneConductor(BlockState::isCollisionShapeFullBlock), DrawBridgeBlockEntityTypes.DRAW_BRIDGE);
		registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH));
	}
	
	// Tick drawbridge
	
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		if ((type != blockEntityType.get()) || level.isClientSide()) {
			return null;
		}
		return (level_, pos, state_, instance) -> DrawBridgeBlockEntity.serverTick(level_, pos, state_, (DrawBridgeBlockEntity) instance);
	}
	
	// Trigger drawbridge
	
	@Override
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
		if (level.isClientSide) {
			return;
		}
		getBlockEntity(level, pos).map(DrawBridgeBlockEntity.class::cast).ifPresent(DrawBridgeBlockEntity::neighborChanged);
	}
	
	// Open gui
	
	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		return openMenu(level, pos, player, true);
	}
	
	// Drop items
	
	@SuppressWarnings("deprecation")
	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
		if (!state.is(newState.getBlock())) {
			getBlockEntity(level, pos).map(DrawBridgeBlockEntity.class::cast).ifPresent(drawBridge -> {
				Containers.dropContents(level, pos, drawBridge.getSlots().getInventory());
				Containers.dropContents(level, pos, drawBridge.getRenderSlot().getInventory());
				level.updateNeighbourForOutputSignal(pos, this);
			});
			super.onRemove(state, level, pos, newState, isMoving);
		}
	}
	
	// Facing stuff
	
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return defaultBlockState().setValue(FACING, context.getNearestLookingDirection().getOpposite());
	}
	
	@Override
	public BlockState rotate(BlockState state, Rotation rotation) {
		return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public BlockState mirror(BlockState state, Mirror mirror) {
		return state.rotate(mirror.getRotation(state.getValue(FACING)));
	}
	
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}
	
	// Simulate camouflage block
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return getRenderBlockStateProperty(level, pos, renderState -> renderState.getShape(level, pos, context), Shapes::block);
	}
	
	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return getRenderBlockStateProperty(level, pos, renderState -> renderState.getCollisionShape(level, pos, context), Shapes::block);
	}
	
	@Override
	public SoundType getSoundType(BlockState state, LevelReader level, BlockPos pos, Entity entity) {
		return getRenderBlockStateProperty(level, pos, renderState -> renderState.getSoundType(level, pos, entity), () -> soundType);
	}
	
	@Override
	public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
		return getRenderBlockStateProperty(level, pos, renderState -> renderState.getLightEmission(level, pos), () -> 0);
	}
	
	@Override
	public boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos) {
		return getRenderBlockStateProperty(level, pos, renderState -> renderState.propagatesSkylightDown(level, pos), () -> false);
	}
	
	@Override
	public float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) {
		return getRenderBlockStateProperty(level, pos, renderState -> renderState.getShadeBrightness(level, pos), () -> 0.2F);
	}
	
	private <T> T getRenderBlockStateProperty(BlockGetter level, BlockPos pos, Function<BlockState, T> function, Supplier<T> other) {
		return getBlockEntity(level, pos) //
				.map(DrawBridgeBlockEntity.class::cast) //
				.filter(DrawBridgeBlockEntity::hasRenderBlockState) //
				.map(drawBridge -> function.apply(drawBridge.getRenderBlockState())) //
				.orElseGet(other);
	}
	
}
