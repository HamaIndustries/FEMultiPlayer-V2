package net.fe.overworldStage.context;

import net.fe.Player;
import net.fe.overworldStage.ClientOverworldStage;
import net.fe.overworldStage.CursorContext;
import net.fe.overworldStage.Zone;
import net.fe.unit.Unit;
import chu.engine.anim.AudioPlayer;

// TODO: Auto-generated Javadoc
/**
 * The Class Idle.
 */
public class Idle extends CursorContext {
	
	/** The player. */
	private Player player;
	
	/** The heal. */
	private Zone move,attack,heal;
	
	/**
	 * Instantiates a new idle.
	 *
	 * @param s the s
	 * @param p the p
	 */
	public Idle(ClientOverworldStage s, Player p) {
		super(s, null);
		player = p;
	}
	
	/* (non-Javadoc)
	 * @see net.fe.overworldStage.OverworldContext#startContext()
	 */
	public void startContext(){
//		boolean movable = false;
//		for(Unit u: stage.getCurrentPlayer().getParty()){
//			if(!u.hasMoved()){
//				movable = true;
//			}
//		}
//		if(movable){
//			super.startContext();
//			cursorChanged();
//		} else {
//			stage.end();
//		}
		super.startContext();
		cursorChanged();
	}
	
	
	/* (non-Javadoc)
	 * @see net.fe.overworldStage.OverworldContext#cleanUp()
	 */
	public void cleanUp(){
		removeZones();
	}

	/* (non-Javadoc)
	 * @see net.fe.overworldStage.OverworldContext#onSelect()
	 */
	@Override
	public void onSelect() {
		Unit u = getHoveredUnit();
		AudioPlayer.playAudio("select");
		if(u!=null && u.getParty() == player.getParty() && !u.hasMoved()){
			new UnitSelected(stage, this, u).startContext();
		} else {
			new EndMenu(stage, this).startContext();
		}

	}
	
	@Override
	public void onNextUnit() {
		Unit hovered = getHoveredUnit();
		Unit target = null;
		boolean found = false;
		for (Unit unit : player.getParty()) {
			if (unit.hasMoved())
				continue;
			
			// If the current unit was found, the target is the next one.
			if (found) {
				target = unit;
				break;
			}
			
			// By default, the target is the first valid unit...
			if (target == null)
				target = unit;
			
			// ... which is the one we use if there is no hovered target (but also if the hovered unit is the last valid unit).
			if (hovered == null)
				break;
			
			if (unit == hovered)
				found = true;
		}
		
		if (target != null) {
			cursorWillChange();
			cursor.setXCoord(target.getXCoord());
			cursor.setYCoord(target.getYCoord());
			cursorChanged();
		}
	}


	/* (non-Javadoc)
	 * @see net.fe.overworldStage.OverworldContext#onCancel()
	 */
	@Override
	public void onCancel() {
		//Nothing
	}
	
	/* (non-Javadoc)
	 * @see net.fe.overworldStage.CursorContext#cursorWillChange()
	 */
	public void cursorWillChange(){
		removeZones();
		Unit u = getHoveredUnit();
		if(u!=null && !u.hasMoved()){
			u.sprite.setAnimation("IDLE");
		}
		AudioPlayer.playAudio("cursor");
	}

	/* (non-Javadoc)
	 * @see net.fe.overworldStage.CursorContext#cursorChanged()
	 */
	public void cursorChanged(){
		Unit u = getHoveredUnit();
		
		if(u!=null && !u.hasMoved()){
			addZones(u);
			if(u.getParty() == stage.getCurrentPlayer().getParty()){
				u.sprite.setAnimation("SELECTED");
			}
		}
	}
	
	/**
	 * Adds the zones.
	 *
	 * @param u the u
	 */
	public void addZones(Unit u){
		this.move = new Zone(stage.grid.getPossibleMoves(u), Zone.MOVE_LIGHT);
		this.attack = Zone.minus(
				new Zone(stage.grid.getAttackRange(u),Zone.ATTACK_LIGHT), move);
		this.heal = Zone.minus(Zone.minus(
				new Zone(stage.grid.getHealRange(u),Zone.HEAL_LIGHT), move), attack);
		stage.addEntity(move);
		stage.addEntity(attack);
		stage.addEntity(heal);
	}
	
	/**
	 * Removes the zones.
	 */
	public void removeZones(){
		stage.removeEntity(move);
		stage.removeEntity(attack);
		stage.removeEntity(heal);
	}

}
