package info.u_team.draw_bridge.container;

import info.u_team.draw_bridge.init.DrawBridgeContainerTypes;
import info.u_team.draw_bridge.tileentity.DrawBridgeTileEntity;
import info.u_team.u_team_core.container.USyncedTileEntityContainer;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.container.*;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.items.IItemHandler;

public class DrawBridgeContainer extends USyncedTileEntityContainer<DrawBridgeTileEntity> {
	
	// Client
	public DrawBridgeContainer(int id, PlayerInventory playerInventory, PacketBuffer buffer) {
		super(DrawBridgeContainerTypes.DRAW_BRIDGE, id, playerInventory, buffer);
	}
	
	// Server
	public DrawBridgeContainer(int id, PlayerInventory playerInventory, DrawBridgeTileEntity tileEntity) {
		super(DrawBridgeContainerTypes.DRAW_BRIDGE, id, playerInventory, tileEntity);
	}
	
	@Override
	protected void init(boolean server) {
		tileEntity.getSlots().ifPresent(inventory -> appendInventory((IItemHandler) inventory, 2, 5, 8, 18));
		tileEntity.getRenderSlot().ifPresent(inventory -> appendInventory((IItemHandler) inventory, 1, 1, 134, 18));
		appendPlayerInventory(playerInventory, 8, 68);
	}
	
	@Override
	public ItemStack transferStackInSlot(PlayerEntity player, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		final Slot slot = inventorySlots.get(index);
		if (slot != null && slot.getHasStack()) {
			final ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			if (index < 11) {
				if (!mergeItemStack(itemstack1, 11, 47, true)) {
					return ItemStack.EMPTY;
				}
			} else {
				if (index >= 38) {
					if (!mergeItemStack(itemstack1, 0, 10, false)) {
						if (!mergeItemStack(itemstack1, 11, 38, false)) {
							return ItemStack.EMPTY;
						}
					}
				} else {
					if (!mergeItemStack(itemstack1, 0, 10, false)) {
						if (!mergeItemStack(itemstack1, 38, 47, false)) {
							return ItemStack.EMPTY;
						}
					}
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
			slot.onTake(player, itemstack1);
		}
		
		return itemstack;
	}
}
