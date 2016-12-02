package net.fe.network.message;

import java.util.ArrayList;
import java.util.Arrays;

import net.fe.game.fightStage.AttackRecord;
import net.fe.game.unit.UnitIdentifier;
import net.fe.network.Message;
import net.fe.network.command.Command;

// TODO: Auto-generated Javadoc
/**
 * A message indicating actions that a Unit should take
 */
public final class CommandMessage extends Message {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 8131511231319584473L;
	
	/** The unit. */
	public final UnitIdentifier unit;
	
	/** The commands. */
	public final Command[] commands;
	
	/** The attack records. */
	public ArrayList<AttackRecord> attackRecords;
	
	/**
	 * Instantiates a new command message.
	 *
	 * @param unit the unit
	 * @param atk the atk
	 * @param commands the commands
	 */
	public CommandMessage(UnitIdentifier unit, 
			ArrayList<AttackRecord> atk, Command... commands) {
		this.commands = commands;
		this.unit = unit;
		this.attackRecords = atk;
	}
	
	/* (non-Javadoc)
	 * @see net.fe.network.Message#toString()
	 */
	public String toString(){
		if(unit == null){
			return super.toString() + Arrays.toString(commands);
		} else {
			return super.toString() + unit.name + Arrays.toString(commands);
		}
	}

}

