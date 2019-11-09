package info.u_team.draw_bridge.data;

import info.u_team.draw_bridge.DrawBridgeMod;
import info.u_team.draw_bridge.data.provider.*;
import info.u_team.u_team_core.data.GenerationData;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@EventBusSubscriber(modid = DrawBridgeMod.MODID, bus = Bus.MOD)
public class DrawBridgeDataGenerator {
	
	@SubscribeEvent
	public static void data(GatherDataEvent event) {
		final GenerationData data = new GenerationData(DrawBridgeMod.MODID, event);
		if (event.includeServer()) {
			data.addProvider(DrawBridgeLootTableProvider::new);
			data.addProvider(DrawBridgeRecipesProvider::new);
		}
		if (event.includeClient()) {
			data.addProvider(DrawBridgeBlockStatesProvider::new);
			data.addProvider(DrawBridgeItemModelsProvider::new);
		}
	}
	
}
