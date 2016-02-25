package net.fe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

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
public class Session implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 696432583909698581L;
	
	/** The players. */
	private HashMap<Byte, Player> players;
	
	/** The objective. */
	private Objective objective;
	
	/** The map. */
	private String map;
	
	/** The max units. */
	private int maxUnits;
	
	/** The modifiers. */
	private Set<Modifier> modifiers;
	
	/** The pick mode. */
	private PickMode pickMode;
	
	/**
	 * Instantiates a new session.
	 */
	public Session() {
		players = new HashMap<Byte, Player>();
		objective = new Rout();
		modifiers = new HashSet<Modifier>();
		pickMode = new Draft();
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
	 * Sets the objective.
	 *
	 * @param objective the new objective
	 */
	public void setObjective(Objective objective) {
		this.objective = objective;
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
	 * Sets the map.
	 *
	 * @param map the new map
	 */
	public void setMap(String map) {
		this.map = map;
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
	 * Sets the max units.
	 *
	 * @param i the new max units
	 */
	public void setMaxUnits(int i) {
		maxUnits = i;
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
	 * Adds the modifier.
	 *
	 * @param m the m
	 */
	public void addModifier(Modifier m) {
		modifiers.add(m);
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
	 * Sets the pick mode.
	 *
	 * @param selectedItem the new pick mode
	 */
	public void setPickMode(PickMode selectedItem) {
		pickMode = selectedItem;
	}
	
	
}
