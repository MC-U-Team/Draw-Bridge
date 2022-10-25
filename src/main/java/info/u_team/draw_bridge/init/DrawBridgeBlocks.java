package info.u_team.draw_bridge.init;

import info.u_team.draw_bridge.DrawBridgeMod;
import info.u_team.draw_bridge.block.DrawBridgeBlock;
import info.u_team.u_team_core.util.registry.BlockDeferredRegister;
import info.u_team.u_team_core.util.registry.BlockRegistryObject;
import net.minecraft.world.item.BlockItem;
import net.minecraftforge.eventbus.api.IEventBus;

public class DrawBridgeBlocks {
	
	public static final BlockDeferredRegister BLOCKS = BlockDeferredRegister.create(DrawBridgeMod.MODID);
	
	public static final BlockRegistryObject<DrawBridgeBlock, BlockItem> DRAW_BRIDGE = BLOCKS.register("draw_bridge", DrawBridgeBlock::new);
	
	public static void registerMod(IEventBus bus) {
		BLOCKS.register(bus);
	}
	
}
