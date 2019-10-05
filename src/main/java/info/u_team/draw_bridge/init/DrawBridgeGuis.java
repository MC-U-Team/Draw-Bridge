package info.u_team.draw_bridge.init;

import info.u_team.draw_bridge.DrawBridgeMod;
import info.u_team.draw_bridge.screen.DrawBridgeScreen;
import info.u_team.draw_bridge.tileentity.DrawBridgeTileEntity;
import info.u_team.draw_bridge.tileentity.render.DrawBridgeTileEntityRender;
import info.u_team.u_team_core.util.registry.ClientRegistry;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = DrawBridgeMod.MODID, bus = Bus.MOD, value = Dist.CLIENT)
public class DrawBridgeGuis {
	
	@SubscribeEvent
	public static void register(FMLClientSetupEvent event) {
		ScreenManager.registerFactory(DrawBridgeContainerTypes.DRAW_BRIDGE, DrawBridgeScreen::new);
		ClientRegistry.registerSpecialTileEntityRenderer(DrawBridgeTileEntity.class, new DrawBridgeTileEntityRender());
	}
	
}
