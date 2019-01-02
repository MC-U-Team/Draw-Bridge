package info.u_team.draw_bridge.proxy;

import info.u_team.draw_bridge.init.*;
import net.minecraftforge.fml.common.event.*;

public class CommonProxy {
	
	public void preinit(FMLPreInitializationEvent event) {
		DrawBridgeBlocks.preinit();
	}
	
	public void init(FMLInitializationEvent event) {
		DrawBridgeCreativeTabs.init();
	}
	
	public void postinit(FMLPostInitializationEvent event) {
	}
	
}
