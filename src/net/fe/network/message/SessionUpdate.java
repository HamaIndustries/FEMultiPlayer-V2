package net.fe.network.message;

import net.fe.Session;
import net.fe.network.Message;

// TODO: Auto-generated Javadoc
/**
 * The Class SessionUpdate.
 */
public class SessionUpdate extends Message {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1593840263817973024L;
	
	/** The session. */
	public Session session;
	
	/**
	 * Instantiates a new session update.
	 *
	 * @param s the s
	 */
	public SessionUpdate(Session s) {
		session = s;
	}

}
