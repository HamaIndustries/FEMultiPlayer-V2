package net.fe.fightStage;

import net.fe.rng.RNG;
import net.fe.unit.Unit;

/** Skill that, on a successful hit, will reduce enemy hp to one. */
public final class EclipseSix extends CombatTrigger {
	private static final long serialVersionUID = -6539654970701395612L;
	public EclipseSix(){
		super(NO_NAME_MOD, YOUR_TURN_MOD | SHOW_IN_PREVIEW);
	}
	@Override
	public boolean attempt(Unit user, boolean initiator, int range, Unit opponent, RNG rng) {
		return true;
	}

	@Override
	public int runDamageMod(Unit a, Unit d, int damage) {
		int currentHp = d.getHp();
		if (currentHp <= 1) {
			return 1;
		} else {
			return currentHp - 1;
		}
	}
	
	public String getName() {
		return "Eclipse";
	}
	
	public CombatTrigger getCopy(){
		return new EclipseSix();
	}
	
	@Override
	public boolean equals(Object o){
		return o instanceof EclipseSix;
	}
	public int hashCode() { return (int)serialVersionUID; }
}
