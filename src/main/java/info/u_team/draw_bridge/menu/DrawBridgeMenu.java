package info.u_team.draw_bridge.menu;

import info.u_team.draw_bridge.blockentity.DrawBridgeBlockEntity;
import info.u_team.draw_bridge.init.DrawBridgeBlocks;
import info.u_team.draw_bridge.init.DrawBridgeMenuTypes;
import info.u_team.draw_bridge.menu.slot.DrawBridgeSlot;
import info.u_team.u_team_core.api.network.NetworkEnvironment;
import info.u_team.u_team_core.api.sync.MessageHolder;
import info.u_team.u_team_core.api.sync.MessageHolder.EmptyMessageHolder;
import info.u_team.u_team_core.menu.ItemHandlerSlotCreator;
import info.u_team.u_team_core.menu.UBlockEntityContainerMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class DrawBridgeMenu extends UBlockEntityContainerMenu<DrawBridgeBlockEntity> {
	
	private final ContainerLevelAccess access;
	
	// Messages from the client to the server
	private MessageHolder speedMessage;
	private EmptyMessageHolder needRedstoneMessage;
	private EmptyMessageHolder camouflageBlockStateMessage;
	
	// Client
	public DrawBridgeMenu(int containerId, Inventory playerInventory, FriendlyByteBuf buffer) {
		super(DrawBridgeMenuTypes.DRAW_BRIDGE.get(), containerId, playerInventory, buffer);
		access = ContainerLevelAccess.NULL;
	}
	
	// Server
	public DrawBridgeMenu(int containerId, Inventory playerInventory, DrawBridgeBlockEntity blockEntity) {
		super(DrawBridgeMenuTypes.DRAW_BRIDGE.get(), containerId, playerInventory, blockEntity);
		access = ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos());
	}
	
	@Override
	public boolean stillValid(Player player) {
		return stillValid(access, player, DrawBridgeBlocks.DRAW_BRIDGE.get());
	}
	
	@Override
	protected void init(NetworkEnvironment environment) {
		addSlots((index, xPosition, yPosition) -> new DrawBridgeSlot(blockEntity, blockEntity.getSlots().getInventory(), index, xPosition, yPosition), 2, 5, 8, 18);
		addSlots(ItemHandlerSlotCreator.of(blockEntity.getRenderSlot()), 1, 1, 170, 27);
		addPlayerInventory(playerInventory, 26, 86);
		addDataHolderToClient(blockEntity.getExtendedHolder());
		addDataHolderToClient(blockEntity.getSpeedHolder());
		addDataHolderToClient(blockEntity.getNeedRedstoneHolder());
		speedMessage = addDataHolderToServer(new MessageHolder(buffer -> blockEntity.setSpeed(buffer.readByte())));
		needRedstoneMessage = addDataHolderToServer(new EmptyMessageHolder(() -> {
			blockEntity.setNeedRedstone(!blockEntity.isNeedRedstone());
			blockEntity.neighborChanged();
		}));
		camouflageBlockStateMessage = addDataHolderToServer(new EmptyMessageHolder(() -> {
			blockEntity.setRenderSlotStateProperty(blockEntity.getRenderSlotStateProperty() + 1);
			blockEntity.updateRenderState();
			blockEntity.sendChangesToClient();
		}));
	}
	
	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		final Slot slot = slots.get(index);
		if (slot != null && slot.hasItem()) {
			final ItemStack itemstack1 = slot.getItem();
			itemstack = itemstack1.copy();
			if (index < 11) {
				if (!moveItemStackTo(itemstack1, 11, 47, true)) {
					return ItemStack.EMPTY;
				}
			} else {
				if (index >= 38) {
					if (!moveItemStackTo(itemstack1, 0, 10, false)) {
						return ItemStack.EMPTY;
					}
				} else {
					if (!moveItemStackTo(itemstack1, 0, 10, false)) {
						return ItemStack.EMPTY;
					}
				}
			}
			if (itemstack1.isEmpty()) {
				slot.set(ItemStack.EMPTY);
			} else {
				slot.setChanged();
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
	
	public EmptyMessageHolder getCamouflageBlockStateMessage() {
		return camouflageBlockStateMessage;
	}
}
