package net.fe.network.message;

import net.fe.network.Message;

// TODO: Auto-generated Javadoc
/**
 * The Class ReadyMessage.
 */
public class ReadyMessage extends Message {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2430536015559627885L;

	/**
	 * Instantiates a new ready message.
	 *
	 * @param origin the origin
	 */
	public ReadyMessage(byte origin) {
		super(origin);
	}

	/**
	 * Instantiates a new ready message.
	 */
	public ReadyMessage() {
		super();
	}

}
