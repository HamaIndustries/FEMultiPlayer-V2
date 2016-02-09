package net.fe.network.message;

import net.fe.network.Message;

// TODO: Auto-generated Javadoc
/**
 * The Class QuitMessage.
 */
public class QuitMessage extends Message {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 6558427589809693714L;
	
	/**
	 * Instantiates a new quit message.
	 *
	 * @param origin the origin
	 */
	public QuitMessage(byte origin) {
		super(origin);
	}

}
