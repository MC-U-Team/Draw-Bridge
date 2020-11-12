package info.u_team.draw_bridge.container;

import info.u_team.draw_bridge.block.DrawBridgeBlock;
import info.u_team.draw_bridge.container.slot.DrawBridgeSlot;
import info.u_team.draw_bridge.init.DrawBridgeContainerTypes;
import info.u_team.draw_bridge.tileentity.DrawBridgeTileEntity;
import info.u_team.draw_bridge.util.DrawBridgeCamouflageRenderTypes;
import info.u_team.u_team_core.api.sync.MessageHolder;
import info.u_team.u_team_core.api.sync.MessageHolder.EmptyMessageHolder;
import info.u_team.u_team_core.container.UTileEntityContainer;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

public class DrawBridgeContainer extends UTileEntityContainer<DrawBridgeTileEntity> {
	
	// Messages from the client to the server
	private MessageHolder speedMessage;
	private EmptyMessageHolder needRedstoneMessage;
	private EmptyMessageHolder camouflageTypeMessage;
	
	// Client
	public DrawBridgeContainer(int id, PlayerInventory playerInventory, PacketBuffer buffer) {
		super(DrawBridgeContainerTypes.DRAW_BRIDGE.get(), id, playerInventory, buffer);
	}
	
	// Server
	public DrawBridgeContainer(int id, PlayerInventory playerInventory, DrawBridgeTileEntity tileEntity) {
		super(DrawBridgeContainerTypes.DRAW_BRIDGE.get(), id, playerInventory, tileEntity);
	}
	
	@Override
	protected void init(boolean server) {
		appendInventory(tileEntity.getSlots().getInventory(), (inv, index, xPosition, yPosition) -> new DrawBridgeSlot(tileEntity, inv, index, xPosition, yPosition), 2, 5, 8, 18);
		appendInventory(tileEntity.getRenderSlot(), 1, 1, 170, 36);
		appendPlayerInventory(playerInventory, 26, 86);
		addServerToClientTracker(tileEntity.getExtendedHolder());
		addServerToClientTracker(tileEntity.getSpeedHolder());
		addServerToClientTracker(tileEntity.getNeedRedstoneHolder());
		speedMessage = addClientToServerTracker(new MessageHolder(buffer -> tileEntity.setSpeed(buffer.readByte())));
		needRedstoneMessage = addClientToServerTracker(new EmptyMessageHolder(() -> {
			tileEntity.setNeedRedstone(!tileEntity.isNeedRedstone());
			tileEntity.neighborChanged();
		}));
		camouflageTypeMessage = addClientToServerTracker(new EmptyMessageHolder(() -> {
			if (tileEntity.hasWorld()) {
				final BlockState previousState = tileEntity.getBlockState();
				final DrawBridgeCamouflageRenderTypes type = DrawBridgeCamouflageRenderTypes.getType(previousState.getBlock()).cycle();
				if (previousState.getBlock() != type.getBlock()) {
					final BlockState newState = type.getBlock().getDefaultState().with(DrawBridgeBlock.FACING, previousState.get(DrawBridgeBlock.FACING));
					tileEntity.getWorld().setBlockState(tileEntity.getPos(), newState, 2);
					tileEntity.updateContainingBlockInfo();
				}
			}
		}));
	}
	
	@Override
	public ItemStack transferStackInSlot(PlayerEntity player, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		final Slot slot = inventorySlots.get(index);
		if (slot != null && slot.getHasStack()) {
			final ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			if (index < 11) {
				if (!mergeItemStack(itemstack1, 11, 47, true)) {
					return ItemStack.EMPTY;
				}
			} else {
				if (index >= 38) {
					if (!mergeItemStack(itemstack1, 0, 10, false)) {
						return ItemStack.EMPTY;
					}
				} else {
					if (!mergeItemStack(itemstack1, 0, 10, false)) {
						return ItemStack.EMPTY;
					}
				}
			}
			if (itemstack1.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}
			
			if (itemstack1.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}
			slot.onTake(player, itemstack1);
		}
		
		return itemstack;
	}
	
	public MessageHolder getSpeedMessage() {
		return speedMessage;
	}
	
	public EmptyMessageHolder getNeedRedstoneMessage() {
		return needRedstoneMessage;
	}
	
	public EmptyMessageHolder getCamouflageTypeMessage() {
		return camouflageTypeMessage;
	}
}
