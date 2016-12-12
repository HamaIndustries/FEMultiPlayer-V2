package net.fe.game.fightStage.skill;

import net.fe.game.fightStage.CombatTrigger;
import net.fe.game.unit.Unit;

// TODO: Auto-generated Javadoc
/**
 * The Class PavisePlus.
 */
public class PavisePlus extends CombatTrigger {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -5629273200054077795L;

	/**
	 * Instantiates a new pavise.
	 * 
	 * Info: Halves damage taken.
	 * 
	 * Chance: Skl
	 */
	public PavisePlus() {
		super(NO_NAME_MOD | SHOW_IN_PREVIEW, ENEMY_TURN_MOD);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.fe.fightStage.CombatTrigger#attempt(net.fe.unit.Unit, int)
	 */
	@Override
	public boolean attempt(Unit user, int range, Unit opponent) {
		// return true;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.fe.fightStage.CombatTrigger#runDamageMod(net.fe.unit.Unit,
	 * net.fe.unit.Unit, int)
	 */
	@Override
	public int runDamageMod(Unit a, Unit d, int dmg) {
		return dmg / 2;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.fe.fightStage.CombatTrigger#getCopy()
	 */
	public CombatTrigger getCopy() {
		return new PavisePlus();
	}

}
