package info.u_team.draw_bridge.blockentity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import info.u_team.draw_bridge.block.DrawBridgeBlock;
import info.u_team.draw_bridge.init.DrawBridgeBlocks;
import info.u_team.draw_bridge.init.DrawBridgeBlockEntityTypes;
import info.u_team.draw_bridge.menu.DrawBridgeMenu;
import info.u_team.draw_bridge.util.InventoryStackHandler;
import info.u_team.draw_bridge.util.SingleStackInventoryStackHandler;
import info.u_team.u_team_core.api.block.MenuSyncedBlockEntity;
import info.u_team.u_team_core.api.sync.DataHolder;
import info.u_team.u_team_core.blockentity.UBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;

public class DrawBridgeBlockEntity extends UBlockEntity implements MenuSyncedBlockEntity {
	
	public static final ModelProperty<BlockState> BLOCKSTATE_PROPERTY = new ModelProperty<BlockState>();
	
	private final InventoryStackHandler slots = new SingleStackInventoryStackHandler(10);
	
	private final InventoryStackHandler renderSlot = new SingleStackInventoryStackHandler(1) {
		
		@Override
		public boolean isItemValid(int index, ItemStack stack) {
			final Item item = stack.getItem();
			if (!(item instanceof BlockItem) || item == DrawBridgeBlocks.DRAW_BRIDGE.get().asItem()) {
				return false;
			}
			return true;
		}
		
		@Override
		protected void onLoaded() {
			updateRenderState();
		}
		
		@Override
		protected void slotChanged(int index) {
			if (!hasLevel() || level.isClientSide()) {
				return;
			}
			renderSlotStateProperty = -1;
			updateRenderState();
			sendChangesToClient();
		}
	};
	
	private int renderSlotStateProperty;
	
	private boolean powered;
	private int speed;
	private boolean needRedstone = true;
	private int extendState;
	private boolean extended;
	private final boolean[] ourBlocks = new boolean[10];
	
	private int localSpeed;
	
	private BlockState renderBlockState;
	
	// Used for syncing the data in containers
	private final DataHolder extendedHolder = DataHolder.createBooleanHolder(() -> extended, value -> extended = value);
	private final DataHolder speedHolder = DataHolder.createByteHolder(() -> (byte) speed, value -> speed = value); // The speed is only in the range of 0 to 100 so we can cast to a byte here
	private final DataHolder needRedstoneHolder = DataHolder.createBooleanHolder(() -> needRedstone, value -> needRedstone = value);
	
	public DrawBridgeBlockEntity(BlockPos pos, BlockState state) {
		super(DrawBridgeBlockEntityTypes.DRAW_BRIDGE.get(), pos, state);
	}
	
	// Neighbor update
	
	public void neighborChanged() {
		final boolean newPowered = level.hasNeighborSignal(worldPosition);
		updatePoweredState(newPowered);
		
		final Set<DrawBridgeBlockEntity> drawBridges = new HashSet<>();
		collect(drawBridges, this, 0);
		
		final boolean newPoweredState = drawBridges.stream().anyMatch(drawBridge -> level.hasNeighborSignal(drawBridge.worldPosition)) | newPowered;
		drawBridges.stream().forEach(drawBridge -> drawBridge.updatePoweredState(newPoweredState));
	}
	
	// Drawbridge logic
	
	private void updatePoweredState(boolean newPowered) {
		powered = needRedstone ? newPowered : !newPowered;
	}
	
	private void collect(Set<DrawBridgeBlockEntity> tileEntites, DrawBridgeBlockEntity callerTileEntity, int depth) {
		if (depth >= 20) {
			return;
		}
		getNeighbors(callerTileEntity.worldPosition).stream().forEach(neighbor -> {
			final BlockEntity tileEntity = level.getBlockEntity(neighbor);
			if (!(tileEntity instanceof DrawBridgeBlockEntity)) {
				return;
			}
			final DrawBridgeBlockEntity drawBridge = (DrawBridgeBlockEntity) tileEntity;
			
			if (tileEntites.add(drawBridge)) {
				drawBridge.collect(tileEntites, drawBridge, depth + 1);
			}
		});
	}
	
	private List<BlockPos> getNeighbors(BlockPos except) {
		return getPosExcept(worldPosition, except).filter(pos -> level.getBlockState(pos).getBlock() == DrawBridgeBlocks.DRAW_BRIDGE.get()).collect(Collectors.toList());
	}
	
	private Stream<BlockPos> getPosExcept(BlockPos start, BlockPos except) {
		return Stream.of(Direction.values()).map(start::relative).filter(pos -> !pos.equals(except));
	}
	
	public static void serverTick(Level level, BlockPos pos, BlockState state, DrawBridgeBlockEntity blockEntity) {
		if (blockEntity.localSpeed <= 1) {
			blockEntity.localSpeed = blockEntity.speed;
			if (blockEntity.powered && blockEntity.extendState < 10) {
				if (blockEntity.localSpeed == 0) {
					for (int i = blockEntity.extendState; i < 10; i++) {
						blockEntity.extend();
					}
				} else {
					blockEntity.extend();
				}
				blockEntity.setChanged();
			} else if (!blockEntity.powered && blockEntity.extendState > 0) {
				if (blockEntity.localSpeed == 0) {
					for (int i = blockEntity.extendState; i > 0; i--) {
						blockEntity.retract();
					}
				} else {
					blockEntity.retract();
				}
				blockEntity.setChanged();
			}
		}
		blockEntity.localSpeed--;
	}
	
	private void extend() {
		final Direction facing = level.getBlockState(worldPosition).getValue(DrawBridgeBlock.FACING);
		trySetBlock(facing);
		extended = ++extendState > 0;
	}
	
	private void trySetBlock(Direction facing) {
		final BlockPos newPos = worldPosition.relative(facing, extendState + 1);
		if ((level.isEmptyBlock(newPos) /* || world.getFluidState(newPos).getFluid() == Fluids.EMPTY */)) {
			final ItemStack itemstack = slots.getStackInSlot(extendState);
			if (itemstack.isEmpty()) {
				ourBlocks[extendState] = false;
			} else {
				final Block block = Block.byItem(itemstack.getItem());
				level.setBlock(newPos, block.defaultBlockState(), 2);
				slots.getInventory().removeItemNoUpdate(extendState);
				ourBlocks[extendState] = true;
			}
		} else {
			ourBlocks[extendState] = false;
		}
	}
	
	private void retract() {
		final Direction facing = level.getBlockState(worldPosition).getValue(DrawBridgeBlock.FACING);
		extended = --extendState > 0;
		tryRemoveBlock(facing);
	}
	
	private void tryRemoveBlock(Direction facing) {
		if (ourBlocks[extendState]) {
			final BlockPos newPos = worldPosition.relative(facing, extendState + 1);
			if (!level.isEmptyBlock(newPos)) {
				final BlockState state = level.getBlockState(newPos);
				final Block block = state.getBlock();
				
				final ItemStack stack = new ItemStack(block);
				slots.getInventory().setItem(extendState, stack);
				
				level.setBlock(newPos, Blocks.AIR.defaultBlockState(), 2);
			}
			ourBlocks[extendState] = false;
		}
	}
	
	// NBT
	
	@Override
	public void loadNBT(CompoundTag compound) {
		slots.deserializeNBT(compound.getCompound("slots"));
		
		renderSlotStateProperty = compound.getInt("render_slot_state_property");
		renderSlot.deserializeNBT(compound.getCompound("render_slot"));
		
		powered = compound.getBoolean("powered");
		extendState = compound.getInt("extend");
		extended = extendState > 0;
		speed = compound.getInt("speed");
		needRedstone = compound.getBoolean("need_redstone");
		
		final CompoundTag ourBlocksTag = compound.getCompound("our_blocks");
		for (int i = 0; i < ourBlocks.length; i++) {
			if (ourBlocksTag.contains("" + i)) {
				ourBlocks[i] = ourBlocksTag.getBoolean("" + i);
			} else {
				ourBlocks[i] = false;
			}
		}
	}
	
	@Override
	public void saveNBT(CompoundTag compound) {
		compound.put("slots", slots.serializeNBT());
		
		compound.putInt("render_slot_state_property", renderSlotStateProperty);
		compound.put("render_slot", renderSlot.serializeNBT());
		
		compound.putBoolean("powered", powered);
		compound.putInt("extend", extendState);
		compound.putInt("speed", speed);
		compound.putBoolean("need_redstone", needRedstone);
		
		final CompoundTag ourBlocksTag = new CompoundTag();
		for (int i = 0; i < ourBlocks.length; i++) {
			ourBlocksTag.putBoolean("" + i, ourBlocks[i]);
		}
		compound.put("our_blocks", ourBlocksTag);
	}
	
	// Container
	
	@Override
	public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
		return new DrawBridgeMenu(id, playerInventory, this);
	}
	
	@Override
	public Component getDisplayName() {
		return Component.translatable("container.drawbridge.draw_bridge");
	}
	
	// Slot getter
	
	public InventoryStackHandler getSlots() {
		return slots;
	}
	
	public InventoryStackHandler getRenderSlot() {
		return renderSlot;
	}
	
	// Sync methods for container
	
	@Override
	public void sendInitialMenuDataToClient(FriendlyByteBuf buffer) {
		buffer.writeBoolean(extended);
		buffer.writeVarInt(speed);
		buffer.writeBoolean(needRedstone);
	}
	
	@OnlyIn(Dist.CLIENT)
	@Override
	public void handleInitialMenuDataFromServer(FriendlyByteBuf buffer) {
		extended = buffer.readBoolean();
		speed = buffer.readVarInt();
		needRedstone = buffer.readBoolean();
	}
	
	public DataHolder getExtendedHolder() {
		return extendedHolder;
	}
	
	public DataHolder getSpeedHolder() {
		return speedHolder;
	}
	
	public DataHolder getNeedRedstoneHolder() {
		return needRedstoneHolder;
	}
	
	// Getter and setter
	
	public boolean isExtended() {
		return extended;
	}
	
	public int getSpeed() {
		return speed;
	}
	
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	
	public boolean isNeedRedstone() {
		return needRedstone;
	}
	
	public void setNeedRedstone(boolean needRedstone) {
		this.needRedstone = needRedstone;
	}
	
	public boolean hasRenderBlockState() {
		return renderBlockState != null;
	}
	
	public BlockState getRenderBlockState() {
		return renderBlockState;
	}
	
	public int getRenderSlotStateProperty() {
		return renderSlotStateProperty;
	}
	
	public void setRenderSlotStateProperty(int renderSlotStateProperty) {
		this.renderSlotStateProperty = renderSlotStateProperty;
	}
	
	// Util methods for render block
	
	private void writeRenderState(CompoundTag compound) {
		if (renderBlockState != null) {
			compound.put("render", NbtUtils.writeBlockState(renderBlockState));
		}
	}
	
	private void readRenderState(CompoundTag compound) {
		if (compound.contains("render")) {
			renderBlockState = NbtUtils.readBlockState(compound.getCompound("render"));
		} else {
			renderBlockState = null;
		}
	}
	
	public void updateRenderState() {
		final ItemStack stack = renderSlot.getStackInSlot(0);
		
		if (stack.isEmpty()) {
			renderBlockState = null;
			renderSlotStateProperty = -1;
			return;
		}
		final Item item = stack.getItem();
		if (!(item instanceof BlockItem) || item == DrawBridgeBlocks.DRAW_BRIDGE.get().asItem()) {
			return;
		}
		
		final Block block = ((BlockItem) item).getBlock();
		final List<BlockState> validStates = block.getStateDefinition().getPossibleStates();
		
		if (renderSlotStateProperty >= 0 && renderSlotStateProperty < validStates.size()) {
			renderBlockState = validStates.get(renderSlotStateProperty);
		} else {
			renderSlotStateProperty = -1;
			renderBlockState = block.defaultBlockState();
		}
	}
	
	// Sync methods chunk
	
	@Override
	public void sendChunkLoadData(CompoundTag compound) {
		writeRenderState(compound);
	}
	
	@Override
	public void handleChunkLoadData(CompoundTag compound) {
		readRenderState(compound);
		level.getChunkSource().getLightEngine().checkBlock(worldPosition);
	}
	
	@Override
	public void sendUpdateStateData(CompoundTag compound) {
		writeRenderState(compound);
	}
	
	@Override
	public void handleUpdateStateData(CompoundTag compound) {
		readRenderState(compound);
		requestModelDataUpdate();
		level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 0);
		level.getChunkSource().getLightEngine().checkBlock(worldPosition);
	}
	
	// Model data
	
	@Override
	public ModelData getModelData() {
		if (renderBlockState != null) {
			return ModelData.builder().with(BLOCKSTATE_PROPERTY, renderBlockState).build();
		}
		return ModelData.EMPTY;
	}
	
}
