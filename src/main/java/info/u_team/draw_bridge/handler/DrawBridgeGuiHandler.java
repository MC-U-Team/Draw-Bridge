package info.u_team.draw_bridge.handler;

import info.u_team.draw_bridge.container.ContainerDrawBridge;
import info.u_team.draw_bridge.gui.GuiDrawBridge;
import info.u_team.draw_bridge.tileentity.TileEntityDrawBridge;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class DrawBridgeGuiHandler implements IGuiHandler {
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		final BlockPos pos = new BlockPos(x, y, z);
		if (ID == 0) {
			final TileEntity tileentity = world.getTileEntity(pos);
			if (tileentity instanceof TileEntityDrawBridge) {
				return new ContainerDrawBridge((TileEntityDrawBridge) tileentity, player);
			}
		}
		return null;
	}
	
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		final BlockPos pos = new BlockPos(x, y, z);
		if (ID == 0) {
			final TileEntity tileentity = world.getTileEntity(pos);
			if (tileentity instanceof TileEntityDrawBridge) {
				return new GuiDrawBridge((TileEntityDrawBridge) tileentity, player);
			}
		}
		return null;
	}
	
}
