package net.fe.game.modifier;

import net.fe.game.fightStage.CombatTrigger;
import net.fe.game.unit.Unit;

// TODO: Auto-generated Javadoc
/**
 * The Class Miracle.
 */
public class Miracle extends CombatTrigger {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -8613896121666026506L;

	/**
	 * Instantiates a new miracle.
	 */
	public Miracle() {
		super(APPEND_NAME_AFTER_MOD, ENEMY_TURN_MOD);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.fe.fightStage.CombatTrigger#attempt(net.fe.unit.Unit, int)
	 */
	@Override
	public boolean attempt(Unit user, int range, Unit opponent) {
		if (user.getHp() == 1)
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.fe.fightStage.CombatTrigger#runDamageMod(net.fe.unit.Unit,
	 * net.fe.unit.Unit, int)
	 */
	@Override
	public int runDamageMod(Unit a, Unit d, int damage) {
		if (d.getHp() - damage <= 0) {
			return d.getHp() - 1;
		} else {
			return damage;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.fe.fightStage.CombatTrigger#getCopy()
	 */
	public CombatTrigger getCopy() {
		return new Miracle();
	}

}
