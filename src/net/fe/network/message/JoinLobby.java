package net.fe.network.message;

import net.fe.network.Message;

// TODO: Auto-generated Javadoc
/**
 * The Class JoinLobby.
 */
public final class JoinLobby extends Message {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 4749147769637678401L;

	/** The name to display when refrencing this player */
	public final String nickname;

	/**
	 * Instantiates a new join lobby.
	 *
	 * @param origin the origin
	 * @param player the player
	 */
	public JoinLobby(byte origin, String nickname) {
		super(origin);
		this.nickname = nickname;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.fe.network.Message#toString()
	 */
	public String toString() {
		return super.toString() + nickname;
	}

}
