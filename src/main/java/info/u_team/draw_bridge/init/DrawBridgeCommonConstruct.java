package info.u_team.draw_bridge.init;

import info.u_team.draw_bridge.DrawBridgeMod;
import info.u_team.u_team_core.api.construct.Construct;
import info.u_team.u_team_core.api.construct.ModConstruct;

@Construct(modid = DrawBridgeMod.MODID)
public class DrawBridgeCommonConstruct implements ModConstruct {
	
	@Override
	public void construct() {
		DrawBridgeBlockEntityTypes.register();
		DrawBridgeBlocks.register();
		DrawBridgeCreativeTabs.register();
		DrawBridgeMenuTypes.register();
	}
	
}
