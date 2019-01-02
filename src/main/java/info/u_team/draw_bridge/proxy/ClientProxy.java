package info.u_team.draw_bridge.proxy;

import info.u_team.u_team_core.registry.*;
import info.uteam.drawbridges.model.DBMModelLoader;
import info.uteam.drawbridges.tiles.*;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.common.event.*;

public class ClientProxy extends CommonProxy {
	
	@Override
	public void preinit(FMLPreInitializationEvent event) {
		super.preinit(event);
		ModelLoaderRegistry.registerLoader(new DBMModelLoader());
	}
	
	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);
		ClientRegistry.registerSpecialTileEntityRenderer(DBMDrawbridgeTile.class, new SpecialDrawbridgeRender());
	}
	
	@Override
	public void postinit(FMLPostInitializationEvent event) {
		super.postinit(event);
	}
	
}
