package info.u_team.draw_bridge.data.provider;

import static info.u_team.draw_bridge.init.DrawBridgeBlocks.*;

import com.google.common.collect.ImmutableList;

import info.u_team.draw_bridge.block.DrawBridgeBlock;
import info.u_team.u_team_core.data.*;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.*;
import net.minecraftforge.client.model.generators.ConfiguredModel;

public class DrawBridgeBlockStatesProvider extends CommonBlockStatesProvider {
	
	public DrawBridgeBlockStatesProvider(GenerationData data) {
		super(data);
	}
	
	@Override
	protected void registerStatesAndModels() {
		final ResourceLocation side = modLoc("block/draw_bridge_side");
		final ResourceLocation front = modLoc("block/draw_bridge_front");
		final ResourceLocation back = modLoc("block/draw_bridge_back");
		
		getVariantBuilder(DRAW_BRIDGE.get()).forAllStates(state -> {
			final Direction direction = state.get(BlockStateProperties.FACING);
			return ConfiguredModel.builder() //
					.modelFile(models().cube("draw_bridge", side, side, front, back, side, side).texture("particle", side)) //
					.rotationX(direction == Direction.DOWN ? 90 : direction == Direction.UP ? 270 : 0) //
					.rotationY(direction.getAxis().isVertical() ? 0 : (((int) direction.getHorizontalAngle()) + 180) % 360) //
					.build(); //
		});
		
		for (DrawBridgeBlock renderBlocks : ImmutableList.of(DRAW_BRIDGE_CUTOUT.get(), DRAW_BRIDGE_CUTOUT_MIPPED.get(), DRAW_BRIDGE_TRANSLUCTENT.get(), DRAW_BRIDGE_TRIPWIRE.get())) {
			getVariantBuilder(renderBlocks).forAllStates(state -> {
				final Direction direction = state.get(BlockStateProperties.FACING);
				return ConfiguredModel.builder() //
						.modelFile(models().getExistingFile(modLoc("draw_bridge"))) //
						.rotationX(direction == Direction.DOWN ? 90 : direction == Direction.UP ? 270 : 0) //
						.rotationY(direction.getAxis().isVertical() ? 0 : (((int) direction.getHorizontalAngle()) + 180) % 360) //
						.build(); //
			});
		}
	}
}
