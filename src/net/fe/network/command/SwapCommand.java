package net.fe.network.command;

import java.util.ArrayList;

import net.fe.fightStage.AttackRecord;
import net.fe.overworldStage.ClientOverworldStage;
import net.fe.overworldStage.OverworldStage;
import net.fe.unit.Unit;

public final class SwapCommand extends Command {
	
	
	private static final long serialVersionUID = -3945531642092346657L;

	private ArrayList<Unit> swaps;
	
	public SwapCommand(ArrayList<Unit> swaps) {
		this.swaps = swaps;
	}
	
	@Override
	public ArrayList<AttackRecord> applyServer(OverworldStage stage, Unit unit) {
		for(int i = 0; i < swaps.size(); i += 2)
			stage.grid.swap(swaps.get(i), swaps.get(i + 1));
		return null;
	}
	
	@Override
	public Runnable applyClient(ClientOverworldStage stage, Unit unit, ArrayList<AttackRecord> attackRecords, Runnable callback) {
		return () -> {
			for(int i = 0; i < swaps.size(); i += 2)
				stage.grid.swap(swaps.get(i), swaps.get(i + 1));
			callback.run();
		};
	}
	
	@Override
	public String toString() {
		return "Swap[" + swaps.stream().map(unit -> unit.name).toArray(String[]::new) + "]";
	}
}
