package info.u_team.draw_bridge.tileentity;

import java.util.ArrayList;
import java.util.List;

import info.u_team.draw_bridge.block.BlockDrawBridge;
import info.u_team.draw_bridge.inventory.InventoryOneSlotImplemention;
import info.u_team.u_team_core.api.ISyncedContainerTileEntity;
import info.u_team.u_team_core.tileentity.UTileEntity;
import info.u_team.u_team_core.util.NonNullListUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;

public class TileEntityDrawBridge extends UTileEntity implements ITickable, IInventory, ISyncedContainerTileEntity {
	
	private NonNullList<ItemStack> itemstacks;
	
	private boolean powered;
	private int speed;
	private boolean needsrs = true;
	private int extended;
	private boolean[] ourBlocks = new boolean[10];
	
	private InventoryOneSlotImplemention renderSlot;
	
	private int localSpeed;
	
	public TileEntityDrawBridge() {
		super(new TileEntityType<TileEntity>(factoryIn, datafixerTypeIn))
		itemstacks = NonNullList.withSize(10, ItemStack.EMPTY);
		renderSlot = new InventoryOneSlotImplemention(this, 1);
	}
	
	// Neighbor update
	public void neighborChanged() {
		boolean newPowered = needsrs ? world.isBlockPowered(pos) : !world.isBlockPowered(pos);
		if (newPowered == powered) {
			return; // Check that we only run the following code if the state (redstone power) was
					// really changed
		}
		
		powered = newPowered;
		
		System.out.println("STATE OF ORIGIN: " + pos);
		
		ArrayList<BlockPos> oldPos = new ArrayList<>();
		oldPos.add(pos);
		
		System.out.println(oldPos);
		
		powerNext(oldPos, 0);
		
	}
	
	// Recursive power next
	public void powerNext(List<BlockPos> oldPos, int depth) {
		if (depth > 10) { // Make this 10 for now
			return;
		}
		for (EnumFacing facing : EnumFacing.values()) {
			BlockPos newPos = pos.offset(facing);
			if (oldPos.contains(newPos)) {
				continue;
			}
			oldPos.add(newPos);
			TileEntity tileentity = world.getTileEntity(newPos);
			if (tileentity instanceof TileEntityDrawBridge) {
				TileEntityDrawBridge drawbridge = (TileEntityDrawBridge) tileentity;
				System.out.println(drawbridge + " - " + drawbridge.powered + " - " + drawbridge.pos + " - " + newPos);
				drawbridge.togglePowerState();
				drawbridge.powerNext(oldPos, depth + 1);
			}
		}
	}
	
	// Toggle power state
	public void togglePowerState() {
		powered = !powered;
	}
	
	@Override
	public void tick() {
		if (world.isRemote) {
			return;
		}
		if (localSpeed <= 1) {
			localSpeed = speed;
			if (powered && extended < 10) {
				if (localSpeed == 0) {
					for (int i = extended; i < 10; i++) {
						extend();
					}
				} else {
					extend();
				}
				markDirty();
			} else if (!powered && extended > 0) {
				if (localSpeed == 0) {
					for (int i = extended; i > 0; i--) {
						retract();
					}
				} else {
					retract();
				}
				markDirty();
			}
		}
		localSpeed--;
	}
	
	@Deprecated
	public boolean isPowered(BlockPos old, int depth) {
		boolean powered = needsrs ? world.isBlockPowered(pos) : !world.isBlockPowered(pos);
		if (depth > 10)
			return powered;
		if (needsrs && !powered) {
			for (EnumFacing face : EnumFacing.VALUES) {
				BlockPos pos2 = pos.offset(face);
				if (pos2.equals(old))
					continue;
				TileEntity ent = world.getTileEntity(pos2);
				if (ent != null && ent instanceof TileEntityDrawBridge) {
					if (powered = ((TileEntityDrawBridge) ent).isPowered(pos, depth + 1)) {
						return powered;
					}
				}
			}
		}
		return powered;
	}
	
	private void extend() {
		EnumFacing facing = world.getBlockState(pos).getValue(BlockDrawBridge.FACING);
		trySetBlock(facing);
		extended++;
	}
	
	@SuppressWarnings("deprecation")
	private void trySetBlock(EnumFacing facing) {
		BlockPos newPos = pos.offset(facing, extended + 1);
		if (world.isAirBlock(newPos) || world.getBlockState(newPos).getBlock() instanceof BlockLiquid) {
			ItemStack itemstack = getStackInSlot(extended);
			Block block = Block.getBlockFromItem(itemstack.getItem());
			world.setBlockState(newPos, block.getStateFromMeta(itemstack.getMetadata()));
			removeStackFromSlot(extended);
			ourBlocks[extended] = true;
		} else {
			ourBlocks[extended] = false;
		}
	}
	
	private void retract() {
		EnumFacing facing = world.getBlockState(pos).getValue(BlockDrawBridge.FACING);
		extended--;
		tryRemoveBlock(facing);
	}
	
	private void tryRemoveBlock(EnumFacing facing) {
		if (ourBlocks[extended]) {
			BlockPos newPos = pos.offset(facing, extended + 1);
			if (!world.isAirBlock(newPos)) {
				IBlockState state = world.getBlockState(newPos);
				Block block = state.getBlock();
				
				ItemStack stack = new ItemStack(block, 1, block.getMetaFromState(state));
				setInventorySlotContents(extended, stack);
				
				world.setBlockToAir(newPos);
			}
		}
	}
	
	// Chunk update
	
	@Override
	public void getChunkLoadServerSyncData(NBTTagCompound compound) {
		writeRenderSlot(compound);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void handleChunkLoadClientSyncData(NBTTagCompound compound) {
		readRenderSlot(compound);
	}
	
	// Container synchronization
	
	// Server -> client
	@Override
	public void getServerSyncContainerData(NBTTagCompound compound) {
		compound.setInteger("extended", extended);
		compound.setInteger("speed", speed);
		compound.setBoolean("needsrs", needsrs);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void handleFromServerSyncContainerData(NBTTagCompound compound) {
		extended = compound.getInteger("extended");
		speed = compound.getInteger("speed");
		needsrs = compound.getBoolean("needsrs");
	}
	
	// Client -> server
	@SideOnly(Side.CLIENT)
	@Override
	public void getClientSyncContainerData(NBTTagCompound compound) {
		compound.setInteger("speed", speed);
		compound.setBoolean("needsrs", needsrs);
	}
	
	@Override
	public void handleFromClientSyncContainerData(NBTTagCompound compound) {
		speed = Math.min(100, compound.getInteger("speed"));
		needsrs = compound.getBoolean("needsrs");
		neighborChanged(); // Force update if need redstone has been changed
	}
	
	// Force render update
	
	public void forceRerendering() {
		getWorld().markBlockRangeForRenderUpdate(getPos(), getPos());
	}
	
	// getter and setter
	
	public boolean isExtended() {
		return extended > 0;
	}
	
	public InventoryOneSlotImplemention getRenderSlot() {
		return renderSlot;
	}
	
	public int getSpeed() {
		return speed;
	}
	
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	
	public boolean needsRedstone() {
		return this.needsrs;
	}
	
	public void setNeedsRedstone(boolean needsrs) {
		this.needsrs = needsrs;
	}
	
	// Nbt
	
	@Override
	public void readNBT(NBTTagCompound compound) {
		ItemStackHelper.loadAllItems(compound, itemstacks);
		
		powered = compound.getBoolean("powered");
		extended = compound.getInteger("extended");
		speed = compound.getInteger("speed");
		needsrs = compound.getBoolean("needsrs");
		
		NBTTagCompound ourBlocksTag = compound.getCompoundTag("ourBlocks");
		for (int i = 0; i < ourBlocks.length; i++) {
			if (ourBlocksTag.hasKey("" + i)) {
				ourBlocks[i] = ourBlocksTag.getBoolean("" + i);
			} else {
				ourBlocks[i] = false;
			}
		}
		readRenderSlot(compound);
	}
	
	@Override
	public void writeNBT(NBTTagCompound compound) {
		ItemStackHelper.saveAllItems(compound, itemstacks);
		
		compound.setBoolean("powered", powered);
		compound.setInteger("extended", extended);
		compound.setInteger("speed", speed);
		compound.setBoolean("needsrs", needsrs);
		
		NBTTagCompound ourBlocksTag = new NBTTagCompound();
		for (int i = 0; i < ourBlocks.length; i++) {
			ourBlocksTag.setBoolean("" + i, ourBlocks[i]);
		}
		compound.setTag("ourBlocks", ourBlocksTag);
		
		writeRenderSlot(compound);
	}
	
	// Special nbt reading
	
	private void readRenderSlot(NBTTagCompound compound) {
		NBTTagCompound renderSlotTag = compound.getCompoundTag("renderSlot");
		if (renderSlotTag != null && !renderSlotTag.isEmpty()) {
			renderSlot.setInventorySlotContents(0, new ItemStack(renderSlotTag));
		}
	}
	
	private void writeRenderSlot(NBTTagCompound compound) {
		NBTTagCompound renderSlotTag = new NBTTagCompound();
		renderSlot.getStackInSlot(0).writeToNBT(renderSlotTag);
		compound.setTag("renderSlot", renderSlotTag);
	}
	
	// Inventory handling
	
	@Override
	public String getName() {
		return "drawbridge";
	}
	
	@Override
	public boolean hasCustomName() {
		return false;
	}
	
	@Override
	public int getSizeInventory() {
		return itemstacks.size();
	}
	
	@Override
	public boolean isEmpty() {
		for (ItemStack itemstack : itemstacks) {
			if (!itemstack.isEmpty()) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public ItemStack getStackInSlot(int index) {
		return itemstacks.get(index);
	}
	
	@Override
	public ItemStack decrStackSize(int index, int count) {
		return ItemStackHelper.getAndSplit(itemstacks, index, count);
	}
	
	@Override
	public ItemStack removeStackFromSlot(int index) {
		return ItemStackHelper.getAndRemove(itemstacks, index);
	}
	
	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		itemstacks.set(index, stack);
		if (stack.getCount() > this.getInventoryStackLimit()) {
			stack.setCount(this.getInventoryStackLimit());
		}
	}
	
	@Override
	public int getInventoryStackLimit() {
		return 1;
	}
	
	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return true;
	}
	
	@Override
	public void openInventory(EntityPlayer player) {
	}
	
	@Override
	public void closeInventory(EntityPlayer player) {
	}
	
	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return false;
	}
	
	@Override
	public int getField(int id) {
		return 0;
	}
	
	@Override
	public void setField(int id, int value) {
	}
	
	@Override
	public int getFieldCount() {
		return 0;
	}
	
	@Override
	public void clear() {
		itemstacks.clear();
	}
	
}
