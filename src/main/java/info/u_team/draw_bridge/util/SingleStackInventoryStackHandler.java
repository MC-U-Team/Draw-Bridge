package info.u_team.draw_bridge.util;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.NonNullList;

public class SingleStackInventoryStackHandler extends InventoryStackHandler {
	
	public SingleStackInventoryStackHandler(int size) {
		super(size);
	}
	
	@Override
	protected Container createInventory(NonNullList<ItemStack> stacks) {
		return new Inventory(stacks, this) {
			
			@Override
			public int getMaxStackSize() {
				return 1;
			}
		};
	}
	
}
