package info.u_team.draw_bridge.init;

import info.u_team.draw_bridge.DrawBridgeMod;
import info.u_team.draw_bridge.tileentity.DrawBridgeTileEntity;
import info.u_team.u_team_core.util.registry.TileEntityTypeDeferredRegister;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;

public class DrawBridgeTileEntityTypes {
	
	public static final TileEntityTypeDeferredRegister TILE_ENTITY_TYPES = TileEntityTypeDeferredRegister.create(DrawBridgeMod.MODID);
	
	public static final RegistryObject<TileEntityType<DrawBridgeTileEntity>> DRAW_BRIDGE = TILE_ENTITY_TYPES.register("draw_bridge", () -> TileEntityType.Builder.create(DrawBridgeTileEntity::new, DrawBridgeBlocks.DRAW_BRIDGE.get()));
	
	public static void register(IEventBus bus) {
		TILE_ENTITY_TYPES.register(bus);
	}
	
}
