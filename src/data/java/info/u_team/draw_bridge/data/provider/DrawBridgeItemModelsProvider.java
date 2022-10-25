package info.u_team.draw_bridge.data.provider;

import static info.u_team.draw_bridge.init.DrawBridgeBlocks.DRAW_BRIDGE;

import info.u_team.u_team_core.data.CommonItemModelProvider;
import info.u_team.u_team_core.data.GenerationData;

public class DrawBridgeItemModelsProvider extends CommonItemModelProvider {
	
	public DrawBridgeItemModelsProvider(GenerationData generationData) {
		super(generationData);
	}
	
	@Override
	public void register() {
		simpleBlock(DRAW_BRIDGE.get());
	}
	
}
