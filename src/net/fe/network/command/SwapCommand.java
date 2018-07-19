package net.fe.network.command;

import java.util.ArrayList;
import java.util.Arrays;

import net.fe.fightStage.AttackRecord;
import net.fe.overworldStage.ClientOverworldStage;
import net.fe.overworldStage.Node;
import net.fe.overworldStage.OverworldStage;
import net.fe.overworldStage.context.FormationContext;
import net.fe.unit.Unit;

public final class SwapCommand extends Command {
	
	
	private static final long serialVersionUID = -3945531642092346657L;

	private Node[] swaps;
	private byte id;
	
	public SwapCommand(ArrayList<Node> swaps, byte id) {
		this.swaps = swaps.toArray(new Node[0]);
		this.id = id;
	}
	
	@Override
	public ArrayList<AttackRecord> applyServer(OverworldStage stage, Unit unit) {
		for(int i = 0; i < swaps.length; i += 2)
			stage.grid.swap(swaps[i], swaps[i + 1]);
		return null;
	}
	
	@Override
	public Runnable applyClient(ClientOverworldStage stage, Unit unit, ArrayList<AttackRecord> attackRecords, Runnable callback) {
		return () -> {
			for(int i = 0; i < swaps.length; i += 2)
				stage.grid.swap(swaps[i], swaps[i + 1]);
			if(stage.getContext() instanceof FormationContext)
				((FormationContext) stage.getContext()).removePlayer(id);
			callback.run();
		};
	}
	
	@Override
	public String toString() {
		return "Swap" + Arrays.toString(swaps);
	}
}
