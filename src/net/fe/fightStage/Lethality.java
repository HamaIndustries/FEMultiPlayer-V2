package net.fe.fightStage;

import net.fe.RNG;
import net.fe.unit.Unit;

// TODO: Auto-generated Javadoc
/**
 * The Class Lethality.
 */
public class Lethality extends CombatTrigger {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -7524008345607446540L;

	/**
	 * Instantiates a new lethality.
	 */
	public Lethality(){
		super(REPLACE_NAME_AFTER_PRE, YOUR_TURN_PRE);
	}
	
	/* (non-Javadoc)
	 * @see net.fe.fightStage.CombatTrigger#attempt(net.fe.unit.Unit, int)
	 */
	@Override
	public boolean attempt(Unit user, int range, Unit opponent) {
		return (!opponent.getTheClass().name.equals("Lord")) &&
				RNG.get() < user.get("Skl") / 3;
	}

	/* (non-Javadoc)
	 * @see net.fe.fightStage.CombatTrigger#runPreAttack(net.fe.fightStage.CombatCalculator, net.fe.unit.Unit, net.fe.unit.Unit)
	 */
	@Override
	public boolean runPreAttack(CombatCalculator stage, Unit a, Unit d) {
		a.setTempMod("Str", 9000);
		a.setTempMod("Hit", 9000);
		return true;
	}
	
	/* (non-Javadoc)
	 * @see net.fe.fightStage.CombatTrigger#getCopy()
	 */
	public CombatTrigger getCopy(){
		return new Lethality();
	}

}
