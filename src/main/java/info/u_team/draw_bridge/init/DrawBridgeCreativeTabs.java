package info.u_team.draw_bridge.init;

import info.u_team.draw_bridge.DrawBridgeConstants;
import info.u_team.u_team_core.creativetab.UCreativeTab;

public class DrawBridgeCreativeTabs {
	
	public static final UCreativeTab tab = new UCreativeTab(DrawBridgeConstants.MODID, "tab");
	
	public static void init() {
		tab.setIcon(DrawBridgeBlocks.draw_bridge);
	}
	
}
