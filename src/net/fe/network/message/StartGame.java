package net.fe.network.message;

import net.fe.network.Message;

// TODO: Auto-generated Javadoc
/**
 * The Class StartGame.
 */
public class StartGame extends Message {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 996855764072238544L;
	
	/**
	 * Instantiates a new start game.
	 *
	 * @param origin the origin
	 */
	public StartGame(byte origin) {
		super(origin);
	}
}
