package info.u_team.draw_bridge.data.provider;

import static info.u_team.draw_bridge.init.DrawBridgeBlocks.DRAW_BRIDGE;
import static info.u_team.draw_bridge.init.DrawBridgeItemGroups.GROUP;

import info.u_team.u_team_core.data.*;

public class DrawBridgeLanguagesProvider extends CommonLanguagesProvider {
	
	public DrawBridgeLanguagesProvider(GenerationData data) {
		super(data);
	}
	
	@Override
	public void addTranslations() {
		
		final String container = "container.drawbridge.draw_bridge";
		
		// English
		add(GROUP, "Drawbridge");
		addBlock(DRAW_BRIDGE, "Drawbridge");
		add(container, "Drawbridge");
		add(container + ".speed", "Speed:");
		add(container + ".ticks", "Ticks");
		add(container + ".need_redstone", "Need redstone power?");
		add(container + ".render_type.solid", "Solid");
		add(container + ".render_type.cutout", "Cutout");
		add(container + ".render_type.cutout_mipped", "Cutout Mipped");
		add(container + ".render_type.translucent", "Translucent");
		add(container + ".render_type.tripwire", "Tripwire");
		
		// German
		add("de_de", GROUP, "Zugbrücke");
		addBlock("de_de", DRAW_BRIDGE, "Zugbrücke");
		add("de_de", container, "Zugbrücke");
		add("de_de", container + ".speed", "Geschwindigkeit:");
		add("de_de", container + ".ticks", "Ticks");
		add("de_de", container + ".need_redstone", "Wird ein Redstonesignal benötigt?");
	}
	
}
