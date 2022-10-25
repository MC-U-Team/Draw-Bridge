package info.u_team.draw_bridge.menu.slot;

import info.u_team.draw_bridge.blockentity.DrawBridgeBlockEntity;
import info.u_team.draw_bridge.init.DrawBridgeBlocks;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;

public class DrawBridgeSlot extends Slot {
	
	private final DrawBridgeBlockEntity drawBridge;
	
	public DrawBridgeSlot(DrawBridgeBlockEntity drawBridge, Container inventory, int index, int xPosition, int yPosition) {
		super(inventory, index, xPosition, yPosition);
		this.drawBridge = drawBridge;
	}
	
	@Override
	public boolean mayPickup(Player player) {
		return !drawBridge.isExtended();
	}
	
	@Override
	public boolean mayPlace(ItemStack stack) {
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
