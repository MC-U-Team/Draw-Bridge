package info.u_team.draw_bridge.data.provider;

import static info.u_team.draw_bridge.init.DrawBridgeBlocks.DRAW_BRIDGE;

import java.util.function.Consumer;

import info.u_team.u_team_core.data.*;
import net.minecraft.block.Blocks;
import net.minecraft.data.*;
import net.minecraftforge.common.Tags;

public class DrawBridgeRecipesProvider extends CommonRecipesProvider {
	
	public DrawBridgeRecipesProvider(GenerationData data) {
		super(data);
	}
	
	@Override
	protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
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
