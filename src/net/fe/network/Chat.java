package net.fe.network;

import java.util.ArrayList;
import java.util.List;

import net.fe.Player;

// TODO: Auto-generated Javadoc
/**
 * Manages list of chat messages.
 *
 * @author Shawn
 */
public class Chat {
	
	/** The chat messages. */
	private ArrayList<Chatlog> chatMessages;

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
	 * Gets the.
	 *
	 * @param i the i
	 * @return the string
	 */
	public String get(int i) {
		return chatMessages.get(i).toString();
	}
	
	/**
	 * Gets the last.
	 *
	 * @param i the i
	 * @return the last
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
	private class Chatlog {
		
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
