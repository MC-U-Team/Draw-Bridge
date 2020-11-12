package info.u_team.draw_bridge.init;

import net.minecraft.client.renderer.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class DrawBridgeRenderTypes {
	
	private static void setup(FMLClientSetupEvent event) {
		RenderTypeLookup.setRenderLayer(DrawBridgeBlocks.DRAW_BRIDGE_SOLID.get(), RenderType.getSolid());
		RenderTypeLookup.setRenderLayer(DrawBridgeBlocks.DRAW_BRIDGE_CUTOUT.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(DrawBridgeBlocks.DRAW_BRIDGE_CUTOUT_MIPPED.get(), RenderType.getCutoutMipped());
		RenderTypeLookup.setRenderLayer(DrawBridgeBlocks.DRAW_BRIDGE_TRANSLUCTENT.get(), RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(DrawBridgeBlocks.DRAW_BRIDGE_TRIPWIRE.get(), RenderType.getTripwire());
	}
	
	public static void registerMod(IEventBus bus) {
		bus.addListener(DrawBridgeRenderTypes::setup);
	}
	
}
