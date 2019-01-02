package info.u_team.draw_bridge.event;

import info.u_team.draw_bridge.DrawBridgeConstants;
import info.u_team.draw_bridge.model.BakedModelDrawBridge;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.util.registry.IRegistry;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.*;

@SideOnly(Side.CLIENT)
public class EventHandlerModelBake {
	
	@SubscribeEvent
	public static void on(ModelBakeEvent event) {
		IRegistry<ModelResourceLocation, IBakedModel> modelregistry = event.getModelRegistry();
		modelregistry.getKeys().stream().filter(modelresource -> modelresource.getPath().equals(DrawBridgeConstants.MODID) && modelresource.getNamespace().equals("drawbridge")).forEach(modelresource -> {
			IBakedModel model = modelregistry.getObject(modelresource);
			modelregistry.putObject(modelresource, new BakedModelDrawBridge(model));
		});
	}
}
