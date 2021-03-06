package info.u_team.draw_bridge.tileentity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import info.u_team.draw_bridge.block.DrawBridgeBlock;
import info.u_team.draw_bridge.container.DrawBridgeContainer;
import info.u_team.draw_bridge.init.DrawBridgeBlocks;
import info.u_team.draw_bridge.init.DrawBridgeTileEntityTypes;
import info.u_team.draw_bridge.util.InventoryStackHandler;
import info.u_team.draw_bridge.util.SingleStackInventoryStackHandler;
import info.u_team.u_team_core.api.sync.BufferReferenceHolder;
import info.u_team.u_team_core.api.sync.IInitSyncedTileEntity;
import info.u_team.u_team_core.tileentity.UTickableTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;

public class DrawBridgeTileEntity extends UTickableTileEntity implements IInitSyncedTileEntity {
	
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
			if (!hasWorld() || world.isRemote()) {
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
	private final BufferReferenceHolder extendedHolder = BufferReferenceHolder.createBooleanHolder(() -> extended, value -> extended = value);
	private final BufferReferenceHolder speedHolder = BufferReferenceHolder.createByteHolder(() -> (byte) speed, value -> speed = value); // The speed is only in the range of 0 to 100 so we can cast to a byte here
	private final BufferReferenceHolder needRedstoneHolder = BufferReferenceHolder.createBooleanHolder(() -> needRedstone, value -> needRedstone = value);
	
	public DrawBridgeTileEntity() {
		super(DrawBridgeTileEntityTypes.DRAW_BRIDGE.get());
	}
	
	// Neighbor update
	
	public void neighborChanged() {
		final boolean newPowered = world.isBlockPowered(pos);
		updatePoweredState(newPowered);
		
		final Set<DrawBridgeTileEntity> drawBridges = new HashSet<>();
		collect(drawBridges, this, 0);
		
		final boolean newPoweredState = drawBridges.stream().anyMatch(drawBridge -> world.isBlockPowered(drawBridge.pos)) | newPowered;
		drawBridges.stream().forEach(drawBridge -> drawBridge.updatePoweredState(newPoweredState));
	}
	
	// Drawbridge logic
	
	private void updatePoweredState(boolean newPowered) {
		powered = needRedstone ? newPowered : !newPowered;
	}
	
	private void collect(Set<DrawBridgeTileEntity> tileEntites, DrawBridgeTileEntity callerTileEntity, int depth) {
		if (depth >= 20) {
			return;
		}
		getNeighbors(callerTileEntity.pos).stream().forEach(neighbor -> {
			final TileEntity tileEntity = world.getTileEntity(neighbor);
			if (!(tileEntity instanceof DrawBridgeTileEntity)) {
				return;
			}
			final DrawBridgeTileEntity drawBridge = (DrawBridgeTileEntity) tileEntity;
			
			if (tileEntites.add(drawBridge)) {
				drawBridge.collect(tileEntites, drawBridge, depth + 1);
			}
		});
	}
	
	private List<BlockPos> getNeighbors(BlockPos except) {
		return getPosExcept(pos, except).filter(pos -> world.getBlockState(pos).getBlock() == DrawBridgeBlocks.DRAW_BRIDGE.get()).collect(Collectors.toList());
	}
	
	private Stream<BlockPos> getPosExcept(BlockPos start, BlockPos except) {
		return Stream.of(Direction.values()).map(start::offset).filter(pos -> !pos.equals(except));
	}
	
	@Override
	public void tickServer() {
		if (localSpeed <= 1) {
			localSpeed = speed;
			if (powered && extendState < 10) {
				if (localSpeed == 0) {
					for (int i = extendState; i < 10; i++) {
						extend();
					}
				} else {
					extend();
				}
				markDirty();
			} else if (!powered && extendState > 0) {
				if (localSpeed == 0) {
					for (int i = extendState; i > 0; i--) {
						retract();
					}
				} else {
					retract();
				}
				markDirty();
			}
		}
		localSpeed--;
	}
	
	private void extend() {
		final Direction facing = world.getBlockState(pos).get(DrawBridgeBlock.FACING);
		trySetBlock(facing);
		extended = ++extendState > 0;
	}
	
	private void trySetBlock(Direction facing) {
		final BlockPos newPos = pos.offset(facing, extendState + 1);
		if ((world.isAirBlock(newPos) /* || world.getFluidState(newPos).getFluid() == Fluids.EMPTY */)) {
			final ItemStack itemstack = slots.getStackInSlot(extendState);
			if (itemstack.isEmpty()) {
				ourBlocks[extendState] = false;
			} else {
				final Block block = Block.getBlockFromItem(itemstack.getItem());
				world.setBlockState(newPos, block.getDefaultState(), 2);
				slots.getInventory().removeStackFromSlot(extendState);
				ourBlocks[extendState] = true;
			}
		} else {
			ourBlocks[extendState] = false;
		}
	}
	
	private void retract() {
		final Direction facing = world.getBlockState(pos).get(DrawBridgeBlock.FACING);
		extended = --extendState > 0;
		tryRemoveBlock(facing);
	}
	
	private void tryRemoveBlock(Direction facing) {
		if (ourBlocks[extendState]) {
			final BlockPos newPos = pos.offset(facing, extendState + 1);
			if (!world.isAirBlock(newPos)) {
				final BlockState state = world.getBlockState(newPos);
				final Block block = state.getBlock();
				
				final ItemStack stack = new ItemStack(block);
				slots.getInventory().setInventorySlotContents(extendState, stack);
				
				world.setBlockState(newPos, Blocks.AIR.getDefaultState(), 2);
			}
			ourBlocks[extendState] = false;
		}
	}
	
	// NBT
	
	@Override
	public void readNBT(BlockState state, CompoundNBT compound) {
		slots.deserializeNBT(compound.getCompound("slots"));
		
		renderSlotStateProperty = compound.getInt("render_slot_state_property");
		renderSlot.deserializeNBT(compound.getCompound("render_slot"));
		
		powered = compound.getBoolean("powered");
		extendState = compound.getInt("extend");
		extended = extendState > 0;
		speed = compound.getInt("speed");
		needRedstone = compound.getBoolean("need_redstone");
		
		final CompoundNBT ourBlocksTag = compound.getCompound("our_blocks");
		for (int i = 0; i < ourBlocks.length; i++) {
			if (ourBlocksTag.contains("" + i)) {
				ourBlocks[i] = ourBlocksTag.getBoolean("" + i);
			} else {
				ourBlocks[i] = false;
			}
		}
	}
	
	@Override
	public void writeNBT(CompoundNBT compound) {
		compound.put("slots", slots.serializeNBT());
		
		compound.putInt("render_slot_state_property", renderSlotStateProperty);
		compound.put("render_slot", renderSlot.serializeNBT());
		
		compound.putBoolean("powered", powered);
		compound.putInt("extend", extendState);
		compound.putInt("speed", speed);
		compound.putBoolean("need_redstone", needRedstone);
		
		final CompoundNBT ourBlocksTag = new CompoundNBT();
		for (int i = 0; i < ourBlocks.length; i++) {
			ourBlocksTag.putBoolean("" + i, ourBlocks[i]);
		}
		compound.put("our_blocks", ourBlocksTag);
	}
	
	// Container
	
	@Override
	public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity player) {
		return new DrawBridgeContainer(id, playerInventory, this);
	}
	
	@Override
	public ITextComponent getDisplayName() {
		return new TranslationTextComponent("container.drawbridge.draw_bridge");
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
	public void sendInitialDataBuffer(PacketBuffer buffer) {
		buffer.writeBoolean(extended);
		buffer.writeVarInt(speed);
		buffer.writeBoolean(needRedstone);
	}
	
	@OnlyIn(Dist.CLIENT)
	@Override
	public void handleInitialDataBuffer(PacketBuffer buffer) {
		extended = buffer.readBoolean();
		speed = buffer.readVarInt();
		needRedstone = buffer.readBoolean();
	}
	
	public BufferReferenceHolder getExtendedHolder() {
		return extendedHolder;
	}
	
	public BufferReferenceHolder getSpeedHolder() {
		return speedHolder;
	}
	
	public BufferReferenceHolder getNeedRedstoneHolder() {
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
	
	private void writeRenderState(CompoundNBT compound) {
		if (renderBlockState != null) {
			compound.put("render", NBTUtil.writeBlockState(renderBlockState));
		}
	}
	
	private void readRenderState(CompoundNBT compound) {
		if (compound.contains("render")) {
			renderBlockState = NBTUtil.readBlockState(compound.getCompound("render"));
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
		final List<BlockState> validStates = block.getStateContainer().getValidStates();
		
		if (renderSlotStateProperty >= 0 && renderSlotStateProperty < validStates.size()) {
			renderBlockState = validStates.get(renderSlotStateProperty);
		} else {
			renderSlotStateProperty = -1;
			renderBlockState = block.getDefaultState();
		}
	}
	
	// Sync methods chunk
	
	@Override
	public void sendChunkLoadData(CompoundNBT compound) {
		writeRenderState(compound);
	}
	
	@Override
	public void handleChunkLoadData(CompoundNBT compound) {
		readRenderState(compound);
		world.getChunkProvider().getLightManager().checkBlock(pos);
	}
	
	@Override
	public void sendUpdateStateData(CompoundNBT compound) {
		writeRenderState(compound);
	}
	
	@Override
	public void handleUpdateStateData(CompoundNBT compound) {
		readRenderState(compound);
		requestModelDataUpdate();
		world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), 0);
		world.getChunkProvider().getLightManager().checkBlock(pos);
	}
	
	// Model data
	
	@Override
	public IModelData getModelData() {
		if (renderBlockState != null) {
			return new ModelDataMap.Builder().withInitial(BLOCKSTATE_PROPERTY, renderBlockState).build();
		}
		return EmptyModelData.INSTANCE;
	}
	
	// Send update tag even if tag is empty
	
	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		final CompoundNBT compound = new CompoundNBT();
		sendUpdateStateData(compound);
		return new SUpdateTileEntityPacket(pos, -1, compound);
	}
	
}
