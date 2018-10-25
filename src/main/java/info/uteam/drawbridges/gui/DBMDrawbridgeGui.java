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

package info.uteam.drawbridges.gui;

import info.u_team.u_team_core.container.UContainer;
import info.u_team.u_team_core.gui.UGuiContainer;
import info.uteam.drawbridges.DBMConstants;
import net.minecraft.util.ResourceLocation;

/**
 * @author MrTroble
 *
 */
public class DBMDrawbridgeGui extends UGuiContainer{

	public static final ResourceLocation BACKGROUND = new ResourceLocation(DBMConstants.MODID, "gui/drawbridge_gui.png");
	
	/**
	 * @param container
	 */
	public DBMDrawbridgeGui(UContainer container) {
		super(container, BACKGROUND);
	}

}
