package net.fe.overworldStage.context;

import chu.engine.anim.AudioPlayer;
import net.fe.overworldStage.*;
import net.fe.unit.Unit;

// TODO: Auto-generated Javadoc
/**
 * The Class UnitSelected.
 */
public class UnitSelected extends CursorContext {
	
	/** The heal. */
	private Zone move, attack, heal;
	
	/** The selected. */
	private Unit selected;
	
	/** The path. */
	private Path path;

	/**
	 * Instantiates a new unit selected.
	 *
	 * @param s the s
	 * @param prev the prev
	 * @param u the u
	 */
	public UnitSelected(ClientOverworldStage s, OverworldContext prev, Unit u) {
		super(s, prev);
		selected = u;
	}
	
	/* (non-Javadoc)
	 * @see net.fe.overworldStage.OverworldContext#startContext()
	 */
	public void startContext(){
		super.startContext();
		selected.sprite.setAnimation("DOWN");
		grid.move(selected, selected.getOrigX(), selected.getOrigY(), false);
		this.move = new Zone(grid.getPossibleMoves(selected), Zone.MOVE_DARK);
		this.attack = Zone.minus(new Zone(grid.getAttackRange(selected),
				Zone.ATTACK_DARK), move);
		this.heal = Zone.minus(Zone.minus(new Zone(grid.getHealRange(selected),
				Zone.HEAL_DARK), move), attack);
		stage.addEntity(move);
		stage.addEntity(attack);
		stage.addEntity(heal);
		
		stage.setSelectedUnit(selected);
		
		updatePath();
	}
	
	/* (non-Javadoc)
	 * @see net.fe.overworldStage.OverworldContext#cleanUp()
	 */
	public void cleanUp(){
		stage.removeEntity(attack);
		stage.removeEntity(move);
		stage.removeEntity(heal);
		stage.removeEntity(path);
	}

	/* (non-Javadoc)
	 * @see net.fe.overworldStage.OverworldContext#onSelect()
	 */
	@Override
	public void onSelect() {
		if (path == null) return;
		if (move.getNodes().contains(new Node(cursor.getXCoord(), cursor.getYCoord()))) {
			grid.move(selected, cursor.getXCoord(),	cursor.getYCoord(), true);
			stage.setControl(false);
			AudioPlayer.playAudio("select");
			selected.move(path, new Runnable() {
				@Override
				public void run() {
					stage.setControl(true);
					new UnitMoved(stage, UnitSelected.this,
							selected, false, false).startContext();
				}
			});
			// We don't want to display the path/range while moving.
			cleanUp();
		}
	}
	
	@Override
	public void onNextUnit() {
		cursor.setXCoord(selected.getXCoord());
		cursor.setYCoord(selected.getYCoord());
		stage.setUnitInfoUnit(selected);
		updatePath();
		AudioPlayer.playAudio("cancel");
	}

	/* (non-Javadoc)
	 * @see net.fe.overworldStage.OverworldContext#onCancel()
	 */
	@Override
	public void onCancel() {
		cursor.setXCoord(selected.getOrigX());
		cursor.setYCoord(selected.getOrigY());
		stage.setUnitInfoUnit(selected);
		// clear variables set by starting the context
		stage.setSelectedUnit(null);
		super.onCancel();
	}
	
	/* (non-Javadoc)
	 * @see net.fe.overworldStage.CursorContext#cursorWillChange()
	 */
	public void cursorWillChange(){
		//Nothing
	}

	/* (non-Javadoc)
	 * @see net.fe.overworldStage.CursorContext#cursorChanged()
	 */
	public void cursorChanged() {
		updatePath();
		AudioPlayer.playAudio("cursor");
	}

	/**
	 * Update path.
	 */
	private void updatePath() {
		stage.removeEntity(path);
		path = stage.grid.improvePath(selected, cursor.getXCoord(), cursor.getYCoord(), path);
		if (path != null) {
			stage.addEntity(path);
		}
	}

}
