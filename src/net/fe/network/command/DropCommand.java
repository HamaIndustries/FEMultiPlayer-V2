package net.fe.network.command;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

import net.fe.overworldStage.OverworldStage;
import net.fe.game.fightStage.AttackRecord;
import net.fe.game.unit.Item;
import net.fe.game.unit.RiseTome;
import net.fe.game.unit.Unit;
import net.fe.game.unit.UnitIdentifier;
import net.fe.overworldStage.ClientOverworldStage;
import net.fe.overworldStage.Path;
import net.fe.overworldStage.Node;

import java.util.Optional;

public final class DropCommand extends Command {
	
	private static final long serialVersionUID = 6468268282716381357L;
	
	private final int dropX;
	private final int dropY;
	
	public DropCommand(int dropX, int dropY) {
		this.dropX = dropX;
		this.dropY = dropY;
	}
	
	@Override
	public ArrayList<AttackRecord> applyServer(OverworldStage stage, Unit unit) {
		
		//TODO: validate
		unit.drop(dropX, dropY);
		return null;
	}
	
	@Override
	public Runnable applyClient(ClientOverworldStage stage, Unit unit, ArrayList<AttackRecord> attackRecords, Runnable callback) {
		
		return new Runnable() {
			public void run() {
				unit.setMoved(true);
				unit.rescuedUnit().setMoved(true);
				unit.drop(dropX, dropY);
				stage.checkEndGame();
				callback.run();
			}
		};
	}
	
	@Override
	public String toString() {
		return "Drop[" + dropX + "," + dropY + "]";
	}
}
