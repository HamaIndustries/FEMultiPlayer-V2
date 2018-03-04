package net.fe.overworldStage.context;

import chu.engine.anim.AudioPlayer;
import net.fe.network.command.WaitCommand;
import net.fe.overworldStage.*;
import net.fe.overworldStage.Zone.RangeIndicator;
import net.fe.overworldStage.Zone.ZoneType;
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
		this.move = new RangeIndicator(grid.getPossibleMoves(selected), ZoneType.MOVE_DARK);
		this.attack = new RangeIndicator(grid.getAttackRange(selected), ZoneType.ATTACK_DARK).minus(move);
		this.heal = new RangeIndicator(grid.getHealRange(selected), ZoneType.HEAL_DARK).minus(move).minus(attack);
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
			
			int invalidIndex = -1;
			Node[] nodes = path.getAllNodes();
			for(int i = 0; i < nodes.length; i++) {
				System.out.println(grid.getUnit(nodes[i].x, nodes[i].y));
				if (grid.getUnit(nodes[i].x, nodes[i].y) != null && !grid.getUnit(nodes[i].x, nodes[i].y).getParty().isAlly(selected.getParty())) {
					//Bump
					invalidIndex = i;
					break;
				}
			}
			
			if(invalidIndex != -1)
				path.truncate(invalidIndex);
			
			grid.move(selected, path.destination().x, path.destination().y, true);
			stage.setControl(false);
			AudioPlayer.playAudio("select");

			final int localVariableInvalidIndexDefinedInAnEnclosingScopeMustBeFinalOrEffectivelyFinal = invalidIndex;
			
			selected.move(path, () -> {
				stage.setControl(true);
				UnitMoved context = new UnitMoved(stage, UnitSelected.this, selected, false, false);
				context.startContext();
				if(localVariableInvalidIndexDefinedInAnEnclosingScopeMustBeFinalOrEffectivelyFinal != -1)
					context.performAction("Wait");
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
