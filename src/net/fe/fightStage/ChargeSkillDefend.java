package net.fe.fightStage;

import net.fe.rng.RNG;
import net.fe.unit.Unit;

/**
 * 
 * 
 * Effect: Increments skillCharge when the user takes damage.
 * 		The amount to increment by is a parameter. <br/>
 * Trigger: Always
 */
public final class ChargeSkillDefend extends CombatTrigger {
	private static final long serialVersionUID = 1L;
	
	private final int amount;
	private final int source;
	
	/**
	 * @param amount the amount to increment skillCharge by
	 * @param source Whether this is from a unit or a weapon.
	 * 		Mostly so that this skill can stack despite the use
	 * 		of HashMaps in CombatCalculator.
	 */
	public ChargeSkillDefend(int amount, int source) {
		super(NO_NAME_MOD, 0);
		this.amount = amount;
		this.source = source;
	}
	
	/* (non-Javadoc)
	 * @see net.fe.fightStage.CombatTrigger#attempt(net.fe.unit.Unit, int)
	 */
	@Override
	public boolean attempt(Unit user, int range, Unit opponent, RNG rng) {
		return true;
	}

	/* (non-Javadoc)
	 * @see net.fe.fightStage.CombatTrigger#runPreAttack(net.fe.fightStage.CombatCalculator, net.fe.unit.Unit, net.fe.unit.Unit)
	 */
	@Override
	public int runEnemyTurnSkillCharge(int damage) {
		return (damage == 0 ? 0 : amount);
	}
	
	/* (non-Javadoc)
	 * @see net.fe.fightStage.CombatTrigger#getCopy()
	 */
	public CombatTrigger getCopy(){
		return new ChargeSkillDefend(amount, source);
	}

	
	protected boolean canEquals(Object other) {
		return other instanceof ChargeSkillDefend;
	}
	@Override
	public boolean equals(Object other) {
		return super.equals(other) &&
			other instanceof ChargeSkillDefend &&
			((ChargeSkillDefend) other).canEquals(this) &&
			((ChargeSkillDefend) other).amount == this.amount &&
			((ChargeSkillDefend) other).source == this.source;
	}
	@Override public int hashCode() { return "ChargeSkillDefend".hashCode() + amount + source * 31; }
	@Override public String toString() { return "ChargeSkillDefend[" + amount + "," + source + "]"; }
}
