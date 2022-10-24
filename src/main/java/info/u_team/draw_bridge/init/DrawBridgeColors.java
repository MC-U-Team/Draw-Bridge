package info.u_team.draw_bridge.init;

import org.apache.logging.log4j.LogManager;

import info.u_team.draw_bridge.tileentity.DrawBridgeTileEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class DrawBridgeColors {
	
	private static void colorHandlerBlock(ColorHandlerEvent.Block event) {
		event.getBlockColors().register((state, world, pos, tintIndex) -> {
			if (world != null && pos != null) {
				final BlockEntity tileEntity = world.getBlockEntity(pos);
				if (tileEntity instanceof DrawBridgeTileEntity) {
					final DrawBridgeTileEntity drawBridge = (DrawBridgeTileEntity) tileEntity;
					if (drawBridge.hasRenderBlockState()) {
						try {
							return event.getBlockColors().getColor(drawBridge.getRenderBlockState(), world, pos, tintIndex);
						} catch (final Exception ex) {
							LogManager.getLogger().warn("Failed to get camouflage block color for camouflage blockstate " + drawBridge.getRenderBlockState() + " at position " + pos, ex);
						}
					}
				}
			}
			return -1;
		}, DrawBridgeBlocks.DRAW_BRIDGE.get(), DrawBridgeBlocks.DRAW_BRIDGE_CUTOUT.get(), DrawBridgeBlocks.DRAW_BRIDGE_CUTOUT_MIPPED.get(), DrawBridgeBlocks.DRAW_BRIDGE_TRANSLUCENT.get(), DrawBridgeBlocks.DRAW_BRIDGE_TRIPWIRE.get());
	}
	
	public static void registerMod(IEventBus bus) {
		bus.addListener(DrawBridgeColors::colorHandlerBlock);
	}
	
}
