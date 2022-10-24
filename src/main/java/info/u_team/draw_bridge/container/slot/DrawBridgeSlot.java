package info.u_team.draw_bridge.container.slot;

import info.u_team.draw_bridge.init.DrawBridgeBlocks;
import info.u_team.draw_bridge.tileentity.DrawBridgeTileEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;

public class DrawBridgeSlot extends Slot {
	
	private final DrawBridgeTileEntity drawBridge;
	
	public DrawBridgeSlot(DrawBridgeTileEntity drawBridge, Container inventory, int index, int xPosition, int yPosition) {
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
