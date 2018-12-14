package net.fe.unit;

/**
 * An item which increases a unit's skillCharge when used
 */
public final class SkillChargingItem extends Item {
	private static final long serialVersionUID = 1L;
	
	public static final SkillChargingItem INC10 = new SkillChargingItem("Olivi Grass", 10, 4, 3000);
	
	/** The amount to increase skillCharge by */
	public final int amount;
	
	/**
	 * Instantiates a new healing item.
	 *
	 * @param name the name of the item
	 * @param amount the amount to heal by
	 * @param id the icon index
	 * @param cost the item's shop price
	 */
	public SkillChargingItem(String name, int amount, int id, int cost){
		super(name, 3, id, cost);
		this.amount = amount;
	}
	
	@Override
	void use(Unit user){
		super.use(user);
		user.incrementSkillCharge(amount);
	}
	
	@Override
	public SkillChargingItem getCopy(){
		return new SkillChargingItem(name, amount, id, getCost());
	}
	
	@Override
	public int compareTo(Item that) {
		if(that instanceof SkillChargingItem){
			return amount - ((SkillChargingItem) that).amount;
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
		return other instanceof SkillChargingItem;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other != null && other instanceof SkillChargingItem) {
			SkillChargingItem o2 = (SkillChargingItem) other;
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
		return "SkillChargingItem [" +
			"name: " + name + "; " +
			"maxUses: " + getMaxUses() + "; " +
			"uses: " + getUses() + "; " +
			"id: " + id + "; " +
			"cost: " + getCost() + "; " +
			"amount: " + amount + "]";
	}
}
