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
	
	private static final long serialVersionUID = 2408000878615714583L;
	
	private final Node[] path;
	
	public MoveCommand(Node[] path) {
		this.path = path;
	}
	
	@Override
	public ArrayList<AttackRecord> applyServer(OverworldStage stage, Unit unit) {
		
		//TODO: validate
		stage.grid.move(unit, path[path.length - 1].x, path[path.length - 1].y, false);
		return null;
	}
	
	@Override
	public Runnable applyClient(ClientOverworldStage stage, Unit unit, ArrayList<AttackRecord> attackRecords, Runnable callback) {
		
		return () -> {
			Path p = new Path();
			p.setNodes(path);
			stage.grid.move(unit, path[path.length - 1].x, path[path.length - 1].y, true);
			unit.move(p, callback);
			Node[] nodes = p.getAllNodes();
			ArrayList<Node> visibleNodes = new ArrayList<Node>();
			for(int i = 0; i < nodes.length; i++)
				if(!stage.getFog().contains(nodes[i]))
					visibleNodes.add(nodes[i]);
			stage.includeInView(visibleNodes.toArray(new Node[0]));
		};
	}
	
	@Override
	public String toString() {
		return "Move[" + java.util.Arrays.toString(path) + "]";
	}
}
