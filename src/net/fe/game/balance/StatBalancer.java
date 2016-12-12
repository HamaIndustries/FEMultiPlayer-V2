package net.fe.game.balance;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import net.fe.game.unit.Unit;
import net.fe.game.unit.UnitFactory;
import net.fe.game.unit.WeaponFactory;

public class StatBalancer extends JFrame {

	private static final long serialVersionUID = 1L;

	private TreeMap<String, BalancerTab> classTabs;

	public StatBalancer(List<Unit> units) {
		super("FE:Multiplayer Stat Balancer");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		classTabs = new TreeMap<String, BalancerTab>();

		JTabbedPane panel = new JTabbedPane();

		HashMap<String, ArrayList<BalanceData>> data = new HashMap<String, ArrayList<BalanceData>>();
		for (Unit u : units) {
			String unitClass = u.getUnitClass().name;
			if (!data.containsKey(unitClass)) {

				data.put(unitClass, new ArrayList<BalanceData>());
			}
			data.get(unitClass).add(new BalanceData(u));
		}

		for (String unitClass : data.keySet()) {

			BalancerTab tab = new BalancerTab(data.get(unitClass));
			classTabs.put(unitClass, tab);
			panel.addTab(unitClass, tab);
		}

		add(panel);

		JButton button = new JButton("Export");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser(".");
				int ret = chooser.showSaveDialog(StatBalancer.this);
				if (ret == JFileChooser.APPROVE_OPTION) {
					export(chooser.getSelectedFile());
				}
			}
		});
		add(button, BorderLayout.SOUTH);

		pack();
	}

	private void export(File f) {
		try {
			PrintWriter pw = new PrintWriter(f);
			String head = "#Name\t\tClass\t\t\tLv\t";
			for (String stat : BalanceData.ORDER) {
				head += stat + "\t";
			}
			head += "Con\tMov\t\t";
			for (String stat : BalanceData.ORDER) {
				head += stat + "\t";
			}
			head += "\tGender";
			pw.println(head);
			for (String unitClass : classTabs.keySet()) {
				pw.println(classTabs.get(unitClass).exportString());
			}
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				WeaponFactory.loadWeapons();
				UnitFactory.loadUnits();
				StatBalancer balancer = new StatBalancer(UnitFactory.getAllUnits());
				balancer.setLocationRelativeTo(null);
				balancer.setVisible(true);
			}
		});
	}
}
