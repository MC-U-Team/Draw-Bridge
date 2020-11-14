package info.u_team.draw_bridge.init;

import info.u_team.draw_bridge.DrawBridgeMod;
import info.u_team.draw_bridge.block.*;
import info.u_team.u_team_core.util.registry.*;
import net.minecraft.item.BlockItem;
import net.minecraftforge.eventbus.api.IEventBus;

public class DrawBridgeBlocks {
	
	public static final BlockDeferredRegister BLOCKS = BlockDeferredRegister.create(DrawBridgeMod.MODID);
	
	public static final BlockRegistryObject<DrawBridgeBlock, BlockItem> DRAW_BRIDGE = BLOCKS.register("draw_bridge", DrawBridgeBlock::new);
	
	public static final BlockRegistryObject<DrawBridgeBlock, BlockItem> DRAW_BRIDGE_CUTOUT = BLOCKS.register("draw_bridge_cutout", () -> new DrawBridgeCamouflage(DRAW_BRIDGE));
	public static final BlockRegistryObject<DrawBridgeBlock, BlockItem> DRAW_BRIDGE_CUTOUT_MIPPED = BLOCKS.register("draw_bridge_cutout_mipped", () -> new DrawBridgeCamouflage(DRAW_BRIDGE));
	public static final BlockRegistryObject<DrawBridgeBlock, BlockItem> DRAW_BRIDGE_TRANSLUCENT = BLOCKS.register("draw_bridge_translucent", () -> new DrawBridgeCamouflage(DRAW_BRIDGE));
	public static final BlockRegistryObject<DrawBridgeBlock, BlockItem> DRAW_BRIDGE_TRIPWIRE = BLOCKS.register("draw_bridge_tripwire", () -> new DrawBridgeCamouflage(DRAW_BRIDGE));
	
	public static void registerMod(IEventBus bus) {
		BLOCKS.register(bus);
	}
	
}
