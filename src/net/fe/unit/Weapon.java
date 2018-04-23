package net.fe.unit;

import java.util.*;
import java.util.function.Function;

import net.fe.fightStage.Brave;
import net.fe.fightStage.CombatTrigger;
import net.fe.fightStage.EclipseSix;
import net.fe.fightStage.LunaPlus;
import net.fe.fightStage.Nosferatu;
import net.fe.fightStage.CrossBow;
import net.fe.overworldStage.StartOfPhaseEffect;
import net.fe.overworldStage.startOfPhaseEffect.ChangeHp;

// TODO: Auto-generated Javadoc
/**
 * The Class Weapon.
 */
public final class Weapon extends Item {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 6496663141806177211L;
	
	/** The modifiers. */
	public final Statistics modifiers;
	
	/** The crit. */
	public final int mt, hit, crit;
	
	/** The weapon's range. */
	public final Function<Statistics, List<Integer>> range;
	
	/** The type. */
	public final Type type;
	
	/** The effective. */
	public final List<String> effective;
	
	/** The pref. */
	public final String pref;

	
	/**
	 * Instantiates a new weapon.
	 *
	 * @param name the weapon's name
	 * @param maxUses the durability of the weapon
	 * @param id icon number
	 * @param cost price in shop
	 * @param type the type of the weapon
	 * @param range the weapon's range. If this weapon is expected to travel over a network, this
	 * 		should be serializable and have a stable hashCode. Note that lambdas don't generally
	 * 		satisfy this property
	 */
	public Weapon(String name, int maxUses, int id, int cost,
			Type type, int mt, int hit, int crit, 
			Function<Statistics, List<Integer>> range,
			Statistics modifiers,
			List<String> effective, String pref) {
		super(name, maxUses, id, cost);
		this.type = type;
		this.modifiers = modifiers;
		this.mt = mt;
		this.hit = hit;
		this.crit = crit;
		this.effective = java.util.Collections.unmodifiableList(new ArrayList<String>(effective));
		this.range = range;
		this.pref = pref;
	}
	
	/**
	 * Weapon categories
	 */
	public enum Type{
		
		/** Swords */
		SWORD,
		/** Lances */
		LANCE,
		/** Axes */
		AXE,
		/** Bows */
		BOW,
		/** Crossbows */
		CROSSBOW,
		/** Light tomes */
		LIGHT,
		/** Anima tomes */
		ANIMA,
		/** Dark Tomes */
		DARK,
		/** Staves */
		STAFF;
		
		/**
		 * Weapon triangle modifier
		 *
		 * @param other the other
		 * @return 1 if advantage, -1 if disadvantage
		 */
		public int triangleModifier(Type other){
			switch(this){
			case SWORD:
				if(other == LANCE) return -1;
				if(other == AXE) return 1;
				return 0;
			case LANCE:
				if(other == AXE) return -1;
				if(other == SWORD) return 1;
				return 0;
			case AXE:
				if(other == SWORD) return -1;
				if(other == LANCE) return 1;
				return 0;
				
			case LIGHT:
				if(other == ANIMA) return -1;
				if(other == DARK) return 1;
				return 0;
			case ANIMA:
				if(other == DARK) return -1;
				if(other == LIGHT) return 1;
				return 0;
			case DARK:
				if(other == LIGHT) return -1;
				if(other == ANIMA) return 1;
				return 0;
			default:
				return 0;
			}
		}
		
		/**
		 * Checks if is magic.
		 *
		 * @return true, if is magic
		 */
		public boolean isMagic(){
			return this == ANIMA || this == LIGHT || this == DARK;
		}
	}
	
	/**
	 * Weapon triangle modifier
	 *
	 * @param other the other weapon
	 * @return 1 if advantage, -1 if disadvantage
	 */
	public int triMod(Weapon other){ 
		if(other == null) return 0;
		if(this.name.contains("reaver") || other.name.contains("reaver")){
			if(this.name.contains("reaver") && other.name.contains("reaver")){
				return type.triangleModifier(other.type);
			}
			return -2*type.triangleModifier(other.type);
		}
		return type.triangleModifier(other.type);
	}
	
	/**
	 * Checks if is magic.
	 *
	 * @return true, if is magic
	 */
	public boolean isMagic(){
		return type.isMagic();
	}
	
	/**
	 * Gets the triggers.
	 *
	 * @return the triggers
	 */
	public List<CombatTrigger> getTriggers(){
		ArrayList<CombatTrigger> triggers = new ArrayList<CombatTrigger>();
		if(name.contains("Brave")){
			triggers.add(new Brave());
		} else if (name.equals("Nosferatu")){
			triggers.add(new Nosferatu());
		} else if (name.equals("Lunase")){
			triggers.add(new LunaPlus());
		} else if (name.equals("Eclipse")){
			triggers.add(new EclipseSix());
		} else if (type == Type.CROSSBOW) {
			triggers.add(new CrossBow());
		}
		return triggers;
	}
	
	/**
	 * Returns effects that should apply to the wielder at the start of
	 * that unit's turn, if this item is equipped
	 */
	public List<StartOfPhaseEffect> getStartOfPhaseEffects() {
		ArrayList<StartOfPhaseEffect> retval = new ArrayList<>();
		if (name.contains("Blessed")) {
			retval.add(new ChangeHp(5, 0));
		}
		return retval;
	}
	
	
	/* (non-Javadoc)
	 * @see net.fe.unit.Item#getCopy()
	 */
	public Weapon getCopy(){
		return new Weapon(name, getMaxUses(), id, getCost(),
				type, mt, hit, crit, range,
				modifiers, effective, pref);
	}
	
	/** Returns an item identical to this one, with the exception of an updated mt, hit and crit */
	public Weapon getCopyWithNewMtHitCrit(int newmt, int newhit, int newcrit){
		return new Weapon(name, getMaxUses(), id, getCost(),
				type, newmt, newhit, newcrit, range,
				modifiers, effective, pref);
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Item that) {
		if(that instanceof Weapon){
			int first = this.type.compareTo(((Weapon) that).type);
			if(first != 0) return first;
			int second = this.getCost() - that.getCost();
			return second;
		} else {
			return -1;
		}
	}
	
	@Override
	public int hashCode() {
		return (((((super.hashCode() * 31 +
				this.type.ordinal()) * 31 +
				this.range.hashCode()) * 31 +
				this.modifiers.hashCode()) * 31 +
				java.util.Objects.hashCode(this.pref)) * 31 +
				this.effective.hashCode()) * 31 +
				(crit << 14 + hit << 7 + mt);
	}
	
	@Override
	protected boolean canEquals(Object other) {
		return other instanceof Weapon;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other != null && other instanceof Weapon) {
			Weapon o2 = (Weapon) other;
			if (o2.canEquals(this)) {
				return super.equals(o2) &&
					this.mt == o2.mt &&
					this.hit == o2.hit &&
					this.crit == o2.crit &&
					this.type == o2.type &&
					this.range.equals(o2.range) &&
					this.effective.equals(o2.effective) &&
					(this.pref == null ?
							o2.pref == null :
							this.pref.equals(o2.pref)
					) &&
					this.modifiers.equals(o2.modifiers);
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	@Override
	public String toString() {
		return "Weapon [" +
			"name: " + name + "; " +
			"maxUses: " + getMaxUses() + "; " +
			"uses: " + getUses() + "; " +
			"id: " + id + "; " +
			"cost: " + getCost() + "; " +
			"type: " + type + "; " +
			"mt: " + mt + "; " +
			"hit: " + hit + "; " +
			"range: " + range + "; " +
			"modifiers: " + modifiers + "; " +
			"effective: " + effective + "; " +
			"pref: " + pref + "]";
	}
}
