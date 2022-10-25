package info.u_team.draw_bridge.data.provider;

import static info.u_team.draw_bridge.init.DrawBridgeBlocks.DRAW_BRIDGE;

import info.u_team.u_team_core.data.CommonBlockStateProvider;
import info.u_team.u_team_core.data.GenerationData;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.ConfiguredModel;

public class DrawBridgeBlockStatesProvider extends CommonBlockStateProvider {
	
	public DrawBridgeBlockStatesProvider(GenerationData generationData) {
		super(generationData);
	}
	
	@Override
	public void register() {
		final ResourceLocation side = modLoc("block/draw_bridge_side");
		final ResourceLocation front = modLoc("block/draw_bridge_front");
		final ResourceLocation back = modLoc("block/draw_bridge_back");
		
		getVariantBuilder(DRAW_BRIDGE.get()).forAllStates(state -> {
			final Direction direction = state.getValue(BlockStateProperties.FACING);
			return ConfiguredModel.builder() //
					.modelFile(models().cube("draw_bridge", side, side, front, back, side, side).texture("particle", side)) //
					.rotationX(direction == Direction.DOWN ? 90 : direction == Direction.UP ? 270 : 0) //
					.rotationY(direction.getAxis().isVertical() ? 0 : (((int) direction.toYRot()) + 180) % 360) //
					.build(); //
		});
	}
}
