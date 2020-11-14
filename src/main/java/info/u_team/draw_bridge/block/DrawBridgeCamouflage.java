package info.u_team.draw_bridge.block;

import java.util.function.Supplier;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.*;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class DrawBridgeCamouflage extends DrawBridgeBlock {
	
	private final Supplier<? extends DrawBridgeBlock> baseDrawBridgeBlockSupplier;
	
	public DrawBridgeCamouflage(Supplier<? extends DrawBridgeBlock> baseDrawBridgeBlockSupplier) {
		super(addLootFrom(baseDrawBridgeBlockSupplier, Properties.create(DRAW_BRIDGE_MATERIAL)));
		this.baseDrawBridgeBlockSupplier = baseDrawBridgeBlockSupplier;
	}
	
	@Override
	public BlockItem getBlockItem() {
		return null;
	}
	
	@Override
	public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player) {
		return new ItemStack(baseDrawBridgeBlockSupplier.get());
	}
	
	private static Properties addLootFrom(Supplier<? extends DrawBridgeBlock> baseDrawBridgeBlockSupplier, Properties properties) {
		final Supplier<ResourceLocation> lootTableSupplier = () -> baseDrawBridgeBlockSupplier.get().getLootTable();
		ObfuscationReflectionHelper.setPrivateValue(Properties.class, properties, lootTableSupplier, "lootTableSupplier");
		return properties;
	}
	
}
