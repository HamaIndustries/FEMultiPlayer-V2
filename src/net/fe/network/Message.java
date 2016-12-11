package net.fe.network;

import java.io.Serializable;

public abstract class Message implements Serializable {

	private static final long serialVersionUID = 8838417404744137405L;

	public int origin;
	
	public Message() {

	}

	public Message(int origin) {
		this.origin = origin;
	}

	public String toString() {
		return origin + " " + getClass().getSimpleName().toUpperCase() + "::";
	}
	
}
