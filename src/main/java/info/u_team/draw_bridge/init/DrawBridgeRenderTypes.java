package info.u_team.draw_bridge.init;

import info.u_team.draw_bridge.DrawBridgeMod;
import net.minecraft.client.renderer.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = DrawBridgeMod.MODID, bus = Bus.MOD, value = Dist.CLIENT)
public class DrawBridgeRenderTypes {
	
	@SubscribeEvent
	public static void register(FMLClientSetupEvent event) {
		// Translucent
		final RenderType translucent = RenderType.getTranslucent();
		
		RenderTypeLookup.setRenderLayer(DrawBridgeBlocks.DRAW_BRIDGE.get(), translucent);
	}
	
}
