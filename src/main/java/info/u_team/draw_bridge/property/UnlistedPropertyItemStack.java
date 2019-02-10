package info.u_team.draw_bridge.property;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.property.IUnlistedProperty;

public class UnlistedPropertyItemStack implements IUnlistedProperty<ItemStack> {
	
	private final String name;
	
	public static UnlistedPropertyItemStack create(String name) {
		return new UnlistedPropertyItemStack(name);
	}
	
	protected UnlistedPropertyItemStack(String name) {
		this.name = name;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public boolean isValid(ItemStack value) {
		return true;
	}
	
	@Override
	public Class<ItemStack> getType() {
		return ItemStack.class;
	}
	
	@Override
	public String valueToString(ItemStack value) {
		return value.toString();
	}
	
}
