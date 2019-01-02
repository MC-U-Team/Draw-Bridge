package info.u_team.draw_bridge.block;

import info.u_team.draw_bridge.DrawBridgeConstants;
import info.u_team.draw_bridge.init.DrawBridgeCreativeTabs;
import info.u_team.draw_bridge.property.UnlistedPropertyItemStack;
import info.u_team.draw_bridge.tileentity.TileEntityDrawBridge;
import info.u_team.u_team_core.block.UBlockTileEntity;
import info.u_team.u_team_core.tileentity.UTileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.*;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.*;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.property.IExtendedBlockState;

public class BlockDrawBridge extends UBlockTileEntity {
	
	public static final PropertyDirection FACING = PropertyDirection.create("facing");
	
	public static final UnlistedPropertyItemStack ITEMSTACK = UnlistedPropertyItemStack.create("item");
	
	public BlockDrawBridge(String name) {
		super(name, Material.IRON, DrawBridgeCreativeTabs.tab, new UTileEntityProvider(new ResourceLocation(DrawBridgeConstants.MODID, "draw_bridge"), TileEntityDrawBridge.class));
		setDefaultState(getDefaultState().withProperty(FACING, EnumFacing.NORTH));
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		player.openGui(DrawBridgeConstants.MODID, 0, world, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}
	
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		TileEntity tileentity = world.getTileEntity(pos);
		if (tileentity instanceof TileEntityDrawBridge) {
			InventoryHelper.dropInventoryItems(world, pos, (TileEntityDrawBridge) tileentity);
			world.updateComparatorOutputLevel(pos, this);
		}
		
		super.breakBlock(world, pos, state);
	}
	
	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
		if (state instanceof IExtendedBlockState) {
			IExtendedBlockState extended = (IExtendedBlockState) state;
			TileEntity tileentity = world instanceof ChunkCache ? ((ChunkCache) world).getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK) : world.getTileEntity(pos); // Get save tileentity
			if (tileentity instanceof TileEntityDrawBridge) {
				return extended.withProperty(ITEMSTACK, ((TileEntityDrawBridge) tileentity).getRenderSlot().getStackInSlot(0));
			}
		}
		return state;
	}
	
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
	
}
