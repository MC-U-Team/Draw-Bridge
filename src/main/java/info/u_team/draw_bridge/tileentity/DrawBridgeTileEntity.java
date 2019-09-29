package info.u_team.draw_bridge.tileentity;

import java.util.*;
import java.util.stream.*;

import info.u_team.draw_bridge.block.DrawBridgeBlock;
import info.u_team.draw_bridge.container.DrawBridgeContainer;
import info.u_team.draw_bridge.init.*;
import info.u_team.draw_bridge.util.InventoryStackHandler;
import info.u_team.u_team_core.api.sync.IAutoSyncedTileEntity;
import info.u_team.u_team_core.container.USyncedTileEntityContainer;
import info.u_team.u_team_core.tileentity.UTileEntity;
import net.minecraft.block.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.*;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.*;
import net.minecraftforge.common.util.LazyOptional;

public class DrawBridgeTileEntity extends UTileEntity implements ITickableTileEntity, IAutoSyncedTileEntity {
	
	private final LazyOptional<InventoryStackHandler> slots = LazyOptional.of(() -> new InventoryStackHandler(10) {
		
		@Override
		public int getInventoryStackLimit() {
			return 1;
		}
	});
	private final LazyOptional<InventoryStackHandler> renderSlot = LazyOptional.of(() -> new InventoryStackHandler(1));
	
	private boolean powered;
	private int speed;
	private boolean needsrs = true;
	private int extended;
	private boolean[] ourBlocks = new boolean[10];
	private int localSpeed;
	
	public DrawBridgeTileEntity() {
		super(DrawBridgeTileEntityTypes.DRAW_BRIDGE);
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
	
	private void updatePoweredState(boolean powered) {
		this.powered = needsrs ? powered : !powered;
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
		return getPosExcept(pos, except).filter(pos -> world.getBlockState(pos).getBlock() == DrawBridgeBlocks.DRAW_BRIDGE).collect(Collectors.toList());
	}
	
	private Stream<BlockPos> getPosExcept(BlockPos start, BlockPos except) {
		return Stream.of(Direction.values()).map(start::offset).filter(pos -> !pos.equals(except));
	}
	
	@Override
	public void tick() {
		if (world.isRemote) {
			return;
		}
		if (localSpeed <= 1) {
			localSpeed = speed;
			if (powered && extended < 10) {
				if (localSpeed == 0) {
					for (int i = extended; i < 10; i++) {
						extend();
					}
				} else {
					extend();
				}
				markDirty();
			} else if (!powered && extended > 0) {
				if (localSpeed == 0) {
					for (int i = extended; i > 0; i--) {
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
		Direction facing = world.getBlockState(pos).get(DrawBridgeBlock.FACING);
		trySetBlock(facing);
		extended++;
	}
	
	private void trySetBlock(Direction facing) {
		BlockPos newPos = pos.offset(facing, extended + 1);
		if (slots.isPresent() && (world.isAirBlock(newPos) /* || world.getFluidState(newPos).getFluid() == Fluids.EMPTY */)) {
			slots.ifPresent(inventory -> {
				ItemStack itemstack = inventory.getStackInSlot(extended);
				Block block = Block.getBlockFromItem(itemstack.getItem());
				world.setBlockState(newPos, block.getDefaultState(), 2);
				inventory.removeStackFromSlot(extended);
				ourBlocks[extended] = true;
			});
		} else {
			ourBlocks[extended] = false;
		}
	}
	
	private void retract() {
		Direction facing = world.getBlockState(pos).get(DrawBridgeBlock.FACING);
		extended--;
		tryRemoveBlock(facing);
	}
	
	private void tryRemoveBlock(Direction facing) {
		if (ourBlocks[extended] && slots.isPresent()) {
			BlockPos newPos = pos.offset(facing, extended + 1);
			if (!world.isAirBlock(newPos)) {
				slots.ifPresent(inventory -> {
					BlockState state = world.getBlockState(newPos);
					Block block = state.getBlock();
					
					ItemStack stack = new ItemStack(block);
					inventory.setInventorySlotContents(extended, stack);
					
					world.setBlockState(newPos, Blocks.AIR.getDefaultState(), 2);
				});
			}
		}
	}
	
	@Override
	public void readNBT(CompoundNBT compound) {
		slots.ifPresent(inventory -> inventory.deserializeNBT(compound.getCompound("slots")));
		renderSlot.ifPresent(inventory -> inventory.deserializeNBT(compound.getCompound("render_slot")));
		
		powered = compound.getBoolean("powered");
		extended = compound.getInt("extended");
		speed = compound.getInt("speed");
		needsrs = compound.getBoolean("needsrs");
		
		final CompoundNBT ourBlocksTag = compound.getCompound("our_blocks");
		for (int i = 0; i < ourBlocks.length; i++) {
			if (ourBlocksTag.hasUniqueId("" + i)) {
				ourBlocks[i] = ourBlocksTag.getBoolean("" + i);
			} else {
				ourBlocks[i] = false;
			}
		}
	}
	
	@Override
	public void writeNBT(CompoundNBT compound) {
		slots.ifPresent(inventory -> compound.put("slots", inventory.serializeNBT()));
		renderSlot.ifPresent(inventory -> compound.put("render_slot", inventory.serializeNBT()));
		
		compound.putBoolean("powered", powered);
		compound.putInt("extended", extended);
		compound.putInt("speed", speed);
		compound.putBoolean("needsrs", needsrs);
		
		final CompoundNBT ourBlocksTag = new CompoundNBT();
		for (int i = 0; i < ourBlocks.length; i++) {
			ourBlocksTag.putBoolean("" + i, ourBlocks[i]);
		}
		compound.put("our_blocks", ourBlocksTag);
	}
	
	@Override
	public USyncedTileEntityContainer<?> createMenu(int id, PlayerInventory playerInventory, PlayerEntity player) {
		return new DrawBridgeContainer(id, playerInventory, this);
	}
	
	@Override
	public ITextComponent getDisplayName() {
		return new StringTextComponent("Draw Bridge"); // TODO language file
	}
	
	public LazyOptional<InventoryStackHandler> getSlots() {
		return slots;
	}
	
	public LazyOptional<InventoryStackHandler> getRenderSlot() {
		return renderSlot;
	}
	
}
