package net.fe.network.server.commands;

import net.fe.network.server.FEServerConsole;

import java.io.IOException;
import java.util.ArrayList;

public enum FEServerCommand{
	HELP("help", "help command", "Get a description of the specified command", "", x -> {
		String commandName;
		if(x.length == 0)
			commandName = "help";
		else
			commandName = x[0];
		try {
			FEServerCommand command = get(commandName);
			System.out.printf("Name : %s%nUsage : %s%nDescription : %s%n%s%n",
					command, command.syntax, command.description, command.descriptionExtension);
		} catch (CommandNotFoundException e) {
			System.out.println("Couldn't find the specified command (Does it actually exist?)");
		}
	}),
	LIST("list", "list", "Lists all the commands available", "", x -> {
		ArrayList<FEServerCommand> arraylist = new ArrayList<FEServerCommand>();
		for(FEServerCommand command : values())
			arraylist.add(command);
		arraylist.sort((a, b) -> a.syntax.compareTo(b.syntax));
		String ans = String.format("%-15s%s%n", "Command", "Syntax");
		for(FEServerCommand command : arraylist)
			ans += String.format("%-15s%s%n", command, command.syntax);
		System.out.println(ans);
	}),
	CLOSE("close", "close", "Close the server", "Force the server to close and effectively kill the process", x -> {
		System.out.println("Close the server? (y/n)");
		if(FEServerConsole.console.readLine().toLowerCase().matches("\\s*y(es)?\\s*")){
			System.out.println("Closing the server");
			System.exit(0);
		}
		System.out.println("Did not close the server");
	});
	
	private String pattern;
	private String syntax;
	private String description;
	private String descriptionExtension;
	private VoidFunction func;
	
	private FEServerCommand(String pattern, String syntax, String description, String descriptionExtension, VoidFunction func) {
		this.pattern = pattern;
		this.description = syntax;
		this.func = func;
		this.description = description;
		this.descriptionExtension = descriptionExtension;
		this.syntax = syntax;
	}
	
	public static FEServerCommand get(String in) throws CommandNotFoundException {
		//System.out.println(Arrays.toString(getCommandList()));
		for(FEServerCommand command : values()){
			//System.out.printf("Checking command %s, %s (%s)%n", command, command.getPattern(), in.matches(command.pattern));
			if(in.matches(command.pattern))
				return command;
		}
		throw new CommandNotFoundException("Couldn't find command " + in);
	}

	public String toString() {
		return name();
	}
	

	public void call(String[] params) {
		func.apply(params);
	}
	
	private interface VoidFunction {
		public void apply(String[] params);
	}
	
}
