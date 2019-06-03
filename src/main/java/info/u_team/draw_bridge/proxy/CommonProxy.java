package info.u_team.draw_bridge.proxy;

import info.u_team.draw_bridge.DrawBridgeConstants;
import info.u_team.draw_bridge.gui.GuiDrawBridge;
import info.u_team.draw_bridge.init.DrawBridgeCreativeTabs;
import info.u_team.draw_bridge.tileentity.TileEntityDrawBridge;
import info.u_team.u_team_core.registry.GuiRegistry;
import net.minecraft.util.ResourceLocation;

public class CommonProxy {
	
	public void preinit() {
		
	}
	
	public void init() {
		GuiRegistry.registerTileEntity(new ResourceLocation(DrawBridgeConstants.MODID, "drawbridge"), data -> new GuiDrawBridge((TileEntityDrawBridge) data.getTileentity(), data.getPlayer()));
	}
	
	public void postinit() {
		DrawBridgeCreativeTabs.init();
	}
	
}
