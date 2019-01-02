package info.u_team.draw_bridge.model;

import info.u_team.u_team_core.registry.ModelRegistry;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.*;

public class DBMModelLoader implements ICustomModelLoader {
	
	public DBMModelLoader() {
	}
	
	@Override
	public void onResourceManagerReload(IResourceManager resourceManager) {
		
	}
	
	@Override
	public boolean accepts(ResourceLocation modelLocation) {
		if (modelLocation instanceof ModelResourceLocation) {
			ModelResourceLocation loc = (ModelResourceLocation) modelLocation;
			if (loc.getVariant().startsWith("costum=true")) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public IModel loadModel(ResourceLocation modelLocation) throws Exception {
		System.out.println(modelLocation);
		return new DBMModel();
	}
	
}
