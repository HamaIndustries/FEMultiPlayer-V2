package net.fe.fightStage;

import net.fe.rng.RNG;
import net.fe.unit.Unit;

/**
 * 
 * 
 * Effect: Triple damage by spending a full skill charge. <br/>
 * Trigger: enemy phase and skillCharge is full
 */
public final class RageEp extends CombatTrigger {
	private static final long serialVersionUID = 1L;
	
	private static final int COST = Unit.MAX_SKILL_CHARGE;
	
	/**
	 */
	public RageEp(){
		// APPEND_NAME_AFTER_MOD doesn't append the name if runDamageMod
		// doesn't change the damage, say from 0 to 0. Hence the need to use
		// REPLACE_NAME_AFTER_PRE, and REPLACE_... is the only reason to have
		// YOUR_TURN_PRE
		super(REPLACE_NAME_AFTER_PRE, YOUR_TURN_PRE | YOUR_TURN_MOD);
	}
	
	@Override
	public boolean attempt(Unit user, boolean initiator, int range, Unit opponent, RNG rng) {
		return !initiator && user.getSkillCharge() >= COST;
	}

	@Override
	public int runDamageMod(Unit a, Unit d, int damage){
		return damage * 3;
	}
	
	@Override
	public int runYourTurnSkillCharge() {
		return -COST;
	}
	
	public CombatTrigger getCopy(){
		return new RageEp();
	}
	
	protected boolean canEquals(Object other) {
		return other instanceof RageEp;
	}
	@Override
	public boolean equals(Object other) {
		return super.equals(other) &&
			other instanceof RageEp &&
			((RageEp) other).canEquals(this);
	}
	@Override public int hashCode() { return "RageEp".hashCode(); }
	@Override public String toString() { return "RageEp"; }
	@Override public String getName() { return "Rage"; }
}
