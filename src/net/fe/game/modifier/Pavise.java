package net.fe.game.modifier;

import net.fe.game.fightStage.CombatTrigger;
import net.fe.game.unit.Unit;

// TODO: Auto-generated Javadoc
/**
 * The Class Gamble.
 */
public class Pavise extends CombatTrigger {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 5208500314150806973L;

	/**
	 * Instantiates a new gamble.
	 */
	public Pavise() {
		super(APPEND_NAME_AFTER_MOD, ENEMY_TURN_MOD | SHOW_IN_PREVIEW);
	}

	public int runDamageMod(Unit a, Unit d, int damage) {
		return damage / 2;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.fe.fightStage.CombatTrigger#attempt(net.fe.unit.Unit, int)
	 */
	@Override
	public boolean attempt(Unit user, int range, Unit opponent) {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.fe.fightStage.CombatTrigger#getCopy()
	 */
	public CombatTrigger getCopy() {
		return new Pavise();
	}

}
