package info.u_team.draw_bridge.container;

import info.u_team.draw_bridge.tileentity.DBMDrawbridgeTile;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.tileentity.TileEntity;

public class SlotBlock extends Slot {
	
	public SlotBlock(IInventory inventoryIn, int index, int xPosition, int yPosition) {
		super(inventoryIn, index, xPosition, yPosition);
	}
	
	@Override
	public boolean canTakeStack(EntityPlayer playerIn) {
		if (this.inventory instanceof DBMDrawbridgeTile) {
			TileEntity ent = (TileEntity) this.inventory;
			if (!ent.getWorld().isBlockPowered(ent.getPos()))
				return true;
		}
		return false;
	}
	
	@Override
	public int getSlotStackLimit() {
		return 1;
	}
	
	@Override
	public boolean isItemValid(ItemStack stack) {
		if (stack.getItem() instanceof ItemBlock) {
			Block block = Block.getBlockFromItem(stack.getItem());
			@SuppressWarnings("deprecation")
			IBlockState state = block.getStateFromMeta(stack.getMetadata());
			if (state.isBlockNormalCube()) {
				if (this.inventory instanceof DBMDrawbridgeTile) {
					TileEntity ent = (TileEntity) this.inventory;
					if (!ent.getWorld().isBlockPowered(ent.getPos()))
						return true;
				}
			}
		}
		return false;
	}
}
