package info.u_team.draw_bridge.data.provider;

import static info.u_team.draw_bridge.init.DrawBridgeBlocks.DRAW_BRIDGE;

import java.util.function.Consumer;

import info.u_team.u_team_core.data.CommonRecipesProvider;
import info.u_team.u_team_core.data.GenerationData;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraftforge.common.Tags;

public class DrawBridgeRecipesProvider extends CommonRecipesProvider {
	
	public DrawBridgeRecipesProvider(GenerationData data) {
		super(data);
	}
	
	@Override
	protected void registerRecipes(Consumer<FinishedRecipe> consumer) {
		ShapedRecipeBuilder.shaped(DRAW_BRIDGE.get()) //
				.pattern("ICI") //
				.pattern("RPR") //
				.pattern("RPR") //
				.define('I', getIngredientOfTag(Tags.Items.INGOTS_IRON)) //
				.define('C', getIngredientOfTag(Tags.Items.CHESTS_WOODEN)) //
				.define('R', getIngredientOfTag(Tags.Items.DUSTS_REDSTONE)) //
				.define('P', Blocks.PISTON) //
				.unlockedBy("has_redstone", hasItem(Tags.Items.DUSTS_REDSTONE)) //
				.save(consumer);
	}
}
