package info.u_team.draw_bridge.container.slot;

import info.u_team.draw_bridge.tileentity.TileEntityDrawBridge;
import net.minecraft.item.ItemStack;

public class SlotDrawBridgeChangeModel extends SlotDrawBridge {
	
	public SlotDrawBridgeChangeModel(TileEntityDrawBridge drawbridge, int xPosition, int yPosition) {
		super(drawbridge, drawbridge.getRenderSlot(), 0, xPosition, yPosition);
	}
	
	@Override
	public void putStack(ItemStack stack) {
		super.putStack(stack);
		drawbridge.forceRerendering(); // Force chunk rerendering
	}
}
