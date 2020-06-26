package info.u_team.draw_bridge.init;

import info.u_team.draw_bridge.DrawBridgeMod;
import info.u_team.draw_bridge.tileentity.DrawBridgeTileEntity;
import info.u_team.u_team_core.tileentitytype.UTileEntityType.UBuilder;
import info.u_team.u_team_core.util.registry.*;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.eventbus.api.*;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.ForgeRegistries;

public class DrawBridgeTileEntityTypes {
	
	public static final TileEntityTypeDeferredRegister TILE_ENTITY_TYPES = TileEntityTypeDeferredRegister.create(DrawBridgeMod.MODID);
	
	public static final TileEntityType<DrawBridgeTileEntity> DRAW_BRIDGE = UBuilder.create("draw_bridge", DrawBridgeTileEntity::new, DrawBridgeBlocks.DRAW_BRIDGE).build();
	
	public static void register(IEventBus bus) {
		TILE_ENTITY_TYPES.register(bus);
	}
	
}
