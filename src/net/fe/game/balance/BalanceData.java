package net.fe.game.balance;

import java.util.HashMap;

import net.fe.game.unit.Unit;

// TODO: Auto-generated Javadoc
/**
 * The Class BalanceData.
 */
public class BalanceData {

	/** The unit. */
	private Unit unit;

	/** The name. */
	public String name;

	/** The bases. */
	public HashMap<String, Integer> bases;

	/** The growths. */
	public HashMap<String, Integer> growths;

	/** The Constant ORDER. */
	public static final String[] ORDER = { "HP", "Str", "Mag", "Skl", "Spd", "Lck", "Def", "Res" };

	/**
	 * Instantiates a new balance data.
	 *
	 * @param u the u
	 */
	public BalanceData(Unit u) {
		unit = u;
		name = u.name;
		bases = new HashMap<String, Integer>(u.bases.toMap());
		growths = new HashMap<String, Integer>(u.growths.toMap());
	}

	/**
	 * Export string.
	 *
	 * @return the string
	 */
	public String exportString() {
		String ans = String.format("%-12s%-16s20\t", name, unit.noGenderName());
		for (String stat : ORDER) {
			ans += bases.get(stat) + "\t";
		}
		ans += unit.getStats().con + "\t" + unit.getStats().mov + "\t\t";
		for (String stat : ORDER) {
			ans += growths.get(stat) + "\t";
		}
		ans += "\t" + unit.gender + "\n";
		return ans;
	}
}
