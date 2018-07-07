package net.fe.network;

import java.io.Serializable;

// TODO: Auto-generated Javadoc
/**
 * The Class Message.
 */
public abstract class Message implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 8838417404744137405L;
	
	/** The origin. */
	public byte origin;
	private long timestamp;
	
	/**
	 * Instantiates a new message.
	 */
	public Message() {
		this((byte) 0);
	}
	
	/**
	 * Instantiates a new message.
	 *
	 * @param origin the origin
	 */
	public Message(byte origin) {
		this.origin = origin;
		timestamp = System.currentTimeMillis();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		String classname = getClass().getSimpleName().toUpperCase();
		classname.replaceAll("MESSAGE", "");
		return origin + " " + getClass().getSimpleName().toUpperCase() + "::";
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
	public long getTimestamp() {
		return timestamp;
	}
}
