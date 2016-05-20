package net.fe.fightStage;

import net.fe.unit.Unit;

// TODO: Auto-generated Javadoc
/**
 * The Class Brave.
 */
public class Brave extends CombatTrigger{
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -2833222257001214176L;

	/**
	 * Instantiates a new brave.
	 */
	public Brave(){
		super(NO_NAME_MOD, YOUR_TURN_POST);
	}

	/* (non-Javadoc)
	 * @see net.fe.fightStage.CombatTrigger#attempt(net.fe.unit.Unit, int)
	 */
	@Override
	public boolean attempt(Unit user, int range, Unit opponent) {
		return true;
	}

	/* (non-Javadoc)
	 * @see net.fe.fightStage.CombatTrigger#runPostAttack(net.fe.fightStage.CombatCalculator, boolean, net.fe.unit.Unit, net.fe.unit.Unit, int, java.lang.String)
	 */
	@Override
	public void runPostAttack(CombatCalculator calc, boolean dir, Unit a, Unit d,
			int damage, String currentEffect) {
		if(d.getHp() > 0 && currentEffect.equals("None")){
			calc.addAttack("Brave");
		}
	}
	
	/* (non-Javadoc)
	 * @see net.fe.fightStage.CombatTrigger#getCopy()
	 */
	public CombatTrigger getCopy(){
		return new Brave();
	}
}
