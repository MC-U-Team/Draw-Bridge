package info.u_team.draw_bridge.data.provider;

import static info.u_team.draw_bridge.init.DrawBridgeBlocks.DRAW_BRIDGE;

import info.u_team.u_team_core.data.*;

public class DrawBridgeItemModelsProvider extends CommonItemModelsProvider {
	
	public DrawBridgeItemModelsProvider(GenerationData data) {
		super(data);
	}
	
	@Override
	protected void registerModels() {
		simpleBlock(DRAW_BRIDGE.get());
	}
	
}
