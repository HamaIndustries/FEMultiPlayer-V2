package net.fe.network.message;

import net.fe.network.Message;

// TODO: Auto-generated Javadoc
/**
 * A message sent when a client leaves the server
 */
public final class QuitMessage extends Message {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 6558427589809693714L;

	/**
	 * Instantiates a new quit message.
	 *
	 * @param origin the leaving client
	 */
	public QuitMessage(byte origin) {
		super(origin);
	}

}
