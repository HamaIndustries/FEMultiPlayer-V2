package net.fe.overworldStage.objective;

import net.fe.Player;
import net.fe.game.unit.Unit;
import net.fe.overworldStage.OverworldStage;

// TODO: Auto-generated Javadoc
/**
 * The Class Seize.
 */
public class Seize implements Objective {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1608192440668201886L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.fe.overworldStage.objective.Objective#evaluate(net.fe.overworldStage.
	 * OverworldStage)
	 */
	@Override
	public int evaluate(OverworldStage stage) {

		// If a player has a Lord on the other player's throne, they win
		// Alternatively, if a player's Lord dies, they lose
		int winner = -1;
		for (Player p : stage.getNonSpectators()) {
			boolean hasLord = false;
			for (int i = 0; i < p.getParty().size(); i++) {
				Unit u = p.getParty().getUnit(i);
				if (u.getUnitClass().name.equals("Lord") && u.getHp() > 0) {
					hasLord = true;
					System.out.println(p.getName() + " has a Lord!");
				}
				if (stage.grid.canSeize(u)) {
					return p.getID();
				}
			}
			if (hasLord) {
				if (winner == -1) {
					winner = p.getID();
				} else {
					winner = -2;
				}
			}
		}
		if (winner > 0) {
			System.out.println(winner + " has a Lord and wins!");
			return winner;
		} else
			return -1;
	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.fe.overworldStage.objective.Objective#getDescription()
	 */
	@Override
	public String getDescription() {
		return "Seize the throne";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Seize";
	}

}
