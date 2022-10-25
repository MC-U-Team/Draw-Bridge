package info.u_team.draw_bridge.init;

import org.apache.logging.log4j.LogManager;

import info.u_team.draw_bridge.tileentity.DrawBridgeTileEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class DrawBridgeColors {
	
	private static void colorHandlerBlock(RegisterColorHandlersEvent.Block event) {
		event.register((state, world, pos, tintIndex) -> {
			if (world != null && pos != null) {
				final BlockEntity tileEntity = world.getBlockEntity(pos);
				if (tileEntity instanceof DrawBridgeTileEntity drawBridge) {
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
		}, DrawBridgeBlocks.DRAW_BRIDGE.get());
	}
	
	public static void registerMod(IEventBus bus) {
		bus.addListener(DrawBridgeColors::colorHandlerBlock);
	}
	
}
