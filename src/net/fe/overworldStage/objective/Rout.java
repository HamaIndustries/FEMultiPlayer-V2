package net.fe.overworldStage.objective;

import net.fe.Player;
import net.fe.game.unit.Unit;
import net.fe.overworldStage.OverworldStage;

// TODO: Auto-generated Javadoc
/**
 * The Class Rout.
 */
public class Rout implements Objective {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -4499698067587946141L;

	/**
	 * Returns winner if only one player has remaining units.
	 *
	 * @param stage The current instance of OverworldStage being used in the client
	 * @return id of the winning player.
	 */
	@Override
	public int evaluate(OverworldStage stage) {
		int winner = -1;
		for(Player p : stage.getTurnOrder()) {
			boolean ded = true;
			for(Unit u : p.getParty()) {
				if(u.getHp() > 0)
					ded = false;
			}
			if(!ded) {
				if(winner == -1) {
					winner = p.getID();
				}
				else return -1;
			}
		}
		return winner;
	}

	/* (non-Javadoc)
	 * @see net.fe.overworldStage.objective.Objective#getDescription()
	 */
	@Override
	public String getDescription() {
		return "Rout the enemy";
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Rout";
	}

}
