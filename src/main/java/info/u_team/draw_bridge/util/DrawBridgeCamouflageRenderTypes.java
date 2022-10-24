package info.u_team.draw_bridge.util;

import static info.u_team.draw_bridge.init.DrawBridgeBlocks.DRAW_BRIDGE;
import static info.u_team.draw_bridge.init.DrawBridgeBlocks.DRAW_BRIDGE_CUTOUT;
import static info.u_team.draw_bridge.init.DrawBridgeBlocks.DRAW_BRIDGE_CUTOUT_MIPPED;
import static info.u_team.draw_bridge.init.DrawBridgeBlocks.DRAW_BRIDGE_TRANSLUCENT;
import static info.u_team.draw_bridge.init.DrawBridgeBlocks.DRAW_BRIDGE_TRIPWIRE;

import java.util.List;
import java.util.function.Supplier;

import com.google.common.collect.ImmutableList;

import info.u_team.draw_bridge.block.DrawBridgeBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

public enum DrawBridgeCamouflageRenderTypes {
	
	SOLID("solid", DRAW_BRIDGE),
	CUTOUT("cutout", DRAW_BRIDGE_CUTOUT),
	CUTOUT_MIPPED("cutout_mipped", DRAW_BRIDGE_CUTOUT_MIPPED),
	TRANSLUCENT("translucent", DRAW_BRIDGE_TRANSLUCENT),
	TRIPWIRE("tripwire", DRAW_BRIDGE_TRIPWIRE);
	
	public static final List<DrawBridgeCamouflageRenderTypes> RENDER_TYPES = ImmutableList.copyOf(values());
	
	private final Component textComponent;
	private final Supplier<? extends DrawBridgeBlock> blockSupplier;
	
	private DrawBridgeCamouflageRenderTypes(String name, Supplier<? extends DrawBridgeBlock> blockSupplier) {
		textComponent = new TranslatableComponent("container.drawbridge.draw_bridge.render_type." + name);
		this.blockSupplier = blockSupplier;
	}
	
	public Component getTextComponent() {
		return textComponent;
	}
	
	public DrawBridgeBlock getBlock() {
		return blockSupplier.get();
	}
	
	public static DrawBridgeCamouflageRenderTypes getType(Block block) {
		return RENDER_TYPES.stream().filter(type -> type.getBlock() == block).findAny().orElse(SOLID);
	}
	
	public DrawBridgeCamouflageRenderTypes cycle() {
		switch (this) {
		case SOLID:
			return CUTOUT;
		case CUTOUT:
			return CUTOUT_MIPPED;
		case CUTOUT_MIPPED:
			return TRANSLUCENT;
		case TRANSLUCENT:
			return TRIPWIRE;
		case TRIPWIRE:
			return SOLID;
		default:
			return SOLID;
		}
	}
	
}
