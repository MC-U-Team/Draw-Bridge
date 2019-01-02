package info.u_team.draw_bridge.container.slot;

import info.u_team.draw_bridge.tileentity.TileEntityDrawBridge;
import net.minecraft.item.ItemStack;

public class SlotDrawBridgeChangeModel extends SlotDrawBridge {
	
	public SlotDrawBridgeChangeModel(TileEntityDrawBridge drawbridge, int index, int xPosition, int yPosition) {
		super(drawbridge, index, xPosition, yPosition);
	}
	
	@Override
	public void putStack(ItemStack stack) {
		super.putStack(stack);
		drawbridge.forceRerendering(); // Force chunk rerendering
	}
	
}
