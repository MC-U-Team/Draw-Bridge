package info.u_team.draw_bridge.event;

import java.util.Map;

import info.u_team.draw_bridge.DrawBridgeConstants;
import info.u_team.draw_bridge.model.BakedModelDrawBridge;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@OnlyIn(Dist.CLIENT)
public class EventHandlerModelBake {
	
	@SubscribeEvent
	public static void on(ModelBakeEvent event) {
		Map<ModelResourceLocation, IBakedModel> modelregistry = event.getModelRegistry();
		modelregistry.keySet().stream().filter(modelresource -> modelresource.getPath().equals(DrawBridgeConstants.MODID) && modelresource.getNamespace().equals("drawbridge")).forEach(modelresource -> {
			IBakedModel model = modelregistry.get(modelresource);
			modelregistry.put(modelresource, new BakedModelDrawBridge(model));
		});
	}
}
