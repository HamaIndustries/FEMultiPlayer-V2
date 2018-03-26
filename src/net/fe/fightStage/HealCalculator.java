package net.fe.fightStage;

import java.util.function.Function;

import net.fe.rng.SimpleRNG;
import net.fe.unit.Unit;
import net.fe.unit.UnitIdentifier;

// TODO: Auto-generated Javadoc
/**
 * The Class HealCalculator.
 */
public class HealCalculator extends CombatCalculator {

	/**
	 * Instantiates a new heal calculator.
	 *
	 * @param u1 the unit id of fighter 1
	 * @param u2 the unit id of fighter 2
	 * @param dereference A function that converts a UnitIdentifier into a Unit
	 */
	public HealCalculator(UnitIdentifier u1, UnitIdentifier u2, Function<UnitIdentifier, Unit> dereference) {
		super(u1, u2, dereference, new SimpleRNG(), new SimpleRNG(), new SimpleRNG());
	}
	
	/* (non-Javadoc)
	 * @see net.fe.fightStage.CombatCalculator#calculate()
	 */
	protected void calculate(){
		final int heal = Math.min(left.getStats().mag + left.getWeapon().mt, 
				right.getStats().maxHp - right.getHp());
		System.out.println("Heal: "+heal+" (Max:"+right.getStats().maxHp+" Curr:"+right.getHp()+")");
		
		left.use(left.getWeapon());
		right.setHp(right.getHp() + heal);
		addToAttackQueue(left, right, "Heal", -heal, 0, 0);
	}
	
}
