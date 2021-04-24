package info.u_team.draw_bridge.container.slot;

import info.u_team.draw_bridge.init.DrawBridgeBlocks;
import info.u_team.draw_bridge.tileentity.DrawBridgeTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;

public class DrawBridgeSlot extends Slot {
	
	private final DrawBridgeTileEntity drawBridge;
	
	public DrawBridgeSlot(DrawBridgeTileEntity drawBridge, IInventory inventory, int index, int xPosition, int yPosition) {
		super(inventory, index, xPosition, yPosition);
		this.drawBridge = drawBridge;
	}
	
	@Override
	public boolean canTakeStack(PlayerEntity player) {
		return !drawBridge.isExtended();
	}
	
	@Override
	public boolean isItemValid(ItemStack stack) {
		if (drawBridge.isExtended()) {
			return false;
		}
		if (stack == null || !(stack.getItem() instanceof BlockItem)) {
			return false;
		}
		if (stack.getItem() == DrawBridgeBlocks.DRAW_BRIDGE.get().asItem()) {
			return false;
		}
		return true;
	}
	
}
