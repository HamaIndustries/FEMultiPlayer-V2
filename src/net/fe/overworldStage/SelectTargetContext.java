package net.fe.overworldStage;

import java.util.ArrayList;
import java.util.List;

import chu.engine.anim.AudioPlayer;
import net.fe.unit.Unit;

// TODO: Auto-generated Javadoc
/**
 * The Class SelectTargetContext.
 */
public abstract class SelectTargetContext extends OverworldContext {
	
	/** The zone. */
	private Zone zone;
	
	/** The targets. */
	private List<Unit> targets;
	
	/** The selected. */
	protected int selected;
	
	/** The unit. */
	protected Unit unit;
	
	/** The friendly. */
	protected boolean friendly;

	/**
	 * Instantiates a new select target context.
	 *
	 * @param stage the stage
	 * @param context the context
	 * @param z the z
	 * @param u the u
	 * @param friendly the friendly
	 */
	public SelectTargetContext(ClientOverworldStage stage, OverworldContext context, Zone z,
			Unit u, boolean friendly) {
		super(stage, context);
		zone = z;
		targets = new ArrayList<Unit>();
		this.unit = u;
		this.friendly = friendly;
		
	}
	
	/* (non-Javadoc)
	 * @see net.fe.overworldStage.OverworldContext#startContext()
	 */
	public void startContext(){
		super.startContext();
		findTargets(unit, friendly);
		stage.addEntity(zone);
		updateCursor();
	}
	
	/* (non-Javadoc)
	 * @see net.fe.overworldStage.OverworldContext#cleanUp()
	 */
	public void cleanUp(){
		super.cleanUp();
		stage.removeEntity(zone);

	}

	/**
	 * Find targets.
	 *
	 * @param unit the unit
	 * @param friendly the friendly
	 */
	private void findTargets(Unit unit, boolean friendly) {
		targets.clear();
		for (Node n : zone.getNodes()) {
			Unit u = grid.getUnit(n.x, n.y);
			if(u!= null && validTarget(u)){
				targets.add(u);
			}
		}
		if (0 == targets.size()) {
			throw new IllegalStateException("No valid targets found");
		}
	}
	
	/**
	 * Valid target.
	 *
	 * @param u the u
	 * @return true, if successful
	 */
	public boolean validTarget(Unit u){
		return friendly == u.getParty().isAlly(stage.getCurrentPlayer().getParty());
	}

	/* (non-Javadoc)
	 * @see net.fe.overworldStage.OverworldContext#onSelect()
	 */
	@Override
	public void onSelect() {
		AudioPlayer.playAudio("select");
		unitSelected(grid.getUnit(cursor.getXCoord(), cursor.getYCoord()));
	}

	/**
	 * Unit selected.
	 *
	 * @param u the u
	 */
	public abstract void unitSelected(Unit u);

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
		if(selected < 0){
			selected+=targets.size();
		}
		updateCursor();
	}

	/**
	 * Next target.
	 */
	public void nextTarget() {
		selected++;
		selected%= targets.size();
		updateCursor();
	}
	
	/**
	 * Gets the current target.
	 *
	 * @return the current target
	 */
	public Unit getCurrentTarget(){
		return targets.get(selected);
	}

	/**
	 * Update cursor.
	 */
	public void updateCursor() {
		AudioPlayer.playAudio("cursor");
		cursor.setXCoord(targets.get(selected).getXCoord());
		cursor.setYCoord(targets.get(selected).getYCoord());
		stage.setUnitInfoUnit(targets.get(selected));
	}

	/* (non-Javadoc)
	 * @see net.fe.overworldStage.OverworldContext#onCancel()
	 */
	@Override
	public void onCancel() {
		super.onCancel();
		//Reset the position of the cursor on cancels
		
		cursor.setXCoord(unit.getXCoord());
		cursor.setYCoord(unit.getYCoord());
		stage.setUnitInfoUnit(unit);
	}

}
