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

package info.uteam.drawbridges;

import info.uteam.drawbridges.proxy.DBMCommonProxy;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.*;

/**
 * 
 * @author MrTroble
 *
 */

@Mod(modid = DBMConstants.MODID, name = DBMConstants.MODNAME, version = DBMConstants.VERSION,  acceptedMinecraftVersions = DBMConstants.MCVERSION, dependencies = DBMConstants.DEPENDENCIES)
public class DBMMain {

	@SidedProxy(clientSide=DBMConstants.CLIENT_PROXY,serverSide=DBMConstants.SERVER_PROXY)
	public static DBMCommonProxy PROXY;
	
	@EventHandler
	public void preinit(FMLPreInitializationEvent ev) {
		PROXY.preinit();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent ev) {
		PROXY.init();
	}

	@EventHandler
	public void postinit(FMLPostInitializationEvent ev) {
		PROXY.postinit();
	}
}
