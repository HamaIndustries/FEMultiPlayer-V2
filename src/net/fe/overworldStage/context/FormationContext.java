package net.fe.overworldStage.context;

import net.fe.Player;

public interface FormationContext {
	/**
	 * Called when a player is done with formations.
	 * @param player The player.
	 */
	public void removePlayer(byte id);
}