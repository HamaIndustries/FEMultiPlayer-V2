package net.fe.network;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Logger;
import java.time.LocalDateTime;

import net.fe.lobbystage.LobbyStage;
import net.fe.network.message.ClientInit;
import net.fe.network.message.CommandMessage;
import net.fe.network.message.JoinTeam;
import net.fe.network.message.PartyMessage;
import net.fe.network.message.KickMessage;
import net.fe.network.message.QuitMessage;
import net.fe.network.message.ReadyMessage;

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
public final class ServerListener extends Thread {
	
	/** a logger (theoretically initialized in Server) */
	private static final Logger logger = Logger.getLogger("net.fe.network.Server");
	
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
	private final byte clientId;
	
	/**
	 * Instantiates a new server listener.
	 *
	 * @param main the main
	 * @param socket the socket
	 */
	public ServerListener(Server main, Socket socket, byte clientId) {
		super("Listener "+ clientId);
		this.clientId = clientId;
		this.socket = socket;
		this.main = main;
		try {
			out = new ObjectOutputStream(socket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(socket.getInputStream());
			logger.fine("LISTENER: I/O streams initialized");
			sendMessage(new ClientInit((byte) 0, clientId, main.getSession()));
		} catch (IOException e) {
			logger.throwing("ServerListener", "<init>", e);
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run() {
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
			main.clients.remove(this);
			in.close();
			out.close();
			socket.close();
		} catch (Exception e) {
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
		} finally {
			main.clients.remove(this);
		}
	}
	
	/**
	 * Process input.
	 *
	 * @param message the message
	 */
	public void processInput(Message message) {
		synchronized(main.messagesLock) {
			if (message.origin == clientId) {
				if (message instanceof QuitMessage) {
					clientQuit = true;
				}
				main.messages.add(message);
				main.messagesLock.notifyAll();
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
				clientQuit = true;
			}
		} catch (IOException e) {
			logger.severe("SERVER Unable to send message!");
			logger.throwing("ServerListener", "sendMessage", e);
		}
	}

}
