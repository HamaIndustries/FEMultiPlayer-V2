package net.fe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import net.fe.network.Chat;
import net.fe.network.Message;
import net.fe.network.message.ChatMessage;
import net.fe.network.message.JoinTeam;
import net.fe.network.message.JoinLobby;
import net.fe.network.message.QuitMessage;
import net.fe.modifier.Modifier;
import net.fe.overworldStage.objective.Objective;
import net.fe.overworldStage.objective.Rout;
import net.fe.pick.Draft;
import net.fe.pick.PickMode;

// TODO: Auto-generated Javadoc
/**
 * Contains data on game setup and players.
 * @author Shawn
 *
 */
public final class Session implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 696432583909698581L;
	
	/** The players. */
	private final HashMap<Byte, Player> players;
	
	/** The chatlog */
	private final Chat chatlog;
	
	/** The objective. */
	private final Objective objective;
	
	/** The map. */
	private final String map;
	
	/** The max units. */
	private final int maxUnits;
	
	/** The modifiers. */
	private final Set<Modifier> modifiers;
	
	/** The pick mode. */
	private final PickMode pickMode;
	
	/**
	 * Instantiates a new session with default values.
	 */
	public Session() {
		this(new Rout(), "test", 8, new HashSet<>(), new Draft());
	}
	
	/**
	 * Instantiates a new session with the specified values
	 */
	public Session(Objective objective, String map, int maxUnits, Set<Modifier> modifiers, PickMode pickMode) {
		players = new HashMap<Byte, Player>();
		this.chatlog = new Chat();
		this.objective = objective;
		this.modifiers = java.util.Collections.unmodifiableSet(new HashSet<Modifier>(modifiers));
		this.maxUnits = maxUnits;
		this.map = map;
		this.pickMode = pickMode;
	}
	
	/**
	 * Adds the player.
	 *
	 * @param p the p
	 */
	public void addPlayer(Player p) {
		players.put(p.getID(), p);
	}
	
	/**
	 * Adds the player.
	 *
	 * @param id the id
	 * @param p the p
	 */
	public void addPlayer(byte id, Player p) {
		players.put(id, p);
		p.setClientID(id);
	}
	
	/**
	 * Removes the player.
	 *
	 * @param p the p
	 * @return the player
	 */
	public Player removePlayer(Player p) {
		return players.remove(p.getID());
	}
	
	/**
	 * Removes the player.
	 *
	 * @param id the id
	 * @return the player
	 */
	public Player removePlayer(byte id) {
		return players.remove(id);
	}
	
	/**
	 * Gets the player.
	 *
	 * @param id the id
	 * @return the player
	 */
	public Player getPlayer(byte id) {
		return players.get(id);
	}
	
	/**
	 * Num players.
	 *
	 * @return the int
	 */
	public int numPlayers() {
		return players.size();
	}
	
	/**
	 * Gets the player map.
	 *
	 * @return the player map
	 */
	public HashMap<Byte, Player> getPlayerMap() {
		return players;
	}
	
	/**
	 * Gets the players.
	 *
	 * @return the players
	 */
	public Player[] getPlayers() {
		return players.values().toArray(new Player[players.size()]);
	}
	
	/**
	 * Returns a list of players, filtered to not include players that are spectators
	 */
	public Player[] getNonSpectators() {
		ArrayList<Player> ans = new ArrayList<Player>();
		for(Player p : this.getPlayers()) {
			if(!p.isSpectator()) ans.add(p);
		}
		return ans.toArray(new Player[ans.size()]);
	}
	
	/**
	 * Gets the objective.
	 *
	 * @return the objective
	 */
	public Objective getObjective() {
		return objective;
	}
	
	/**
	 * Gets the chatlog.
	 * @return the chatlog
	 */
	public Chat getChatlog() {
		return chatlog;
	}
	
	/**
	 * Gets the map.
	 *
	 * @return the map
	 */
	public String getMap() {
		return map;
	}
	
	/**
	 * Gets the max units.
	 *
	 * @return the max units
	 */
	public int getMaxUnits() {
		return maxUnits;
	}
	
	/**
	 * Gets the modifiers.
	 *
	 * @return the modifiers
	 */
	public Set<Modifier> getModifiers() {
		return modifiers;
	}
	
	/**
	 * Gets the pick mode.
	 *
	 * @return the pick mode
	 */
	public PickMode getPickMode() {
		return pickMode;
	}

	/**
	 * Perform an action in response to the message
	 */
	public void handleMessage(Message message) {
		if(message instanceof JoinLobby) {
			JoinLobby join = (JoinLobby)message;
			this.addPlayer((byte) join.origin, new Player(join.nickname, join.origin));
		} else if(message instanceof QuitMessage) {
			QuitMessage quit = (QuitMessage)message;
			Player player = this.getPlayer(quit.origin);
			if (player != null) {
				this.getChatlog().add(player, "has quit the game");
			}
			this.removePlayer(quit.origin);
		} else if(message instanceof JoinTeam) {
			JoinTeam join = (JoinTeam)message;
			this.getPlayer(join.origin).setTeam(join.team);
			if(join.team == Player.TEAM_BLUE) {
				this.getPlayer(join.origin).getParty().setColor(Party.TEAM_BLUE);
			} else if(join.team == Player.TEAM_RED) {
				this.getPlayer(join.origin).getParty().setColor(Party.TEAM_RED);
			}
			this.getPlayer(join.origin).ready = false;
		} else if (message instanceof ChatMessage) {
			ChatMessage chatMsg = (ChatMessage)message;
			this.getChatlog().add(this.getPlayer(chatMsg.origin), chatMsg.text);
		}
	}
	
}
