package net.fe.fightStage;

import net.fe.rng.RNG;
import net.fe.unit.Unit;

// TODO: Auto-generated Javadoc
/**
 * The Class Pavise.
 */
public class Pavise extends CombatTrigger {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -5629273200054077795L;

	/**
	 * Instantiates a new pavise.
	 * 
	 * Info: Halves damage taken.
	 * 
	 * Chance: Skl
	 */
	public Pavise() {
		super(APPEND_NAME_AFTER_MOD, ENEMY_TURN_MOD);
	}

	/* (non-Javadoc)
	 * @see net.fe.fightStage.CombatTrigger#attempt(net.fe.unit.Unit, int)
	 */
	@Override
	public boolean attempt(Unit user, boolean initiator, int range, Unit opponent, RNG rng) {
		//return true;
		return rng.test(user.getStats().skl);
	}
	
	/* (non-Javadoc)
	 * @see net.fe.fightStage.CombatTrigger#runDamageMod(net.fe.unit.Unit, net.fe.unit.Unit, int)
	 */
	@Override
	public int runDamageMod(Unit a, Unit d, int dmg){
		return dmg/2;
	}
	
	/* (non-Javadoc)
	 * @see net.fe.fightStage.CombatTrigger#getCopy()
	 */
	public CombatTrigger getCopy(){
		return new Pavise();
	}

}
