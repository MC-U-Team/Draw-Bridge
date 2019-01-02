package info.u_team.draw_bridge.proxy;

import info.u_team.draw_bridge.DrawBridgeConstants;
import info.u_team.draw_bridge.handler.DrawBridgeGuiHandler;
import info.u_team.draw_bridge.init.*;
import info.u_team.u_team_core.registry.CommonRegistry;
import net.minecraftforge.fml.common.event.*;

public class CommonProxy {
	
	public void preinit(FMLPreInitializationEvent event) {
		DrawBridgeBlocks.preinit();
	}
	
	public void init(FMLInitializationEvent event) {
		DrawBridgeCreativeTabs.init();
		CommonRegistry.registerGuiHandler(DrawBridgeConstants.MODID, new DrawBridgeGuiHandler());
	}
	
	public void postinit(FMLPostInitializationEvent event) {
	}
	
}
