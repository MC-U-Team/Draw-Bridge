package info.u_team.draw_bridge.gui;

import info.u_team.draw_bridge.DrawBridgeConstants;
import info.u_team.draw_bridge.container.ContainerDrawBridge;
import info.u_team.draw_bridge.tileentity.TileEntityDrawBridge;
import info.u_team.u_team_core.gui.UGuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.*;

@SideOnly(Side.CLIENT)
public class GuiDrawBridge extends UGuiContainer {
	
	public static final ResourceLocation BACKGROUND = new ResourceLocation(DrawBridgeConstants.MODID, "textures/gui/drawbridge.png");
	
	public GuiDrawBridge(TileEntityDrawBridge tileentity, EntityPlayer entityplayer) {
		super(new ContainerDrawBridge(tileentity, entityplayer), BACKGROUND);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		renderHoveredToolTip(mouseX, mouseY);
	}
}
