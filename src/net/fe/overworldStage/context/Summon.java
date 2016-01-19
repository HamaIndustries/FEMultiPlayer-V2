package net.fe.overworldStage.context;

import java.util.ArrayList;
import java.util.List;

import chu.engine.anim.AudioPlayer;
import net.fe.overworldStage.*;
import net.fe.unit.Unit;

public class Summon extends OverworldContext {
	private Zone zone;
	private List<Node> targets;
	protected int selected;
	protected Unit unit;

	public Summon(ClientOverworldStage stage, OverworldContext context, Zone z, Unit unit) {
		super(stage, context);
		zone = z;
		targets = new ArrayList<Node>();
		this.unit = unit;
	}
	
	public void startContext() {
		super.startContext();
		findTargets(unit);
		stage.addEntity(zone);
		updateCursor();
	}

	public void cleanUp() {
		super.cleanUp();
		stage.removeEntity(zone);
	}

	private void findTargets(Unit unit) {
		targets.clear();
		for (Node n : zone.getNodes()) {
			Unit u = grid.getUnit(n.x, n.y);
			if (u == null
					&& grid.getTerrain(n.x, n.y).getMoveCost(
							net.fe.unit.Class.createClass("Phantom")) <
							unit.get("Mov")) {
				targets.add(n);
			}
		}
	}

	@Override
	public void onSelect() {
		AudioPlayer.playAudio("select");
		stage.addCmd("SUMMON", getCurrentTarget().x, getCurrentTarget().y);
		stage.send();
		cursor.setXCoord(unit.getXCoord());
		cursor.setYCoord(unit.getYCoord());
		stage.reset();
	}

	public void onUp() {
		prevTarget();
	}

	public void onDown() {
		nextTarget();
	}

	public void onLeft() {
		prevTarget();
	}

	public void onRight() {
		nextTarget();
	}

	public void prevTarget() {
		selected--;
		if (selected < 0) {
			selected += targets.size();
		}
		updateCursor();
	}

	public void nextTarget() {
		selected++;
		selected %= targets.size();
		updateCursor();
	}

	public Node getCurrentTarget() {
		return targets.get(selected);
	}

	public void updateCursor() {
		AudioPlayer.playAudio("cursor");
		cursor.setXCoord(targets.get(selected).x);
		cursor.setYCoord(targets.get(selected).y);
	}

	@Override
	public void onCancel() {
		super.onCancel();
		// Reset the position of the cursor on cancels
		cursor.setXCoord(unit.getXCoord());
		cursor.setYCoord(unit.getYCoord());
	}

}
