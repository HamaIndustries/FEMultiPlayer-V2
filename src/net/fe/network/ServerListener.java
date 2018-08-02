package net.fe.network;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;
import java.util.logging.Logger;

import net.fe.network.message.ClientInit;
import net.fe.network.message.KickMessage;
import net.fe.network.message.QuitMessage;
import net.fe.network.message.RejoinMessage;

// TODO: Auto-generated Javadoc
/**
 * The listener interface for receiving server events.
 * The class that is interested in processing a server
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addServerListener<code> method. When
 * the server event occurs, that object's appropriate
 * method is invoked.
 *
 * @see ServerEvent
 */
public final class ServerListener {
	
	/** a logger (theoretically initialized in Server) */
	private static final Logger logger = Logger.getLogger("net.fe.network.Server");
	
	private static final Random rng = new Random();
	
	/** The socket. */
	private final Socket socket;
	
	/** The out. */
	private ObjectOutputStream out;
	
	/** The in. */
	private ObjectInputStream in;
	
	/** The main. */
	private final Server main;
	
	/** The client quit. */
	private volatile boolean clientQuit;
	
	/** The client that this is linked to. */
	private byte clientId;
	private long token;
	
	public ServerListener(Server main, Socket socket, byte clientId, long token) {
		this.clientId = clientId;
		this.socket = socket;
		this.main = main;
		this.token = token;
		try {
			out = new ObjectOutputStream(socket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(socket.getInputStream());
			logger.fine("LISTENER: I/O streams initialized");
			sendMessage(new ClientInit((byte) 0, clientId, main.getSession(), token));
		} catch (IOException e) {
			logger.throwing("ServerListener", "<init>", e);
		}
	}
	
	/**
	 * Instantiates a new server listener.
	 *
	 * @param main the main
	 * @param socket the socket
	 */
	public ServerListener(Server main, Socket socket, byte clientId) {
		this(main, socket, clientId, rng.nextLong());
	}
	
	public void start() {
		new Thread(() -> {
			try {
				logger.fine("LISTENER: Start");
				Message message;
				clientQuit = false;
				while(!clientQuit) {
					message = (Message) in.readObject();
					logger.fine("[RECV]" + message);
					processInput(message);
				}
				logger.fine("LISTENER: Exit");
			} catch (Throwable e) {
				quit(true);
				System.err.println("Exception occurred, writing to logs...");
				e.printStackTrace();
				try{
					File errLog = new File("error_log_server_listener" + System.currentTimeMillis()%100000000 + ".log");
					PrintWriter pw = new PrintWriter(errLog);
					e.printStackTrace(pw);
					pw.close();
				}catch (IOException e2){
					e2.printStackTrace();
				}
			}
		}, "Listener "+ clientId).start();
	}
	
	/**
	 * Process input.
	 *
	 * @param message the message
	 */
	public void processInput(Message message) {
		if(message instanceof RejoinMessage) {
			RejoinMessage rejoin = (RejoinMessage) message;
			if(main.validateRejoinRequest(rejoin)) {
				this.token = rejoin.getToken();
				this.clientId = rejoin.origin;
			} else {
				sendMessage(new KickMessage((byte) 0, rejoin.origin, "Reconnection failed: Timed out"));
				quit(false);
			}
		} else {
			synchronized(main.messagesLock) {
				if (message.origin == clientId) {
					if (message instanceof QuitMessage) {
						quit(false);
					}
					main.messages.add(message);
					main.messagesLock.notifyAll();
				}
			}
		}
	}
	
	/**
	 * Send message.
	 *
	 * @param message the message
	 */
	public void sendMessage(Message message) {
		try {
			out.writeObject(message);
			out.flush();
			logger.fine("SERVER sent message: [" + message.toString() + "]");
			if (message instanceof KickMessage && ((KickMessage) message).player == clientId) {
				quit(false);
			}
		} catch (IOException e) {
			logger.severe("SERVER Unable to send message!");
			logger.throwing("ServerListener", "sendMessage", e);
		}
	}
	
	/**
	 * Closes the socket and the I/O streams.
	 */
	private void close() {
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Make the client quit the server. Only takes effect if the client hasn't quit.
	 * @param allowReconnect If the client is allowed to reconnect to the server
	 * via a {@link RejoinMessage}.
	 */
	private void quit(boolean allowReconnect) {
		if(!clientQuit) {
			clientQuit = true;
			main.clients.remove(this);
			close();
			if(allowReconnect)
				main.pastClients.put(System.currentTimeMillis(), this);
		}
	}

	public byte getId() {
		return clientId;
	}

	public long getToken() {
		return token;
	}

}
