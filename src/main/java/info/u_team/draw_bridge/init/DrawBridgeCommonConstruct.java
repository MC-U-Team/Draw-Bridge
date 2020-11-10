package info.u_team.draw_bridge.init;

import info.u_team.draw_bridge.DrawBridgeMod;
import info.u_team.u_team_core.api.construct.*;
import info.u_team.u_team_core.util.registry.BusRegister;

@Construct(modid = DrawBridgeMod.MODID)
public class DrawBridgeCommonConstruct implements IModConstruct {
	
	@Override
	public void construct() {
		BusRegister.registerMod(DrawBridgeBlocks::register);
		BusRegister.registerMod(DrawBridgeContainerTypes::register);
		BusRegister.registerMod(DrawBridgeTileEntityTypes::register);
	}
	
}
