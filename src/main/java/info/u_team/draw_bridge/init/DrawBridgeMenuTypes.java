package info.u_team.draw_bridge.init;

import info.u_team.draw_bridge.DrawBridgeMod;
import info.u_team.draw_bridge.menu.DrawBridgeMenu;
import info.u_team.u_team_core.menutype.UMenuType;
import info.u_team.u_team_core.util.registry.CommonDeferredRegister;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class DrawBridgeMenuTypes {
	
	public static final CommonDeferredRegister<MenuType<?>> MENU_TYPES = CommonDeferredRegister.create(ForgeRegistries.MENU_TYPES, DrawBridgeMod.MODID);
	
	public static final RegistryObject<MenuType<DrawBridgeMenu>> DRAW_BRIDGE = MENU_TYPES.register("draw_bridge", () -> new UMenuType<>(DrawBridgeMenu::new));
	
	public static void registerMod(IEventBus bus) {
		MENU_TYPES.register(bus);
	}
	
}
