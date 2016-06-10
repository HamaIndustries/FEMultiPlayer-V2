package net.fe.network.message;

import net.fe.network.Message;

/**
 * A message sent when the server disconnects a player
 */
public final class KickMessage extends Message {
	private static final long serialVersionUID = 6558427589809693714L;
	
	/** The client that is being told to leave */
	public final byte player;
	/** The reason for the kick */
	public final String reason;
	
	/**
	 * Instantiates a new KickMessage.
	 *
	 * @param origin the thing initiating the kick.
	 * @param player the leaving client
	 * @param reason the reason
	 */
	public KickMessage(byte origin, byte player, String reason) {
		super(origin);
		this.player = player;
		this.reason = reason;
	}
}
