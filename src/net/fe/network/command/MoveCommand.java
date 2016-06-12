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

public final class MoveCommand extends Command {
	
	private static final long serialVersionUID = 6468268282716381357L;
	
	private final int moveX;
	private final int moveY;
	
	public MoveCommand(int moveX, int moveY) {
		this.moveX = moveX;
		this.moveY = moveY;
	}
	
	@Override
	public ArrayList<AttackRecord> applyServer(OverworldStage stage, Unit unit) {
		
		//TODO: validate
		stage.grid.move(unit, unit.getXCoord() + moveX, unit.getYCoord() + moveY, false);
		return null;
	}
	
	@Override
	public Runnable applyClient(ClientOverworldStage stage, Unit unit, ArrayList<AttackRecord> attackRecords, Runnable callback) {
		
		return new Runnable() {
			public void run() {
				Path p = stage.grid.getShortestPath(unit, unit.getXCoord()+moveX, unit.getYCoord()+moveY);
				stage.grid.move(unit, unit.getXCoord()+moveX, unit.getYCoord()+moveY, true);
				unit.move(p, callback);
				stage.includeInView(p.getAllNodes());
			}
		};
	}
	
	@Override
	public String toString() {
		return "Move[" + moveX + "," + moveY + "]";
	}
}
