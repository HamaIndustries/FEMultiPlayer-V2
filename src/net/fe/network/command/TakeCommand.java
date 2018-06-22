package net.fe.network.command;

import java.util.ArrayList;

import net.fe.fightStage.AttackRecord;
import net.fe.overworldStage.ClientOverworldStage;
import net.fe.overworldStage.OverworldStage;
import net.fe.unit.Unit;
import net.fe.unit.UnitIdentifier;

public final class TakeCommand extends Command {
	
	private static final long serialVersionUID = 6468268282716381357L;
	
	private final UnitIdentifier sourceId;
	
	public TakeCommand(UnitIdentifier sourceId) {
		this.sourceId = sourceId;
	}
	
	@Override
	public ArrayList<AttackRecord> applyServer(OverworldStage stage, Unit destination) {
		
		Unit source = stage.getUnit(sourceId);
		source.give(destination); // throws IllegalStateException
		return null;
	}
	
	@Override
	public Runnable applyClient(ClientOverworldStage stage, Unit destination, ArrayList<AttackRecord> attackRecords, Runnable callback) {
		
		return new Runnable() {
			public void run() {
				Unit source = stage.getUnit(sourceId);
				source.give(destination);
				stage.checkEndGame();
				callback.run();
			}
		};
	}
	
	@Override
	public String toString() {
		return "Take[" + sourceId + "]";
	}
}
