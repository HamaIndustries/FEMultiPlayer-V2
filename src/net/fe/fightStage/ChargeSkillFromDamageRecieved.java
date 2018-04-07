package net.fe.fightStage;

import net.fe.rng.RNG;
import net.fe.unit.Unit;

/**
 * Increase skill charge when the user takes damage
 * 
 * Effect: Increments skillCharge when the user takes damage.
 * 	The amount incremented is the damage received times a parameter.
 * Trigger: Always
 */
public final class ChargeSkillFromDamageRecieved extends CombatTrigger {
	private static final long serialVersionUID = 1L;
	
	private final int multiplier;
	
	/**
	 * @param multiplier the skill charge increment multiplier
	 */
	public ChargeSkillFromDamageRecieved(int multiplier) {
		super(NO_NAME_MOD, 0);
		this.multiplier = multiplier;
	}
	
	@Override
	public boolean attempt(Unit user, boolean initiator, int range, Unit opponent, RNG rng) {
		return true;
	}

	@Override
	public int runEnemyTurnSkillCharge(int damage) {
		return damage * multiplier;
	}
	
	@Override
	public CombatTrigger getCopy(){
		return new ChargeSkillFromDamageRecieved(multiplier);
	}

	
	protected boolean canEquals(Object other) {
		return other instanceof ChargeSkillFromDamageRecieved;
	}
	@Override
	public boolean equals(Object other) {
		return super.equals(other) &&
			other instanceof ChargeSkillFromDamageRecieved &&
			((ChargeSkillFromDamageRecieved) other).canEquals(this) &&
			((ChargeSkillFromDamageRecieved) other).multiplier == this.multiplier;
	}
	@Override public int hashCode() { return "ChargeSkillFromDamageRecieved".hashCode() + multiplier; }
	@Override public String toString() { return "ChargeSkillFromDamageRecieved[" + multiplier + "]"; }
}
