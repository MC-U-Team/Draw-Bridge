package info.u_team.draw_bridge.proxy;

import info.u_team.draw_bridge.init.DrawBridgeBlocks;
import info.u_team.draw_bridge.init.DrawBridgeCreativeTabs;

public class CommonProxy {
	
	public void preinit() {
		DrawBridgeBlocks.preinit();
	}
	
	public void init() {
		DrawBridgeCreativeTabs.init();
	//	GuiRegistry.register(location, function);registerGuiHandler(DrawBridgeConstants.MODID, new DrawBridgeGuiHandler());
	}
	
	public void postinit() {
	}
	
}
