package net.fe.unit;


// TODO: Auto-generated Javadoc
/**
 * The Class HealingItem.
 */
public final class HealingItem extends Item {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -6169687038185884864L;
	
	/** The vulnerary. */
	public static final HealingItem VULNERARY = new HealingItem("Vulnerary", 10, 0, 300);
	
	/** The concoction. */
	public static final HealingItem CONCOCTION = new HealingItem("Concoction", 20, 1, 1300);
	
	/** The elixir. */
	public static final HealingItem ELIXIR = new HealingItem("Elixir", 99, 2, 3000);
	
	/** The amount. */
	public final int amount;
	
	/**
	 * Instantiates a new healing item.
	 *
	 * @param name the name of the item
	 * @param amount the amount to heal by
	 * @param id the icon index
	 * @param cost the item's shop price
	 */
	public HealingItem(String name, int amount, int id, int cost){
		super(name, 3, id, cost);
		this.amount = amount;
	}
	
	/* (non-Javadoc)
	 * @see net.fe.unit.Item#use(net.fe.unit.Unit)
	 */
	public int use(Unit user){
		super.use(user);
		int maxHeal = user.get("HP") - user.getHp();
		user.setHp(user.getHp() + Math.min(amount, maxHeal));
		return amount;
	}
	
	/* (non-Javadoc)
	 * @see net.fe.unit.Item#getCopy()
	 */
	public HealingItem getCopy(){
		return new HealingItem(name, amount, id, getCost());
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Item that) {
		if(that instanceof HealingItem){
			return amount - ((HealingItem) that).amount;
		} else if(that instanceof RiseTome){
			return -1;
		} else {
			return 1;
		}
	}
	
	@Override
	public int hashCode() {
		return super.hashCode() * 31 + amount;
	}
	
	@Override
	protected boolean canEquals(Object other) {
		return other instanceof HealingItem;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other != null && other instanceof HealingItem) {
			HealingItem o2 = (HealingItem) other;
			if (o2.canEquals(this)) {
				return super.equals(o2) &&
					this.amount == o2.amount;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	@Override
	public String toString() {
		return "HealingItem [" +
			"name: " + name + "; " +
			"maxUses: " + getMaxUses() + "; " +
			"uses: " + getUses() + "; " +
			"id: " + id + "; " +
			"cost: " + getCost() + "; " +
			"amount: " + amount + "]";
	}
}
