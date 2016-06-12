package net.fe.fightStage;

import net.fe.RNG;
import net.fe.unit.Unit;

// TODO: Auto-generated Javadoc
/**
 * The Class Colossus.
 * 
 * Info: Doubles the user's Str.
 * 
 * Chance: Skl%
 */
public class Colossus extends CombatTrigger{
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -155383449100454663L;

	/**
	 * Instantiates a new colossus.
	 */
	public Colossus(){
		super(REPLACE_NAME_AFTER_PRE, YOUR_TURN_PRE);
	}
	
	/* (non-Javadoc)
	 * @see net.fe.fightStage.CombatTrigger#attempt(net.fe.unit.Unit, int)
	 */
	@Override
	public boolean attempt(Unit user, int range, Unit opponent) {
		return range == 1 && RNG.get() < user.getStats().skl/2;
	}

	/* (non-Javadoc)
	 * @see net.fe.fightStage.CombatTrigger#runPreAttack(net.fe.fightStage.CombatCalculator, net.fe.unit.Unit, net.fe.unit.Unit)
	 */
	@Override
	public boolean runPreAttack(CombatCalculator stage, Unit a, Unit d) {
		a.setTempMod("Str", a.getStats().str);
		return true;
	}
	
	/* (non-Javadoc)
	 * @see net.fe.fightStage.CombatTrigger#getCopy()
	 */
	public CombatTrigger getCopy(){
		return new Colossus();
	}
}
