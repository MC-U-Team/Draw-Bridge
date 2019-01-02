package info.u_team.draw_bridge.handler;

import info.u_team.draw_bridge.DrawBridgeConstants;
import info.u_team.draw_bridge.init.DBMGuis;
import info.u_team.u_team_core.container.UContainer;
import info.u_team.u_team_core.gui.UGuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class DBMGuiHandler implements IGuiHandler {
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		try {
			return DBMGuis.getContainer(ID).getConstructor(EntityPlayer.class, World.class, BlockPos.class).newInstance(player, world, new BlockPos(x, y, z));
		} catch (Exception ex) {
			DrawBridgeConstants.LOGGER.error("Some gui container seems to be wrong.", ex);
			return null;
		}
	}
	
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		try {
			Object container = this.getServerGuiElement(ID, player, world, x, y, z);
			Class<? extends UGuiContainer> gui = DBMGuis.getGui(ID);
			if (container == null) {
				return gui.getConstructor(EntityPlayer.class, World.class, BlockPos.class).newInstance(player, world, new BlockPos(x, y, z));
			} else {
				return gui.getConstructor(UContainer.class).newInstance(container);
			}
		} catch (Exception ex) {
			DrawBridgeConstants.LOGGER.error("Some gui seems to be wrong.", ex);
			return null;
		}
	}
	
}
