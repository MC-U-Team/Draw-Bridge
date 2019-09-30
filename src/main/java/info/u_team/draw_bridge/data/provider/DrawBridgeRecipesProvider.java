package info.u_team.draw_bridge.data.provider;

import static info.u_team.draw_bridge.init.DrawBridgeBlocks.DRAW_BRIDGE;

import java.util.function.Consumer;

import net.minecraft.block.Blocks;
import net.minecraft.data.*;
import net.minecraftforge.common.Tags;

public class DrawBridgeRecipesProvider extends CommonRecipesProvider {
	
	public DrawBridgeRecipesProvider(DataGenerator generator) {
		super(generator);
	}
	
	@Override
	protected void addRecipes(Consumer<IFinishedRecipe> consumer) {
		ShapedRecipeBuilder.shapedRecipe(DRAW_BRIDGE) //
				.patternLine("ICI") //
				.patternLine("RPR") //
				.patternLine("RPR") //
				.key('I', getIngredientOfTag(Tags.Items.INGOTS_IRON)) //
				.key('C', getIngredientOfTag(Tags.Items.CHESTS_WOODEN)) //
				.key('R', getIngredientOfTag(Tags.Items.DUSTS_REDSTONE)) //
				.key('P', Blocks.PISTON) //
				.addCriterion("has_redstone", hasItem(Tags.Items.DUSTS_REDSTONE)) //
				.build(consumer);
	}
}
