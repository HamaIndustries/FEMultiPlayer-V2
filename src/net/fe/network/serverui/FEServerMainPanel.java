package net.fe.network.serverui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerNumberModel;

import net.fe.Session;
import net.fe.modifier.DivineIntervention;
import net.fe.modifier.MadeInChina;
import net.fe.modifier.Modifier;
import net.fe.modifier.ProTactics;
import net.fe.modifier.SuddenDeath;
import net.fe.modifier.Treasury;
import net.fe.modifier.Vegas;
import net.fe.modifier.Veterans;
import net.fe.network.FEServer;
import net.fe.overworldStage.ClientOverworldStage.FogType;
import net.fe.overworldStage.ClientOverworldStage.SpectatorFogOption;
import net.fe.overworldStage.objective.Objective;
import net.fe.overworldStage.objective.Rout;
import net.fe.overworldStage.objective.Seize;
import net.fe.pick.AllPick;
import net.fe.pick.Draft;
import net.fe.pick.PickMode;
import net.fe.rng.GuaranteedRNG;
import net.fe.rng.NullRNG;
import net.fe.rng.RNG;
import net.fe.rng.SimpleRNG;
import net.fe.rng.TrueHitRNG;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.JCheckBox;

/**
 * The initial panel displayed to the server's host.
 * @author wellme
 */
public class FEServerMainPanel extends JPanel {

	private static final long serialVersionUID = -5397645919464825016L;
	
	private JButton startServer;
	private ToolTipList<Modifier> unselectedModifiersList;
	private JScrollPane modifiersScrollPane;
	private JButton removeModifierBtn;
	private JButton addModifierBtn;
	private JPanel buttonsPanel;
	private ToolTipList<Modifier> selectedModifiersList;
	private JScrollPane selectedModifiersScrollPane;
	private JPanel modifiersPane;
	private JLabel modifiersLabel;
	private JSeparator separator;
	private JComboBox<PickMode> pickModeBox;
	private JLabel lblPickMode;
	private JComboBox<Objective> objComboBox;
	private JLabel objLabel;
	private JPanel objectivePanel;
	private JLabel mapNameLabel;
	private JComboBox<String> mapSelectionBox;
	private JLabel label;
	private JSpinner maxUnitsSpinner;
	private JPanel mapPanel;
	private JPanel mainPanel;

	private Runnable serverStart;
	private JPanel pnlPort;
	private JLabel lblPort;
	private JSpinner spnPort;
	private JPanel pnlRNG;
	private JLabel lblHitRng;
	private JComboBox<RNG> cbbHitRNG;
	private JLabel lblCritRng;
	private JComboBox<RNG> cbbCritRNG;
	private JLabel lblSkillRng;
	private JComboBox<RNG> cbbSkillRNG;
	
	private DefaultListModel<Modifier> selectedModifiersModel;
	private DefaultListModel<Modifier> unselectedModifiersModel;
	private JPanel pnlFogOfWar;
	private JLabel lblFogOfWar;
	private JComboBox<FogType> cbbFogOfWar;
	private JLabel lblSpectatorFog;
	private JComboBox<SpectatorFogOption> cbbSpectatorFog;
	private JLabel lblSight;
	private JComboBox<SightOption> cbbSight;
	private JPanel pnlToggleFog;
	private JPanel panel_1;
	private JPanel panel_2;
	private JPanel panel_3;
	private JLabel lblRegularSight;
	private JSpinner spnRegularSight;
	private JLabel lblThiefSight;
	private JSpinner spnThiefSight;
	
	private ChangeListener spnSightChanged = new ChangeListener() {
		public void stateChanged(ChangeEvent e) {
			cbbSight.setSelectedItem(cbbSight.getItemAt(0));
		}
	};
	private JCheckBox chckbxShowOutOf;
	private JPanel panel;
	private JCheckBox chckbxAllowRedoingMovement;
	
	/**
	 * Initializes the panel.
	 */
	public FEServerMainPanel() {
		final Map<String, Objective[]> maps = new HashMap<String, Objective[]>();
		//Oh god what is this?
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

		setLayout(new BorderLayout(0, 0));
		selectedModifiersModel = new DefaultListModel<Modifier>();
		// Modifiers
		unselectedModifiersModel = new DefaultListModel<Modifier>();
		unselectedModifiersModel.addElement(new MadeInChina());
		unselectedModifiersModel.addElement(new Treasury());
		unselectedModifiersModel.addElement(new Veterans());
		unselectedModifiersModel.addElement(new DivineIntervention());
		unselectedModifiersModel.addElement(new SuddenDeath());
		unselectedModifiersModel.addElement(new Vegas());
		unselectedModifiersModel.addElement(new ProTactics());
		
		sortListModels();

		mainPanel = new JPanel();
		add(mainPanel, BorderLayout.CENTER);
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

		mapPanel = new JPanel();
		mainPanel.add(mapPanel);
		mapPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		mapNameLabel = new JLabel("Map: ");
		mapPanel.add(mapNameLabel);

		objectivePanel = new JPanel();
		mainPanel.add(objectivePanel);

		objLabel = new JLabel("Objective: ");
		objectivePanel.add(objLabel);

		objComboBox = new JComboBox<>();
		objectivePanel.add(objComboBox);

		// populate list of maps
		mapSelectionBox = new JComboBox<>();
		mapSelectionBox.addItemListener(e -> objComboBox.setModel(new DefaultComboBoxModel<Objective>(maps.get(mapSelectionBox.getSelectedItem()))));
		mapPanel.add(mapSelectionBox);
		mapSelectionBox.setModel(new DefaultComboBoxModel<String>(maps.keySet().toArray(new String[0])));

		label = new JLabel("Max units: ");
		mapPanel.add(label);

		maxUnitsSpinner = new JSpinner();
		mapPanel.add(maxUnitsSpinner);
		maxUnitsSpinner.setModel(new SpinnerNumberModel(8, 1, 8, 1));

		// Objectives
		ComboBoxModel<Objective> oModel = new DefaultComboBoxModel<>(maps.get(mapSelectionBox.getSelectedItem()));
		objComboBox.setModel(oModel);

		lblPickMode = new JLabel("Pick mode: ");
		objectivePanel.add(lblPickMode);

		// Pick modes
		ComboBoxModel<PickMode> pickModeModel = new DefaultComboBoxModel<>(new PickMode[]{new AllPick(), new Draft()});
		pickModeBox = new JComboBox<>();
		pickModeBox.setModel(pickModeModel);
		objectivePanel.add(pickModeBox);

		separator = new JSeparator();
		mainPanel.add(separator);

		modifiersLabel = new JLabel("Modifiers");
		modifiersLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		mainPanel.add(modifiersLabel);

		modifiersPane = new JPanel();
		mainPanel.add(modifiersPane);
		modifiersPane.setLayout(new BoxLayout(modifiersPane, BoxLayout.X_AXIS));

		selectedModifiersScrollPane = new JScrollPane();
		selectedModifiersScrollPane.setPreferredSize(new Dimension(120, 150));
		modifiersPane.add(selectedModifiersScrollPane);

		modifiersScrollPane = new JScrollPane();
		modifiersScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		modifiersScrollPane.setPreferredSize(new Dimension(120, 150));

		unselectedModifiersList = new ToolTipList<>();
		unselectedModifiersList.setToolTipFunction(modifier -> modifier.getDescription());
		modifiersScrollPane.add(unselectedModifiersList);
		unselectedModifiersList.setModel(unselectedModifiersModel);
		modifiersScrollPane.setViewportView(unselectedModifiersList);

		selectedModifiersList = new ToolTipList<>();
		selectedModifiersScrollPane.add(selectedModifiersList);
		selectedModifiersList.setToolTipFunction(modifier -> modifier.getDescription());
		selectedModifiersList.setModel(selectedModifiersModel);
		selectedModifiersScrollPane.setViewportView(selectedModifiersList);

		buttonsPanel = new JPanel();
		modifiersPane.add(buttonsPanel);

		addModifierBtn = new JButton("<-- Add");
		addModifierBtn.addActionListener(arg0 -> {
			int index = -1;
			while((index = unselectedModifiersList.getSelectedIndex()) != -1)
				selectedModifiersModel.add(0, unselectedModifiersModel.remove(index));
			sortListModels();
		});
		buttonsPanel.setLayout(new GridLayout(0, 1, 0, 0));
		buttonsPanel.add(addModifierBtn);

		removeModifierBtn = new JButton("Remove -->");
		removeModifierBtn.addActionListener(e -> {
			int index = -1;
			while((index = selectedModifiersList.getSelectedIndex()) != -1)
				unselectedModifiersModel.add(0, selectedModifiersModel.remove(index));
			sortListModels();
		});
		buttonsPanel.add(removeModifierBtn);

		modifiersPane.add(modifiersScrollPane);

		pnlPort = new JPanel();
		mainPanel.add(pnlPort);

		lblPort = new JLabel("Port:");
		lblPort.setToolTipText("Don't change this unless you know what you're doing!");
		pnlPort.add(lblPort);

		spnPort = new JSpinner();
		spnPort.setToolTipText("Don't change this unless you know what you're doing!");
		spnPort.setModel(new SpinnerNumberModel(FEServer.DEFAULT_PORT, 0, 65565, 1));
		pnlPort.add(spnPort);
		
		pnlRNG = new JPanel();
		mainPanel.add(pnlRNG);
		
		lblHitRng = new JLabel("Hit RNG: ");
		pnlRNG.add(lblHitRng);
		
		
		cbbHitRNG = new JComboBox<>();
		cbbHitRNG.setModel(new DefaultComboBoxModel<RNG>(new RNG[] {new SimpleRNG(), new TrueHitRNG(), new GuaranteedRNG()}));
		cbbHitRNG.setSelectedIndex(1);
		pnlRNG.add(cbbHitRNG);
		
		//Secret settings :o
		lblCritRng = new JLabel("Crit RNG:");
		pnlRNG.add(lblCritRng);
		
		cbbCritRNG = new JComboBox<>();
		cbbCritRNG.setModel(new DefaultComboBoxModel<RNG>(new RNG[] {new SimpleRNG(), new NullRNG()}));
		pnlRNG.add(cbbCritRNG);
		
		lblSkillRng = new JLabel("Skill RNG:");
		pnlRNG.add(lblSkillRng);
		
		cbbSkillRNG = new JComboBox<>();
		cbbSkillRNG.setModel(new DefaultComboBoxModel<RNG>(new RNG[] {new SimpleRNG(), new NullRNG()}));
		pnlRNG.add(cbbSkillRNG);
		
		pnlFogOfWar = new JPanel();
		pnlFogOfWar.setAlignmentY(Component.TOP_ALIGNMENT);
		mainPanel.add(pnlFogOfWar);
		pnlFogOfWar.setLayout(new BorderLayout(0, 0));
		
		pnlToggleFog = new JPanel();
		pnlFogOfWar.add(pnlToggleFog, BorderLayout.NORTH);
		
		lblFogOfWar = new JLabel("Fog of war:");
		pnlToggleFog.add(lblFogOfWar);
		
		cbbFogOfWar = new JComboBox<FogType>();
		pnlToggleFog.add(cbbFogOfWar);
		cbbFogOfWar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean enabled = cbbFogOfWar.getSelectedItem() != FogType.NONE;
				cbbSpectatorFog.setEnabled(enabled);
				cbbSight.setEnabled(enabled);
				spnRegularSight.setEnabled(enabled);
				spnThiefSight.setEnabled(enabled);
				chckbxShowOutOf.setEnabled(enabled);
				chckbxAllowRedoingMovement.setEnabled(enabled);
			}
		});
		cbbFogOfWar.setModel(new DefaultComboBoxModel<FogType>(FogType.values()));
		
		panel_1 = new JPanel();
		pnlFogOfWar.add(panel_1, BorderLayout.SOUTH);
		panel_1.setLayout(new GridLayout(0, 1, 0, 0));
		
		panel_2 = new JPanel();
		panel_1.add(panel_2);
		
		lblSpectatorFog = new JLabel("Spectator fog:");
		panel_2.add(lblSpectatorFog);
		
		cbbSpectatorFog = new JComboBox<SpectatorFogOption>();
		panel_2.add(cbbSpectatorFog);
		cbbSpectatorFog.setEnabled(false);
		cbbSpectatorFog.setModel(new DefaultComboBoxModel<SpectatorFogOption>(SpectatorFogOption.values()));
		
		lblSight = new JLabel("Sight:");
		panel_2.add(lblSight);
		
		cbbSight = new JComboBox<>();
		cbbSight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SightOption option = (SightOption) cbbSight.getSelectedItem();
				spnRegularSight.removeChangeListener(spnSightChanged);
				spnThiefSight.removeChangeListener(spnSightChanged);
				
				spnRegularSight.setValue(option.getRegularSight());
				spnThiefSight.setValue(option.getThiefSight());
				
				spnRegularSight.addChangeListener(spnSightChanged);;
				spnThiefSight.addChangeListener(spnSightChanged);
			}
		});
		panel_2.add(cbbSight);
		cbbSight.setEnabled(false);
		Vector<SightOption> vector = new Vector<>();
		vector.add(new SightOption() {
			
			@Override
			public int getThiefSight() {
				return (Integer) spnThiefSight.getValue();
			}
			
			@Override
			public int getRegularSight() {
				return (Integer) spnRegularSight.getValue();
			}
			
			@Override
			public String toString() {
				return "Custom";
			}
		});
		vector.addAll(Arrays.asList(FESightOption.values()));
		cbbSight.setModel(new DefaultComboBoxModel<>(vector));
		
		panel_3 = new JPanel();
		panel_1.add(panel_3);
		
		lblRegularSight = new JLabel("Regular sight:");
		panel_3.add(lblRegularSight);
		
		spnRegularSight = new JSpinner();
		spnRegularSight.addChangeListener(spnSightChanged);
		spnRegularSight.setEnabled(false);
		spnRegularSight.setModel(new SpinnerNumberModel(0, 0, 255, 1));
		panel_3.add(spnRegularSight);
		
		lblThiefSight = new JLabel("Thief sight:");
		panel_3.add(lblThiefSight);
		
		spnThiefSight = new JSpinner();
		spnThiefSight.addChangeListener(spnSightChanged);
		spnThiefSight.setEnabled(false);
		spnThiefSight.setModel(new SpinnerNumberModel(0, 0, 255, 1));
		panel_3.add(spnThiefSight);

		startServer = new JButton("Start server");
		startServer.addActionListener(e -> {
			if (serverStart != null)
				serverStart.run();
		});
		add(startServer, BorderLayout.SOUTH);
		

		cbbSight.setSelectedItem(FESightOption.FE7_8);
		
		panel = new JPanel();
		panel_1.add(panel);
		
		chckbxShowOutOf = new JCheckBox("Show out of sight interruptions:");
		chckbxShowOutOf.setToolTipText("If this is selected, players will be able to see when a unit fails to complete an action due to the fog even when they're not in sight");
		chckbxShowOutOf.setEnabled(false);
		panel.add(chckbxShowOutOf);
		
		chckbxAllowRedoingMovement = new JCheckBox("Allow redoing movement");
		chckbxAllowRedoingMovement.setToolTipText("Uncheck this to prevent players from backing out of their movements to prevent \"scouting\". \r\nThis only affects moves that pass through a fogged tile.");
		chckbxAllowRedoingMovement.setSelected(true);
		chckbxAllowRedoingMovement.setEnabled(false);
		panel.add(chckbxAllowRedoingMovement);
	}
	
	private void sortListModels() {
		Modifier[] temp = new Modifier[unselectedModifiersModel.size()];
		unselectedModifiersModel.copyInto(temp);
		Arrays.sort(temp, (a, b) -> a.toString().compareTo(b.toString()));
		for(int i = 0; i < temp.length; i++) 
			unselectedModifiersModel.set(i, temp[i]);
		temp = new Modifier[selectedModifiersModel.size()];
		selectedModifiersModel.copyInto(temp);
		Arrays.sort(temp, (a, b) -> a.toString().compareTo(b.toString()));
		for(int i = 0; i < temp.length; i++) 
			selectedModifiersModel.set(i, temp[i]);
	}

	/**
	 * Sets the runnable that should be executed when the "Start server" button is pressed.<BR>
	 * Note : the runnable will not be executed in a separate thread.
	 * @param serverStart the runnable that should be executed when the "Start server" button is pressed.
	 */
	public void setServerStartRunnable(Runnable serverStart) {
		this.serverStart = serverStart;
	}

	/**
	 * Returns the port selected.
	 * @return The port.
	 */
	public int getPort() {
		return (Integer) spnPort.getValue();
	}

	/**
	 * Returns the session described by the current values set in the different components of the frame.
	 * @return the session described by the current values set in the different components of the frame.
	 */
	public Session getSession() {
		HashSet<Modifier> mods = new HashSet<Modifier>();
		for (int i = 0; i < selectedModifiersList.getModel().getSize(); i++)
			mods.add(selectedModifiersList.getModel().getElementAt(i));
		return new Session(
				(Objective) objComboBox.getSelectedItem(),
				(String) mapSelectionBox.getSelectedItem(),
				(Integer) maxUnitsSpinner.getValue(), mods,
				(PickMode) pickModeBox.getSelectedItem(),
				(RNG) cbbHitRNG.getSelectedItem(),
				(RNG) cbbCritRNG.getSelectedItem(),
				(RNG) cbbSkillRNG.getSelectedItem(),
				(FogType) cbbFogOfWar.getSelectedItem(),
				(SpectatorFogOption) cbbSpectatorFog.getSelectedItem(),
				((FESightOption) cbbSight.getSelectedItem()).getRegularSight(),
				((FESightOption) cbbSight.getSelectedItem()).getThiefSight(),
				chckbxShowOutOf.isSelected(),
				chckbxAllowRedoingMovement.isSelected()
			);
	}
	
	public static interface SightOption {
		public int getRegularSight();
		public int getThiefSight();
		public String toString();
	}
	
	private enum FESightOption implements SightOption {
		FE7_8(3, 8, "Blazing Sword/Sacred Stones"),
		FE5(3, 3, "Thracia"),
		FE9_10(3, 5, "Tellius"),
		FE6(5, 10, "Binding Blade"),
		FE11_12(2, 3, "SNES remake");
		
		private String name;
		private voidToIntFunction regularSight;
		private voidToIntFunction thiefSight;
		
		private FESightOption(int regularSight, int thiefSight, String name) {
			this.name = name;
			this.regularSight = () -> regularSight;
			this.thiefSight = () -> thiefSight;
		}
		

		@Override
		public String toString() {
			return name;
		}
		
		public int getRegularSight() {
			return regularSight.eval();
		}
		
		public int getThiefSight() {
			return thiefSight.eval();
		}
		
		private static interface voidToIntFunction {
			int eval();
		}
	}
}
