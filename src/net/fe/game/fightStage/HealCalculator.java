package net.fe.game.fightStage;

import java.util.function.Function;

import net.fe.game.unit.Unit;
import net.fe.game.unit.UnitIdentifier;

public class HealCalculator extends CombatCalculator {

	public HealCalculator(UnitIdentifier u1, UnitIdentifier u2, Function<UnitIdentifier, Unit> dereference) {
		super(u1, u2, dereference);
	}

	protected void calculate() {
		final int heal = Math.min(left.getStats().mag + left.getWeapon().mt, right.getStats().maxHp - right.getHp());
		System.out.println("Heal: " + heal + " (Max:" + right.getStats().maxHp + " Curr:" + right.getHp() + ")");

		left.use(left.getWeapon());
		right.setHp(right.getHp() + heal);
		addToAttackQueue(left, right, "Heal", -heal, 0);
	}

}
