package net.fe.unit;

import java.io.Serializable;


// TODO: Auto-generated Javadoc
/**
 * The Class Item.
 */
public abstract class Item implements Serializable, Comparable<Item>{
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 210303763886733870L;
	
	/** The name. */
	public final String name;
	
	/** The max uses. */
	private int maxUses;
	
	/** The uses. */
	private int uses;
	
	/** The cost. */
	private int cost;
	
	/** The id. */
	public int id;
	
	/**
	 * Instantiates a new item.
	 *
	 * @param name the name
	 */
	public Item(String name){
		this.name = name;
	}
	
	/**
	 * Use.
	 *
	 * @param user the user
	 * @return the int
	 */
	int use(Unit user){
		uses--;
		return 0;
	}
	
	/**
	 * Gets the uses.
	 *
	 * @return the uses
	 */
	public int getUses(){
		return uses;
	}
	
	/**
	 * Sets the max uses.
	 *
	 * @param x the new max uses
	 */
	public void setMaxUses(int x){
		uses = x;
		maxUses = x;
	}
	
	/**
	 * Gets the max uses.
	 *
	 * @return the max uses
	 */
	public int getMaxUses(){
		return maxUses;
	}
	
	/**
	 * Sets the uses debugging.
	 *
	 * @param uses the new uses debugging
	 */
	public void setUsesDEBUGGING(int uses){
		this.uses = uses;
	}
	
	/**
	 * Gets the cost.
	 *
	 * @return the cost
	 */
	public int getCost(){
		return cost;
	}
	
	/**
	 * Sets the cost.
	 *
	 * @param gold the new cost
	 */
	public void setCost(int gold){
		cost = gold;
	}
	
	/**
	 * Gets the copy.
	 *
	 * @return the copy
	 */
	public abstract Item getCopy();
	
	/**
	 * Gets the item.
	 *
	 * @param name the name
	 * @return the item
	 */
	public static Item getItem(String name){
		if(name.equals("Vulnerary")) return HealingItem.VULNERARY.getCopy();
		if(name.equals("Concoction")) return HealingItem.CONCOCTION.getCopy();
		if(name.equals("Elixir")) return HealingItem.ELIXIR.getCopy();
		if(name.equals("Rise")) return new RiseTome();
		return WeaponFactory.getWeapon(name);
	}
}
