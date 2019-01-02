package info.u_team.draw_bridge.block;

import info.u_team.draw_bridge.DrawBridgeConstants;
import info.u_team.draw_bridge.container.ContainerDrawBridge;
import info.u_team.draw_bridge.gui.GuiDrawBridge;
import info.u_team.draw_bridge.init.*;
import info.u_team.draw_bridge.tileentity.DBMDrawbridgeTile;
import info.u_team.u_team_core.block.UBlockTileEntity;
import info.u_team.u_team_core.tileentity.UTileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.*;
import net.minecraft.block.state.*;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.*;
import net.minecraftforge.common.property.*;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

public class BlockDrawBridge extends UBlockTileEntity {
	
	public static final PropertyDirection FACING = PropertyDirection.create("facing");
	public static final PropertyBool COSTUM = PropertyBool.create("costum");
	protected int gui;
	
	public BlockDrawBridge(String name, Material materialIn) {
		super(name, materialIn, DrawBridgeCreativeTabs.dbm_tab, new UTileEntityProvider(new ResourceLocation(DrawBridgeConstants.MODID, "draw_bridge"), true, DBMDrawbridgeTile.class));
		gui = DBMGuis.addContainer(ContainerDrawBridge.class);
		if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
			DBMGuis.addGuiContainer(GuiDrawBridge.class, gui);
		}
		setDefaultState(getDefaultState().withProperty(FACING, EnumFacing.NORTH).withProperty(COSTUM, false));
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		playerIn.openGui(DrawBridgeConstants.MODID, gui, worldIn, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}
	
	private static final PropertyItemStack STACKS = PropertyItemStack.create();
	
	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
		TileEntity ent = world.getTileEntity(pos);
		if (ent instanceof DBMDrawbridgeTile) {
			DBMDrawbridgeTile dbt = (DBMDrawbridgeTile) ent;
			return ((IExtendedBlockState) (new ExtendedBlockState(this, new IProperty[] { FACING, COSTUM }, new IUnlistedProperty[] { STACKS }).getBaseState())).withProperty(STACKS, dbt.getRender());
		}
		return state;
	}
	
	static class PropertyItemStack implements IUnlistedProperty<ItemStack> {
		
		private PropertyItemStack() {
		}
		
		public static PropertyItemStack create() {
			return new PropertyItemStack();
		}
		
		@Override
		public String getName() {
			return "itemstacks";
		}
		
		@Override
		public boolean isValid(ItemStack value) {
			return true;
		}
		
		@Override
		public Class<ItemStack> getType() {
			return ItemStack.class;
		}
		
		@Override
		public String valueToString(ItemStack value) {
			String str = value.getItem().getRegistryName().toString() + "[" + value.getMetadata() + "]";
			return str;
		}
		
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		return getDefaultState().withProperty(FACING, facing).withProperty(COSTUM, false);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(FACING, EnumFacing.byIndex(meta)).withProperty(COSTUM, false);
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FACING).getIndex();
	}
	
	@Override
	public IBlockState withRotation(IBlockState state, Rotation rotation) {
		return state.withProperty(FACING, rotation.rotate(state.getValue(FACING))).withProperty(COSTUM, false);
	}
	
	@Override
	public IBlockState withMirror(IBlockState state, Mirror mirror) {
		return state.withRotation(mirror.toRotation(state.getValue(FACING))).withProperty(COSTUM, false);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		TileEntity ent = blockAccess.getTileEntity(pos);
		if (ent != null && ent instanceof DBMDrawbridgeTile) {
			DBMDrawbridgeTile tile = (DBMDrawbridgeTile) ent;
			return !tile.hasRender();
		}
		return super.shouldSideBeRendered(blockState, blockAccess, pos, side);
	}
	
	@Override
	public boolean isBlockNormalCube(IBlockState state) {
		return false;
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING, COSTUM });
	}
	
	@Override
	public void registerModel() {
		super.registerModel();
	}
	
}
