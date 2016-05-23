package net.fe.network.command;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import net.fe.fightStage.AttackRecord;
import net.fe.overworldStage.OverworldStage;
import net.fe.overworldStage.ClientOverworldStage;
import net.fe.overworldStage.Path;
import net.fe.overworldStage.Node;
import net.fe.unit.UnitIdentifier;
import net.fe.unit.Unit;
import net.fe.unit.Item;
import net.fe.unit.RiseTome;
import java.util.Optional;

public final class TakeCommand extends Command {
	
	private static final long serialVersionUID = 6468268282716381357L;
	
	private final UnitIdentifier otherId;
	
	public TakeCommand(UnitIdentifier otherId) {
		this.otherId = otherId;
	}
	
	public ArrayList<AttackRecord> applyServer(OverworldStage stage, Unit unit) {
		
		Unit other = stage.getUnit(otherId);
		other.give(unit); // throws IllegalStateException
		return null;
	}
	
	public Runnable applyClient(ClientOverworldStage stage, Unit unit, ArrayList<AttackRecord> attackRecords) {
		
		return new Runnable() {
			public void run() {
				Unit other = stage.getUnit(otherId);
				other.give(unit);
				stage.checkEndGame();
			}
		};
	}
	
	@Override
	public String toString() {
		return "Take[" + otherId + "]";
	}
}
