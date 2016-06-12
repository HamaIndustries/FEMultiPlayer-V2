package net.fe.network.message;

import net.fe.network.Message;

// TODO: Auto-generated Javadoc
/**
 * The Class JoinTeam.
 */
public final class JoinTeam extends Message {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -2805120675582622842L;
	
	/** The team. */
	public final int team;
	
	/**
	 * Instantiates a new join team.
	 *
	 * @param origin the origin
	 * @param team the team
	 */
	public JoinTeam(byte origin, int team) {
		super(origin);
		this.team = team;
	}
	
	/* (non-Javadoc)
	 * @see net.fe.network.Message#toString()
	 */
	public String toString() {
		return super.toString()+team;
	}
}
