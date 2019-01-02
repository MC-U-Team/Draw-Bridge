/*-*****************************************************************************
 * Copyright 2018 U-Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/

package info.u_team.draw_bridge.model;

import info.u_team.u_team_core.registry.ModelRegistry;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.*;

/**
 * @author MrTroble
 *
 */
public class DBMModelLoader implements ICustomModelLoader {

	/**
	 * 
	 */
	public DBMModelLoader() {
	}
	
	/* (non-Javadoc)
	 * @see net.minecraftforge.client.model.ICustomModelLoader#onResourceManagerReload(net.minecraft.client.resources.IResourceManager)
	 */
	@Override
	public void onResourceManagerReload(IResourceManager resourceManager) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see net.minecraftforge.client.model.ICustomModelLoader#accepts(net.minecraft.util.ResourceLocation)
	 */
	@Override
	public boolean accepts(ResourceLocation modelLocation) {
		if(modelLocation instanceof ModelResourceLocation) {
			ModelResourceLocation loc = (ModelResourceLocation) modelLocation;
			if(loc.getVariant().startsWith("costum=true")) {
				return true;
			}
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see net.minecraftforge.client.model.ICustomModelLoader#loadModel(net.minecraft.util.ResourceLocation)
	 */
	@Override
	public IModel loadModel(ResourceLocation modelLocation) throws Exception {
		System.out.println(modelLocation);
		return new DBMModel();
	}

}
