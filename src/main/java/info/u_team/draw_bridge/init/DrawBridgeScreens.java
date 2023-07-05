package info.u_team.draw_bridge.init;

import info.u_team.draw_bridge.screen.DrawBridgeScreen;
import info.u_team.u_team_core.api.registry.client.MenuScreenRegister;
import net.minecraft.Util;

public class DrawBridgeScreens {
	
	private static final MenuScreenRegister MENU_SCREENS = Util.make(MenuScreenRegister.create(), menuScreens -> {
		menuScreens.register(DrawBridgeMenuTypes.DRAW_BRIDGE, DrawBridgeScreen::new);
	});
	
	static void register() {
		MENU_SCREENS.register();
	}
	
}
