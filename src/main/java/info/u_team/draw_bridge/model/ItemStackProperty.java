package info.u_team.draw_bridge.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import com.google.common.collect.Lists;

import net.minecraft.state.IProperty;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.IRegistry;

public class ItemStackProperty implements IProperty<ResourceLocation> {

	public static ItemStackProperty create(String name) {
		return new ItemStackProperty(name);
	}
	
	private String name;
	private ArrayList<ResourceLocation> list = Lists.newArrayList();
	
	@SuppressWarnings("deprecation")
	private ItemStackProperty(String name) {
		this.name = name;
		IRegistry.field_212618_g.forEach(st -> list.add(st.getBlock().getRegistryName()));
		list.forEach(System.out::println);
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public Collection<ResourceLocation> getAllowedValues() {
		return list;
	}

	@Override
	public Class<ResourceLocation> getValueClass() {
		return ResourceLocation.class;
	}

	@Override
	public Optional<ResourceLocation> parseValue(String value) {
		return Optional.of(ResourceLocation.makeResourceLocation(value.replaceFirst("_", ":")));
	}

	@Override
	public String getName(ResourceLocation value) {
		return value.toString().replace(":", "_");
	}

}
