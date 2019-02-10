package info.u_team.draw_bridge.util;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

public class BlockStateUtil {
	
	@SuppressWarnings("deprecation")
	public static IBlockState getBlockState(ItemStack stack) {
		if (stack == null || stack == ItemStack.EMPTY) {
			return null;
		}
		Block block = Block.getBlockFromItem(stack.getItem());
		return block.getStateFromMeta(stack.getMetadata());
	}
	
}
