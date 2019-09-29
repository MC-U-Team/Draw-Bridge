package info.u_team.draw_bridge.init;

import info.u_team.draw_bridge.DrawBridgeMod;
import info.u_team.draw_bridge.container.DrawBridgeContainer;
import info.u_team.u_team_core.containertype.UContainerType;
import info.u_team.u_team_core.util.registry.BaseRegistryUtil;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = DrawBridgeMod.MODID, bus = Bus.MOD)
public class DrawBridgeContainerTypes {
	
	public static final ContainerType<DrawBridgeContainer> DRAW_BRIDGE = new UContainerType<DrawBridgeContainer>("draw_bridge", DrawBridgeContainer::new);
	
	@SubscribeEvent
	public static void register(Register<ContainerType<?>> event) {
		BaseRegistryUtil.getAllGenericRegistryEntriesAndApplyNames(DrawBridgeMod.MODID, ContainerType.class).forEach(event.getRegistry()::register);
	}
	
}
