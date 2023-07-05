package info.u_team.draw_bridge.init;

import info.u_team.draw_bridge.DrawBridgeMod;
import info.u_team.draw_bridge.menu.DrawBridgeMenu;
import info.u_team.u_team_core.api.registry.CommonRegister;
import info.u_team.u_team_core.api.registry.RegistryEntry;
import info.u_team.u_team_core.menutype.UMenuType;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;

public class DrawBridgeMenuTypes {
	
	public static final CommonRegister<MenuType<?>> MENU_TYPES = CommonRegister.create(Registries.MENU, DrawBridgeMod.MODID);
	
	public static final RegistryEntry<UMenuType<DrawBridgeMenu>> DRAW_BRIDGE = MENU_TYPES.register("draw_bridge", () -> new UMenuType<>(DrawBridgeMenu::new));
	
	static void register() {
		MENU_TYPES.register();
	}
	
}
