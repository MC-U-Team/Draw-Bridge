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

package info.uteam.drawbridges.init;

import info.u_team.draw_bridge.DrawBridgeConstants;
import info.u_team.u_team_core.registry.BlockRegistry;
import info.u_team.u_team_core.util.RegistryUtil;
import info.uteam.drawbridges.block.DBMDrawbridge;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

/**
 * @author MrTroble
 *
 */
public class DBMBlocks {

	public static final Block draw_bridge = new DBMDrawbridge("drawbridge", Material.IRON);
	
	public static void preinit() {
		BlockRegistry.register(DrawBridgeConstants.MODID, RegistryUtil.getRegistryEntries(Block.class, DBMBlocks.class));
	}
	
}
