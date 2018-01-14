package net.fe.overworldStage.context;

import java.util.ArrayList;
import java.util.List;

import chu.engine.anim.AudioPlayer;
import net.fe.overworldStage.*;
import net.fe.unit.Unit;
import net.fe.network.command.DropCommand;

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
					&& grid.getTerrain(n.x, n.y).getMoveCost(
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
		//If there's a unit in the fog
		//This is guaranteed to succeed as long as the vision range of the unit is greater than one.
		while (grid.getUnit(getCurrentTarget().x, getCurrentTarget().y) != null)
			nextTarget();
		DropCommand c = new DropCommand(getCurrentTarget().x, getCurrentTarget().y);
		c.applyClient(stage, unit, null, new EmptyRunnable()).run();
		stage.addCmd(c);
		stage.send();
		cursor.setXCoord(unit.getXCoord());
		cursor.setYCoord(unit.getYCoord());
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
	}

	private static final class EmptyRunnable implements Runnable {
		@Override public void run() {}
	}
}
