package net.fe.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;
import java.time.LocalDateTime;

import net.fe.FEMultiplayer;
import net.fe.Party;
import net.fe.Player;
import net.fe.Session;
import net.fe.network.message.ClientInit;
import net.fe.network.message.EndGame;
import net.fe.network.message.JoinLobby;
import net.fe.network.message.QuitMessage;

// TODO: Auto-generated Javadoc
/**
 * The Class Client.
 */
public class Client {
	
	/** a logger */
	private static final Logger logger = Logger.getLogger("net.fe.network.Client");
	static {
		logger.setLevel(java.util.logging.Level.FINER);
		try {
			java.nio.file.Files.createDirectories(new java.io.File("logs").toPath());
			String file = "logs/client_log_" + LocalDateTime.now().toString().replace("T", "@").replace(":", "-") + ".log";
			java.util.logging.Handler h = new java.util.logging.FileHandler(file);
			h.setFormatter(new java.util.logging.SimpleFormatter());
			logger.addHandler(h);
		} catch (IOException e) {
			logger.throwing("net.fe.network.Client", "logging initializing", e);
		}
	}
	
	/** The server socket. */
	private Socket serverSocket;
	
	/** The out. */
	private ObjectOutputStream out;
	
	/** The in. */
	private ObjectInputStream in;
	
	/** The server in. */
	private Thread serverIn;
	
	/** The session. */
	private Session session;
	
	/** The open. */
	private boolean open = false;
	
	/** The close requested. */
	private boolean closeRequested = false;
	
	/** The winner. */
	public volatile byte winner = -1;
	
	/** The id. */
	byte id;
	
	/** The messages. Should only operate on if the monitor to messagesLock is held */
	public final CopyOnWriteArrayList<Message> messages;
	
	/** A lock which should be waited upon or notified for changes to messages */
	public final Object messagesLock;
	
	/**
	 * Instantiates a new client.
	 *
	 * @param ip the ip
	 * @param port the port
	 */
	public Client(String ip, int port) {
		messages = new CopyOnWriteArrayList<Message>();
		session = new Session();
		messagesLock = new Object();
		try {
			logger.info("CLIENT: Connecting to server: "+ip+":"+port);
			serverSocket = new Socket(ip, port);
			logger.info("CLIENT: Successfully connected!");
			out = new ObjectOutputStream(serverSocket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(serverSocket.getInputStream());
			logger.info("CLIENT: I/O streams initialized");
			open = true;
			serverIn = new Thread(new Runnable() {
				public void run() {
					try {
						Message message;
						while((message = (Message)in.readObject()) != null) {
							logger.info("CLIENT: Read " + message);
							processInput(message);
						}
						in.close();
						out.close();
						serverSocket.close();
					} catch (IOException e) {
						logger.warning("CLIENT: EXIT");
						logger.throwing("ClientNetworkingReader", "run", e);
					} catch (ClassNotFoundException e) {
						logger.throwing("ClientNetworkingReader", "run", e);
					}
				}
			}, "ClientNetworkingReader");
		} catch (IOException e) {
			logger.throwing("Client", "<init>", e);
		}
	}
	
	/**
	 * Start.
	 */
	public void start() {
		serverIn.start();
	}
	
	/**
	 * Process input.
	 *
	 * @param message the message
	 */
	private void processInput(Message message) {
		if(message instanceof ClientInit) {
			ClientInit message2 = (ClientInit) message;
			if (message2.hashes.equals(ClientInit.Hashes.pullFromStatics())) {
				this.id = message2.clientID;
				this.session = message2.session;
				FEMultiplayer.getLocalPlayer().setClientID(message2.clientID);
				if(id >= 2) {
					FEMultiplayer.getLocalPlayer().getParty().setColor(Party.TEAM_RED);
				}
				logger.info("CLIENT: Recieved ID "+id+" from server");
				// Send a join lobby request
				sendMessage(new JoinLobby(id, FEMultiplayer.getLocalPlayer()));
			} else {
				logger.info("CLIENT: Mismatched hashes:" +
						"\n\tServer: " + message2.hashes +
						"\n\tClient: " + ClientInit.Hashes.pullFromStatics());
				this.quit();
			}
		} else if (message instanceof QuitMessage) {
			if(message.origin == id && closeRequested) {
				close();
			}
		} else if(message instanceof EndGame) {
			winner = (byte) ((EndGame)message).winner;
		}
		
		session.handleMessage(message);
		
		synchronized (messagesLock) {
			messages.add(message);
		}
	}
	
	/**
	 * Close.
	 */
	private void close() {
		try {
			in.close();
			out.close();
			serverSocket.close();
			open = false;
		} catch (IOException e) {
			logger.throwing("Client", "close", e);
		}
	}
	
	/**
	 * Quit.
	 */
	public void quit() {
		sendMessage(new QuitMessage(id));
		// simple security to prevent clients closing other clients
		closeRequested = true;
	}
	
	/**
	 * Send message.
	 *
	 * @param message the message
	 */
	public void sendMessage(Message message) {
		try {
			message.origin = id;
			out.writeObject(message);
			logger.info("CLIENT: Sent message ["+message.toString()+"]");
		} catch (IOException e) {
			logger.severe("CLIENT Unable to send message: ["+message.toString()+"]");
			logger.throwing("Client", "sendMessage", e);
		}
	}
	
	/**
	 * Checks if is open.
	 *
	 * @return true, if is open
	 */
	public boolean isOpen() {
		return open;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public byte getID() {
		return id;
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
