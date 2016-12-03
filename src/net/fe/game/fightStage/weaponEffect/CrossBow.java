package net.fe.game.fightStage.weaponEffect;

import net.fe.RNG;
import net.fe.game.fightStage.CombatCalculator;
import net.fe.game.fightStage.CombatTrigger;
import net.fe.game.unit.Unit;

/** A skill that always negates the users str and mag */
public final class CrossBow extends CombatTrigger {
	private static final long serialVersionUID = -6539654970701395612L;

	public CrossBow() {
		super(NO_NAME_MOD, YOUR_TURN_PRE | SHOW_IN_PREVIEW);
	}

	@Override
	public boolean attempt(Unit user, int range, Unit other) {
		return true;
	}

	@Override
	public boolean runPreAttack(CombatCalculator stage, Unit a, Unit d) {
		a.setTempMod("Str", -a.getStats().str);
		a.setTempMod("Mag", -a.getStats().mag);
		return true;
	}

	public String getName() {
		return "Crossbow";
	}

	public CombatTrigger getCopy() {
		return new CrossBow();
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof CrossBow;
	}

	public int hashCode() {
		return (int) serialVersionUID;
	}
}
