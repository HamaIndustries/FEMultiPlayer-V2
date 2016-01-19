package net.fe.fightStage;

import net.fe.RNG;
import net.fe.unit.Unit;

/** A skill that always negates enemy defenses */
public class LunaPlus extends CombatTrigger {
	private static final long serialVersionUID = -6539654970701395612L;
	public LunaPlus(){
		super(REPLACE_NAME_AFTER_PRE, YOUR_TURN_PRE);
	}
	@Override
	public boolean attempt(Unit user, int range) {
		return true;
	}

	@Override
	public boolean runPreAttack(CombatCalculator stage, Unit a, Unit d) {
		d.setTempMod("Def", -d.get("Def"));
		d.setTempMod("Res", -d.get("Res"));
		return true;
	}
	
	public CombatTrigger getCopy(){
		return new LunaPlus();
	}

}
