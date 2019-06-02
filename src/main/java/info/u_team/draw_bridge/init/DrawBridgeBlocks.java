package info.u_team.draw_bridge.init;

import info.u_team.draw_bridge.DrawBridgeConstants;
import info.u_team.draw_bridge.block.BlockDrawBridge;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(bus=Bus.MOD, modid = DrawBridgeConstants.MODID)
public class DrawBridgeBlocks {
	
	public static Block draw_bridge = null;
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void postinit(RegistryEvent.Register<Block> reg) {
		if(draw_bridge == null) draw_bridge = new BlockDrawBridge("drawbridge");
		reg.getRegistry().register(draw_bridge);
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void postinititem(RegistryEvent.Register<Item> reg) {
		if(draw_bridge == null) draw_bridge = new BlockDrawBridge("drawbridge");
		reg.getRegistry().register(new ItemBlock(draw_bridge, new Item.Properties().group(DrawBridgeCreativeTabs.tab)).setRegistryName(draw_bridge.getRegistryName()));
	}
	
}
