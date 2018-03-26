package net.fe.fightStage;

import net.fe.rng.RNG;
import net.fe.unit.Unit;

/**
 * 
 * 
 * Effect: Triple damage by spending 3 skill charge. <br/>
 * Trigger: skillCharge > 3
 */
public final class Rage extends CombatTrigger {
	private static final long serialVersionUID = 1L;
	
	/**
	 */
	public Rage(){
		super(APPEND_NAME_AFTER_MOD, YOUR_TURN_MOD);
	}
	
	@Override
	public boolean attempt(Unit user, int range, Unit opponent, RNG rng) {
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
		return new Rage();
	}
	
	protected boolean canEquals(Object other) {
		return other instanceof Rage;
	}
	@Override
	public boolean equals(Object other) {
		return super.equals(other) &&
			other instanceof Rage &&
			((Rage) other).canEquals(this);
	}
	@Override public int hashCode() { return "Rage".hashCode(); }
	@Override public String toString() { return "Rage[]"; }
}
