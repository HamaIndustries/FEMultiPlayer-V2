package net.fe.network;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

import net.fe.Player;

// TODO: Auto-generated Javadoc
/**
 * Manages list of chat messages.
 *
 * @author Shawn
 */
public final class Chat implements Serializable {
	
	private static final long serialVersionUID = -3975855319618106122L;
	
	/** The chat messages. */
	private final ArrayList<Chatlog> chatMessages;

	/**
	 * Instantiates a new chat.
	 */
	public Chat() {
		chatMessages = new ArrayList<Chatlog>();
	}
	
	/**
	 * Adds the.
	 *
	 * @param p the p
	 * @param line the line
	 */
	public void add(Player p, String line) {
		chatMessages.add(new Chatlog(p, line));
	}
	
	
	/**
	 * Gets the last i messages from this chat
	 *
	 * @param i the number of messages to return
	 * @return A list of size <var>i</var> containing the most recent messages added to this Chat.
	 *       If there are not at least i messages, this is filled with ""s to reach the desired size.
	 */
	public List<String> getLast(int i) {
		List<String> list = new ArrayList<String>();
		for(int j = chatMessages.size()-i; j < chatMessages.size(); j++) {
			if(j < 0) {
				list.add("");
			} else {
				list.add(chatMessages.get(j).toString());
			}
		}
		return list;
	}
	
	/**
	 * The Class Chatlog.
	 */
	private final class Chatlog implements Serializable {
		
		private static final long serialVersionUID = -3975855319618106122L;
		
		/** The player. */
		Player player;
		
		/** The line. */
		String line;
		
		/**
		 * Instantiates a new chatlog.
		 *
		 * @param p the p
		 * @param s the s
		 */
		public Chatlog(Player p, String s) {
			player = p;
			line = s;
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		public String toString() {
			if(player == null) {
				return line;
			} else {
				return player.getName() + ": " + line;
			}
		}
	}
	
}
