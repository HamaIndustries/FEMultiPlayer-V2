package net.fe.fightStage;

import net.fe.rng.RNG;
import net.fe.unit.Unit;

/**
 * 
 * 
 * Effect: Triple damage by spending 3 skill charge. <br/>
 * Trigger: enemy phase and skillCharge > 3
 */
public final class RageEp extends CombatTrigger {
	private static final long serialVersionUID = 1L;
	
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
		return !initiator && user.getSkillCharge() >= 3;
	}

	@Override
	public int runDamageMod(Unit a, Unit d, int damage){
		return damage * 3;
	}
	
	@Override
	public int runYourTurnSkillSpend() {
		return 3;
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
