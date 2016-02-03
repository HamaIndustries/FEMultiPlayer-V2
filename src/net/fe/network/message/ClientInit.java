package net.fe.network.message;

import net.fe.Session;
import net.fe.network.Message;

// TODO: Auto-generated Javadoc
/**
 * The Class ClientInit.
 */
public class ClientInit extends Message {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2559169995718261494L;
	
	/** The client id. */
	public byte clientID;
	
	/** The session. */
	public Session session;	// Session data
	
	/**
	 * Instantiates a new client init.
	 *
	 * @param origin the origin
	 * @param clientID the client id
	 * @param s the s
	 */
	public ClientInit(byte origin, byte clientID, Session s) {
		super(origin);
		this.clientID = clientID;
		this.session = s;
	}
	
	/* (non-Javadoc)
	 * @see net.fe.network.Message#toString()
	 */
	public String toString(){
		return super.toString() + "clientID = " + clientID;
	}
}
