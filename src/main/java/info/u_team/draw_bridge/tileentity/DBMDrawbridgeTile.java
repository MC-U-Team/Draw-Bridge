package info.u_team.draw_bridge.tileentity;

import java.util.ArrayList;

import com.google.common.collect.Lists;

import info.u_team.draw_bridge.block.BlockDrawBridge;
import info.u_team.draw_bridge.container.ContainerDrawBridge;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.*;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class DBMDrawbridgeTile extends DBMTileEntityGUI {
	
	private boolean last_state = false, costum = false;
	private int count = 0;
	private ArrayList<Integer> offsets = Lists.newArrayList();
	
	public DBMDrawbridgeTile() {
		super(11, "dbm_drawbridge_tile");
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public void update() {
		if (world.isRemote)
			return;
		if (this.hasRender() != costum) {
			costum = this.hasRender();
			world.setBlockState(pos, world.getBlockState(pos).withProperty(BlockDrawBridge.COSTUM, costum));
		}
		boolean state = world.isBlockPowered(pos);
		if (state != last_state) {
			IBlockState bstate = world.getBlockState(pos);
			if (bstate == null || bstate.getBlock() == null || !(bstate.getBlock() instanceof BlockDrawBridge))
				return;
			EnumFacing face = bstate.getValue(BlockDrawBridge.FACING);
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
				for (; count > 0; count--) {
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
	
	@Override
	public int getInventoryStackLimit() {
		return 1;
	}
	
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
	
	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		return new int[0];
	}
	
	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
		return false;
	}
	
	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		return false;
	}
	
	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return false;
	}
	
	@Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
		return new ContainerDrawBridge(playerIn, world, pos);
	}
	
}
