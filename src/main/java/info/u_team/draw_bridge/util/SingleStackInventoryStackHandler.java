package info.u_team.draw_bridge.util;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class SingleStackInventoryStackHandler extends InventoryStackHandler {
	
	public SingleStackInventoryStackHandler(int size) {
		super(size);
	}
	
	@Override
	protected IInventory createInventory(NonNullList<ItemStack> stacks) {
		return new Inventory(stacks, this) {
			
			@Override
			public int getInventoryStackLimit() {
				return 1;
			}
		};
	}
	
}
