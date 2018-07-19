package net.fe.overworldStage.context;

import chu.engine.anim.AudioPlayer;
import net.fe.overworldStage.ClientOverworldStage;
import net.fe.overworldStage.CursorContext;
import net.fe.overworldStage.OverworldContext;
import net.fe.overworldStage.Zone;
import net.fe.overworldStage.Zone.ZoneType;
import net.fe.unit.Unit;

public abstract class OverworldInspectionContext extends CursorContext {

	private Zone move, attack, heal;
	
	public OverworldInspectionContext(ClientOverworldStage s, OverworldContext prevContext) {
		super(s, prevContext);
	}

	@Override
	public void cursorChanged() {
		Unit u = getHoveredUnit();
		
		stage.setUnitInfoUnit(u);
		if(u != null && !u.hasMoved()) {
			addZones(u);
			if(u.getParty() == stage.getCurrentPlayer().getParty())
				u.sprite.setAnimation("SELECTED");
		}
	}

	@Override
	public void cursorWillChange() {
		removeZones();
		Unit u = getHoveredUnit();
		if(u != null && !u.hasMoved())
			u.sprite.setAnimation("IDLE");
		AudioPlayer.playAudio("cursor");
	}
	
	private void addZones(Unit u) {
		this.move = new Zone(stage.grid.getPossibleMoves(u), ZoneType.MOVE_LIGHT);
		this.attack = new Zone(stage.grid.getAttackRange(u), ZoneType.ATTACK_LIGHT).minus(move);
		this.heal = new Zone(stage.grid.getHealRange(u), ZoneType.HEAL_LIGHT).minus(move).minus(attack);
		stage.addEntity(move);
		stage.addEntity(attack);
		stage.addEntity(heal);
	}
	
	private void removeZones() {
		stage.removeEntity(move);
		stage.removeEntity(attack);
		stage.removeEntity(heal);
	}

}