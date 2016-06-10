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

import net.fe.Player;
import net.fe.Session;
import net.fe.lobbystage.LobbyStage;
import net.fe.modifier.DivineIntervention;
import net.fe.modifier.MadeInChina;
import net.fe.modifier.Modifier;
import net.fe.modifier.ProTactics;
import net.fe.modifier.SuddenDeath;
import net.fe.modifier.Treasury;
import net.fe.modifier.Vegas;
import net.fe.modifier.Veterans;
import net.fe.network.message.*;
import net.fe.overworldStage.objective.Objective;
import net.fe.overworldStage.objective.Rout;
import net.fe.overworldStage.objective.Seize;
import net.fe.pick.AllPick;
import net.fe.pick.Draft;
import net.fe.pick.PickMode;
import net.fe.unit.Unit;
import net.fe.unit.UnitIdentifier;
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
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		final JFrame frame = new JFrame("FEServer");
		
		final Map<String, Objective[]> maps = new HashMap<String, Objective[]>();
		{
			Rout rout = new Rout();
			Seize seize = new Seize();
			
			maps.put("delphino", new Objective[]{rout});
			maps.put("town", new Objective[]{rout});
			maps.put("alpea", new Objective[]{seize});
			maps.put("plains", new Objective[]{rout, seize});
			maps.put("fort", new Objective[]{rout, seize});
			maps.put("decay", new Objective[]{rout, seize});
		}
		
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		DefaultListModel<Modifier> selectedModifiersModel = new DefaultListModel<Modifier>();
		// Modifiers
		DefaultListModel<Modifier> unselectedModifiersModel = new DefaultListModel<Modifier>();
		unselectedModifiersModel.addElement(new MadeInChina());
		unselectedModifiersModel.addElement(new Treasury());
		unselectedModifiersModel.addElement(new Veterans());
		unselectedModifiersModel.addElement(new DivineIntervention());
		unselectedModifiersModel.addElement(new SuddenDeath());
		unselectedModifiersModel.addElement(new Vegas());
		unselectedModifiersModel.addElement(new ProTactics());
		
		final JPanel mainPanel = new JPanel();
		frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		
		JPanel mapPanel = new JPanel();
		mainPanel.add(mapPanel);
		mapPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JLabel mapNameLabel = new JLabel("Map: ");
		mapPanel.add(mapNameLabel);
		
		JPanel objectivePanel = new JPanel();
		mainPanel.add(objectivePanel);
		
		JLabel objLabel = new JLabel("Objective: ");
		objectivePanel.add(objLabel);
		
		final JComboBox<Objective> objComboBox = new JComboBox<>();
		objectivePanel.add(objComboBox);
		
		// populate list of maps
		final JComboBox<String> mapSelectionBox = new JComboBox<>();
		mapSelectionBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				objComboBox.setModel(new DefaultComboBoxModel<Objective>(maps.get(mapSelectionBox.getSelectedItem())));
			}
		});
		mapPanel.add(mapSelectionBox);
		mapSelectionBox.setModel(new DefaultComboBoxModel<String>(maps.keySet().toArray(new String[0])));
		
		JLabel label = new JLabel("Max units: ");
		mapPanel.add(label);
		
		final JSpinner maxUnitsSpinner = new JSpinner();
		mapPanel.add(maxUnitsSpinner);
		maxUnitsSpinner.setModel(new SpinnerNumberModel(8, 1, 8, 1));
		
		// Objectives
		ComboBoxModel<Objective> oModel = new DefaultComboBoxModel<>(maps.get(mapSelectionBox.getSelectedItem()));
		objComboBox.setModel(oModel);
		
		JLabel lblPickMode = new JLabel("Pick mode: ");
		objectivePanel.add(lblPickMode);
		
		// Pick modes
		ComboBoxModel<PickMode> pickModeModel = new DefaultComboBoxModel<>(new PickMode[] { new AllPick(), new Draft()});
		final JComboBox<PickMode> pickModeBox = new JComboBox<>();
		pickModeBox.setModel(pickModeModel);
		objectivePanel.add(pickModeBox);
		
		JSeparator separator = new JSeparator();
		mainPanel.add(separator);
		
		JLabel modifiersLabel = new JLabel("Modifiers");
		modifiersLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		mainPanel.add(modifiersLabel);
		
		JPanel modifiersPane = new JPanel();
		mainPanel.add(modifiersPane);
		modifiersPane.setLayout(new BoxLayout(modifiersPane, BoxLayout.X_AXIS));
		
		JScrollPane selectedModifiersScrollPane = new JScrollPane();
		selectedModifiersScrollPane.setPreferredSize(new Dimension(120,150));
		modifiersPane.add(selectedModifiersScrollPane);
		
		JScrollPane modifiersScrollPane = new JScrollPane();
		modifiersScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		modifiersScrollPane.setPreferredSize(new Dimension(120,150));
		
		final ModifierList modifiersList = new ModifierList();
		modifiersList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		modifiersScrollPane.add(modifiersList);
		modifiersList.setModel(unselectedModifiersModel);
		modifiersScrollPane.setViewportView(modifiersList);
		
		final ModifierList selectedModifiersList = new ModifierList();
		selectedModifiersList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		selectedModifiersScrollPane.add(selectedModifiersList);
		selectedModifiersList.setModel(selectedModifiersModel);
		selectedModifiersScrollPane.setViewportView(selectedModifiersList);
		
		JPanel buttonsPanel = new JPanel();
		modifiersPane.add(buttonsPanel);
		
		JButton addModifierBtn = new JButton("<-- Add");
		addModifierBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int index = modifiersList.getSelectedIndex();
				if(index != -1) {
					Modifier o = unselectedModifiersModel.getElementAt(index);
					unselectedModifiersModel.remove(modifiersList.getSelectedIndex());
					selectedModifiersModel.add(0,o);
				}
			}
		});
		buttonsPanel.setLayout(new GridLayout(0, 1, 0, 0));
		buttonsPanel.add(addModifierBtn);
		
		JButton removeModifierBtn = new JButton("Remove -->");
		removeModifierBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int index = selectedModifiersList.getSelectedIndex();
				if(index != -1) {
					Modifier o = selectedModifiersModel.getElementAt(index);
					selectedModifiersModel.remove(selectedModifiersList.getSelectedIndex());
					unselectedModifiersModel.add(0,o);
				}
			}
		});
		buttonsPanel.add(removeModifierBtn);
		
		modifiersPane.add(modifiersScrollPane);
		
		Component verticalStrut = Box.createVerticalStrut(20);
		mainPanel.add(verticalStrut);
		
		final JButton startServer = new JButton("Start server");
		startServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					frame.getContentPane().add(new JLabel("Server IP: " + InetAddress.getLocalHost().getHostAddress()){
						private static final long serialVersionUID = 1L;
					{
						this.setFont(getFont().deriveFont(20f));
						this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
					}}, BorderLayout.NORTH);
					frame.remove(mainPanel);
					frame.remove(startServer);
				} catch (UnknownHostException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				{
					final JButton kickAll = new JButton("Kick all");
					kickAll.addActionListener((e2) -> FEServer.resetToLobbyAndKickPlayers());
					frame.getContentPane().add(kickAll);
				}
				frame.pack();
				Thread serverThread = new Thread() {
					public void run() {
						HashSet<Modifier> mods = new HashSet<Modifier>();
						for(int i=0; i< selectedModifiersList.getModel().getSize(); i++) {
							mods.add(selectedModifiersList.getModel().getElementAt(i));
						}
						Session s = new Session(
							(Objective)objComboBox.getSelectedItem(),
							(String)mapSelectionBox.getSelectedItem(),
							(Integer)maxUnitsSpinner.getValue(),
							mods,
							(PickMode)pickModeBox.getSelectedItem()
						);
						
						FEServer feserver = new FEServer(s);
						try{
							feserver.init();
							feserver.loop();
						} catch (Exception e){
							System.err.println("Exception occurred, writing to logs...");
							e.printStackTrace();
							try{
								File errLog = new File("error_log_server" + System.currentTimeMillis()%100000000 + ".log");
								PrintWriter pw = new PrintWriter(errLog);
								e.printStackTrace(pw);
								pw.close();
							}catch (IOException e2){
								e2.printStackTrace();
							}
							System.exit(0);
						}
					}
				};
				serverThread.start();
			}
		});
		frame.getContentPane().add(startServer, BorderLayout.SOUTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
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
		net.fe.unit.WeaponFactory.loadWeapons();
		net.fe.unit.UnitFactory.loadUnits();
		
		Thread serverThread = new Thread() {
			public void run() {
				server.start(21255);
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
			for(Message m : messages) {server.getSession().handleMessage(m);}
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

}

class ModifierList extends JList<Modifier> {
	
	private static final long serialVersionUID = 561574462354745569L;

	public String getToolTipText(MouseEvent event) {
		Point p = event.getPoint();
		int index = locationToIndex(p);
		String tip = ((Modifier) getModel().getElementAt(index)).getDescription();
		return tip;
	}
	
}
