package info.u_team.draw_bridge.data.provider;

import static info.u_team.draw_bridge.init.DrawBridgeBlocks.DRAW_BRIDGE;

import java.util.function.BiConsumer;

import info.u_team.u_team_core.data.CommonLootTableProvider;
import info.u_team.u_team_core.data.GenerationData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;

public class DrawBridgeLootTableProvider extends CommonLootTableProvider {
	
	public DrawBridgeLootTableProvider(GenerationData generationData) {
		super(generationData);
	}
	
	@Override
	public void register(BiConsumer<ResourceLocation, LootTable> consumer) {
		registerBlock(DRAW_BRIDGE, addBasicBlockLootTable(DRAW_BRIDGE.get()), consumer);
	}
	
}
