package info.u_team.draw_bridge;

import info.u_team.draw_bridge.init.*;
import info.u_team.u_team_core.util.registry.BusRegister;
import info.u_team.u_team_core.util.verify.JarSignVerifier;
import net.minecraftforge.fml.common.Mod;

@Mod(DrawBridgeMod.MODID)
public class DrawBridgeMod {
	
	public static final String MODID = "drawbridge";
	
	public DrawBridgeMod() {
		JarSignVerifier.checkSigned(MODID);
		register();
	}
	
	private void register() {
		BusRegister.registerMod(DrawBridgeBlocks::register);
		BusRegister.registerMod(DrawBridgeContainerTypes::register);
		BusRegister.registerMod(DrawBridgeTileEntityTypes::register);
	}
	
}
