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
	private final int maxUses;
	
	/** The uses. */
	private int uses;
	
	/** The cost. */
	private final int cost;
	
	/** The id. */
	public final int id;
	
	/**
	 * Instantiates a new item.
	 *
	 * @param name the name
	 */
	public Item(String name, int maxUses, int id, int cost){
		this.name = name;
		this.id = id;
		this.maxUses = maxUses;
		this.uses = maxUses;
		this.cost = cost;
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
	public final int getUses(){
		return uses;
	}
	
	/**
	 * Gets the max uses.
	 *
	 * @return the max uses
	 */
	public final int getMaxUses(){
		return maxUses;
	}
	
	/**
	 * Sets the uses debugging.
	 *
	 * @param uses the new uses debugging
	 */
	public final void setUsesDEBUGGING(int uses){
		this.uses = uses;
	}
	
	/**
	 * Gets the cost.
	 *
	 * @return the cost
	 */
	public final int getCost(){
		return cost;
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
	
	public static Iterable<Item> getAllItems() {
		final java.util.ArrayList<Item> retVal = new java.util.ArrayList<>();
		WeaponFactory.getAllWeapons().forEach(retVal::add);
		retVal.add(HealingItem.VULNERARY.getCopy());
		retVal.add(HealingItem.CONCOCTION.getCopy());
		retVal.add(HealingItem.ELIXIR.getCopy());
		retVal.add(new RiseTome());
		return retVal;
	}
	
	@Override
	public int hashCode() {
		return ((name.hashCode() * 31 +
				id) * 31 +
				maxUses) * 31 +
				cost;
	}
	
	protected boolean canEquals(Object other) {
		return other instanceof Item;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other != null && other instanceof Item) {
			Item o2 = (Item) other;
			if (o2.canEquals(this)) {
				return this.name.equals(o2.name) &&
					this.id == o2.id &&
					this.maxUses == o2.maxUses &&
					this.uses == o2.uses &&
					this.cost == o2.cost;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
}
