package net.fe.fightStage;

import net.fe.unit.UnitIdentifier;

public class SingCmd extends CombatCalculator {
	public SingCmd(UnitIdentifier u1, UnitIdentifier u2, boolean local) {
		super(u1, u2, local);
	}
	protected void calculate(){
		right.setMoved(false);
		addToAttackQueue(left, right, "Sing", 0, 0);
	}
}
