package net.fe.fightStage;

import net.fe.rng.RNG;
import net.fe.unit.Unit;

// TODO: Auto-generated Javadoc
/**
 * The Class Miracle.
 */
public class Miracle extends CombatTrigger {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -8613896121666026506L;

	/**
	 * Instantiates a new miracle.
	 * 
	 * Info: If damage dealt would be lethal, user's Hp becomes 1 instead.
	 * Will not proc if Hp = 1.
	 * 
	 * Chance: Lck
	 */
	public Miracle() {
		super(APPEND_NAME_AFTER_MOD, ENEMY_TURN_MOD);
	}

	/* (non-Javadoc)
	 * @see net.fe.fightStage.CombatTrigger#attempt(net.fe.unit.Unit, int)
	 */
	@Override
	public boolean attempt(Unit user, int range, Unit opponent, RNG rng) {
		//return true;
		return rng.test(user.getStats().lck) && user.getHp() != 1;
	}
	
	/* (non-Javadoc)
	 * @see net.fe.fightStage.CombatTrigger#runDamageMod(net.fe.unit.Unit, net.fe.unit.Unit, int)
	 */
	@Override
	public int runDamageMod(Unit a, Unit d, int damage){
		if(d.getHp() - damage <= 0){
			return d.getHp() - 1;
		} else {
			return damage;
		}
	}
	
	/* (non-Javadoc)
	 * @see net.fe.fightStage.CombatTrigger#getCopy()
	 */
	public CombatTrigger getCopy(){
		return new Miracle();
	}

}
