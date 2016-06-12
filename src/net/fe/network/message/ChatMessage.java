package net.fe.network.message;

import net.fe.network.Message;

// TODO: Auto-generated Javadoc
/**
 * The Class ChatMessage.
 */
public final class ChatMessage extends Message {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -386437094794678483L;
	
	/** The text. */
	public String text;
	
	/**
	 * Instantiates a new chat message.
	 *
	 * @param origin the origin
	 * @param text the text
	 */
	public ChatMessage(byte origin, String text) {
		super(origin);
		this.text = text;
	}
	
	/* (non-Javadoc)
	 * @see net.fe.network.Message#toString()
	 */
	public String toString() {
		return super.toString() + text;
	}
	
}
