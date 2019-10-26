package info.u_team.draw_bridge.init;

import java.util.Map;

import info.u_team.draw_bridge.DrawBridgeMod;
import info.u_team.draw_bridge.model.DrawBridgeModel;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = DrawBridgeMod.MODID, bus = Bus.MOD, value = Dist.CLIENT)
public class DrawBridgeModels {
	
	@SubscribeEvent
	public static void register(ModelBakeEvent event) {
		final Map<ResourceLocation, IBakedModel> modelRegistry = event.getModelRegistry();
		final ResourceLocation registyName = DrawBridgeBlocks.DRAW_BRIDGE.getRegistryName();
		modelRegistry.entrySet().stream().filter(entry -> {
			final ResourceLocation location = entry.getKey();
			if(location == null) return false;
			return location.getNamespace().equals(registyName.getNamespace()) && location.getPath().startsWith(registyName.getPath());
		}).forEach(entry -> entry.setValue(new DrawBridgeModel(entry.getValue())));
	}
	
}
