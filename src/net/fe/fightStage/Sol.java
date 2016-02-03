package net.fe.fightStage;

import net.fe.RNG;
import net.fe.unit.Unit;

// TODO: Auto-generated Javadoc
/**
 * The Class Sol.
 */
public class Sol extends CombatTrigger {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 4213665653533259538L;
	
	/** The ranged. */
	private transient boolean ranged;
	
	/**
	 * Instantiates a new sol.
	 *
	 * @param rangeok the rangeok
	 */
	public Sol(boolean rangeok) {
		super(REPLACE_NAME_AFTER_PRE, YOUR_TURN_PRE + YOUR_TURN_DRAIN);
		ranged = rangeok;
	}

	/* (non-Javadoc)
	 * @see net.fe.fightStage.CombatTrigger#attempt(net.fe.unit.Unit, int)
	 */
	@Override
	public boolean attempt(Unit user, int range) {
		return (ranged || range == 1) && RNG.get() < user.get("Skl");
	}
	
	/* (non-Javadoc)
	 * @see net.fe.fightStage.CombatTrigger#runDrain(net.fe.unit.Unit, net.fe.unit.Unit, int)
	 */
	public int runDrain(Unit a, Unit d, int damage){
		if (damage == 0)
			return 0;
		return Math.min(damage / 2, a.get("HP") - a.getHp());
	}
	
	/* (non-Javadoc)
	 * @see net.fe.fightStage.CombatTrigger#getCopy()
	 */
	public CombatTrigger getCopy(){
		return new Sol(ranged);
	}
}
