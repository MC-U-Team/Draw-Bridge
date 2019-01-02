package info.u_team.draw_bridge.container;

import info.u_team.u_team_core.container.UContainer;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ContainerDrawBridge extends UContainer {
	
	public ContainerDrawBridge(EntityPlayer player, World world, BlockPos pos) {
		IInventory inv = (IInventory) world.getTileEntity(pos);
		for (int height = 0; height < 2; height++) {
			for (int width = 0; width < 5; width++) {
				addSlotToContainer(new SlotBlock(inv, width + height * 5, width * 18 + 8, height * 18 + 8));
			}
		}
		addSlotToContainer(new SlotBlock(inv, 10, 151, 8));
		appendPlayerInventory(player.inventory, 8, 84);
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		System.out.println(index);
		return ItemStack.EMPTY;
	}
	
}
