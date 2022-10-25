package info.u_team.draw_bridge.init;

import info.u_team.draw_bridge.screen.DrawBridgeScreen;
import info.u_team.u_team_core.event.RegisterMenuScreensEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class DrawBridgeScreens {
	
	private static void register(RegisterMenuScreensEvent event) {
		event.registerScreen(DrawBridgeMenuTypes.DRAW_BRIDGE, DrawBridgeScreen::new);
	}
	
	public static void registerMod(IEventBus bus) {
		bus.addListener(DrawBridgeScreens::register);
	}
	
}
