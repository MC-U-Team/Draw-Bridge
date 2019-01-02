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

package info.uteam.drawbridges.tiles;

import java.util.ArrayList;

import com.google.common.collect.Lists;

import info.uteam.drawbridges.block.DBMDrawbridge;
import info.uteam.drawbridges.container.DBMDrawbridgeContainer;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.*;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

/**
 * @author MrTroble
 *
 */
public class DBMDrawbridgeTile extends DBMTileEntityGUI {

	private boolean last_state = false, costum = false;
	private int count = 0;
	private ArrayList<Integer> offsets = Lists.newArrayList();

	public DBMDrawbridgeTile() {
		super(11, "dbm_drawbridge_tile");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.minecraft.util.ITickable#update()
	 */
	@Override
	@SuppressWarnings("deprecation")
	public void update() {
		if (world.isRemote)
			return;
		if (this.hasRender() != costum) {
			costum = this.hasRender();
			world.setBlockState(pos, world.getBlockState(pos).withProperty(DBMDrawbridge.COSTUM, costum));
		}
		boolean state = world.isBlockPowered(pos);
		if (state != last_state) {
			IBlockState bstate = world.getBlockState(pos);
			if (bstate == null || bstate.getBlock() == null || !(bstate.getBlock() instanceof DBMDrawbridge))
				return;
			EnumFacing face = bstate.getValue(DBMDrawbridge.FACING);
			last_state = state;
			if (last_state) {
				count = 0;
				BlockPos last_pos = pos;
				offsets.clear();
				for (int i = 0; i < 10; i++) {
					BlockPos before = last_pos;
					last_pos = last_pos.offset(face);
					System.out.println(last_pos);
					if (!world.isAirBlock(last_pos)) {
						offsets.add(new Integer(i));
						count++;
						continue;
					}
					System.out.println(i + " -> " + this.itemstacks.get(i));
					if (this.itemstacks.get(i) == ItemStack.EMPTY || this.itemstacks.get(i) == null || this.itemstacks.get(i).getItem() == Item.getItemFromBlock(Blocks.AIR)) {
						last_pos = last_pos.offset(face);
						world.setBlockToAir(last_pos);
						count++;
						continue;
					}
					Block bl = Block.getBlockFromItem(this.itemstacks.get(i).getItem());
					IBlockState bst = bl.getStateFromMeta(this.itemstacks.get(i).getMetadata());
					world.setBlockState(before, bst);
					count++;
					this.decrStackSize(i, 1);
					System.out.println("setState");
				}
			} else {
				for ( ; count > 0; count--) {
					if (world.isAirBlock(pos.offset(face, count)) || offsets.contains(new Integer(count - 1))) {
						continue;
					}
					IBlockState bstb = world.getBlockState(pos.offset(face, count));
					world.setBlockToAir(pos.offset(face, count));
					Block blb = bstb.getBlock();
					ItemStack stack = new ItemStack(blb, 1, blb.getMetaFromState(bstb));
					this.setInventorySlotContents(count - 1, stack);
				}
			}
		}
	}

	public boolean hasRender() {
		return !this.getStackInSlot(10).isEmpty();
	}

	public ItemStack getRender() {
		return this.getStackInSlot(10);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see info.uteam.drawbridges.tiles.DBMTileEntityGUI#getInventoryStackLimit()
	 */
	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * info.uteam.drawbridges.tiles.DBMTileEntityGUI#writeNBT(net.minecraft.nbt.
	 * NBTTagCompound)
	 */
	@Override
	public void writeNBT(NBTTagCompound compound) {
		super.writeNBT(compound);
		compound.setBoolean("state", last_state);
		compound.setInteger("count", count);
		NBTTagList list = new NBTTagList();
		for (Integer nt : offsets) {
			list.appendTag(new NBTTagInt(nt));
		}
		compound.setTag("offsets", list);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see info.uteam.drawbridges.tiles.DBMTileEntityGUI#readNBT(net.minecraft.nbt.
	 * NBTTagCompound)
	 */
	@Override
	public void readNBT(NBTTagCompound compound) {
		super.readNBT(compound);
		last_state = compound.getBoolean("state");
		count = compound.getInteger("count");
		NBTTagList list = (NBTTagList) compound.getTag("offsets");
		list.forEach(tag -> {
			NBTTagInt nt = (NBTTagInt) tag;
			offsets.add(nt.getInt());
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.minecraft.inventory.ISidedInventory#getSlotsForFace(net.minecraft.util.
	 * EnumFacing)
	 */
	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		return new int[0];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.minecraft.inventory.ISidedInventory#canInsertItem(int,
	 * net.minecraft.item.ItemStack, net.minecraft.util.EnumFacing)
	 */
	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.minecraft.inventory.ISidedInventory#canExtractItem(int,
	 * net.minecraft.item.ItemStack, net.minecraft.util.EnumFacing)
	 */
	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.minecraft.inventory.IInventory#isItemValidForSlot(int,
	 * net.minecraft.item.ItemStack)
	 */
	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.minecraft.world.IInteractionObject#createContainer(net.minecraft.entity.
	 * player.InventoryPlayer, net.minecraft.entity.player.EntityPlayer)
	 */
	@Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
		return new DBMDrawbridgeContainer(playerIn, world, pos);
	}

}
