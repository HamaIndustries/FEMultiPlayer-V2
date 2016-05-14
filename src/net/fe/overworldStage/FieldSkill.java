package net.fe.overworldStage;

import java.io.Serializable;

import net.fe.unit.Unit;

/**
 * An action that can a unit can perform after moving
 * @TODO: figure out a way to get OverworldStage to ask this how to processCommands
 */
public abstract class FieldSkill implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 6468268282716381357L;
	
	/**
	 * 
	 */
	public FieldSkill(){
	}
	
	/**
	 * Checks whether the unit is allowed to use the specified skill
	 * @param actor the unit that wants to use a skill
	 * @param grid the grid containing the unit
	 */
	public abstract boolean allowed(Unit actor, Grid grid);
	
	/**
	 * Returns the context to start when this command is selected
	 */
	public abstract OverworldContext onSelect(ClientOverworldStage stage, OverworldContext context, Zone z, Unit unit);
	
	/**
	 * Returns a set of spaces which contain possible - not neccessarily valid - targets of this action.
	 */
	public abstract Zone getZone(Unit actor, Grid grid);
	
	/**
	 * Returns the displayed name of this skill
	 */
	public String getName(){
		return getClass().getSimpleName();
	}
	
}
