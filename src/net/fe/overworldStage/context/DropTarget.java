package net.fe.overworldStage.context;

import java.util.ArrayList;
import java.util.List;

import chu.engine.anim.AudioPlayer;
import net.fe.network.command.Command;
import net.fe.network.command.DropCommand;
import net.fe.network.command.InterruptedCommand;
import net.fe.overworldStage.ClientOverworldStage;
import net.fe.overworldStage.Node;
import net.fe.overworldStage.OverworldContext;
import net.fe.overworldStage.Zone;
import net.fe.unit.Unit;

// TODO: Auto-generated Javadoc
/**
 * The Class DropTarget.
 */
public class DropTarget extends OverworldContext {
	
	/** The zone. */
	private Zone zone;
	
	/** The targets. */
	private List<Node> targets;
	
	/** The selected. */
	protected int selected;
	
	/** The unit. */
	protected Unit unit;

	/**
	 * Instantiates a new drop target.
	 *
	 * @param stage the stage
	 * @param context the context
	 * @param z the z
	 * @param u the u
	 */
	public DropTarget(ClientOverworldStage stage, OverworldContext context,
			Zone z, Unit u) {
		super(stage, context);
		zone = z;
		targets = new ArrayList<Node>();
		this.unit = u;

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
							unit.rescuedUnit().getTheClass()) < unit
							.rescuedUnit().getStats().mov) {
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
		if(grid.getUnit(x, y) != null || unit.rescuedUnit().getStats().mov < grid.getTerrain(x, y).getMoveCost(unit.rescuedUnit().getTheClass()))
			c = new InterruptedCommand(new Node(x, y));
		else
			c = new DropCommand(x, y);
		
		c.applyClient(stage, unit, null, new EmptyRunnable()).run();
		stage.addCmd(c);
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

	private static final class EmptyRunnable implements Runnable {
		@Override public void run() {}
	}
}
