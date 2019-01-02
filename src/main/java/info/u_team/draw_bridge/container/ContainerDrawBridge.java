package info.u_team.draw_bridge.container;

import info.u_team.draw_bridge.container.slot.*;
import info.u_team.draw_bridge.tileentity.TileEntityDrawBridge;
import info.u_team.u_team_core.container.UContainerTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerDrawBridge extends UContainerTileEntity {
	
	public ContainerDrawBridge(TileEntityDrawBridge tileentity, EntityPlayer player) {
		super(tileentity);
		for (int height = 0; height < 2; height++) {
			for (int width = 0; width < 5; width++) {
				addSlotToContainer(new SlotDrawBridge(tileentity, tileentity, width + height * 5, width * 18 + 8, height * 18 + 8));
			}
		}
		addSlotToContainer(new SlotDrawBridgeChangeModel(tileentity, 151, 8));
		appendPlayerInventory(player.inventory, 8, 84);
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);
		
		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			
			if (index < 11) {
				if (!this.mergeItemStack(itemstack1, 11, 47, true)) {
					return ItemStack.EMPTY;
				}
			} else {
				if (!this.mergeItemStack(itemstack1, 0, 10, false)) {
					return ItemStack.EMPTY;
				}
			}
			
			if (itemstack1.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}
			
			if (itemstack1.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}
			
			slot.onTake(playerIn, itemstack1);
		}
		
		return itemstack;
	}
	
}
