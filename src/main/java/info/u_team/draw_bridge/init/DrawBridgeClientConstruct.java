package info.u_team.draw_bridge.init;

import info.u_team.draw_bridge.DrawBridgeMod;
import info.u_team.u_team_core.api.construct.Construct;
import info.u_team.u_team_core.api.construct.ModConstruct;
import info.u_team.u_team_core.util.registry.BusRegister;

@Construct(modid = DrawBridgeMod.MODID, client = true)
public class DrawBridgeClientConstruct implements ModConstruct {
	
	@Override
	public void construct() {
		BusRegister.registerMod(DrawBridgeColors::registerMod);
		BusRegister.registerMod(DrawBridgeModels::registerMod);
		BusRegister.registerMod(DrawBridgeScreens::registerMod);
	}
	
}
