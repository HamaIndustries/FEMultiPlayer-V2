package net.fe.fightStage;

import net.fe.unit.UnitIdentifier;

public class ActionCalculator extends CombatCalculator {
	public ActionCalculator(UnitIdentifier u1, UnitIdentifier u2, boolean local) {
		super(u1, u2, local);
	}
	
	@Override
	protected void calculate(){
		left.setStole(true);
		attack(true, "None");
	}
}
