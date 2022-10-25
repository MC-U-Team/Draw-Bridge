package info.u_team.draw_bridge.init;

import java.util.Map;

import info.u_team.draw_bridge.model.DrawBridgeModel;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class DrawBridgeModels {
	
	private static void onModelBake(ModelEvent.BakingCompleted event) {
		final Map<ResourceLocation, BakedModel> modelRegistry = event.getModels();
		final ResourceLocation registyName = DrawBridgeBlocks.DRAW_BRIDGE.getId();
		modelRegistry.entrySet().stream().filter(entry -> {
			final ResourceLocation location = entry.getKey();
			if (location == null) {
				return false;
			}
			return location.getNamespace().equals(registyName.getNamespace()) && location.getPath().startsWith(registyName.getPath());
		}).forEach(entry -> entry.setValue(new DrawBridgeModel(entry.getValue())));
	}
	
	public static void registerMod(IEventBus bus) {
		bus.addListener(DrawBridgeModels::onModelBake);
	}
	
}
