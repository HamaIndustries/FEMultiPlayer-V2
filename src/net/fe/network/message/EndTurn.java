package net.fe.network.message;

import java.util.HashMap;
import java.util.function.Function;

import net.fe.FEMultiplayer;
import net.fe.game.unit.Unit;
import net.fe.game.unit.UnitIdentifier;
import net.fe.network.FEServer;
import net.fe.network.Message;

// TODO: Auto-generated Javadoc
/**
 * The Class EndTurn.
 */
public final class EndTurn extends Message {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 390213251720606794L;

	/** The unit hp. */
	private final HashMap<UnitIdentifier, Integer> unitHp;

	/**
	 * Instantiates a new end turn.
	 */
	public EndTurn() {
		unitHp = new HashMap<UnitIdentifier, Integer>();
		for (Unit u : FEMultiplayer.getLocalPlayer().getParty()) {
			unitHp.put(new UnitIdentifier(u), u.getHp());
		}
	}

	/**
	 * Checks that the HPs claimed by this EndTurn match what this thinks the
	 * HPs are
	 *
	 * @param dereference A function that converts a UnitIdentifier into a Unit
	 */
	public void checkHp(Function<UnitIdentifier, Unit> dereference) {
		for (UnitIdentifier u : unitHp.keySet()) {
			if (dereference.apply(u).getHp() != unitHp.get(u)) {
				throw new IllegalStateException("Desynched HP: " + u.name);
			}
		}
	}
}
