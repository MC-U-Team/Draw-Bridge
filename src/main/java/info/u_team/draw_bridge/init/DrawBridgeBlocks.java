package info.u_team.draw_bridge.init;

import info.u_team.draw_bridge.DrawBridgeMod;
import info.u_team.draw_bridge.block.DrawBridgeBlock;
import info.u_team.u_team_core.api.registry.BlockRegister;
import info.u_team.u_team_core.api.registry.BlockRegistryEntry;
import net.minecraft.world.item.Item;

public class DrawBridgeBlocks {
	
	public static final BlockRegister BLOCKS = BlockRegister.create(DrawBridgeMod.MODID);
	
	public static final BlockRegistryEntry<DrawBridgeBlock, Item> DRAW_BRIDGE = BLOCKS.register("draw_bridge", DrawBridgeBlock::new);
	
	static void register() {
		BLOCKS.register();
	}
	
}
