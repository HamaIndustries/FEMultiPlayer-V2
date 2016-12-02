package net.fe.network.message;

import net.fe.network.Message;

// TODO: Auto-generated Javadoc
/**
 * The Class DraftMessage.
 */
public class DraftMessage extends Message {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -2354614661871074893L;

	/** The unit names. */
	public String[] unitNames;

	/**
	 * Instantiates a new draft message.
	 *
	 * @param unitNames the unit names
	 */
	public DraftMessage(String[] unitNames) {
		super();
		this.unitNames = unitNames;
	}
}
