package net.fe.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import net.fe.Session;
import net.fe.overworldStage.objective.Seize;

// TODO: Auto-generated Javadoc
/**
 * The Class Server.
 */
public class Server {
	
	/** The server socket. */
	ServerSocket serverSocket;
	
	/** The close requested. */
	boolean closeRequested = false;
	
	/** The clients. */
	volatile ArrayList<ServerListener> clients;
	
	/** The messages. */
	public volatile ArrayList<Message> messages;
	
	/** The log. */
	public ServerLog log;
	
	/** The session. */
	private Session session;
	
	/** The allow connections. */
	public boolean allowConnections;
	
	/** The counter. */
	byte counter = 1;
	
	/**
	 * Instantiates a new server.
	 */
	public Server() {
		messages = new ArrayList<Message>();
		clients = new ArrayList<ServerListener>();
		session = new Session();
		session.setObjective(new Seize());
		log = new ServerLog();
		allowConnections = true;
	}
	
	/**
	 * Start.
	 *
	 * @param port the port
	 */
	public void start(int port) {
		try {
			serverSocket = new ServerSocket(port);
			System.out.println("SERVER: Waiting for connections...");
			while(!closeRequested) {
				Socket connectSocket = serverSocket.accept();
				System.out.println("SERVER: Connection #"+counter+" accepted!");
				ServerListener listener = new ServerListener(this, connectSocket);
				clients.add(listener);
				listener.start();
				counter++;
			}
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Sends a message to all clients.
	 *
	 * @param message the message
	 */
	public void broadcastMessage(Message message) {
		log.logMessage(message, true);
		for(ServerListener out : clients) {
			out.sendMessage(message);
		}
	}
	
	/**
	 * Sends a message only to the given client.
	 *
	 * @param client the client
	 * @param message the message
	 */
	public void sendMessage(ServerListener client, Message message) {
		log.logMessage(message, true);
		client.sendMessage(message);
	}
	
	/**
	 * Gets the count.
	 *
	 * @return the count
	 */
	public byte getCount() {
		return counter;
	}

	/**
	 * Gets the session.
	 *
	 * @return the session
	 */
	public Session getSession() {
		return session;
	}
}
