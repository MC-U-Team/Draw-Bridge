package info.u_team.draw_bridge.init;

import info.u_team.draw_bridge.DrawBridgeMod;
import info.u_team.u_team_core.api.registry.CreativeModeTabRegister;
import info.u_team.u_team_core.api.registry.RegistryEntry;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class DrawBridgeCreativeTabs {
	
	public static final CreativeModeTabRegister CREATIVE_TABS = CreativeModeTabRegister.create(DrawBridgeMod.MODID);
	
	public static final RegistryEntry<CreativeModeTab> TAB = CREATIVE_TABS.register("tab", builder -> {
		builder.icon(() -> new ItemStack(DrawBridgeBlocks.DRAW_BRIDGE.get()));
		builder.displayItems((parameters, output) -> {
			DrawBridgeBlocks.BLOCKS.itemIterable().forEach(output::accept);
		});
	});
	
	static void register() {
		CREATIVE_TABS.register();
	}
}
