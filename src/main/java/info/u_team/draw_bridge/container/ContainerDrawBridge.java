package info.u_team.draw_bridge.container;

import info.u_team.draw_bridge.container.slot.*;
import info.u_team.draw_bridge.tileentity.TileEntityDrawBridge;
import info.u_team.u_team_core.container.UContainerTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ContainerDrawBridge extends UContainerTileEntity {
	
	public ContainerDrawBridge(TileEntityDrawBridge tileentity, EntityPlayer player) {
		super(tileentity);
		for (int height = 0; height < 2; height++) {
			for (int width = 0; width < 5; width++) {
				addSlotToContainer(new SlotDrawBridge(tileentity, width + height * 5, width * 18 + 8, height * 18 + 8));
			}
		}
		addSlotToContainer(new SlotDrawBridgeChangeModel(tileentity, 10, 151, 8));
		appendPlayerInventory(player.inventory, 8, 84);
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		return ItemStack.EMPTY;
	}
	
}
