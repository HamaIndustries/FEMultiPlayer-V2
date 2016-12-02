package net.fe.game.fightStage.skill;

import net.fe.RNG;
import net.fe.game.fightStage.CombatCalculator;
import net.fe.game.fightStage.CombatTrigger;
import net.fe.game.unit.Unit;

// TODO: Auto-generated Javadoc
/**
 * The Class Luna.
 */
public final class Luna extends CombatTrigger {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -6539654970701395612L;

	/** The ranged. */
	private final boolean ranged;

	/**
	 * Instantiates a new luna.
	 *
	 * Info: Halves enemy Res/Def.
	 *
	 * Chance: Skl/2
	 *
	 * @param rangeok the rangeok
	 */
	public Luna(boolean rangeok) {
		super(REPLACE_NAME_AFTER_PRE, YOUR_TURN_PRE);
		ranged = rangeok;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.fe.fightStage.CombatTrigger#attempt(net.fe.unit.Unit, int)
	 */
	@Override
	public boolean attempt(Unit user, int range, Unit opponent) {

		return (ranged || range == 1) && RNG.get() < user.getStats().skl / 2;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.fe.fightStage.CombatTrigger#runPreAttack(net.fe.fightStage.
	 * CombatCalculator, net.fe.unit.Unit, net.fe.unit.Unit)
	 */
	@Override
	public boolean runPreAttack(CombatCalculator stage, Unit a, Unit d) {
		d.setTempMod("Def", -d.getStats().def / 2);
		d.setTempMod("Res", -d.getStats().res / 2);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.fe.fightStage.CombatTrigger#getCopy()
	 */
	public CombatTrigger getCopy() {
		return new Luna(ranged);
	}

	protected boolean canEquals(Object other) {
		return other instanceof Luna;
	}

	@Override
	public boolean equals(Object other) {
		return super.equals(other) && other instanceof Luna && ((Luna) other).canEquals(this)
		        && ((Luna) other).ranged == this.ranged;
	}

	@Override
	public int hashCode() {
		return "Luna".hashCode() + (ranged ? 1 : 0);
	}

	@Override
	public String toString() {
		return "Luna[" + (ranged ? "melee" : "ranged") + "]";
	}
}
