package info.u_team.draw_bridge.data.provider;

import static info.u_team.draw_bridge.init.DrawBridgeBlocks.DRAW_BRIDGE;
import static info.u_team.draw_bridge.init.DrawBridgeCreativeTabs.GROUP;

import info.u_team.u_team_core.data.CommonLanguagesProvider;
import info.u_team.u_team_core.data.GenerationData;

public class DrawBridgeLanguagesProvider extends CommonLanguagesProvider {
	
	public DrawBridgeLanguagesProvider(GenerationData generationData) {
		super(generationData);
	}
	
	@Override
	public void register() {
		
		final String container = "container.drawbridge.draw_bridge";
		
		// English
		add(GROUP, "Drawbridge");
		addBlock(DRAW_BRIDGE, "Drawbridge");
		add(container, "Drawbridge");
		add(container + ".speed", "Speed:");
		add(container + ".ticks", "Ticks");
		add(container + ".need_redstone", "Need redstone power?");
		add(container + ".render_type", "Rendertype of the camouflage model");
		add(container + ".render_type.solid", "Solid");
		add(container + ".render_type.cutout", "Cutout");
		add(container + ".render_type.cutout_mipped", "Cutout Mipped");
		add(container + ".render_type.translucent", "Translucent");
		add(container + ".render_type.tripwire", "Tripwire");
		add(container + ".cycle_block_state", "Cycle State");
		add(container + ".block_state", "Blockstate");
		add(container + ".no_cycle_block_state", "Block has only one state");
		add(container + ".camouflage", "Camouflage");
		
		// German
		add("de_de", GROUP, "Zugbrücke");
		addBlock("de_de", DRAW_BRIDGE, "Zugbrücke");
		add("de_de", container, "Zugbrücke");
		add("de_de", container + ".speed", "Geschwindigkeit:");
		add("de_de", container + ".ticks", "Ticks");
		add("de_de", container + ".need_redstone", "Wird ein Redstonesignal benötigt?");
		add("de_de", container + ".render_type", "Rendertyp des Tarnmodells");
		add("de_de", container + ".render_type.solid", "Solid");
		add("de_de", container + ".render_type.cutout", "Cutout");
		add("de_de", container + ".render_type.cutout_mipped", "Cutout Mipped");
		add("de_de", container + ".render_type.translucent", "Translucent");
		add("de_de", container + ".render_type.tripwire", "Tripwire");
		add("de_de", container + ".cycle_block_state", "Ändern des Blockzustands");
		add("de_de", container + ".block_state", "Blockzustand");
		add("de_de", container + ".no_cycle_block_state", "Block hat nur einen Zustand");
		add("de_de", container + ".camouflage", "Tarnung");
	}
	
}
