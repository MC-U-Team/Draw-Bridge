package info.u_team.draw_bridge.init;

import info.u_team.draw_bridge.DrawBridgeMod;
import info.u_team.draw_bridge.tileentity.DrawBridgeTileEntity;
import info.u_team.u_team_core.util.registry.TileEntityTypeDeferredRegister;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;

public class DrawBridgeTileEntityTypes {
	
	public static final TileEntityTypeDeferredRegister TILE_ENTITY_TYPES = TileEntityTypeDeferredRegister.create(DrawBridgeMod.MODID);
	
	public static final RegistryObject<BlockEntityType<DrawBridgeTileEntity>> DRAW_BRIDGE = TILE_ENTITY_TYPES.register("draw_bridge", () -> BlockEntityType.Builder.of(DrawBridgeTileEntity::new, DrawBridgeBlocks.DRAW_BRIDGE.get(), DrawBridgeBlocks.DRAW_BRIDGE_CUTOUT.get(), DrawBridgeBlocks.DRAW_BRIDGE_CUTOUT_MIPPED.get(), DrawBridgeBlocks.DRAW_BRIDGE_TRANSLUCENT.get(), DrawBridgeBlocks.DRAW_BRIDGE_TRIPWIRE.get()));
	
	public static void registerMod(IEventBus bus) {
		TILE_ENTITY_TYPES.register(bus);
	}
	
}
