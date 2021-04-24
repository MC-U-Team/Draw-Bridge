package info.u_team.draw_bridge.data.provider;

import static info.u_team.draw_bridge.init.DrawBridgeBlocks.DRAW_BRIDGE;

import java.util.function.BiConsumer;

import info.u_team.u_team_core.data.CommonLootTablesProvider;
import info.u_team.u_team_core.data.GenerationData;
import net.minecraft.loot.LootTable;
import net.minecraft.util.ResourceLocation;

public class DrawBridgeLootTableProvider extends CommonLootTablesProvider {
	
	public DrawBridgeLootTableProvider(GenerationData data) {
		super(data);
	}
	
	@Override
	protected void registerLootTables(BiConsumer<ResourceLocation, LootTable> consumer) {
		registerBlock(DRAW_BRIDGE, addBasicBlockLootTable(DRAW_BRIDGE.get()), consumer);
	}
	
}
