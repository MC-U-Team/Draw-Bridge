package info.u_team.draw_bridge.init;

import info.u_team.draw_bridge.DrawBridgeMod;
import info.u_team.draw_bridge.blockentity.DrawBridgeBlockEntity;
import info.u_team.u_team_core.api.registry.BlockEntityTypeRegister;
import info.u_team.u_team_core.api.registry.RegistryEntry;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class DrawBridgeBlockEntityTypes {
	
	public static final BlockEntityTypeRegister BLOCK_ENTITY_TYPES = BlockEntityTypeRegister.create(DrawBridgeMod.MODID);
	
	public static final RegistryEntry<BlockEntityType<DrawBridgeBlockEntity>> DRAW_BRIDGE = BLOCK_ENTITY_TYPES.register("draw_bridge", () -> BlockEntityType.Builder.of(DrawBridgeBlockEntity::new, DrawBridgeBlocks.DRAW_BRIDGE.get()));
	
	static void register() {
		BLOCK_ENTITY_TYPES.register();
	}
	
}
