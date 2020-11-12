package info.u_team.draw_bridge.util;

import static info.u_team.draw_bridge.init.DrawBridgeBlocks.*;

import java.util.function.Supplier;

import info.u_team.draw_bridge.block.DrawBridgeBlock;

public enum DrawBridgeCamouflageRenderTypes {
	
	SOLID(DRAW_BRIDGE),
	CUTOUT(DRAW_BRIDGE_CUTOUT),
	CUTOUT_MIPPED(DRAW_BRIDGE_CUTOUT_MIPPED),
	TRANSLUCTENT(DRAW_BRIDGE_TRANSLUCTENT),
	TRIPWIRE(DRAW_BRIDGE_TRIPWIRE);
	
	private final Supplier<? extends DrawBridgeBlock> blockSupplier;
	
	private DrawBridgeCamouflageRenderTypes(Supplier<? extends DrawBridgeBlock> blockSupplier) {
		this.blockSupplier = blockSupplier;
	}
	
	public DrawBridgeBlock getBlock() {
		return blockSupplier.get();
	}
	
}
