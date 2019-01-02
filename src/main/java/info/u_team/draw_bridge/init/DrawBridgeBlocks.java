package info.u_team.draw_bridge.init;

import info.u_team.draw_bridge.DrawBridgeConstants;
import info.u_team.draw_bridge.block.BlockDrawBridge;
import info.u_team.u_team_core.registry.BlockRegistry;
import info.u_team.u_team_core.util.RegistryUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class DrawBridgeBlocks {
	
	public static final Block draw_bridge = new BlockDrawBridge("drawbridge");
	
	public static void preinit() {
		BlockRegistry.register(DrawBridgeConstants.MODID, RegistryUtil.getRegistryEntries(Block.class, DrawBridgeBlocks.class));
	}
	
}
