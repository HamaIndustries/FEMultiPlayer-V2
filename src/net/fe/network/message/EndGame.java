package net.fe.network.message;

import net.fe.network.Message;

// TODO: Auto-generated Javadoc
/**
 * The Class EndGame.
 */
public class EndGame extends Message {

	/** The winner. */
	public int winner;
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 654326008504474145L;
	
	/**
	 * Instantiates a new end game.
	 *
	 * @param origin the origin
	 * @param winner the winner
	 */
	public EndGame(byte origin, int winner) {
		super(origin);
		this.winner = winner;
	}
	
	/* (non-Javadoc)
	 * @see net.fe.network.Message#toString()
	 */
	public String toString(){
		return "ENDGAME:: winner = " + winner;
	}

}
