package net.fe.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;
import java.time.LocalDateTime;

import net.fe.Session;
import net.fe.overworldStage.objective.Seize;

// TODO: Auto-generated Javadoc
/**
 * The Class Server.
 */
public final class Server {
	
	/** a logger */
	private static final Logger logger = Logger.getLogger("net.fe.network.Server");
	static {
		logger.setLevel(java.util.logging.Level.FINER);
		logger.addHandler(new java.util.logging.ConsoleHandler());
		try {
			java.nio.file.Files.createDirectories(new java.io.File("logs").toPath());
			String file = "logs/server_log_" + LocalDateTime.now().toString().replace("T", "@").replace(":", "-") + ".log";
			java.util.logging.Handler h = new java.util.logging.FileHandler(file);
			h.setFormatter(new java.util.logging.SimpleFormatter());
			logger.addHandler(h);
		} catch (IOException e) {
			logger.throwing("net.fe.network.Client", "logging initializing", e);
		}
	}
	
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
			logger.info("SERVER: Waiting for connections...");
			while(!closeRequested) {
				Socket connectSocket = serverSocket.accept();
				logger.info("SERVER: Connection #"+counter+" accepted!");
				ServerListener listener = new ServerListener(this, connectSocket);
				clients.add(listener);
				listener.start();
				counter++;
			}
			serverSocket.close();
		} catch (IOException e) {
			logger.throwing("Server", "start", e);
		}
	}
	
	/**
	 * Sends a message to all clients.
	 *
	 * @param message the message
	 */
	public void broadcastMessage(Message message) {
		logger.finer("[SEND]" + message);
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
