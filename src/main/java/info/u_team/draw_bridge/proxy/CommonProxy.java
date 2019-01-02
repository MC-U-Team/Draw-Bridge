package info.u_team.draw_bridge.proxy;

import info.uteam.drawbridges.init.*;
import net.minecraftforge.fml.common.event.*;

public class CommonProxy {
	
	public void preinit(FMLPreInitializationEvent event) {
		DBMBlocks.preinit();
		DBMGuis.preinit();
	}
	
	public void init(FMLInitializationEvent event) {
		DBMCreativeTabs.init();
	}
	
	public void postinit(FMLPostInitializationEvent event) {
	}
	
}
