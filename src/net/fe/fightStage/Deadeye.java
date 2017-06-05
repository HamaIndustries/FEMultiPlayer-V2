package net.fe.fightStage;

import net.fe.RNG;
import net.fe.unit.Unit;

// TODO: Auto-generated Javadoc
/**
 * The Class Deadeye.
 */
public class Deadeye extends CombatTrigger {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 5208500314150806972L;
	
	/**
	 * Instantiates a new deadeye.
	 * 
	 * Info: User's Hit/Crit become 9000.
	 * 
	 * Chance: Skl
	 */
	public Deadeye(){
		super(REPLACE_NAME_AFTER_PRE, YOUR_TURN_PRE, "deadeye");
	}
	
	/* (non-Javadoc)
	 * @see net.fe.fightStage.CombatTrigger#runPreAttack(net.fe.fightStage.CombatCalculator, net.fe.unit.Unit, net.fe.unit.Unit)
	 */
	@Override
	public boolean runPreAttack(CombatCalculator stage, Unit a, Unit d) {
		a.setTempMod("Crit", 9000);
		a.setTempMod("Hit", 9000);
		return true;
	}
	
	/* (non-Javadoc)
	 * @see net.fe.fightStage.CombatTrigger#attempt(net.fe.unit.Unit, int)
	 */
	@Override
	public boolean attempt(Unit user, int range, Unit opponent) {
		return RNG.get() < user.getStats().skl;
	}

	/* (non-Javadoc)
	 * @see net.fe.fightStage.CombatTrigger#getCopy()
	 */
	public CombatTrigger getCopy(){
		return new Deadeye();
	}
}
