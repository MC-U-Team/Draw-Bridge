package info.u_team.draw_bridge;

import org.apache.logging.log4j.*;

public class DrawBridgeConstants {
	
	public static final String MODID = "drawbridge";
	public static final String NAME = "Draw Bridge";
	public static final String VERSION = "${version}";
	public static final String MCVERSION = "${mcversion}";
	public static final String DEPENDENCIES = "required:forge@[14.23.4.2745,);required-after:uteamcore@[2.2.1.91,)";
	public static final String UPDATEURL = "https://api.u-team.info/update/drawbridge.json";
	
	public static final String COMMONPROXY = "info.u_team.draw_bridge.proxy.CommonProxy";
	public static final String CLIENTPROXY = "info.u_team.draw_bridge.proxy.ClientProxy";
	
	public static final Logger LOGGER = LogManager.getLogger(NAME);
	
}
