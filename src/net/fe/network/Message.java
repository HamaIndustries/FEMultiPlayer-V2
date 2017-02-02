package net.fe.network;

import java.io.Serializable;

public abstract class Message implements Serializable {

	private static final long serialVersionUID = 8838417404744137405L;

	private int origin;
	private int server;
	
	public Message() {

	}

	public Message(int origin) {
		this.origin = origin;
	}

	public String toString() {
		return origin + " " + getClass().getSimpleName().toUpperCase() + "::";
	}

	public int getServer() {
		return server;
	}

	public void setServer(int server) {
		this.server = server;
	}

	public int getOrigin() {
		return origin;
	}

}
