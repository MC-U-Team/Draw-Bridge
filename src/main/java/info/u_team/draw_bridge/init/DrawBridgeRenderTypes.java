package info.u_team.draw_bridge.init;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class DrawBridgeRenderTypes {
	
	private static void setup(FMLClientSetupEvent event) {
		ItemBlockRenderTypes.setRenderLayer(DrawBridgeBlocks.DRAW_BRIDGE_CUTOUT.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(DrawBridgeBlocks.DRAW_BRIDGE_CUTOUT_MIPPED.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(DrawBridgeBlocks.DRAW_BRIDGE_TRANSLUCENT.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(DrawBridgeBlocks.DRAW_BRIDGE_TRIPWIRE.get(), RenderType.tripwire());
	}
	
	public static void registerMod(IEventBus bus) {
		bus.addListener(DrawBridgeRenderTypes::setup);
	}
	
}
