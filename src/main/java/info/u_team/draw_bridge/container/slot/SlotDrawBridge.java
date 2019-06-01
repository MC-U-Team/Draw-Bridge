package info.u_team.draw_bridge.container.slot;

import info.u_team.draw_bridge.init.DrawBridgeBlocks;
import info.u_team.draw_bridge.tileentity.TileEntityDrawBridge;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class SlotDrawBridge extends Slot {
	
	protected final TileEntityDrawBridge drawbridge;
	
	public SlotDrawBridge(TileEntityDrawBridge drawbridge, IInventory inventory, int index, int xPosition, int yPosition) {
		super(inventory, index, xPosition, yPosition);
		this.drawbridge = drawbridge;
	}
	
	@Override
	public boolean canTakeStack(EntityPlayer playerIn) {
		return !(drawbridge.isExtended() && shouldCheckExtended());
	}
	
	@Override
	public boolean isItemValid(ItemStack stack) {
		if (shouldCheckExtended() && drawbridge.isExtended()) {
			return false;
		}
		if (stack == null || !(stack.getItem() instanceof ItemBlock)) {
			return false;
		}
		if (stack.getItem() == DrawBridgeBlocks.draw_bridge.asItem()) {
			return false;
		}
		return true;
	}
	
	public boolean shouldCheckExtended() {
		return true;
	}
	
}
