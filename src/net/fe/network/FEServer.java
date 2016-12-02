package net.fe.network;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import net.fe.Player;
import net.fe.game.Session;
import net.fe.game.modifier.DivineIntervention;
import net.fe.game.modifier.MadeInChina;
import net.fe.game.modifier.Modifier;
import net.fe.game.modifier.ProTactics;
import net.fe.game.modifier.SuddenDeath;
import net.fe.game.modifier.Treasury;
import net.fe.game.modifier.Vegas;
import net.fe.game.modifier.Veterans;
import net.fe.game.pick.AllPick;
import net.fe.game.pick.Draft;
import net.fe.game.pick.PickMode;
import net.fe.game.unit.Unit;
import net.fe.game.unit.UnitIdentifier;
import net.fe.lobbystage.LobbyStage;
import net.fe.network.message.*;
import net.fe.overworldStage.objective.Objective;
import net.fe.overworldStage.objective.Rout;
import net.fe.overworldStage.objective.Seize;
import chu.engine.Game;
import chu.engine.Stage;

// TODO: Auto-generated Javadoc
/**
 * A game that does not render anything. Manages logic only
 * @author Shawn
 *
 */
public class FEServer extends Game {
	
	/** The server. */
	private static Server server;
	
	/** The current stage. */
	private static Stage currentStage;
	
	/** The lobby. */
	public static LobbyStage lobby;
	
	private static Map<String, Objective[]> maps = new HashMap<String, Objective[]>();
	
	private static FEServerFrame frame;
	
	static {
		Rout rout = new Rout();
		Seize seize = new Seize();
		
		maps.put("delphino", new Objective[]{rout});
		maps.put("town", new Objective[]{rout});
		maps.put("alpea", new Objective[]{seize});
		maps.put("plains", new Objective[]{rout, seize});
		maps.put("fort", new Objective[]{rout, seize});
		maps.put("decay", new Objective[]{rout, seize});
	}
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		
		frame = new FEServerFrame();
	}

	/**
	 * Instantiates a new FE server.
	 */
	public FEServer(Session s) {
		server = new Server(s);
	}
	
	/**
	 * Inits the.
	 */
	public void init() {
		net.fe.game.unit.WeaponFactory.loadWeapons();
		net.fe.game.unit.UnitFactory.loadUnits();
		
		Thread serverThread = new Thread() {
			public void run() {
				server.start(frame.getPort());
			}
		};
		lobby = new LobbyStage(server.getSession());
		currentStage = lobby;
		serverThread.start();
	}
	
	/* (non-Javadoc)
	 * @see chu.engine.Game#loop()
	 */
	@Override
	public void loop() {
		boolean yes = true;
		while(yes) {
			final long time = System.nanoTime();
			final ArrayList<Message> messages = new ArrayList<>();
			synchronized (server.messagesLock) {
				try {
					server.messagesLock.wait(1000);
				} catch (InterruptedException e) {
					// No, really. Has there ever been a meaningful response to an InterruptedException?
				}
				messages.addAll(server.messages);
				for(Message message : messages) {
					if (message instanceof JoinTeam || message instanceof ReadyMessage) {
						if (!(FEServer.getCurrentStage() instanceof LobbyStage)) {
							// ignore message to prevent late-joining players from switching teams or readying up
						} else {
							// TODO: percelate broadcasting of these up to stages
							server.broadcastMessage(message);
						}
					} else if (message instanceof CommandMessage || message instanceof PartyMessage) {
						// If the unit attacked, we need to generate battle results
						// If party; don't tell others until all have selected their party
					} else if (message instanceof KickMessage) {
						// Clients are not allowed to do this.
					} else {
						// TODO: percelate broadcasting of these up to stages
						server.broadcastMessage(message);
					}
					
					server.messages.remove(message);	
				}
			}
			for(Message m : messages) {
				server.getSession().handleMessage(m);
			}
			currentStage.beginStep(messages);
			currentStage.onStep();
			currentStage.endStep();
			timeDelta = System.nanoTime()-time;
		}
	}
	
	/**
	 * Gets the current stage.
	 *
	 * @return the current stage
	 */
	public static Stage getCurrentStage() {
		return currentStage;
	}
	
	/**
	 * Sets the current stage.
	 *
	 * @param stage the new current stage
	 */
	public static void setCurrentStage(Stage stage) {
		currentStage = stage;
	}

	/**
	 * Gets the server.
	 *
	 * @return the server
	 */
	public static Server getServer() {
		return server;
	}
	
	/**
	 * Gets the players.
	 *
	 * @return the players
	 */
	private static HashMap<Byte, Player> getPlayers() {
		return server.getSession().getPlayerMap();
	}

	/**
	 * Reset to lobby.
	 */
	public static void resetToLobby() {
		for(Player p : getPlayers().values()) {
			p.ready = false;
		}
		FEServer.getServer().allowConnections = false;
		currentStage = lobby;
	}
	
	/**
	 * Reset to lobby and kick players.
	 */
	public static void resetToLobbyAndKickPlayers(){
		resetToLobby();
		ArrayList<Byte> ids = new ArrayList<>();
		for(Player p : getPlayers().values()) {
			ids.add(p.getID());
		}
		synchronized(server.messagesLock) {
			for(byte i : ids){
				final KickMessage kick = new KickMessage((byte) 0, i, "Reseting server");
				server.broadcastMessage(kick);
				server.messages.add(kick);
			}
		}
	}

	public static Map<String, Objective[]> getMaps() {
		return maps;
	}

}