package net.fe.network.command;

import java.io.Serializable;
import java.util.ArrayList;

import net.fe.overworldStage.OverworldStage;
import net.fe.game.fightStage.AttackRecord;
import net.fe.game.unit.Unit;
import net.fe.overworldStage.ClientOverworldStage;

import java.util.Optional;

/**
 * Represents A unit portion of a unit's actions during a turn
 */
public abstract class Command implements Serializable {

	private static final long serialVersionUID = 6468268282716381357L;

	/**
	 * Perform the command on a server
	 * 
	 * @param stage the model to act upon
	 * @param mainUnit the primary unit performing actions
	 * @throws IllegalStateException if the command is invalid
	 * @return if the command produces attack records, those records. Else null.
	 */
	public abstract ArrayList<AttackRecord> applyServer(OverworldStage stage, Unit mainUnit);

	/**
	 * Perform the command on a client
	 * 
	 * @param stage the model to act upon
	 * @param mainUnit the primary unit performing actions
	 * @param callback a runnable to call after this has done whatever it does
	 */
	public abstract Runnable applyClient(ClientOverworldStage stage, Unit mainUnit,
	        ArrayList<AttackRecord> attackRecords, Runnable callback);
}
