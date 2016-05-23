package net.fe.network.command;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import net.fe.fightStage.AttackRecord;
import net.fe.overworldStage.OverworldStage;
import net.fe.overworldStage.ClientOverworldStage;
import net.fe.overworldStage.Path;
import net.fe.overworldStage.Node;
import net.fe.overworldStage.Healthbar;
import net.fe.unit.UnitIdentifier;
import net.fe.unit.Unit;
import net.fe.unit.Item;
import net.fe.unit.RiseTome;
import java.util.Optional;

/** a no-op */
public final class WaitCommand extends Command {
	
	private static final long serialVersionUID = 6468268282716381357L;
	
	
	public WaitCommand() {
	}
	
	public ArrayList<AttackRecord> applyServer(OverworldStage stage, Unit primaryUnit) {
		return null;
	}
	
	public Runnable applyClient(ClientOverworldStage stage, Unit primaryUnit, ArrayList<AttackRecord> attackRecords) {
		
		return new Runnable() {
			public void run() {
			}
		};
	}
	
	@Override
	public String toString() {
		return "Wait[]";
	}
}
