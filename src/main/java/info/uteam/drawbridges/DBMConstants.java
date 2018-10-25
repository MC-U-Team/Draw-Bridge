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

import org.apache.logging.log4j.*;

/**
 * 
 * @author MrTroble
 *
 */

public class DBMConstants {
	
	public static final String MODID = "uteam_drawbridges";
	public static final String MODNAME = "UTeam Drawbridges";
	
	public static final String VERSION = "${version}";
	public static final String MCVERSION = "${mcversion}";
    public static final String DEPENDENCIES = "required-after:uteamcore@[2.0.0.75-SNAPSHOT,)";
	
	public static final String CLIENT_PROXY = "info.uteam.drawbridges.proxy";
	public static final String SERVER_PROXY = "info.uteam.drawbridges.proxy";
	
	public static final Logger LOGGER = LogManager.getLogger(MODNAME);
	
}
