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

	/**
	 * Instantiates a new message.
	 */
	public Message() {

	}

	/**
	 * Instantiates a new message.
	 *
	 * @param origin the origin
	 */
	public Message(byte origin) {
		this.origin = origin;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String classname = getClass().getSimpleName().toUpperCase();
		classname.replaceAll("MESSAGE", "");
		return origin + " " + getClass().getSimpleName().toUpperCase() + "::";
	}
}
