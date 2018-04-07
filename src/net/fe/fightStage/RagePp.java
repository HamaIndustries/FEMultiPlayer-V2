package net.fe.fightStage;

import net.fe.rng.RNG;
import net.fe.unit.Unit;

/**
 * 
 * 
 * Effect: Triple damage by spending 3 skill charge. <br/>
 * Trigger: manual selection and skillCharge >= 3
 */
public final class RagePp extends CombatTrigger {
	private static final long serialVersionUID = 1L;
	
	/**
	 */
	public RagePp(){
		// APPEND_NAME_AFTER_MOD doesn't append the name if runDamageMod
		// doesn't change the damage, say from 0 to 0. Hence the need to use
		// REPLACE_NAME_AFTER_PRE, and REPLACE_... is the only reason to have
		// YOUR_TURN_PRE
		super(REPLACE_NAME_AFTER_PRE, YOUR_TURN_PRE | YOUR_TURN_MOD);
	}
	
	@Override
	public boolean attempt(Unit user, boolean initiator, int range, Unit opponent, RNG rng) {
		return user.getSkillCharge() >= 3;
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
		return new RagePp();
	}
	
	protected boolean canEquals(Object other) {
		return other instanceof RagePp;
	}
	@Override
	public boolean equals(Object other) {
		return super.equals(other) &&
			other instanceof RagePp &&
			((RagePp) other).canEquals(this);
	}
	@Override public int hashCode() { return "RagePp".hashCode(); }
	@Override public String toString() { return "RagePp"; }
	@Override public String getName() { return "Rage"; }
}