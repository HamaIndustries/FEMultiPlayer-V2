package net.fe.network.command;

import java.util.ArrayList;
import java.util.LinkedList;

import net.fe.fightStage.AttackRecord;
import net.fe.overworldStage.ClientOverworldStage;
import net.fe.overworldStage.Node;
import net.fe.overworldStage.OverworldStage;
import net.fe.overworldStage.Path;
import net.fe.unit.Unit;

public final class MoveCommand extends Command {
	
	private static final long serialVersionUID = 6468268282716381357L;
	
	private final Node[] path;
	
	public MoveCommand(Node[] path) {
		this.path = path;
	}
	
	@Override
	public ArrayList<AttackRecord> applyServer(OverworldStage stage, Unit unit) {
		
		//TODO: validate
		stage.grid.move(unit, unit.getXCoord() + path[path.length - 1].x, unit.getYCoord() + path[path.length - 1].y, false);
		return null;
	}
	
	@Override
	public Runnable applyClient(ClientOverworldStage stage, Unit unit, ArrayList<AttackRecord> attackRecords, Runnable callback) {
		
		return new Runnable() {
			public void run() {
				Path p = new Path();
				p.setNodes(path);
				stage.grid.move(unit, unit.getXCoord()+path[path.length - 1].x, unit.getYCoord()+path[path.length - 1].y, true);
				unit.move(p, callback);
				stage.includeInView(p.getAllNodes());
			}
		};
	}
	
	@Override
	public String toString() {
		return "Move[" + path[path.length - 1].x + "," + path[path.length - 1].y + "]";
	}
}
