/*-*****************************************************************************
 * Copyright 2018 U-Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/

package info.uteam.drawbridges.block;

import info.u_team.u_team_core.block.UBlockTileEntity;
import info.u_team.u_team_core.tileentity.UTileEntityProvider;
import info.uteam.drawbridges.DBMConstants;
import info.uteam.drawbridges.container.DBMDrawbridgeContainer;
import info.uteam.drawbridges.gui.DBMDrawbridgeGui;
import info.uteam.drawbridges.init.*;
import info.uteam.drawbridges.tiles.DBMDrawbridgeTile;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.*;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

/**
 * @author MrTroble
 *
 */
public class DBMDrawbridge extends UBlockTileEntity {

	public static final PropertyDirection FACING = PropertyDirection.create("facing");
	protected int gui;

	/**
	 * @param name
	 * @param materialIn
	 */
	public DBMDrawbridge(String name, Material materialIn) {
		super(name, materialIn, DBMCreativeTabs.dbm_tab, new UTileEntityProvider(
				new ResourceLocation(DBMConstants.MODID, "draw_bridge"), true, DBMDrawbridgeTile.class));
		gui = DBMGuis.addContainer(DBMDrawbridgeContainer.class);
		if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
			DBMGuis.addGuiContainer(DBMDrawbridgeGui.class, gui);
		}
		setDefaultState(getDefaultState().withProperty(FACING, EnumFacing.NORTH));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.minecraft.block.Block#onBlockActivated(net.minecraft.world.World,
	 * net.minecraft.util.math.BlockPos, net.minecraft.block.state.IBlockState,
	 * net.minecraft.entity.player.EntityPlayer, net.minecraft.util.EnumHand,
	 * net.minecraft.util.EnumFacing, float, float, float)
	 */
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		playerIn.openGui(DBMConstants.MODID, gui, worldIn, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer) {
		return getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(FACING, EnumFacing.byHorizontalIndex(meta));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FACING).getHorizontalIndex();
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
		return new BlockStateContainer(this, FACING);
	}

}
