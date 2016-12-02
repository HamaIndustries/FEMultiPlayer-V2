package net.fe;

public class FailedToConnectException extends Exception {

	private static final long serialVersionUID = -8675905516260076259L;

	public FailedToConnectException(String message) {
		super(message);
	}
}
