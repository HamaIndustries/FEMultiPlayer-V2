package net.fe.fightStage;

import net.fe.unit.Unit;

public class Disarm extends CombatTrigger {

	private static final long serialVersionUID = -1553324449100454663L;
	
	public final String[] attackAnims = {"steal"};
	
	public Disarm(){
		super(REPLACE_NAME_AFTER_PRE, YOUR_TURN_PRE, "disarm");
	}
	
	@Override
	public boolean attempt(Unit user, int range) {
		return false;
	}

	@Override
	public CombatTrigger getCopy() {
		return new Disarm();
	}

}
