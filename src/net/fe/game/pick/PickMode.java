package net.fe.game.pick;

import java.io.Serializable;

import net.fe.game.Session;

// TODO: Auto-generated Javadoc
/**
 * Interface implementing a unit pick mode. Sets up the client and server
 * stages.
 * 
 * @author Shawn
 *
 */
public interface PickMode extends Serializable {

	/**
	 * Sets the up client.
	 *
	 * @param session the new up client
	 */
	public void setUpClient(Session session);

	/**
	 * Sets the up server.
	 *
	 * @param session the new up server
	 */
	public void setUpServer(Session session);
}
