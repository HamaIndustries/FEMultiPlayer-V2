package net.fe.game.balance;

import java.awt.Dimension;

import javax.swing.*;

// TODO: Auto-generated Javadoc
/**
 * The Class StatSpinner.
 */
public class StatSpinner extends JSpinner {
	
	/** The name. */
	public final String name;
	
	/**
	 * Instantiates a new stat spinner.
	 *
	 * @param name the name
	 * @param initial the initial
	 * @param step the step
	 */
	public StatSpinner(String name, int initial, int step){
		super(new SpinnerNumberModel(initial,0,100,step));
		this.name = name;
	}
}
