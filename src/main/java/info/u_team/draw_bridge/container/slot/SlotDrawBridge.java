package info.u_team.draw_bridge.container.slot;

import info.u_team.draw_bridge.init.DrawBridgeBlocks;
import info.u_team.draw_bridge.tileentity.TileEntityDrawBridge;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.*;

public class SlotDrawBridge extends Slot {
	
	protected final TileEntityDrawBridge drawbridge;
	
	public SlotDrawBridge(TileEntityDrawBridge drawbridge, IInventory inventory, int index, int xPosition, int yPosition) {
		super(inventory, index, xPosition, yPosition);
		this.drawbridge = drawbridge;
	}
	
	@Override
	public boolean canTakeStack(EntityPlayer playerIn) {
		return !drawbridge.isExtended();
	}
	
	@Override
	public boolean isItemValid(ItemStack stack) {
		if (shouldCheckExtended() && drawbridge.isExtended()) {
			return false;
		}
		if (stack == null || !(stack.getItem() instanceof ItemBlock)) {
			return false;
		}
		if (stack.getItem() == DrawBridgeBlocks.draw_bridge.getItem()) {
			return false;
		}
		return true;
	}
	
	public boolean shouldCheckExtended() {
		return true;
	}
	
}
