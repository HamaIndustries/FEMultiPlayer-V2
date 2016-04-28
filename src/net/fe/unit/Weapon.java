package net.fe.unit;

import java.util.*;

import net.fe.fightStage.Brave;
import net.fe.fightStage.CombatTrigger;
import net.fe.fightStage.EclipseSix;
import net.fe.fightStage.LunaPlus;
import net.fe.fightStage.Nosferatu;

// TODO: Auto-generated Javadoc
/**
 * The Class Weapon.
 */
public class Weapon extends Item {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 6496663141806177211L;
	
	/** The modifiers. */
	public HashMap<String, Integer> modifiers;
	
	/** The crit. */
	public int mt, hit, crit;
	
	/** The range. */
	public List<Integer> range;
	
	/** The type. */
	public Type type;
	
	/** The effective. */
	public ArrayList<String> effective;
	
	/** The pref. */
	public String pref;

	
	/**
	 * Instantiates a new weapon.
	 *
	 * @param name the name
	 */
	public Weapon(String name) {
		super(name);
		// Initialize modifiers to 0
		modifiers = new HashMap<String, Integer>();
		modifiers.put("Skl", 0);
		modifiers.put("Lck", 0);
		modifiers.put("HP",  0);
		modifiers.put("Str", 0);
		modifiers.put("Mag", 0);
		modifiers.put("Def", 0);
		modifiers.put("Res", 0);
		modifiers.put("Spd", 0);
		modifiers.put("Lvl", 0);
		modifiers.put("Mov", 0);
		modifiers.put("Con", 0);
		modifiers.put("Aid", 0);
		mt = 0;
		hit = 0;
		crit = 0;
		type = null;
		effective = new ArrayList<String>();
	}
	
	/**
	 * The Enum Type.
	 */
	public enum Type{
		
		/** The sword. */
		SWORD, 
 /** The lance. */
 LANCE, 
 /** The axe. */
 AXE, 
 /** The bow. */
 BOW, 
 /** The light. */
 LIGHT, 
 /** The anima. */
 ANIMA, 
 /** The dark. */
 DARK, 
 /** The staff. */
 STAFF;
		
		/**
		 * Triangle modifier.
		 *
		 * @param other the other
		 * @return the int
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
	 * Tri mod.
	 *
	 * @param other the other
	 * @return the int
	 */
	//Returns 1 if advantage, -1 if disadvantage
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
		}
		return triggers;
	}
	
	
	/* (non-Javadoc)
	 * @see net.fe.unit.Item#getCopy()
	 */
	public Weapon getCopy(){
		Weapon w = new Weapon(name);
		w.type = type;
		w.range = new ArrayList<Integer>(range);
		w.mt = mt;
		w.hit = hit;
		w.crit = crit;
		w.setMaxUses(getMaxUses());
		w.setCost(getCost());
		w.effective = new ArrayList<String>(effective);
		w.pref = pref;
		w.modifiers = new HashMap<String, Integer>(modifiers);
		w.id = id;
		return w;
		
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
}
