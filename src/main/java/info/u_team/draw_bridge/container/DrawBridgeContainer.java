package info.u_team.draw_bridge.container;

import info.u_team.draw_bridge.block.DrawBridgeBlock;
import info.u_team.draw_bridge.container.slot.DrawBridgeSlot;
import info.u_team.draw_bridge.init.DrawBridgeContainerTypes;
import info.u_team.draw_bridge.tileentity.DrawBridgeTileEntity;
import info.u_team.draw_bridge.util.DrawBridgeCamouflageRenderTypes;
import info.u_team.u_team_core.api.sync.MessageHolder;
import info.u_team.u_team_core.api.sync.MessageHolder.EmptyMessageHolder;
import info.u_team.u_team_core.container.UTileEntityContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.FriendlyByteBuf;

public class DrawBridgeContainer extends UTileEntityContainer<DrawBridgeTileEntity> {
	
	// Messages from the client to the server
	private MessageHolder speedMessage;
	private EmptyMessageHolder needRedstoneMessage;
	private EmptyMessageHolder camouflageTypeMessage;
	private EmptyMessageHolder camouflageBlockStateMessage;
	
	// Client
	public DrawBridgeContainer(int id, Inventory playerInventory, FriendlyByteBuf buffer) {
		super(DrawBridgeContainerTypes.DRAW_BRIDGE.get(), id, playerInventory, buffer);
	}
	
	// Server
	public DrawBridgeContainer(int id, Inventory playerInventory, DrawBridgeTileEntity tileEntity) {
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
			if (tileEntity.hasLevel()) {
				final BlockState previousState = tileEntity.getBlockState();
				final DrawBridgeCamouflageRenderTypes type = DrawBridgeCamouflageRenderTypes.getType(previousState.getBlock()).cycle();
				if (previousState.getBlock() != type.getBlock()) {
					final BlockState newState = type.getBlock().defaultBlockState().setValue(DrawBridgeBlock.FACING, previousState.getValue(DrawBridgeBlock.FACING));
					tileEntity.getLevel().setBlock(tileEntity.getBlockPos(), newState, 2);
					tileEntity.clearCache();
				}
			}
		}));
		camouflageBlockStateMessage = addClientToServerTracker(new EmptyMessageHolder(() -> {
			tileEntity.setRenderSlotStateProperty(tileEntity.getRenderSlotStateProperty() + 1);
			tileEntity.updateRenderState();
			tileEntity.sendChangesToClient();
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
	
	public EmptyMessageHolder getCamouflageTypeMessage() {
		return camouflageTypeMessage;
	}
	
	public EmptyMessageHolder getCamouflageBlockStateMessage() {
		return camouflageBlockStateMessage;
	}
}
