package info.u_team.draw_bridge.block;

import java.util.function.Function;
import java.util.function.Supplier;

import info.u_team.draw_bridge.init.DrawBridgeItemGroups;
import info.u_team.draw_bridge.init.DrawBridgeTileEntityTypes;
import info.u_team.draw_bridge.tileentity.DrawBridgeTileEntity;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class DrawBridgeBlock extends UEntityBlock {
	
	public static final EnumProperty<Direction> FACING = BlockStateProperties.FACING;
	
	protected static final Material DRAW_BRIDGE_MATERIAL = new Material.Builder(MaterialColor.METAL).notSolidBlocking().build();
	
	public DrawBridgeBlock() {
		this(Properties.of(DRAW_BRIDGE_MATERIAL));
	}
	
	protected DrawBridgeBlock(Properties properties) {
		super(DrawBridgeItemGroups.GROUP, properties.strength(1.5F).noOcclusion().dynamicShape().isRedstoneConductor(BlockState::isCollisionShapeFullBlock), DrawBridgeTileEntityTypes.DRAW_BRIDGE);
		registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH));
	}
	
	// Trigger drawbridge
	
	@Override
	public void neighborChanged(BlockState state, Level world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
		if (world.isClientSide) {
			return;
		}
		getBlockEntity(world, pos).map(DrawBridgeTileEntity.class::cast).ifPresent(DrawBridgeTileEntity::neighborChanged);
	}
	
	// Open gui
	
	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		return openMenu(world, pos, player, true);
	}
	
	// Drop items
	
	@SuppressWarnings("deprecation")
	@Override
	public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
		if (!(newState.getBlock() instanceof DrawBridgeBlock)) {
			getBlockEntity(world, pos).map(DrawBridgeTileEntity.class::cast).ifPresent(drawBridge -> {
				Containers.dropContents(world, pos, drawBridge.getSlots().getInventory());
				Containers.dropContents(world, pos, drawBridge.getRenderSlot().getInventory());
				world.updateNeighbourForOutputSignal(pos, this);
			});
			super.onRemove(state, world, pos, newState, isMoving);
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
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return getRenderBlockStateProperty(world, pos, renderState -> renderState.getShape(world, pos, context), Shapes::block);
	}
	
	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return getRenderBlockStateProperty(world, pos, renderState -> renderState.getCollisionShape(world, pos, context), Shapes::block);
	}
	
	@Override
	public SoundType getSoundType(BlockState state, LevelReader world, BlockPos pos, Entity entity) {
		return getRenderBlockStateProperty(world, pos, renderState -> renderState.getSoundType(world, pos, entity), () -> soundType);
	}
	
	@Override
	public int getLightEmission(BlockState state, BlockGetter world, BlockPos pos) {
		return getRenderBlockStateProperty(world, pos, renderState -> renderState.getLightEmission(world, pos), () -> 0);
	}
	
	@Override
	public boolean propagatesSkylightDown(BlockState state, BlockGetter world, BlockPos pos) {
		return getRenderBlockStateProperty(world, pos, renderState -> renderState.propagatesSkylightDown(world, pos), () -> false);
	}
	
	@OnlyIn(Dist.CLIENT)
	@Override
	public float getShadeBrightness(BlockState state, BlockGetter world, BlockPos pos) {
		return getRenderBlockStateProperty(world, pos, renderState -> renderState.getShadeBrightness(world, pos), () -> 0.2F);
	}
	
	private <T> T getRenderBlockStateProperty(BlockGetter world, BlockPos pos, Function<BlockState, T> function, Supplier<T> other) {
		return getBlockEntity(world, pos) //
				.map(DrawBridgeTileEntity.class::cast) //
				.filter(DrawBridgeTileEntity::hasRenderBlockState) //
				.map(drawBridge -> function.apply(drawBridge.getRenderBlockState())) //
				.orElseGet(other);
	}
	
}
