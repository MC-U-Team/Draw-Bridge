package info.u_team.draw_bridge.block;

import info.u_team.draw_bridge.init.*;
import info.u_team.u_team_core.block.UTileEntityBlock;
import net.minecraft.block.material.Material;

public class DrawBridgeBlock extends UTileEntityBlock {
	
	public DrawBridgeBlock(String name) {
		super(name, DrawBridgeItemGroups.GROUP, Properties.create(Material.IRON).hardnessAndResistance(1.5F), () -> DrawBridgeTileEntityTypes.DRAW_BRIDGE);
	}
	
}
