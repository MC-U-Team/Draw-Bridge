package info.u_team.draw_bridge.block;

import org.apache.commons.lang3.tuple.Pair;

import info.u_team.draw_bridge.DrawBridgeConstants;
import info.u_team.draw_bridge.init.DrawBridgeCreativeTabs;
import info.u_team.draw_bridge.model.ItemStackProperty;
import info.u_team.draw_bridge.tileentity.TileEntityDrawBridge;
import info.u_team.draw_bridge.util.BlockStateUtil;
import info.u_team.u_team_core.block.UBlockTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

public class BlockDrawBridge extends UBlockTileEntity {
	
	public static final DirectionProperty FACING =  DirectionProperty.create("facing", EnumFacing.Plane.HORIZONTAL);
	public static ItemStackProperty ITEMSTACK;
	public static final BooleanProperty ACTIVE = BooleanProperty.create("active");
		
	public static final TileEntityType<TileEntityDrawBridge> TILE_TYPE_DRAWBRIDGE = TileEntityType.register("drawbridge", TileEntityType.Builder.create(TileEntityDrawBridge::new));
	
	
	
	public BlockDrawBridge(String name) {
		super(name, Properties.create(Material.IRON).hardnessAndResistance(1.5f), new Item.Properties().group(DrawBridgeCreativeTabs.tab), TILE_TYPE_DRAWBRIDGE);
		setDefaultState(getDefaultState().with(FACING, EnumFacing.NORTH).with(ACTIVE, false));
		this.setRegistryName(DrawBridgeConstants.MODID, name);
	}
	
	@Override
	protected void fillStateContainer(Builder<Block, IBlockState> builder) {
		ITEMSTACK = ItemStackProperty.create("stack");
		builder.add(FACING);
		builder.add(ACTIVE);
		builder.add(ITEMSTACK);
	}
	
	// Update from redstone
	
	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block neighborBlock, BlockPos neighborPos) {
	/*	if (world.isRemote) {
			return;
		}
		
		boolean newValue = world.isBlockPowered(pos);
		
		for (EnumFacing facing : EnumFacing.values()) {
			BlockPos newPos = pos.offset(facing);
//			if (newPos.equals(neighborPos)) {
//				continue;
//			}
			IBlockState newState = world.getBlockState(newPos);
			if (newState.getBlock() instanceof BlockDrawBridge) {
				newValue = newValue | newState.get(ACTIVE);
			}
		}
		
		boolean oldValue = state.get(ACTIVE);
		if (newValue != oldValue) {
			world.setBlockState(pos, state.with(ACTIVE, newValue));
		}
		
		// TileEntityDrawBridge drawbridge = getDrawBridge(world, pos);
		// if (drawbridge == null) {
		// return;
		// }
		// drawbridge.neighborChanged();*/
	}
	
	// Open gui
	
	@Override
	public boolean onBlockActivated(IBlockState state, World worldIn, BlockPos pos, EntityPlayer player, EnumHand hand,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		return openContainer(worldIn, pos, player, true);
	}
	
	
	// Drop items from drawbridge
	@Override
	public void getDrops(IBlockState state, NonNullList<ItemStack> drops, World world, BlockPos pos, int fortune) {
		TileEntityDrawBridge drawbridge = getDrawBridge(world, pos);
	      for(int i = 0; i < drawbridge.getSizeInventory(); ++i) {
	          ItemStack itemstack = drawbridge.getStackInSlot(i);
	          if (!itemstack.isEmpty()) {
	             drops.add(itemstack);
	          }
	       }
	}
	
	// Used for light blocks
	@Override
	public int getOpacity(IBlockState state, IBlockReader worldIn, BlockPos pos) {
		IBlockState renderState = getRenderBlockState(worldIn, pos);
		if (renderState == null) {
			return 0;
		}
		return renderState.getOpacity(worldIn, pos);
	}
		
	@Override
	public int getLightValue(IBlockState state, IWorldReader world, BlockPos pos) {
		IBlockState renderState = getRenderBlockState(world, pos);
		if (renderState == null) {
			return 0;
		}
		return renderState.getLightValue(world, pos);
	}
	
	// Extended block state for rendering
	
	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockReader world, BlockPos pos) {
		ItemStack stack = getRenderItemStack(world, pos);
		if (stack == ItemStack.EMPTY) {
			return state;
		}
		return state.with(ITEMSTACK, stack.getItem().getRegistryName());
	}
	
	// Block state things	
	@Override
	public IBlockState getStateForPlacement(IBlockState state, EnumFacing facing, IBlockState state2, IWorld world,
			BlockPos pos1, BlockPos pos2, EnumHand hand) {
		return getDefaultState().with(FACING, facing);
	}
			
	@Override
	public IBlockState rotate(IBlockState state, Rotation rotation) {
		return state.with(FACING, rotation.rotate(state.get(FACING)));
	}
	
	
	@SuppressWarnings("deprecation")
	@Override
	public IBlockState mirror(IBlockState state, Mirror mirrorIn) {
		return state.rotate(mirrorIn.toRotation(state.get(FACING)));
	}
	
	// Utility methods
	
	public TileEntityDrawBridge getDrawBridge(IBlockReader world, BlockPos pos) {
		Pair<Boolean, TileEntity> pair = isTileEntityFromProvider(world, pos);
		return pair.getLeft() ? (TileEntityDrawBridge) pair.getRight() : null;
	}
	
	public ItemStack getRenderItemStack(IBlockReader world, BlockPos pos) {
		TileEntityDrawBridge drawbridge = getDrawBridge(world, pos);
		if (drawbridge == null) {
			return ItemStack.EMPTY;
		}
		ItemStack stack = drawbridge.getRenderSlot().getStackInSlot(0);
		if (stack == null || stack.isEmpty()) {
			return ItemStack.EMPTY;
		}
		return stack;
	}
	
	public IBlockState getRenderBlockState(IBlockReader world, BlockPos pos) {
		return BlockStateUtil.getBlockState(getRenderItemStack(world, pos));
	}
	
}
