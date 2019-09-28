package info.u_team.draw_bridge.init;

import info.u_team.draw_bridge.DrawBridgeMod;
import info.u_team.u_team_core.itemgroup.UItemGroup;
import net.minecraft.item.Items;

public class DrawBridgeItemGroups {
	
	public static final UItemGroup GROUP = new UItemGroup(DrawBridgeMod.MODID, "group", () -> Items.ACACIA_BOAT);
	
}
