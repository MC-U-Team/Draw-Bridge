package info.u_team.draw_bridge.init;

import net.minecraft.client.renderer.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class DrawBridgeRenderTypes {
	
	private static void setup(FMLClientSetupEvent event) {
		// Translucent
		final RenderType translucent = RenderType.getTranslucent();
		
		RenderTypeLookup.setRenderLayer(DrawBridgeBlocks.DRAW_BRIDGE.get(), translucent);
	}
	
	public static void registerMod(IEventBus bus) {
		bus.addListener(DrawBridgeRenderTypes::setup);
	}
	
}
