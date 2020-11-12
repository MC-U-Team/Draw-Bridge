package info.u_team.draw_bridge.util;

import static info.u_team.draw_bridge.init.DrawBridgeBlocks.*;

import java.util.List;
import java.util.function.Supplier;

import com.google.common.collect.ImmutableList;

import info.u_team.draw_bridge.block.DrawBridgeBlock;

public enum DrawBridgeCamouflageRenderTypes {
	
	SOLID(DRAW_BRIDGE),
	CUTOUT(DRAW_BRIDGE_CUTOUT),
	CUTOUT_MIPPED(DRAW_BRIDGE_CUTOUT_MIPPED),
	TRANSLUCTENT(DRAW_BRIDGE_TRANSLUCTENT),
	TRIPWIRE(DRAW_BRIDGE_TRIPWIRE);
	
	public static final List<DrawBridgeCamouflageRenderTypes> RENDER_TYPES = ImmutableList.copyOf(values());
	
	private final Supplier<? extends DrawBridgeBlock> blockSupplier;
	
	private DrawBridgeCamouflageRenderTypes(Supplier<? extends DrawBridgeBlock> blockSupplier) {
		this.blockSupplier = blockSupplier;
	}
	
	public DrawBridgeBlock getBlock() {
		return blockSupplier.get();
	}
	
	public static DrawBridgeCamouflageRenderTypes cycle(DrawBridgeCamouflageRenderTypes type) {
		switch (type) {
		case SOLID:
			return CUTOUT;
		case CUTOUT:
			return CUTOUT_MIPPED;
		case CUTOUT_MIPPED:
			return TRANSLUCTENT;
		case TRANSLUCTENT:
			return TRIPWIRE;
		case TRIPWIRE:
			return SOLID;
		default:
			return SOLID;
		}
	}
	
}
