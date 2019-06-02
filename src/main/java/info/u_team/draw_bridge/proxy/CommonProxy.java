package info.u_team.draw_bridge.proxy;

import info.u_team.draw_bridge.init.DrawBridgeCreativeTabs;

public class CommonProxy {
	
	public void preinit() {
		
	}
	
	public void init() {
	//	GuiRegistry.register(location, function);registerGuiHandler(DrawBridgeConstants.MODID, new DrawBridgeGuiHandler());
	}
	
	public void postinit() {
		DrawBridgeCreativeTabs.init();
	}
	
}
