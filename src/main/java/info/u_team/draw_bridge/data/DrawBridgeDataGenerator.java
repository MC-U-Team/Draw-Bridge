package info.u_team.draw_bridge.data;

import info.u_team.draw_bridge.DrawBridgeMod;
import info.u_team.draw_bridge.data.provider.*;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@EventBusSubscriber(modid = DrawBridgeMod.MODID, bus = Bus.MOD)
public class DrawBridgeDataGenerator {
	
	@SubscribeEvent
	public static void data(GatherDataEvent event) {
		final DataGenerator generator = event.getGenerator();
		if (event.includeServer()) {
			generator.addProvider(new DrawBridgeLootTableProvider(generator)); // Generate loot tables
			generator.addProvider(new DrawBridgeRecipesProvider(generator)); // Generate recipes
		}
	}
	
}
