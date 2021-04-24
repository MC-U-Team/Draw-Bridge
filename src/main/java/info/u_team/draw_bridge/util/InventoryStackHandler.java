package info.u_team.draw_bridge.util;

import java.util.stream.IntStream;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;

public class InventoryStackHandler implements IItemHandlerModifiable, INBTSerializable<CompoundNBT> {
	
	private final NonNullList<ItemStack> stacks;
	
	private final IInventory inventory;
	
	public InventoryStackHandler(int size) {
		stacks = NonNullList.withSize(size, ItemStack.EMPTY);
		inventory = createInventory(stacks);
	}
	
	protected IInventory createInventory(NonNullList<ItemStack> stacks) {
		return new Inventory(stacks, this);
	}
	
	public IInventory getInventory() {
		return inventory;
	}
	
	@Override
	public ItemStack getStackInSlot(int index) {
		return stacks.get(index);
	}
	
	@Override
	public int getSlots() {
		return stacks.size();
	}
	
	@Override
	public void setStackInSlot(int index, ItemStack stack) {
		inventory.setInventorySlotContents(index, stack);
	}
	
	@Override
	public ItemStack insertItem(int index, ItemStack stack, boolean simulate) {
		if (stack.isEmpty()) {
			return ItemStack.EMPTY;
		}
		
		if (!isItemValid(index, stack)) {
			return stack;
		}
		
		final ItemStack existing = stacks.get(index);
		
		int limit = getStackLimit(index, stack);
		
		if (!existing.isEmpty()) {
			if (!ItemHandlerHelper.canItemStacksStack(stack, existing)) {
				return stack;
			}
			limit -= existing.getCount();
		}
		
		if (limit <= 0) {
			return stack;
		}
		
		final boolean reachedLimit = stack.getCount() > limit;
		
		if (!simulate) {
			if (existing.isEmpty()) {
				stacks.set(index, reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, limit) : stack);
			} else {
				existing.grow(reachedLimit ? limit : stack.getCount());
			}
			slotChanged(index);
		}
		
		return reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, stack.getCount() - limit) : ItemStack.EMPTY;
	}
	
	@Override
	public ItemStack extractItem(int index, int amount, boolean simulate) {
		if (amount == 0) {
			return ItemStack.EMPTY;
		}
		
		final ItemStack existing = stacks.get(index);
		
		if (existing.isEmpty()) {
			return ItemStack.EMPTY;
		}
		
		final int toExtract = Math.min(amount, existing.getMaxStackSize());
		
		if (existing.getCount() <= toExtract) {
			if (!simulate) {
				stacks.set(index, ItemStack.EMPTY);
				slotChanged(index);
			}
			return existing;
		} else {
			if (!simulate) {
				stacks.set(index, ItemHandlerHelper.copyStackWithSize(existing, existing.getCount() - toExtract));
				slotChanged(index);
			}
			return ItemHandlerHelper.copyStackWithSize(existing, toExtract);
		}
	}
	
	@Override
	public int getSlotLimit(int index) {
		return inventory.getInventoryStackLimit();
	}
	
	@Override
	public boolean isItemValid(int index, ItemStack stack) {
		return true;
	}
	
	protected int getStackLimit(int index, ItemStack stack) {
		return Math.min(getSlotLimit(index), stack.getMaxStackSize());
	}
	
	@Override
	public CompoundNBT serializeNBT() {
		return ItemStackHelper.saveAllItems(new CompoundNBT(), stacks, false);
	}
	
	@Override
	public void deserializeNBT(CompoundNBT compound) {
		ItemStackHelper.loadAllItems(compound, stacks);
		onLoaded();
	}
	
	protected void slotChanged(int index) {
	}
	
	// If used in tileentites, then the world might be null
	protected void onLoaded() {
	}
	
	protected static class Inventory implements IInventory {
		
		private final NonNullList<ItemStack> stacks;
		private final InventoryStackHandler handler;
		
		protected Inventory(NonNullList<ItemStack> stacks, InventoryStackHandler handler) {
			this.stacks = stacks;
			this.handler = handler;
		}
		
		@Override
		public void clear() {
			stacks.clear();
			IntStream.range(0, stacks.size()).forEach(handler::slotChanged);
		}
		
		@Override
		public void setInventorySlotContents(int index, ItemStack stack) {
			stacks.set(index, stack);
			final int limit = handler.getStackLimit(index, stack);
			if (stack.getCount() > limit) {
				stack.setCount(limit);
			}
			handler.slotChanged(index);
		}
		
		@Override
		public ItemStack removeStackFromSlot(int index) {
			final ItemStack stack = ItemStackHelper.getAndRemove(stacks, index);
			handler.slotChanged(index);
			return stack;
		}
		
		@Override
		public void markDirty() {
		}
		
		@Override
		public boolean isUsableByPlayer(PlayerEntity player) {
			return true;
		}
		
		@Override
		public boolean isEmpty() {
			return stacks.stream().allMatch(ItemStack::isEmpty);
		}
		
		@Override
		public ItemStack getStackInSlot(int index) {
			return stacks.get(index);
		}
		
		@Override
		public int getSizeInventory() {
			return stacks.size();
		}
		
		@Override
		public ItemStack decrStackSize(int index, int count) {
			final ItemStack stack = ItemStackHelper.getAndSplit(stacks, index, count);
			handler.slotChanged(index);
			return stack;
		}
	}
	
}
