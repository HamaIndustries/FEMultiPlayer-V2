package net.fe.network.message;

import net.fe.network.Message;

public class RejoinMessage extends Message {

	private static final long serialVersionUID = 6897797603372162386L;
	
	private long lastTimestamp;
	private long token;
	
	public RejoinMessage(long lastTimestamp, long token) {
		this.lastTimestamp = lastTimestamp;
		this.token = token;
	}

	public long getLastTimestamp() {
		return lastTimestamp;
	}

	public long getToken() {
		return token;
	}
}
