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

public final class RescueCommand extends Command {
	
	private static final long serialVersionUID = 6468268282716381357L;
	
	private final UnitIdentifier rescueeId;
	
	public RescueCommand(UnitIdentifier rescueeId) {
		this.rescueeId = rescueeId;
	}
	
	@Override
	public ArrayList<AttackRecord> applyServer(OverworldStage stage, Unit unit) {
		
		//TODO: validate
		Unit rescuee = stage.getUnit(rescueeId);
		unit.rescue(rescuee);
		return null;
	}
	
	@Override
	public Runnable applyClient(ClientOverworldStage stage, Unit unit, ArrayList<AttackRecord> attackRecords, Runnable callback) {
		
		return new Runnable() {
			public void run() {
				Unit rescuee = stage.getUnit(rescueeId);
				unit.setMoved(true);
				unit.rescue(rescuee);
				stage.checkEndGame();
				callback.run();
			}
		};
	}
	
	@Override
	public String toString() {
		return "Rescue[" + rescueeId + "]";
	}
}
