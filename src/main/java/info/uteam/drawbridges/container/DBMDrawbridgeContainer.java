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

import info.u_team.u_team_core.container.UContainer;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author MrTroble
 *
 */
public class DBMDrawbridgeContainer extends UContainer {

	/**
	 * @param player
	 * @param world
	 * @param pos
	 */
	public DBMDrawbridgeContainer(EntityPlayer player, World world, BlockPos pos) {
		IInventory inv = (IInventory) world.getTileEntity(pos);
		for (int height = 0; height < 2; height++) {
			for (int width = 0; width < 5; width++) {
				addSlotToContainer(new SlotBlock(inv, width + height * 5, width * 18 + 8, height * 18 + 8));
			}
		}
		addSlotToContainer(new SlotBlock(inv, 10, 151, 8));
		appendPlayerInventory(player.inventory, 8, 84);
	}
	
	/* (non-Javadoc)
	 * @see net.minecraft.inventory.Container#transferStackInSlot(net.minecraft.entity.player.EntityPlayer, int)
	 */
	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		System.out.println(index);
		return ItemStack.EMPTY;
	}
	
}
