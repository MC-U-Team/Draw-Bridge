package info.u_team.draw_bridge.container.slot;

import info.u_team.draw_bridge.tileentity.TileEntityDrawBridge;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.*;

public class SlotDrawBridge extends Slot {
	
	protected final TileEntityDrawBridge drawbridge;
	
	public SlotDrawBridge(TileEntityDrawBridge drawbridge, int index, int xPosition, int yPosition) {
		super(drawbridge, index, xPosition, yPosition);
		this.drawbridge = drawbridge;
	}
	
	@Override
	public boolean canTakeStack(EntityPlayer playerIn) {
		return !drawbridge.isExtended();
	}
	
	@Override
	public boolean isItemValid(ItemStack stack) {
		return !drawbridge.isExtended() && stack != null && stack.getItem() instanceof ItemBlock;
	}
	
}
