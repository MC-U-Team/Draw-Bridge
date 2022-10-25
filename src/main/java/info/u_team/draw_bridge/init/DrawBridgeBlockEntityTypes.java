package info.u_team.draw_bridge.init;

import info.u_team.draw_bridge.DrawBridgeMod;
import info.u_team.draw_bridge.blockentity.DrawBridgeBlockEntity;
import info.u_team.u_team_core.util.registry.BlockEntityTypeDeferredRegister;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;

public class DrawBridgeBlockEntityTypes {
	
	public static final BlockEntityTypeDeferredRegister BLOCK_ENTITY_TYPES = BlockEntityTypeDeferredRegister.create(DrawBridgeMod.MODID);
	
	public static final RegistryObject<BlockEntityType<DrawBridgeBlockEntity>> DRAW_BRIDGE = BLOCK_ENTITY_TYPES.register("draw_bridge", () -> BlockEntityType.Builder.of(DrawBridgeBlockEntity::new, DrawBridgeBlocks.DRAW_BRIDGE.get()));
	
	public static void registerMod(IEventBus bus) {
		BLOCK_ENTITY_TYPES.register(bus);
	}
	
}