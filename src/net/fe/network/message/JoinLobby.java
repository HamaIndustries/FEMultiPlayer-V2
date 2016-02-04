package net.fe.network.message;

import net.fe.Player;
import net.fe.network.Message;

// TODO: Auto-generated Javadoc
/**
 * The Class JoinLobby.
 */
public class JoinLobby extends Message {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2466162881020245626L;
	
	/** The player. */
	public Player player;
	
	/**
	 * Instantiates a new join lobby.
	 *
	 * @param origin the origin
	 * @param player the player
	 */
	public JoinLobby(byte origin, Player player) {
		super(origin);
		this.player = player;
	}
	
	/* (non-Javadoc)
	 * @see net.fe.network.Message#toString()
	 */
	public String toString() {
		return super.toString()+player;
	}

}
