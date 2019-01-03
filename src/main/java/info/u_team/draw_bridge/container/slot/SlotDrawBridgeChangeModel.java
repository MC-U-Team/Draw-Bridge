package info.u_team.draw_bridge.container.slot;

import info.u_team.draw_bridge.tileentity.TileEntityDrawBridge;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

public class SlotDrawBridgeChangeModel extends SlotDrawBridge {
	
	public SlotDrawBridgeChangeModel(TileEntityDrawBridge drawbridge, int xPosition, int yPosition) {
		super(drawbridge, drawbridge.getRenderSlot(), 0, xPosition, yPosition);
	}
	
	@Override
	public void putStack(ItemStack stack) {
		super.putStack(stack);
		drawbridge.forceRerendering(); // Force chunk rerendering
		drawbridge.getWorld().checkLight(drawbridge.getPos()); // Force light update
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean isItemValid(ItemStack stack) {
		if (super.isItemValid(stack)) {
			Block block = Block.getBlockFromItem(stack.getItem());
			IBlockState state = block.getStateFromMeta(stack.getMetadata());
			return state.isFullBlock();
		}
		return false;
	}
	
	@Override
	public boolean shouldCheckExtended() {
		return false;
	}
}
