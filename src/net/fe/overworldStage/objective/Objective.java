package net.fe.overworldStage.objective;

import java.io.Serializable;

import net.fe.overworldStage.OverworldStage;

// TODO: Auto-generated Javadoc
/**
 * The Interface Objective.
 */
public interface Objective extends Serializable {

	/**
	 * Returns the client ID of the winner, or -1 if there is no winner.
	 *
	 * @param stage the stage
	 * @return the int
	 */
	public int evaluate(OverworldStage stage);

	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription();
}
