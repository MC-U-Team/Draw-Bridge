package info.u_team.draw_bridge.block;

import java.util.function.Supplier;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

public class DrawBridgeCamouflage extends DrawBridgeBlock {
	
	private final Supplier<? extends DrawBridgeBlock> baseDrawBridgeBlockSupplier;
	
	public DrawBridgeCamouflage(Supplier<? extends DrawBridgeBlock> baseDrawBridgeBlockSupplier) {
		super(addLootFrom(baseDrawBridgeBlockSupplier, Properties.of(DRAW_BRIDGE_MATERIAL)));
		this.baseDrawBridgeBlockSupplier = baseDrawBridgeBlockSupplier;
	}
	
//	@Override
//	public Item blockItem() {
//		return baseDrawBridgeBlockSupplier.get().blockItem(); // TODO maybe fix? Not sure if that is right
//	}
	
	@Override
	public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
		return new ItemStack(baseDrawBridgeBlockSupplier.get());
	}
	
	private static Properties addLootFrom(Supplier<? extends DrawBridgeBlock> baseDrawBridgeBlockSupplier, Properties properties) {
		final Supplier<ResourceLocation> lootTableSupplier = () -> baseDrawBridgeBlockSupplier.get().getLootTable();
		ObfuscationReflectionHelper.setPrivateValue(Properties.class, properties, lootTableSupplier, "lootTableSupplier");
		return properties;
	}
	
}
