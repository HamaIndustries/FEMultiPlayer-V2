package net.fe.balance;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.HashMap;

import javax.swing.*;
import javax.swing.event.*;

// TODO: Auto-generated Javadoc
/**
 * The Class BalancerRow.
 */
public class BalancerRow extends JPanel{
	
	/** The data. */
	private BalanceData data;
	
	/** The tab. */
	private BalancerTab tab;
	
	/** The values. */
	private HashMap<String, JLabel> values;
	
	/**
	 * Instantiates a new balancer row.
	 *
	 * @param d the d
	 * @param tab the tab
	 */
	public BalancerRow(BalanceData d, BalancerTab tab){
		this.data = d;
		this.tab = tab;
		values = new HashMap<String, JLabel>();
		setVerticalAlignment(SwingConstants.TOP);
		add(new JLabel(d.name){{
			setPreferredSize(new Dimension(60,3*getPreferredSize().height));
			setHorizontalAlignment(SwingConstants.RIGHT);
			setVerticalAlignment(SwingConstants.TOP);
		}});
		JPanel allStats = new JPanel(new GridLayout(3, 8));
			for(String stat: BalanceData.ORDER){
				StatSpinner spinner = new StatSpinner(stat, d.bases.get(stat), 1);
				spinner.addChangeListener(new ChangeListener(){
					@Override
					public void stateChanged(ChangeEvent e) {
						StatSpinner source = (StatSpinner) e.getSource();
						data.bases.put(source.name, (Integer)source.getValue());
						refresh();
					}
				});
				allStats.add(spinner);
			}
			
			for(String stat: BalanceData.ORDER){
				StatSpinner spinner = new StatSpinner(stat, d.growths.get(stat), 5);
				spinner.addChangeListener(new ChangeListener(){
					@Override
					public void stateChanged(ChangeEvent e) {
						StatSpinner source = (StatSpinner) e.getSource();
						data.growths.put(source.name, (Integer)source.getValue());
						refresh();
					}
				});
				allStats.add(spinner);
			}

			for(String stat: BalanceData.ORDER){
				JLabel label = new JLabel();
				label.setHorizontalAlignment(SwingConstants.CENTER);
				values.put(stat, label);
				allStats.add(label);
			}
			
		add(allStats);
		refresh();
		setMaximumSize(getPreferredSize());
	}
	
	/**
	 * Sets the vertical alignment.
	 *
	 * @param top the new vertical alignment
	 */
	private void setVerticalAlignment(int top) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Refresh.
	 */
	public void refresh(){
		int lv = tab.getLevel();
		for(String stat: BalanceData.ORDER){
			int value = data.bases.get(stat) + (lv - 1) * data.growths.get(stat) / 100;
			values.get(stat).setText(value + "");
		}
	}
	
	/**
	 * Export string.
	 *
	 * @return the string
	 */
	public String exportString(){
		return data.exportString();
	}
}
