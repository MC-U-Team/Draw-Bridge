package info.u_team.draw_bridge.init;

import info.u_team.draw_bridge.DrawBridgeMod;
import info.u_team.draw_bridge.screen.DrawBridgeScreen;
import info.u_team.u_team_core.util.registry.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = DrawBridgeMod.MODID, bus = Bus.MOD, value = Dist.CLIENT)
public class DrawBridgeScreens {
	
	@SubscribeEvent
	public static void register(FMLClientSetupEvent event) {
		MainThreadWorker.run(() -> {
			ClientRegistry.registerScreen(DrawBridgeContainerTypes.DRAW_BRIDGE, DrawBridgeScreen::new);
		});
	}
	
}
