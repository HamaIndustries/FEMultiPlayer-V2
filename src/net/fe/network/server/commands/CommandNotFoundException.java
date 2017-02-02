package net.fe.network.server.commands;

public class CommandNotFoundException extends Exception {

	private static final long serialVersionUID = 877031101876774978L;

	public CommandNotFoundException(String message) {
		super(message);
	}

}
