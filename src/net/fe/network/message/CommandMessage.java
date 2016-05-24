package net.fe.network.message;

import java.util.ArrayList;
import java.util.Arrays;

import net.fe.fightStage.AttackRecord;
import net.fe.network.Message;
import net.fe.network.command.Command;
import net.fe.unit.UnitIdentifier;

// TODO: Auto-generated Javadoc
/**
 * A message indicating actions that a Unit should take
 */
public final class CommandMessage extends Message {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 8131511231319584473L;
	
	/** The unit. */
	public final UnitIdentifier unit;
	
	/** The move x. */
	public final int moveX;
	
	/** The move y. */
	public final int moveY;
	
	/** The commands. */
	public final Command[] commands;
	
	/** The attack records. */
	public ArrayList<AttackRecord> attackRecords;
	
	/**
	 * Instantiates a new command message.
	 *
	 * @param unit the unit
	 * @param moveX the move x
	 * @param moveY the move y
	 * @param atk the atk
	 * @param commands the commands
	 */
	public CommandMessage(UnitIdentifier unit, 
			int moveX, int moveY, ArrayList<AttackRecord> atk, Command... commands) {
		this.commands = commands;
		this.unit = unit;
		this.moveX = moveX;
		this.moveY = moveY;
		this.attackRecords = atk;
	}
	
	/* (non-Javadoc)
	 * @see net.fe.network.Message#toString()
	 */
	public String toString(){
		if(unit == null){
			return super.toString() + Arrays.toString(commands);
		} else {
			return super.toString() + unit.name + " move (" + moveX + ", " + moveY + "):" + Arrays.toString(commands);
		}
	}

}

