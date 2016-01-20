package net.fe.overworldStage.context;

import java.util.ArrayList;
import java.util.HashMap;
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
	
	private static int summonCount = 0;
	public static Unit generateSummon(Unit summoner) {
		net.fe.unit.WeaponFactory.loadWeapons();
		
		HashMap<String, Integer> bases = new HashMap();
		HashMap<String, Integer> growths = new HashMap();
		bases.put("HP", 1);
		bases.put("Str", 5);
		bases.put("Def", 0);
		bases.put("Mag", 0);
		bases.put("Res", 0);
		bases.put("Lck", 0);
		bases.put("Skl", 2);
		bases.put("Spd", 4);
		bases.put("Mov", 5);
		growths.put("HP", 0);
		growths.put("Str", 55);
		growths.put("Def", 15);
		growths.put("Mag", 15);
		growths.put("Res", 15);
		growths.put("Lck", 50);
		growths.put("Skl", 35);
		growths.put("Spd", 45);
		growths.put("Mov", 0);
		summonCount = summonCount + 1;
		final Unit summon = new Unit("Phantom " + summonCount, net.fe.unit.Class.createClass("Berserker"), '-', bases, growths);
		summon.addToInventory(net.fe.unit.Item.getItem("Iron Axe"));
		summon.initializeEquipment();
		summon.setLevel(summoner.get("Lvl"));
		summon.fillHp();
		summon.setMoved(true);
		
		summoner.getParty().addUnit(summon);
		summon.stage = summoner.stage;
		return summon;
	}
}
