package net.fe.network.message;

import java.util.List;

import net.fe.network.Message;
import net.fe.unit.Unit;

// TODO: Auto-generated Javadoc
/**
 * The Class PartyMessage.
 */
public class PartyMessage extends Message {

	/** The team data. */
	public List<Unit> teamData;
	
	/**
	 * Instantiates a new party message.
	 *
	 * @param data the data
	 */
	public PartyMessage(List<Unit> data) {
		super();
		teamData = data;
	}
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -6657648098112938492L;
	
	/* (non-Javadoc)
	 * @see net.fe.network.Message#toString()
	 */
	public String toString(){
		String ans = super.toString() + "\n";
		for(Unit u: teamData){
			ans += "\t" + u.name + " Lv" + u.getLevel() + "\n";
		}
		return ans;
	}
	
}
