package net.fe.network.server;

import java.io.Console;
import java.util.Arrays;

import net.fe.network.server.commands.CommandNotFoundException;
import net.fe.network.server.commands.FEServerCommand;

public class FEServerConsole {
	
	public static final Console console = System.console();
	
	public static void main(String[] args){
		if(console == null){
			System.out.println("Couldn't find the system's console");
			return;
		}
		while(true){
			String in;
			
			in = System.console().readLine();
		
			String[] params = in.split("\\s");
			String command = params[0];
			for(int i = 1; i < params.length; i++)
				params[i-1] = params[i];
			params = Arrays.copyOf(params, params.length - 1);
			
			try {
				FEServerCommand.get(command).call(params);
			} catch (CommandNotFoundException e) {
				System.out.println("Couldn't find the " + command + " command");
			} catch(Exception e) {
				System.out.println("Exception while executing command : " + e);
			}
			
		}
	}
}
