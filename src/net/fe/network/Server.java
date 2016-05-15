package net.fe.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import net.fe.Session;
import net.fe.overworldStage.objective.Seize;

// TODO: Auto-generated Javadoc
/**
 * The Class Server.
 */
public final class Server {
	
	/** The server socket. */
	private ServerSocket serverSocket;
	
	/** The close requested. */
	private boolean closeRequested = false;
	
	/** The clients. */
	final CopyOnWriteArrayList<ServerListener> clients;
	
	/** The messages. Should only operate on if the monitor to messagesLock is held */
	public final ArrayList<Message> messages;
	
	/** A lock which should be wated upon or notified for changes to messages */
	public final Object messagesLock;
	
	/** The log. */
	public final ServerLog log;
	
	/** The session. */
	private final Session session;
	
	/** The allow connections. */
	public boolean allowConnections;
	
	/** The counter. */
	private byte counter = 1;
	
	/**
	 * Instantiates a new server.
	 */
	public Server() {
		messages = new ArrayList<Message>();
		messagesLock = new Object();
		clients = new CopyOnWriteArrayList<ServerListener>();
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
