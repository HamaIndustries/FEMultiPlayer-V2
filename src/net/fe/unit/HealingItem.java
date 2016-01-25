package net.fe.unit;


public class HealingItem extends Item {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6169687038185884864L;
	public static HealingItem VULNERARY = new HealingItem("Vulnerary", 10, 70, 300);
	public static HealingItem CONCOCTION = new HealingItem("Concoction", 20, 71, 1300);
	public static HealingItem ELIXIR = new HealingItem("Elixir", 99, 72, 3000);
	
	public int amount;
	public HealingItem(String name, int amount, int id, int cost){
		super(name);
		setMaxUses(3);
		this.amount = amount;
		this.id = id;
		setCost(cost);
	}
	public int use(Unit user){
		super.use(user);
		int maxHeal = user.get("HP") - user.getHp();
		user.setHp(user.getHp() + Math.min(amount, maxHeal));
		return amount;
	}
	
	public HealingItem getCopy(){
		return new HealingItem(name, amount, id, getCost());
	}
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
