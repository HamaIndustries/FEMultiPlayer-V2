package net.fe.fightStage;

import net.fe.RNG;
import net.fe.unit.Unit;

// TODO: Auto-generated Javadoc
/**
 * The Class Luna.
 */
public final class Luna extends CombatTrigger {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -6539654970701395612L;
	
	/** The ranged. */
	private final boolean ranged;
	
	/**
	 * Instantiates a new luna.
	 *
	 *Info: Halves enemy Res/Def.
	 *
	 *Chance: Skl/2
	 *
	 * @param rangeok the rangeok
	 */
	public Luna(boolean rangeok){
		super(REPLACE_NAME_AFTER_PRE, YOUR_TURN_PRE);
		ranged = rangeok;
	}
	
	/* (non-Javadoc)
	 * @see net.fe.fightStage.CombatTrigger#attempt(net.fe.unit.Unit, int)
	 */
	@Override
	public boolean attempt(Unit user, int range, Unit opponent) {

		return (ranged || range == 1) && RNG.get() < user.get("Skl")/2;
	}

	/* (non-Javadoc)
	 * @see net.fe.fightStage.CombatTrigger#runPreAttack(net.fe.fightStage.CombatCalculator, net.fe.unit.Unit, net.fe.unit.Unit)
	 */
	@Override
	public boolean runPreAttack(CombatCalculator stage, Unit a, Unit d) {
		d.setTempMod("Def", -d.get("Def")/2);
		d.setTempMod("Res", -d.get("Res")/2);
		return true;
	}
	
	/* (non-Javadoc)
	 * @see net.fe.fightStage.CombatTrigger#getCopy()
	 */
	public CombatTrigger getCopy(){
		return new Luna(ranged);
	}

}
