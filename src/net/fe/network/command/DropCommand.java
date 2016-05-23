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

public final class DropCommand extends Command {
	
	private static final long serialVersionUID = 6468268282716381357L;
	
	private final int dropX;
	private final int dropY;
	
	public DropCommand(int dropX, int dropY) {
		this.dropX = dropX;
		this.dropY = dropY;
	}
	
	public ArrayList<AttackRecord> applyServer(OverworldStage stage, Unit unit) {
		
		//TODO: validate
		unit.drop(dropX, dropY);
		return null;
	}
	
	public Runnable applyClient(ClientOverworldStage stage, Unit unit, ArrayList<AttackRecord> attackRecords) {
		
		return new Runnable() {
			public void run() {
				unit.setMoved(true);
				unit.rescuedUnit().setMoved(true);
				unit.drop(dropX, dropY);
				stage.checkEndGame();
			}
		};
	}
	
	@Override
	public String toString() {
		return "Drop[" + dropX + "," + dropY + "]";
	}
}
