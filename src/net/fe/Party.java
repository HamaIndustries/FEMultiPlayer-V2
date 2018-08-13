package net.fe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.newdawn.slick.Color;

import net.fe.unit.Unit;

// TODO: Auto-generated Javadoc
/**
 * The Class Party.
 */
public class Party implements Iterable<Unit>, Serializable{
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1334090578185765598L;
	
	/** The color. */
	private Color color;
	
	/** The units. */
	private ArrayList<Unit> units;
	
	/** The allies. */
	private ArrayList<Party> allies;
	
	/** The Constant TEAM_RED. */
	public static final Color TEAM_RED = new Color(220,0,0);
	
	/** The Constant TEAM_GREEN. */
	public static final Color TEAM_GREEN = new Color(0,190,0);
	
	/** The Constant TEAM_BLUE. */
	public static final Color TEAM_BLUE = new Color(0,0,220);
	
	/**
	 * Instantiates a new party.
	 */
	public Party() {
		units = new ArrayList<Unit>();
		allies = new ArrayList<Party>();
		allies.add(this);
		color = TEAM_BLUE;
	}
	
	/**
	 * Instantiates a new party.
	 *
	 * @param units the units
	 */
	public Party(ArrayList<Unit> units) {
		this.units = units;
		allies = new ArrayList<Party>();
		allies.add(this);
	}
	
	/**
	 * Adds the ally.
	 *
	 * @param p the p
	 */
	public void addAlly(Party p){
		allies.add(p);
	}
	
	/**
	 * Checks if is ally.
	 *
	 * @param p the p
	 * @return true, if is ally
	 */
	public boolean isAlly(Party p){
		return allies.contains(p);
	}
	
	/**
	 * Adds the unit.
	 *
	 * @param unit the unit
	 */
	public void addUnit(Unit unit) {
		units.add(unit);
		unit.setParty(this);
	}
	
	/**
	 * Gets the unit.
	 *
	 * @param index the index
	 * @return the unit
	 */
	public Unit getUnit(int index) {
		return units.get(index);
	}

	/**
	 * Gets the units.
	 *
	 * @return the units
	 */
	public List<Unit> getUnits() {
		return units;
	}
	
	/**
	 * Search.
	 *
	 * @param name the name
	 * @return the unit
	 */
	public Unit search(String name) {
		for(Unit u : units) {
			if(u.name.equals(name)){
				return u;
			}
		}
		return null;
	}
	
	/**
	 * Size.
	 *
	 * @return the int
	 */
	public int size() {
		return units.size();
	}
	
	/**
	 * Sets the color.
	 *
	 * @param c the new color
	 */
	public void setColor(Color c){
		color = c;
	}
	
	/**
	 * Gets the color.
	 *
	 * @return the color
	 */
	public Color getColor(){
		return color;
	}

	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<Unit> iterator() {
		return units.iterator();
	}

	/**
	 * Clear.
	 */
	public void clear() {
		units.clear();
	}
	
	public ArrayList<Party> getAllies() {
		return allies;
	}

}
