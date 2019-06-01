package info.u_team.draw_bridge.proxy;

import info.u_team.draw_bridge.event.EventHandlerModelBake;
import info.u_team.u_team_core.registry.util.CommonRegistry;

public class ClientProxy extends CommonProxy {
	
	@Override
	public void preinit() {
		super.preinit();
		CommonRegistry.registerEventHandler(EventHandlerModelBake.class);
	}
	
	@Override
	public void init() {
		super.init();
	}
	
	@Override
	public void postinit() {
		super.postinit();
	}
	
}
