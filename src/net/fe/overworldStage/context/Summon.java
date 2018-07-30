package net.fe.overworldStage.context;

import java.util.ArrayList;
import java.util.List;

import chu.engine.anim.AudioPlayer;
import net.fe.network.command.Command;
import net.fe.network.command.InterruptedCommand;
import net.fe.network.command.SummonCommand;
import net.fe.overworldStage.ClientOverworldStage;
import net.fe.overworldStage.Node;
import net.fe.overworldStage.OverworldContext;
import net.fe.overworldStage.Zone;
import net.fe.unit.Class;
import net.fe.unit.Statistics;
import net.fe.unit.Unit;
import net.fe.unit.WeaponFactory;

// TODO: Auto-generated Javadoc
/**
 * The Class Summon.
 */
public class Summon extends OverworldContext {
	
	public static final Statistics BASES = new Statistics(
			/* hp = */ 1,
			/* str = */ 5,
			/* mag = */ 0,
			/* skl = */ 2,
			/* spd = */ 4,
			/* def = */ 0,
			/* res = */ 0,
			/* lck = */ 0,
			/* mov = */ 5,
			/* con = */ 11,
			/* aid = */ 10
		);

	public static final Statistics GROWTHS = new Statistics(
				/* hp = */ 0,
				/* str = */ 55,
				/* mag = */ 15,
				/* skl = */ 35,
				/* spd = */ 45,
				/* def = */ 15,
				/* res = */ 15,
				/* lck = */ 50,
				/* mov = */ 0,
				/* con = */ 0,
				/* aid = */ 0
			);
	
	/** The zone. */
	private Zone zone;
	
	/** The targets. */
	private List<Node> targets;
	
	/** The selected. */
	protected int selected;
	
	/** The unit. */
	protected Unit unit;

	/**
	 * Instantiates a new summon.
	 *
	 * @param stage the stage
	 * @param context the context
	 * @param z the z
	 * @param unit the unit
	 */
	public Summon(ClientOverworldStage stage, OverworldContext context, Zone z, Unit unit) {
		super(stage, context);
		zone = z;
		targets = new ArrayList<Node>();
		this.unit = unit;
	}
	
	/* (non-Javadoc)
	 * @see net.fe.overworldStage.OverworldContext#startContext()
	 */
	public void startContext() {
		super.startContext();
		findTargets(unit);
		stage.addEntity(zone);
		updateCursor();
	}

	/* (non-Javadoc)
	 * @see net.fe.overworldStage.OverworldContext#cleanUp()
	 */
	public void cleanUp() {
		super.cleanUp();
		stage.removeEntity(zone);
	}

	/**
	 * Find targets.
	 *
	 * @param unit the unit
	 */
	private void findTargets(Unit unit) {
		targets.clear();
		for (Node n : zone.getNodes()) {
			Unit u = grid.getVisibleUnit(n.x, n.y);
			if (u == null
					&& grid.getVisibleTerrain(n.x, n.y).getMoveCost(
							net.fe.unit.Class.createClass("Phantom")) <
							unit.getStats().mov) {
				targets.add(n);
			}
		}
	}

	/* (non-Javadoc)
	 * @see net.fe.overworldStage.OverworldContext#onSelect()
	 */
	@Override
	public void onSelect() {
		AudioPlayer.playAudio("select");
		// If the selected target was actually invalid (i.e. trying to put down a unit in a fogged tile containing an enemy)
		// tries to find a new valid tile. If that fails, the unit is forced to wait.
		Command c = null;
		int x = getCurrentTarget().x;
		int y = getCurrentTarget().y;
		if(grid.getUnit(x, y) != null || BASES.mov < grid.getTerrain(x, y).getMoveCost(Class.createClass("Phantom")))
			c = new InterruptedCommand(new Node(x, y));
		else
			c = new SummonCommand(x, y);
		
		stage.addCmd(c);
		c.applyClient(stage, unit, null, new EmptyRunnable()).run();
		stage.send();
		cursor.setXCoord(unit.getXCoord());
		cursor.setYCoord(unit.getYCoord());
		stage.setUnitInfoUnit(unit);
		stage.reset();
	}

	/* (non-Javadoc)
	 * @see net.fe.overworldStage.OverworldContext#onUp()
	 */
	public void onUp() {
		prevTarget();
	}

	/* (non-Javadoc)
	 * @see net.fe.overworldStage.OverworldContext#onDown()
	 */
	public void onDown() {
		nextTarget();
	}

	/* (non-Javadoc)
	 * @see net.fe.overworldStage.OverworldContext#onLeft()
	 */
	public void onLeft() {
		prevTarget();
	}

	/* (non-Javadoc)
	 * @see net.fe.overworldStage.OverworldContext#onRight()
	 */
	public void onRight() {
		nextTarget();
	}

	/**
	 * Prev target.
	 */
	public void prevTarget() {
		selected--;
		if (selected < 0) {
			selected += targets.size();
		}
		updateCursor();
	}

	/**
	 * Next target.
	 */
	public void nextTarget() {
		selected++;
		selected %= targets.size();
		updateCursor();
	}

	/**
	 * Gets the current target.
	 *
	 * @return the current target
	 */
	public Node getCurrentTarget() {
		return targets.get(selected);
	}

	/**
	 * Update cursor.
	 */
	public void updateCursor() {
		AudioPlayer.playAudio("cursor");
		cursor.setXCoord(targets.get(selected).x);
		cursor.setYCoord(targets.get(selected).y);
		stage.setUnitInfoUnit(stage.getHoveredUnit());
	}

	/* (non-Javadoc)
	 * @see net.fe.overworldStage.OverworldContext#onCancel()
	 */
	@Override
	public void onCancel() {
		super.onCancel();
		// Reset the position of the cursor on cancels
		cursor.setXCoord(unit.getXCoord());
		cursor.setYCoord(unit.getYCoord());
		stage.setUnitInfoUnit(unit);
	}
	
	/** The summon count. */
	private static int summonCount = 0;
	
	/**
	 * Generate summon.
	 *
	 * @param summoner the summoner
	 * @return the unit
	 */
	public static Unit generateSummon(Unit summoner) {
		WeaponFactory.loadWeapons();
		
		summonCount = summonCount + 1;
		final Unit summon = new Unit("Phantom " + summonCount, net.fe.unit.Class.createClass("Phantom"), '-', BASES, GROWTHS);
		summon.addToInventory(net.fe.unit.Item.getItem("Iron Axe"));
		summon.initializeEquipment();
		summon.setLevel(summoner.getLevel());
		summon.fillHp();
		summon.setMoved(true);
		
		summoner.getParty().addUnit(summon);
		summon.stage = summoner.stage;
		return summon;
	}

	private static final class EmptyRunnable implements Runnable {
		@Override public void run() {}
	}
}
