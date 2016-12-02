package net.fe.game.modifier;

import net.fe.game.fightStage.CombatCalculator;
import net.fe.game.fightStage.CombatTrigger;
import net.fe.game.unit.Unit;

// TODO: Auto-generated Javadoc
/**
 * The Class Gamble.
 */
public class Gamble extends CombatTrigger {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 5208500314150806972L;

	/**
	 * Instantiates a new gamble.
	 */
	public Gamble() {
		super(REPLACE_NAME_AFTER_PRE, YOUR_TURN_PRE, "gamble");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.fe.fightStage.CombatTrigger#runPreAttack(net.fe.fightStage.
	 * CombatCalculator, net.fe.unit.Unit, net.fe.unit.Unit)
	 */
	@Override
	public boolean runPreAttack(CombatCalculator stage, Unit a, Unit d) {
		a.setTempMod("Crit", a.crit());
		a.setTempMod("Hit", -a.hit() / 2);
		return true;
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
		return new Gamble();
	}

}
