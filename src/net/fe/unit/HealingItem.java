package net.fe.unit;


// TODO: Auto-generated Javadoc
/**
 * The Class HealingItem.
 */
public class HealingItem extends Item {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -6169687038185884864L;
	
	/** The vulnerary. */
	public static HealingItem VULNERARY = new HealingItem("Vulnerary", 10, 70, 300);
	
	/** The concoction. */
	public static HealingItem CONCOCTION = new HealingItem("Concoction", 20, 71, 1300);
	
	/** The elixir. */
	public static HealingItem ELIXIR = new HealingItem("Elixir", 99, 72, 3000);
	
	/** The amount. */
	public int amount;
	
	/**
	 * Instantiates a new healing item.
	 *
	 * @param name the name
	 * @param amount the amount
	 * @param id the id
	 * @param cost the cost
	 */
	public HealingItem(String name, int amount, int id, int cost){
		super(name);
		setMaxUses(3);
		this.amount = amount;
		this.id = id;
		setCost(cost);
	}
	
	/* (non-Javadoc)
	 * @see net.fe.unit.Item#use(net.fe.unit.Unit)
	 */
	public int use(Unit user){
		super.use(user);
		int maxHeal = user.getStats().maxHp - user.getHp();
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
}
