package info.u_team.draw_bridge.data.provider;

import static info.u_team.draw_bridge.init.DrawBridgeBlocks.DRAW_BRIDGE;

import java.io.IOException;
import java.nio.file.Path;

import com.google.gson.JsonElement;

import info.u_team.draw_bridge.DrawBridgeMod;
import info.u_team.u_team_core.data.CommonProvider;
import net.minecraft.data.*;
import net.minecraft.util.IItemProvider;
import net.minecraft.world.storage.loot.*;
import net.minecraft.world.storage.loot.conditions.SurvivesExplosion;

public class DrawBridgeLootTableProvider extends CommonProvider {
	
	public DrawBridgeLootTableProvider(DataGenerator generator) {
		super("Loot-Tables", generator);
	}
	
	@Override
	public void act(DirectoryCache cache) throws IOException {
		writeBasicBlockLootTable(cache, DRAW_BRIDGE);
	}
	
	@Override
	protected Path resolvePath(Path outputFolder) {
		return resolveData(outputFolder, DrawBridgeMod.MODID).resolve("loot_tables");
	}
	
	private void writeBasicBlockLootTable(DirectoryCache cache, IItemProvider itemProvider) throws IOException {
		write(cache, getBasicBlockLootTable(itemProvider), path.resolve("blocks").resolve(itemProvider.asItem().getRegistryName().getPath() + ".json"));
	}
	
	private JsonElement getBasicBlockLootTable(IItemProvider itemProvider) {
		return LootTableManager.toJson(LootTable.builder().setParameterSet(LootParameterSets.BLOCK).addLootPool(LootPool.builder().rolls(ConstantRange.of(1)).addEntry(ItemLootEntry.builder(itemProvider)).acceptCondition(SurvivesExplosion.builder())).build());
	}
	
}
