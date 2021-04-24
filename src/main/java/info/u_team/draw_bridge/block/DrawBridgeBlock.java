package info.u_team.draw_bridge.block;

import java.util.function.Function;
import java.util.function.Supplier;

import info.u_team.draw_bridge.init.DrawBridgeItemGroups;
import info.u_team.draw_bridge.init.DrawBridgeTileEntityTypes;
import info.u_team.draw_bridge.tileentity.DrawBridgeTileEntity;
import info.u_team.u_team_core.block.UTileEntityBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;

public class DrawBridgeBlock extends UTileEntityBlock {
	
	public static final EnumProperty<Direction> FACING = BlockStateProperties.FACING;
	
	protected static final Material DRAW_BRIDGE_MATERIAL = new Material.Builder(MaterialColor.IRON).notOpaque().build();
	
	public DrawBridgeBlock() {
		this(Properties.create(DRAW_BRIDGE_MATERIAL));
	}
	
	protected DrawBridgeBlock(Properties properties) {
		super(DrawBridgeItemGroups.GROUP, properties.hardnessAndResistance(1.5F).harvestTool(ToolType.PICKAXE).notSolid().variableOpacity().setOpaque(BlockState::hasOpaqueCollisionShape), DrawBridgeTileEntityTypes.DRAW_BRIDGE);
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
		if (!(newState.getBlock() instanceof DrawBridgeBlock)) {
			isTileEntityFromType(world, pos).map(DrawBridgeTileEntity.class::cast).ifPresent(drawBridge -> {
				InventoryHelper.dropInventoryItems(world, pos, drawBridge.getSlots().getInventory());
				InventoryHelper.dropInventoryItems(world, pos, drawBridge.getRenderSlot().getInventory());
				world.updateComparatorOutputLevel(pos, this);
			});
			super.onReplaced(state, world, pos, newState, isMoving);
		}
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
		return getRenderBlockStateProperty(world, pos, renderState -> renderState.getShape(world, pos, context), VoxelShapes::fullCube);
	}
	
	@Override
	public VoxelShape getCollisionShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
		return getRenderBlockStateProperty(world, pos, renderState -> renderState.getCollisionShape(world, pos, context), VoxelShapes::fullCube);
	}
	
	@Override
	public SoundType getSoundType(BlockState state, IWorldReader world, BlockPos pos, Entity entity) {
		return getRenderBlockStateProperty(world, pos, renderState -> renderState.getSoundType(world, pos, entity), () -> soundType);
	}
	
	@Override
	public int getLightValue(BlockState state, IBlockReader world, BlockPos pos) {
		return getRenderBlockStateProperty(world, pos, renderState -> renderState.getLightValue(world, pos), () -> 0);
	}
	
	@Override
	public boolean propagatesSkylightDown(BlockState state, IBlockReader world, BlockPos pos) {
		return getRenderBlockStateProperty(world, pos, renderState -> renderState.propagatesSkylightDown(world, pos), () -> false);
	}
	
	@OnlyIn(Dist.CLIENT)
	@Override
	public float getAmbientOcclusionLightValue(BlockState state, IBlockReader world, BlockPos pos) {
		return getRenderBlockStateProperty(world, pos, renderState -> renderState.getAmbientOcclusionLightValue(world, pos), () -> 0.2F);
	}
	
	private <T> T getRenderBlockStateProperty(IBlockReader world, BlockPos pos, Function<BlockState, T> function, Supplier<T> other) {
		return isTileEntityFromType(world, pos) //
				.map(DrawBridgeTileEntity.class::cast) //
				.filter(DrawBridgeTileEntity::hasRenderBlockState) //
				.map(drawBridge -> function.apply(drawBridge.getRenderBlockState())) //
				.orElseGet(other);
	}
	
}
