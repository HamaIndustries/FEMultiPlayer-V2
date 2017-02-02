package net.fe.game.fightStage.skill;

import net.fe.game.fightStage.CombatTrigger;
import net.fe.game.unit.Unit;

/** Skill that, on a successful hit, will reduce enemy hp to one. */
public final class EclipseSixPlus extends CombatTrigger {
	private static final long serialVersionUID = -6539654970701395612L;

	public EclipseSixPlus() {
		super(NO_NAME_MOD, YOUR_TURN_MOD | SHOW_IN_PREVIEW);
	}

	@Override
	public boolean attempt(Unit user, int range, Unit opponent) {
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

	public CombatTrigger getCopy() {
		return new EclipseSixPlus();
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof EclipseSixPlus;
	}

	public int hashCode() {
		return (int) serialVersionUID;
	}
}
