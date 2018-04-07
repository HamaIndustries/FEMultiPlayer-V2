package net.fe.fightStage;

import java.io.Serializable;

import net.fe.unit.UnitIdentifier;

// TODO: Auto-generated Javadoc
/**
 * The Class AttackRecord.
 */
public class AttackRecord implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -203200690242377586L;
	
	/** The animation. */
	public String animation;
	
	/** The defender. */
	public UnitIdentifier attacker, defender;
	
	/** The damage. */
	public int damage;
	
	/** The drain. */
	public int drain;
	
	/** The skillCharge gained by the defender */
	public int defenderSkillCharge;
	
	/** The skillCharge gained by the attacker */
	public int attackerSkillCharge;
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return animation + ": " + attacker.name + ", " + defender.name + ", "
				+ damage + ", " + drain + " (drain), "
				+ defenderSkillCharge + " (defenderSkillCharge), "
				+ attackerSkillCharge + " (attackerSkillCharge), "
				;
	}
}