package info.u_team.draw_bridge.handler;

import info.u_team.draw_bridge.container.ContainerDrawBridge;
import info.u_team.draw_bridge.gui.GuiDrawBridge;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class DrawBridgeGuiHandler implements IGuiHandler {
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == 0) {
			return new ContainerDrawBridge(player, world, new BlockPos(x, y, z));
		}
		return null;
	}
	
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == 0) {
			return new GuiDrawBridge(new ContainerDrawBridge(player, world, new BlockPos(x, x, y)));
		}
		return null;
	}
	
}
