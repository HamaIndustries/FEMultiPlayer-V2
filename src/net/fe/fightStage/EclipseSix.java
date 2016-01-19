package net.fe.fightStage;

import net.fe.RNG;
import net.fe.unit.Unit;

/** Skill that, on a successful hit, will reduce enemy hp to one. */
public class EclipseSix extends CombatTrigger {
	private static final long serialVersionUID = -6539654970701395612L;
	public EclipseSix(){
		super(REPLACE_NAME_AFTER_PRE, YOUR_TURN_MOD);
	}
	@Override
	public boolean attempt(Unit user, int range) {
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
	
	public CombatTrigger getCopy(){
		return new EclipseSix();
	}

}
