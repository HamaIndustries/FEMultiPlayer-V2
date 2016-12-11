package net.fe.network.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import net.fe.network.Message;

public class ServerMaster implements Runnable{

	
	private ArrayList<FEServer2> serverList = new ArrayList<FEServer2>();
	
	public static final int DEFAULT_PORT = 21225;
	
	private int port = DEFAULT_PORT;
	private ServerSocket serverSocket;
	private State state;
	private int serverIDCounter;
	private int clientIDCounter;
	
	private ArrayList<ServerClient> clients = new ArrayList<ServerClient>();
	
	{
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			System.out.println("Failed to initialize the server socket : " + e.getMessage());
		}
	}
	
	public FEServer2 newServer(){
		FEServer2 server = new FEServer2(serverIDCounter++);
		serverList.add(server);
		return server;
	}
	
	
	public void reinitializeSocket(int port){
		this.port = port;
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			System.out.println("Failed to initialize the server socket : " + e.getMessage());
			System.out.println("The server is likely dead right now.");
		}
	}
	
	public void acceptSockets() {
		Socket socket;
		try {
			while((socket = serverSocket.accept()) != null){
				clients.add(new ServerClient(socket, clientIDCounter++));
			}
		} catch (IOException e) {
			//Seriously, what am I even supposed to do?
			e.printStackTrace();
		}
	}
	
	private ServerMaster() { }
	
	
	public void startMasterServer(){
		if(state == State.CLOSED){
			new Thread(this).start();
			state = State.RUNNING;
		}
	}

	@Override
	public void run() {
		
		while(state != State.CLOSE_REQUESTED){
			ArrayList<Message> messages = readMessages();
			for(Message message : messages){
				//TODO redirect to the server & process
			}
		}
		state = State.CLOSED;
	}
	
	
	private ArrayList<Message> readMessages(){
		ArrayList<Message> messages = new ArrayList<Message>();
		for(ServerClient sc : clients)
			messages.addAll(sc.readMessages());
		return messages;
	}
	
	private enum State {
		CLOSED, RUNNING, CLOSE_REQUESTED;
	}
}
