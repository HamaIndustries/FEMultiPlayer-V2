package net.fe.network.message;

import java.util.ArrayList;
import java.util.Arrays;

import net.fe.fightStage.AttackRecord;
import net.fe.network.Message;
import net.fe.unit.UnitIdentifier;

// TODO: Auto-generated Javadoc
/**
 * The Class CommandMessage.
 */
public class CommandMessage extends Message {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 8131511231319584473L;
	
	/** The unit. */
	public UnitIdentifier unit;
	
	/** The move x. */
	public int moveX;
	
	/** The move y. */
	public int moveY;
	
	/** The commands. */
	public Object[] commands;
	
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
			int moveX, int moveY, ArrayList<AttackRecord> atk, Object... commands) {
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

