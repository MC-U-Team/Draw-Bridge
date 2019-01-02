package info.u_team.draw_bridge.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;

public class InventoryOneSlotImplemention implements IInventory {
	
	private final TileEntity tileentity;
	
	private ItemStack stack = ItemStack.EMPTY;
	
	private final int stackLimit;
	
	public InventoryOneSlotImplemention(TileEntity tileentity, int stackLimit) {
		this.tileentity = tileentity;
		this.stackLimit = stackLimit;
	}
	
	@Override
	public String getName() {
		return "oneslotinventory";
	}
	
	@Override
	public boolean hasCustomName() {
		return false;
	}
	
	@Override
	public ITextComponent getDisplayName() {
		return null;
	}
	
	@Override
	public int getSizeInventory() {
		return 1;
	}
	
	@Override
	public boolean isEmpty() {
		return stack.isEmpty();
	}
	
	@Override
	public ItemStack getStackInSlot(int index) {
		return stack;
	}
	
	@Override
	public ItemStack decrStackSize(int index, int count) {
		return !stack.isEmpty() && count > 0 ? stack.splitStack(count) : ItemStack.EMPTY;
	}
	
	@Override
	public ItemStack removeStackFromSlot(int index) {
		return stack = ItemStack.EMPTY;
	}
	
	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		this.stack = stack;
		if (stack.getCount() > getInventoryStackLimit()) {
			stack.setCount(getInventoryStackLimit());
		}
	}
	
	@Override
	public int getInventoryStackLimit() {
		return stackLimit;
	}
	
	@Override
	public void markDirty() {
		tileentity.markDirty();
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
		stack = ItemStack.EMPTY;
	}
	
}
