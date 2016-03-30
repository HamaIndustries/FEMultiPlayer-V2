package net.fe.fightStage;

import net.fe.RNG;
import net.fe.unit.Unit;

public class Thievery extends CombatTrigger {

	private static final long serialVersionUID = 74326186321871243L;
	
	public Thievery(){
		super(NO_NAME_MOD, YOUR_TURN_MOD);
	}
	
	@Override
	public boolean attempt(Unit user, int range) {
		//return true;
		return RNG.get() < user.get("Skl")*2 && user.getStole();
	}

	@Override
	public int runDamageMod(Unit a, Unit d, int damage) {
		return 1;
	}
	
	@Override
	public CombatTrigger getCopy() {
		return new Thievery();
	}
	
	@Override
	public boolean equals(Object o){
		return o instanceof Thievery;
	}
	public int hashCode() { return (int)serialVersionUID; }

}
