package info.u_team.draw_bridge;

import static info.u_team.draw_bridge.DrawBridgeConstants.MODID;

import info.u_team.draw_bridge.proxy.ClientProxy;
import info.u_team.draw_bridge.proxy.CommonProxy;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;;

@Mod(MODID)
public class DrawBridgeMod {

	private final CommonProxy proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> CommonProxy::new);

	private static DrawBridgeMod instance;
	
	public static DrawBridgeMod getInstance() {
		return instance;
	}
	
	public DrawBridgeMod() {
		instance = this;
		proxy.preinit();
	}
	
	
	@SubscribeEvent
	public void preinit(FMLCommonSetupEvent event) {
		proxy.init();
	}
	
	@SubscribeEvent
	public void init(FMLLoadCompleteEvent event) {
		proxy.postinit();
	}
	
}
