package info.u_team.draw_bridge.data.provider;

import static info.u_team.draw_bridge.init.DrawBridgeBlocks.DRAW_BRIDGE;

import java.util.function.BiConsumer;

import info.u_team.u_team_core.data.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTable;

public class DrawBridgeLootTableProvider extends CommonLootTablesProvider {
	
	public DrawBridgeLootTableProvider(GenerationData data) {
		super(data);
	}
	
	@Override
	protected void registerLootTables(BiConsumer<ResourceLocation, LootTable> consumer) {
		registerBlock(DRAW_BRIDGE, addBasicBlockLootTable(DRAW_BRIDGE.get()), consumer);
	}
	
}
