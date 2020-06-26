package info.u_team.draw_bridge.init;

import info.u_team.draw_bridge.DrawBridgeMod;
import info.u_team.draw_bridge.container.DrawBridgeContainer;
import info.u_team.u_team_core.containertype.UContainerType;
import info.u_team.u_team_core.util.registry.CommonDeferredRegister;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.ForgeRegistries;

public class DrawBridgeContainerTypes {
	
	public static final CommonDeferredRegister<ContainerType<?>> CONTAINER_TYPES = CommonDeferredRegister.create(ForgeRegistries.CONTAINERS, DrawBridgeMod.MODID);
	
	public static final ContainerType<DrawBridgeContainer> DRAW_BRIDGE = new UContainerType<DrawBridgeContainer>("draw_bridge", DrawBridgeContainer::new);
	
	public static void register(IEventBus bus) {
		CONTAINER_TYPES.register(bus);
	}
	
}
