package info.u_team.draw_bridge.data;

import info.u_team.draw_bridge.DrawBridgeMod;
import info.u_team.draw_bridge.data.provider.DrawBridgeBlockStatesProvider;
import info.u_team.draw_bridge.data.provider.DrawBridgeItemModelsProvider;
import info.u_team.draw_bridge.data.provider.DrawBridgeLanguagesProvider;
import info.u_team.draw_bridge.data.provider.DrawBridgeLootTableProvider;
import info.u_team.draw_bridge.data.provider.DrawBridgeRecipesProvider;
import info.u_team.u_team_core.data.GenerationData;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = DrawBridgeMod.MODID, bus = Bus.MOD)
public class DrawBridgeDataGenerator {
	
	@SubscribeEvent
	public static void data(GatherDataEvent event) {
		final GenerationData data = new GenerationData(DrawBridgeMod.MODID, event);
		data.addProvider(event.includeServer(), DrawBridgeLootTableProvider::new);
		data.addProvider(event.includeServer(), DrawBridgeRecipesProvider::new);
		
		data.addProvider(event.includeClient(), DrawBridgeBlockStatesProvider::new);
		data.addProvider(event.includeClient(), DrawBridgeItemModelsProvider::new);
		data.addProvider(event.includeClient(), DrawBridgeLanguagesProvider::new);
	}
	
}
