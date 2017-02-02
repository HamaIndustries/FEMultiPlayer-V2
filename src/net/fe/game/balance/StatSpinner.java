package net.fe.game.balance;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class StatSpinner extends JSpinner {

	public final String name;

	public StatSpinner(String name, int initial, int step) {
		super(new SpinnerNumberModel(initial, 0, 100, step));
		this.name = name;
	}
}
