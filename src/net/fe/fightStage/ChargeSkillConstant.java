package net.fe.fightStage;

import net.fe.rng.RNG;
import net.fe.unit.Unit;

/**
 * Increase skill charge by a constant amount each round of combat
 * 
 * Effect: Increments skillCharge when the user participates in a round of battle
 * 	The amount incremented is .
 * Trigger: Always
 */
public final class ChargeSkillConstant extends CombatTrigger {
	private static final long serialVersionUID = 1L;
	
	private static final int value = 5;
	
	/**
	 */
	public ChargeSkillConstant() {
		super(NO_NAME_MOD, 0);
	}
	
	@Override
	public boolean attempt(Unit user, boolean initiator, int range, Unit opponent, RNG rng) {
		return true;
	}

	@Override
	public int runEnemyTurnSkillCharge(int damage) {
		return value;
	}
	
	@Override
	public int runYourTurnSkillCharge() {
		return value;
	}
	
	@Override
	public CombatTrigger getCopy(){
		return new ChargeSkillConstant();
	}
	
	protected boolean canEquals(Object other) {
		return other instanceof ChargeSkillConstant;
	}
	@Override
	public boolean equals(Object other) {
		return super.equals(other) &&
			other instanceof ChargeSkillConstant &&
			((ChargeSkillConstant) other).canEquals(this);
	}
	@Override public int hashCode() { return "ChargeSkillConstant".hashCode() + value; }
	@Override public String toString() { return "ChargeSkillConstant[" + value + "]"; }
}
