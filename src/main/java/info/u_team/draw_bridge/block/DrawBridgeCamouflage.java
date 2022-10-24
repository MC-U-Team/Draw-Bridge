package info.u_team.draw_bridge.block;

import java.util.function.Supplier;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.level.BlockGetter;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class DrawBridgeCamouflage extends DrawBridgeBlock {
	
	private final Supplier<? extends DrawBridgeBlock> baseDrawBridgeBlockSupplier;
	
	public DrawBridgeCamouflage(Supplier<? extends DrawBridgeBlock> baseDrawBridgeBlockSupplier) {
		super(addLootFrom(baseDrawBridgeBlockSupplier, Properties.of(DRAW_BRIDGE_MATERIAL)));
		this.baseDrawBridgeBlockSupplier = baseDrawBridgeBlockSupplier;
	}
	
	@Override
	public BlockItem getBlockItem() {
		return null;
	}
	
	@Override
	public ItemStack getPickBlock(BlockState state, HitResult target, BlockGetter world, BlockPos pos, Player player) {
		return new ItemStack(baseDrawBridgeBlockSupplier.get());
	}
	
	private static Properties addLootFrom(Supplier<? extends DrawBridgeBlock> baseDrawBridgeBlockSupplier, Properties properties) {
		final Supplier<ResourceLocation> lootTableSupplier = () -> baseDrawBridgeBlockSupplier.get().getLootTable();
		ObfuscationReflectionHelper.setPrivateValue(Properties.class, properties, lootTableSupplier, "lootTableSupplier");
		return properties;
	}
	
}
