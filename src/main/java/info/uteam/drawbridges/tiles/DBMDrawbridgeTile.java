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

package info.uteam.drawbridges.tiles;

import info.uteam.drawbridges.container.DBMDrawbridgeContainer;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

/**
 * @author MrTroble
 *
 */
public class DBMDrawbridgeTile extends DBMTileEntityGUI {

	public DBMDrawbridgeTile() {
		super(11, "dbm_drawbridge_tile");
	}

	/* (non-Javadoc)
	 * @see net.minecraft.util.ITickable#update()
	 */
	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see net.minecraft.inventory.ISidedInventory#getSlotsForFace(net.minecraft.util.EnumFacing)
	 */
	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		return new int[0];
	}

	/* (non-Javadoc)
	 * @see net.minecraft.inventory.ISidedInventory#canInsertItem(int, net.minecraft.item.ItemStack, net.minecraft.util.EnumFacing)
	 */
	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
		return false;
	}

	/* (non-Javadoc)
	 * @see net.minecraft.inventory.ISidedInventory#canExtractItem(int, net.minecraft.item.ItemStack, net.minecraft.util.EnumFacing)
	 */
	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		return false;
	}

	/* (non-Javadoc)
	 * @see net.minecraft.inventory.IInventory#isItemValidForSlot(int, net.minecraft.item.ItemStack)
	 */
	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return false;
	}

	/* (non-Javadoc)
	 * @see net.minecraft.world.IInteractionObject#createContainer(net.minecraft.entity.player.InventoryPlayer, net.minecraft.entity.player.EntityPlayer)
	 */
	@Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
		return new DBMDrawbridgeContainer(playerIn, world, pos);
	}
	

}
