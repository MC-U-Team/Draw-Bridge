package info.u_team.draw_bridge.init;

import info.u_team.draw_bridge.screen.DrawBridgeScreen;
import info.u_team.u_team_core.util.registry.ClientRegistry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class DrawBridgeScreens {
	
	private static void setup(FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			ClientRegistry.registerScreen(DrawBridgeContainerTypes.DRAW_BRIDGE, DrawBridgeScreen::new);
		});
	}
	
	public static void registerMod(IEventBus bus) {
		bus.addListener(DrawBridgeScreens::setup);
	}
	
}
