package net.fe.game.fightStage.weaponEffect;

import net.fe.RNG;
import net.fe.game.fightStage.CombatCalculator;
import net.fe.game.fightStage.CombatTrigger;
import net.fe.game.unit.Unit;

/** A skill that always negates enemy defenses */
public final class LunaPlus extends CombatTrigger {
	private static final long serialVersionUID = -6539654970701395612L;

	public LunaPlus() {
		super(NO_NAME_MOD, YOUR_TURN_PRE | SHOW_IN_PREVIEW);
	}

	@Override
	public boolean attempt(Unit user, int range, Unit opponent) {
		return true;
	}

	@Override
	public boolean runPreAttack(CombatCalculator stage, Unit a, Unit d) {
		d.setTempMod("Def", -d.getStats().def);
		d.setTempMod("Res", -d.getStats().res);
		return true;
	}

	public String getName() {
		return "LunaPlus";
	}

	public CombatTrigger getCopy() {
		return new LunaPlus();
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof LunaPlus;
	}

	public int hashCode() {
		return (int) serialVersionUID;
	}
}
