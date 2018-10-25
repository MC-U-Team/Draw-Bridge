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

package info.uteam.drawbridges.handler;

import info.u_team.u_team_core.container.UContainer;
import info.u_team.u_team_core.gui.UGuiContainer;
import info.uteam.drawbridges.DBMConstants;
import info.uteam.drawbridges.init.DBMGuis;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

/**
 * @author MrTroble
 *
 */
public class DBMGuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		try {
			return DBMGuis.getContainer(ID).getConstructor(EntityPlayer.class, World.class, BlockPos.class)
					.newInstance(player, world, new BlockPos(x, y, z));
		} catch (Exception ex) {
			DBMConstants.LOGGER.error("Some gui container seems to be wrong.", ex);
			return null;
		}
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		try {
			Object container = this.getServerGuiElement(ID, player, world, x, y, z);
			Class<? extends UGuiContainer> gui = DBMGuis.getGui(ID);
			if (container == null) {
				return gui.getConstructor(EntityPlayer.class, World.class, BlockPos.class).newInstance(player, world,
						new BlockPos(x, y, z));
			} else {
				return gui.getConstructor(UContainer.class).newInstance(container);
			}
		} catch (Exception ex) {
			DBMConstants.LOGGER.error("Some gui seems to be wrong.", ex);
			return null;
		}
	}

}
