package net.fe.fightStage;

import java.io.Serializable;

import net.fe.unit.UnitIdentifier;

// TODO: Auto-generated Javadoc
/**
 * The Class AttackRecord.
 */
public class AttackRecord implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2227786706936956528L;
	
	/** The animation. */
	public String animation;
	
	/** The defender. */
	public UnitIdentifier attacker, defender;
	
	/** The damage. */
	public int damage;
	
	/** The drain. */
	public int drain;
	
	/** The skillCharge gained by the defender */
	public int skillCharge;
	
	/** The skillCharge used by the attacker */
	public int skillSpend;
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return animation + ": " + attacker.name + ", " + defender.name + ", "
				+ damage + ", " + drain + " (drain), "
				+ skillSpend + " (skillSpend), " + skillCharge + " (skillCharge), ";
	}
}