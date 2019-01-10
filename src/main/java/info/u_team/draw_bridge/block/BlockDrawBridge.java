package info.u_team.draw_bridge.block;

import org.apache.commons.lang3.tuple.Pair;

import info.u_team.draw_bridge.DrawBridgeConstants;
import info.u_team.draw_bridge.init.DrawBridgeCreativeTabs;
import info.u_team.draw_bridge.property.UnlistedPropertyItemStack;
import info.u_team.draw_bridge.tileentity.TileEntityDrawBridge;
import info.u_team.u_team_core.block.UBlockTileEntity;
import info.u_team.u_team_core.tileentity.UTileEntityProvider;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.*;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.*;
import net.minecraftforge.common.property.IExtendedBlockState;

public class BlockDrawBridge extends UBlockTileEntity {
	
	public static final PropertyDirection FACING = PropertyDirection.create("facing");
	
	public static final UnlistedPropertyItemStack ITEMSTACK = UnlistedPropertyItemStack.create("item");
	
	public BlockDrawBridge(String name) {
		super(name, Material.IRON, DrawBridgeCreativeTabs.tab, new UTileEntityProvider(new ResourceLocation(DrawBridgeConstants.MODID, "draw_bridge"), TileEntityDrawBridge.class));
		setDefaultState(getDefaultState().withProperty(FACING, EnumFacing.NORTH));
		setHardness(1.5F);
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		return openContainer(DrawBridgeConstants.MODID, 0, world, pos, player, true);
	}
	
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		TileEntityDrawBridge drawbridge = getDrawBridge(world, pos);
		if (drawbridge == null) {
			return;
		}
		InventoryHelper.dropInventoryItems(world, pos, drawbridge);
		world.updateComparatorOutputLevel(pos, this);
		super.breakBlock(world, pos, state);
	}
	
	// Used for light blocks
	
	@Override
	public int getLightOpacity(IBlockState state, IBlockAccess world, BlockPos pos) {
		IBlockState renderState = getRenderBlockState(world, pos);
		if (renderState == null) {
			return 0;
		}
		return renderState.getLightOpacity(world, pos);
	}
	
	@Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
		IBlockState renderState = getRenderBlockState(world, pos);
		if (renderState == null) {
			return 0;
		}
		return renderState.getLightValue(world, pos);
	}
	
	// Extended block state for rendering
	
	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
		if (!(state instanceof IExtendedBlockState)) {
			return state;
		}
		IExtendedBlockState extendedState = (IExtendedBlockState) state;
		ItemStack stack = getRenderItemStack(world, pos);
		if (stack == ItemStack.EMPTY) {
			return extendedState;
		}
		return extendedState.withProperty(ITEMSTACK, stack);
	}
	
	// Block state things
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		return getDefaultState().withProperty(FACING, EnumFacing.getDirectionFromEntityLiving(pos, placer));
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(FACING, EnumFacing.byIndex(meta));
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FACING).getIndex();
	}
	
	@Override
	public IBlockState withRotation(IBlockState state, Rotation rotation) {
		return state.withProperty(FACING, rotation.rotate(state.getValue(FACING)));
	}
	
	@Override
	public IBlockState withMirror(IBlockState state, Mirror mirror) {
		return state.withRotation(mirror.toRotation(state.getValue(FACING)));
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer.Builder(this).add(FACING).add(ITEMSTACK).build();
	}
	
	// Utility methods
	public TileEntityDrawBridge getDrawBridge(IBlockAccess world, BlockPos pos) {
		Pair<Boolean, TileEntity> pair = isTileEntityFromProvider(world, pos);
		return pair.getLeft() ? (TileEntityDrawBridge) pair.getRight() : null;
	}
	
	public ItemStack getRenderItemStack(IBlockAccess world, BlockPos pos) {
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
	
	@SuppressWarnings("deprecation")
	public IBlockState getRenderBlockState(IBlockAccess world, BlockPos pos) {
		ItemStack stack = getRenderItemStack(world, pos);
		if (stack == null) {
			return null;
		}
		Block block = Block.getBlockFromItem(stack.getItem());
		return block.getStateFromMeta(stack.getMetadata());
	}
	
}
