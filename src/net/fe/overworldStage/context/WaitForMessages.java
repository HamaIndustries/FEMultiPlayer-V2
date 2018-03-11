package net.fe.overworldStage.context;

import net.fe.overworldStage.ClientOverworldStage;
import net.fe.overworldStage.CursorContext;
import net.fe.overworldStage.Zone;
import net.fe.overworldStage.Zone.RangeIndicator;
import net.fe.overworldStage.Zone.RangeIndicator.RangeType;
import net.fe.unit.Unit;
import chu.engine.anim.AudioPlayer;

// TODO: Auto-generated Javadoc
/**
 * The Class WaitForMessages.
 */
public class WaitForMessages extends CursorContext {
	
	/** The heal. */
	private Zone move,attack,heal;
	
	/**
	 * Instantiates a new wait for messages.
	 *
	 * @param s the s
	 */
	public WaitForMessages(ClientOverworldStage s) {
		super(s, null);
	}
	
	/* (non-Javadoc)
	 * @see net.fe.overworldStage.OverworldContext#startContext()
	 */
	public void startContext(){
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
		//Nothing
	}


	/* (non-Javadoc)
	 * @see net.fe.overworldStage.OverworldContext#onCancel()
	 */
	@Override
	public void onCancel() {
		//Nothing
	}
	
	@Override
	public void onInspectInventory() {
		Unit u = getHoveredUnit();
		if (u != null && u.getInventory().size() >= 1) {
			AudioPlayer.playAudio("select");
			new InspectInventoryContext(stage, this, u).startContext();
		}
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
	}

	/* (non-Javadoc)
	 * @see net.fe.overworldStage.CursorContext#cursorChanged()
	 */
	public void cursorChanged(){
		Unit u = getHoveredUnit();
		AudioPlayer.playAudio("cursor");
		stage.setUnitInfoUnit(u);
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
		this.move = new RangeIndicator(stage.grid.getPossibleMoves(u), RangeType.MOVE_LIGHT);
		this.attack = new RangeIndicator(stage.grid.getAttackRange(u), RangeType.ATTACK_LIGHT).minus(move);
		this.heal = new RangeIndicator(stage.grid.getHealRange(u), RangeType.HEAL_LIGHT).minus(move).minus(attack);
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
