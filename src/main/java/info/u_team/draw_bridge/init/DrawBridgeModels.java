package info.u_team.draw_bridge.init;

import java.util.Map;

import info.u_team.draw_bridge.model.DrawBridgeModel;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class DrawBridgeModels {
	
	private static void onModelBake(ModelBakeEvent event) {
		final Map<ResourceLocation, IBakedModel> modelRegistry = event.getModelRegistry();
		final ResourceLocation registyName = DrawBridgeBlocks.DRAW_BRIDGE.get().getRegistryName();
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
