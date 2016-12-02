package net.fe.overworldStage;

import chu.engine.entity.Entity;
import net.fe.Player;

// TODO: Auto-generated Javadoc
/**
 * The Class TerrainTrigger.
 */
public abstract class TerrainTrigger {

	/** The start. */
	public final boolean start;

	/**
	 * Instantiates a new terrain trigger.
	 *
	 * @param startTurn the start turn
	 */
	public TerrainTrigger(boolean startTurn) {
		start = startTurn;
	}

	/**
	 * Attempt.
	 *
	 * @param g the g
	 * @param x the x
	 * @param y the y
	 * @param turnPlayer the turn player
	 * @return true, if successful
	 */
	public boolean attempt(OverworldStage g, int x, int y, Player turnPlayer) {
		return false;
	}

	/**
	 * Start of turn.
	 *
	 * @param g the g
	 * @param x the x
	 * @param y the y
	 */
	public void startOfTurn(OverworldStage g, int x, int y) {

	}

	/**
	 * End of turn.
	 *
	 * @param g the g
	 * @param x the x
	 * @param y the y
	 */
	public void endOfTurn(OverworldStage g, int x, int y) {

	}

	/**
	 * Gets the animation.
	 *
	 * @param g the g
	 * @param x the x
	 * @param y the y
	 * @return the animation
	 */
	public Entity getAnimation(OverworldStage g, int x, int y) {
		return null;
	}
}
