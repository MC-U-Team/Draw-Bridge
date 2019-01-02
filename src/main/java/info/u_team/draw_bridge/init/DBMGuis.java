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

package info.u_team.draw_bridge.init;

import java.util.*;

import info.u_team.draw_bridge.DrawBridgeConstants;
import info.u_team.draw_bridge.handler.DBMGuiHandler;
import info.u_team.u_team_core.container.UContainer;
import info.u_team.u_team_core.gui.UGuiContainer;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.*;

/**
 * @author MrTroble, Hycrafthd
 *
 */
public class DBMGuis {

	private static final List<Class<? extends UGuiContainer>> gui_list = new ArrayList<Class<? extends UGuiContainer>>();
	private static final List<Class<? extends UContainer>> container_list = new ArrayList<Class<? extends UContainer>>();
	
	public static void preinit() {
		NetworkRegistry.INSTANCE.registerGuiHandler(DrawBridgeConstants.MODID, new DBMGuiHandler());
	}
	
	public static int addContainer(Class<? extends UContainer> container) {
		int id = gui_list.size();
		gui_list.add(null);
		container_list.add(container);
		return id;
	}
	
	@SideOnly(Side.CLIENT)
	public static void addGuiContainer(Class<? extends UGuiContainer> gui, int id) {
		gui_list.set(id, gui);
	}
	
	@Deprecated // Dont use cause it will not set the container list on server side. That WILL
				// CAUSE BUGS. We need to enhance that
	@SideOnly(Side.CLIENT)
	public static int addGuiOnly(Class<? extends UGuiContainer> gui) {
		int id = gui_list.size();
		gui_list.add(gui);
		container_list.add(null);
		return id;
	}
	
	@SideOnly(Side.CLIENT)
	public static Class<? extends UGuiContainer> getGui(int id) {
		return gui_list.get(id);
	}
	
	public static Class<? extends UContainer> getContainer(int id) {
		return container_list.get(id);
}
	
}
