package net.fe.fightStage;

import net.fe.unit.UnitIdentifier;

// TODO: Auto-generated Javadoc
/**
 * The Class HealCalculator.
 */
public class HealCalculator extends CombatCalculator {

	/**
	 * Instantiates a new heal calculator.
	 *
	 * @param u1 the u1
	 * @param u2 the u2
	 * @param local the local
	 */
	public HealCalculator(UnitIdentifier u1, UnitIdentifier u2, boolean local) {
		super(u1, u2, local);
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
		addToAttackQueue(left, right, "Heal", -heal, 0);
	}
	
}
