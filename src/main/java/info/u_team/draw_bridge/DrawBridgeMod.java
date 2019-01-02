package info.u_team.draw_bridge;

import static info.u_team.draw_bridge.DrawBridgeConstants.*;

import info.u_team.draw_bridge.proxy.*;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.Mod.*;
import net.minecraftforge.fml.common.event.*;;

@Mod(modid = MODID, name = NAME, version = VERSION, acceptedMinecraftVersions = MCVERSION, dependencies = DEPENDENCIES, updateJSON = UPDATEURL)
public class DrawBridgeMod {
	
	@Instance
	private static DrawBridgeMod instance;
	
	public static DrawBridgeMod getInstance() {
		return instance;
	}
	
	@SidedProxy(serverSide = COMMONPROXY, clientSide = CLIENTPROXY)
	private static CommonProxy proxy;
	
	@EventHandler
	public void preinit(FMLPreInitializationEvent event) {
		proxy.preinit(event);
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init(event);
	}
	
	@EventHandler
	public void postinit(FMLPostInitializationEvent event) {
		proxy.postinit(event);
	}
}
