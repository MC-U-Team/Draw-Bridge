package info.u_team.draw_bridge.init;

import info.u_team.draw_bridge.DrawBridgeMod;
import info.u_team.draw_bridge.tileentity.DrawBridgeTileEntity;
import info.u_team.draw_bridge.tileentity.render.DrawBridgeTileEntityRender;
import info.u_team.u_team_core.tileentitytype.UTileEntityType.UBuilder;
import info.u_team.u_team_core.util.registry.BaseRegistryUtil;
import info.u_team.u_team_core.util.registry.ClientRegistry;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = DrawBridgeMod.MODID, bus = Bus.MOD)
public class DrawBridgeTileEntityTypes {
	
	public static final TileEntityType<DrawBridgeTileEntity> DRAW_BRIDGE = UBuilder.create("draw_bridge", DrawBridgeTileEntity::new, DrawBridgeBlocks.DRAW_BRIDGE).build();
	
	@SubscribeEvent
	public static void register(Register<TileEntityType<?>> event) {
		BaseRegistryUtil.getAllGenericRegistryEntriesAndApplyNames(DrawBridgeMod.MODID, TileEntityType.class).forEach(event.getRegistry()::register);
	    ClientRegistry.registerSpecialTileEntityRenderer(DrawBridgeTileEntity.class, new DrawBridgeTileEntityRender());
	}
	
}
