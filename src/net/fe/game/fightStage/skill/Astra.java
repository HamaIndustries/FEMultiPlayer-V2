package net.fe.game.fightStage.skill;

import net.fe.RNG;
import net.fe.game.fightStage.CombatCalculator;
import net.fe.game.fightStage.CombatTrigger;
import net.fe.game.unit.Unit;

// TODO: Auto-generated Javadoc
/**
 * The Class Astra.
 */
public class Astra extends CombatTrigger {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2363437411973357900L;

	/** The counter. */
	private transient int counter;

	/**
	 * Instantiates a new astra.
	 * 
	 * Info: Makes the user attack 5 times at half damage.
	 * 
	 * Chance: Skl/2
	 * 
	 */
	public Astra() {
		super(REPLACE_NAME_AFTER_PRE, YOUR_TURN_PRE + YOUR_TURN_MOD + YOUR_TURN_POST, "astra1", "astra2", "astra3",
		        "astra4", "astra5");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.fe.fightStage.CombatTrigger#attempt(net.fe.unit.Unit, int)
	 */
	@Override
	public boolean attempt(Unit user, int range, Unit opponent) {
		// return true;
		return range == 1 && (RNG.get() < user.getStats().skl / 2 || counter != 0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.fe.fightStage.CombatTrigger#runDamageMod(net.fe.unit.Unit,
	 * net.fe.unit.Unit, int)
	 */
	@Override
	public int runDamageMod(Unit a, Unit d, int damage) {
		return damage / 2;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.fe.fightStage.CombatTrigger#runPostAttack(net.fe.fightStage.
	 * CombatCalculator, boolean, net.fe.unit.Unit, net.fe.unit.Unit, int,
	 * java.lang.String)
	 */
	@Override
	public void runPostAttack(CombatCalculator calc, boolean dir, Unit a, Unit d, int damage, String currentEffect) {
		if (counter == 4) {
			// its the last hit
			counter = 0;
		} else {
			counter++;
			if (d.getHp() > 0) {
				calc.addAttack("Astra");
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.fe.fightStage.CombatTrigger#getName()
	 */
	@Override
	public String getName() {
		return super.getName() + (counter + 1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.fe.fightStage.CombatTrigger#getCopy()
	 */
	public CombatTrigger getCopy() {
		return new Astra();
	}
}
