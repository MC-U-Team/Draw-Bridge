package info.u_team.draw_bridge.data.provider;

import static info.u_team.draw_bridge.init.DrawBridgeBlocks.DRAW_BRIDGE;

import java.util.function.Consumer;

import info.u_team.u_team_core.data.CommonRecipeProvider;
import info.u_team.u_team_core.data.GenerationData;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;

public class DrawBridgeRecipesProvider extends CommonRecipeProvider {
	
	public DrawBridgeRecipesProvider(GenerationData generationData) {
		super(generationData);
	}
	
	@Override
	public void register(Consumer<FinishedRecipe> consumer) {
		ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, DRAW_BRIDGE.get()) //
				.pattern("ICI") //
				.pattern("RPR") //
				.pattern("RPR") //
				.define('I', getIngredientOfTag(Tags.Items.INGOTS_IRON)) //
				.define('C', getIngredientOfTag(Tags.Items.CHESTS_WOODEN)) //
				.define('R', getIngredientOfTag(Tags.Items.DUSTS_REDSTONE)) //
				.define('P', Blocks.PISTON) //
				.unlockedBy("has_redstone", has(Tags.Items.DUSTS_REDSTONE)) //
				.save(consumer);
	}
}
