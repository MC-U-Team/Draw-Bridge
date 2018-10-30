/*-*****************************************************************************
 * Copyright 2018 U-Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/

package info.uteam.drawbridges.container;

import info.uteam.drawbridges.tiles.DBMDrawbridgeTile;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.tileentity.TileEntity;

/**
 * @author MrTroble
 *
 */
public class SlotBlock extends Slot {

	/**
	 * @param inventoryIn
	 * @param index
	 * @param xPosition
	 * @param yPosition
	 */
	public SlotBlock(IInventory inventoryIn, int index, int xPosition, int yPosition) {
		super(inventoryIn, index, xPosition, yPosition);
	}
	
	/* (non-Javadoc)
	 * @see net.minecraft.inventory.Slot#canTakeStack(net.minecraft.entity.player.EntityPlayer)
	 */
	@Override
	public boolean canTakeStack(EntityPlayer playerIn) {
		if(this.inventory instanceof DBMDrawbridgeTile) {
			TileEntity ent =  (TileEntity) this.inventory;
			if(!ent.getWorld().isBlockPowered(ent.getPos()))return true;
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see net.minecraft.inventory.Slot#getSlotStackLimit()
	 */
	@Override
	public int getSlotStackLimit() {
		return 1;
	}

	/* (non-Javadoc)
	 * @see net.minecraft.inventory.Slot#isItemValid(net.minecraft.item.ItemStack)
	 */
	@Override
	public boolean isItemValid(ItemStack stack) {
		if(stack.getItem() instanceof ItemBlock) {
			Block block = Block.getBlockFromItem(stack.getItem());
			@SuppressWarnings("deprecation")
			IBlockState state =  block.getStateFromMeta(stack.getMetadata());
			if(state.isBlockNormalCube()) {
				if(this.inventory instanceof DBMDrawbridgeTile) {
					TileEntity ent =  (TileEntity) this.inventory;
					if(!ent.getWorld().isBlockPowered(ent.getPos()))return true;
				}
			}
		}
		return false;
	}
}
