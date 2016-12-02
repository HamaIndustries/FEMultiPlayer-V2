package net.fe.game.fightStage;

import java.io.Serializable;

import net.fe.game.unit.UnitIdentifier;

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
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return animation + ": " + attacker.name + ", " + defender.name + ", "
				+ damage + ", " + drain + " (drain)";
	}
}