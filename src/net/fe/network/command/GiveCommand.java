package net.fe.network.command;

import java.util.ArrayList;

import net.fe.fightStage.AttackRecord;
import net.fe.overworldStage.ClientOverworldStage;
import net.fe.overworldStage.OverworldStage;
import net.fe.unit.Unit;
import net.fe.unit.UnitIdentifier;

public final class GiveCommand extends Command {
	
	private static final long serialVersionUID = 6468268282716381357L;
	
	private final UnitIdentifier destinationId;
	
	public GiveCommand(UnitIdentifier destinationId) {
		this.destinationId = destinationId;
	}
	
	@Override
	public ArrayList<AttackRecord> applyServer(OverworldStage stage, Unit unit) {
		
		Unit destination = stage.getUnit(destinationId);
		unit.give(destination); // throws IllegalStateException
		return null;
	}
	
	@Override
	public Runnable applyClient(ClientOverworldStage stage, Unit unit, ArrayList<AttackRecord> attackRecords, Runnable callback) {
		
		return new Runnable() {
			public void run() {
				Unit destination = stage.getUnit(destinationId);
				unit.give(destination);
				stage.checkEndGame();
				callback.run();
			}
		};
	}
	
	@Override
	public String toString() {
		return "Take[" + destinationId + "]";
	}
}
