package info.u_team.draw_bridge.util;

public class SingleStackInventoryStackHandler extends InventoryStackHandler {
	
	public SingleStackInventoryStackHandler(int size) {
		super(size);
	}
	
	@Override
	public int getInventoryStackLimit() {
		return 1;
	}
	
}
