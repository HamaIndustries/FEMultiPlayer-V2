package net.fe.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import net.fe.FEMultiplayer;
import net.fe.Party;
import net.fe.Player;
import net.fe.Session;
import net.fe.network.message.ClientInit;
import net.fe.network.message.EndGame;
import net.fe.network.message.JoinLobby;
import net.fe.network.message.QuitMessage;
import net.fe.network.message.SessionUpdate;

// TODO: Auto-generated Javadoc
/**
 * The Class Client.
 */
public class Client {
	
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
	public byte winner = -1;
	
	/** The id. */
	byte id;
	
	/** The messages. */
	public volatile ArrayList<Message> messages;
	
	/**
	 * Instantiates a new client.
	 *
	 * @param ip the ip
	 * @param port the port
	 */
	public Client(String ip, int port) {
		messages = new ArrayList<Message>();
		session = new Session();
		try {
			System.out.println("CLIENT: Connecting to server: "+ip+":"+port);
			serverSocket = new Socket(ip, port);
			System.out.println("CLIENT: Successfully connected!");
			out = new ObjectOutputStream(serverSocket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(serverSocket.getInputStream());
			System.out.println("CLIENT: I/O streams initialized");
			open = true;
			serverIn = new Thread() {
				public void run() {
					try {
						Message message;
						while((message = (Message)in.readObject()) != null) {
							processInput(message);
						}
						in.close();
						out.close();
						serverSocket.close();
					} catch (IOException e) {
						System.out.println("CLIENT: EXIT");
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
			};
		} catch (IOException e) {
			e.printStackTrace();
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
			id = ((ClientInit)message).clientID;
			this.session = ((ClientInit)message).session;
			FEMultiplayer.getLocalPlayer().setClientID(id);
			if(id >= 2) {
				FEMultiplayer.getLocalPlayer().getParty().setColor(Party.TEAM_RED);
			}
			System.out.println("CLIENT: Recieved ID "+id+" from server");
			// Send a join lobby request
			sendMessage(new JoinLobby(id, FEMultiplayer.getLocalPlayer()));
		} else if (message instanceof QuitMessage) {
			if(message.origin == id && closeRequested) {
				close();
			}
		} else if(message instanceof EndGame) {
			winner = (byte) ((EndGame)message).winner;
		} else if(message instanceof SessionUpdate) {
			Session update = ((SessionUpdate)message).session;
			for(Player p : update.getPlayers()) {
				if(!session.getPlayerMap().containsKey(p.getID())) {
					session.addPlayer(p);
				}
			}
			session.setMap(update.getMap());
			session.setMaxUnits(update.getMaxUnits());
			session.setObjective(update.getObjective());
			session.setPickMode(update.getPickMode());
		}
		messages.add(message);
	}
	
	/**
	 * Gets the messages.
	 *
	 * @return the messages
	 */
	public ArrayList<Message> getMessages() {
		return messages;
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
			e.printStackTrace();
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
//			System.out.println("CLIENT: Sent message ["+message.toString()+"]");
		} catch (IOException e) {
			System.err.println("CLIENT Unable to send message!");
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
