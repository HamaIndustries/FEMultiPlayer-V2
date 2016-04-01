package net.fe.fightStage;

import net.fe.unit.UnitIdentifier;

public class ActionCmd extends CombatCalculator {
	private String cmd = "Sing";
	public ActionCmd(UnitIdentifier u1, UnitIdentifier u2, boolean local) {
		super(u1, u2, local);
	}
	public ActionCmd(UnitIdentifier u1, UnitIdentifier u2, boolean local, String comm) {
		super(u1, u2, local);
		this.cmd = comm;
	}
	protected void calculate(){
		addToAttackQueue(left, right, cmd, 0, 0);
		/*
		System.out.println("+++++++++++");
		for(AttackRecord i : getAttackQueue())
			System.out.println(i+" ::::: "+i.animation+" "+i.damage+" "+i.drain+"  <----------           (anim info)");*/
	}
}
