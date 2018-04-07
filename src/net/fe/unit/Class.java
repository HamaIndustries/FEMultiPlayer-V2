package net.fe.unit;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import static java.util.Collections.emptyList;

import net.fe.fightStage.*;
import net.fe.overworldStage.FieldSkill;
import net.fe.overworldStage.fieldskill.*;

// TODO: Auto-generated Javadoc
/**
 * The Class Class.
 */
public final class Class implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 9144407404798873761L;
	
	/** The crit. */
	public final int crit;
	
	/** The master skill. */
	public final List<CombatTrigger> combatSkills;
	
	public final List<FieldSkill> fieldSkills;
	
	/** The usable weapon. */
	public final List<Weapon.Type> usableWeapon;
	
	/** The name. */
	public final String name;
	
	/** The description. */
	public final String description;
	
	public final int sight;
	
	/**
	 * Instantiates a new class.
	 *
	 * @param name the name
	 * @param desc the desc
	 * @param c the class's innate crit rate
	 * @param m the class's combat abilities
	 * @param fs the class's field abilities
	 * @param types the types
	 */
	private Class(String name, String desc, int c, List<? extends CombatTrigger> cs, List<? extends FieldSkill> fs, int sight, Weapon.Type... types){
		this.crit = c;
		this.combatSkills = java.util.Collections.unmodifiableList(new java.util.ArrayList<>(cs));
		this.usableWeapon = java.util.Collections.unmodifiableList(Arrays.asList(types));
		this.name = name;
		this.description = desc;
		this.sight = sight;
		this.fieldSkills = java.util.Collections.unmodifiableList(new java.util.ArrayList<FieldSkill>(fs));
	}
	
	@Override
	public String toString() {
		return "Class [" + name + ", " + description + ", ...]";
	}
	
	@Override
	public int hashCode() {
		return (((this.crit * 31 +
				this.combatSkills.hashCode()) * 31 +
				this.name.hashCode()) * 31 + 
				this.description.hashCode()) * 31 +
				this.usableWeapon.stream().mapToInt((x) -> 1 << x.ordinal()).sum();
	}
	
	@Override
	public boolean equals(Object other) {
		if (other != null && other instanceof Class) {
			Class o2 = (Class) other;
			
			return this.crit == o2.crit &&
				this.combatSkills.equals(o2.combatSkills) &&
				this.usableWeapon.equals(o2.usableWeapon) &&
				this.name.equals(o2.name) &&
				this.description.equals(o2.description);
		} else {
			return false;
		}
	}
	
	/**
	 * Creates the class.
	 *
	 * @param name the name
	 * @return the class
	 */
	public static Class createClass(String name){
		//Lords
		if(name.equals("Roy"))
			return new Class("Lord", 
					"A noble youth who commands armies.",
					0,
					Arrays.asList(new Aether(), new ChargeSkillFromDamageRecieved(1), new RageEp()),
					Arrays.asList(new Shove(), new Rage()),
					3,
					Weapon.Type.SWORD);
		if(name.equals("Eliwood"))
			return new Class("Lord", 
					"A couRageEpous royal who commands armies.",
					 0,
					 Arrays.asList(new Sol(false), new ChargeSkillFromDamageRecieved(1), new RageEp()),
					 Arrays.asList(new Rage()),
					 3,
					 Weapon.Type.SWORD, Weapon.Type.LANCE);
		if(name.equals("Lyn"))
			return new Class("Lord", 
					"A serene youth who commands armies.",
					 0,
					 Arrays.asList(new Astra(), new ChargeSkillFromDamageRecieved(1), new RageEp()),
					 Arrays.asList(new Shove(), new Rage()),
					 3,
					 Weapon.Type.SWORD, Weapon.Type.BOW, Weapon.Type.CROSSBOW);
		if(name.equals("Hector"))
			return new Class("Lord", 
					"A mighty noble who commands armies.",
					 0,
					 Arrays.asList(new Luna(false), new ChargeSkillFromDamageRecieved(1), new RageEp()),
					 Arrays.asList(new Shove(), new Smite(), new Rage()),
					 3, 
					 Weapon.Type.AXE, Weapon.Type.SWORD);
		if(name.equals("Eirika"))
			return new Class("Lord", 
					"A brave princess who commands armies.",
					 0,
					 Arrays.asList(new Luna(false), new ChargeSkillFromDamageRecieved(1), new RageEp()),
					 Arrays.asList(new Rage()),
					 3,
					 Weapon.Type.SWORD);
		if(name.equals("Ephraim"))
			return new Class("Lord", 
					"A skilled prince who commands armies.",
					 0,
					 Arrays.asList(new Sol(false), new ChargeSkillFromDamageRecieved(1), new RageEp()),
					 Arrays.asList(new Rage()),
					 3,
					 Weapon.Type.LANCE);
		if(name.equals("Marth"))
			return new Class("Lord", 
					"A legendary prince who commands armies.",
					 0,
					 Arrays.asList(new Aether(), new ChargeSkillFromDamageRecieved(1), new RageEp()),
					 Arrays.asList(new Shove(), new Rage()),
					 3,
					 Weapon.Type.SWORD);
		if(name.equals("Ike"))
			return new Class("Lord", 
					"A radiant hero who commands armies.",
					 0,
					 Arrays.asList(new Aether(), new ChargeSkillFromDamageRecieved(1), new RageEp()),
					 Arrays.asList(new Shove(), new Smite(), new Rage()),
					 3,
					 Weapon.Type.SWORD, Weapon.Type.AXE);
		
		//Other
		if(name.equals("Sniper"))
			return new Class("Sniper", 
					"An expert archer who has mastered the bow.",
					10,
					Arrays.asList(new Deadeye(), new ChargeSkillFromDamageRecieved(1), new RageEp()),
					Arrays.asList(new Shove(), new Rage()),
					3,
					Weapon.Type.BOW, Weapon.Type.CROSSBOW);
		if(name.equals("Hero"))
			return new Class("Hero", 
					"Battle-hardened warriors who possess exceptional skill.",
					0,
					Arrays.asList(new Luna(false), new ChargeSkillFromDamageRecieved(1), new RageEp()),
					Arrays.asList(new Shove(), new Rage()),
					3,
					Weapon.Type.SWORD, Weapon.Type.AXE);
		if(name.equals("Berserker"))
			return new Class("Berserker",
					"A master pirate who deals devastating attacks.",
					10,
					Arrays.asList(new Colossus(), new ChargeSkillFromDamageRecieved(1), new RageEp()),
					Arrays.asList(new Shove(), new Smite(), new Rage()),
					3,
					Weapon.Type.AXE);
		if(name.equals("Warrior"))
			return new Class("Warrior", 
					"An experienced fighter whose might is second to none.",
					0,
					Arrays.asList(new Colossus(), new ChargeSkillFromDamageRecieved(1), new RageEp()),
					Arrays.asList(new Shove(), new Smite(), new Rage()),
					3,
					Weapon.Type.AXE, Weapon.Type.CROSSBOW);
		if(name.equals("Assassin"))
			return new Class("Assassin", 
					"A deadly killer who lives in the shadows.",
					10,
					Arrays.asList(new Lethality(), new ChargeSkillFromDamageRecieved(2), new RageEp()),
					Arrays.asList(new Shove(), new Rage()),
					8,
					Weapon.Type.SWORD);
		if(name.equals("Paladin"))
			return new Class("Paladin", 
					"An experienced and dignified knight, possessing high mobility.",
					0,
					Arrays.asList(new Sol(false), new ChargeSkillFromDamageRecieved(1), new RageEp()),
					Arrays.asList(new Rage()),
					3,
					Weapon.Type.LANCE, Weapon.Type.SWORD, Weapon.Type.AXE);
		if(name.equals("Sage"))
			return new Class("Sage", 
					"A powerful magic user who wields mighty tomes.",
					0,
					Arrays.asList(new Sol(true), new ChargeSkillFromDamageRecieved(1), new RageEp()),
					Arrays.asList(new Shove(), new Rage()),
					3,
					Weapon.Type.ANIMA, Weapon.Type.LIGHT, Weapon.Type.STAFF);
		if(name.equals("General"))
			return new Class("General", 
					"Armoured knights who possess overpowering strength and defense.",
					0,
					Arrays.asList(new Pavise(), new ChargeSkillFromDamageRecieved(1), new RageEp()),
					Arrays.asList(new Shove(), new Smite(), new Rage()),
					3,
					Weapon.Type.AXE, Weapon.Type.LANCE);
		if(name.equals("Valkyrie"))
			return new Class("Valkyrie", 
					"A cleric who rides a horse into combat.",
					0,
					Arrays.asList(new Miracle(), new ChargeSkillFromDamageRecieved(1), new RageEp()),
					Arrays.asList(new Rage()),
					3,
					Weapon.Type.STAFF, Weapon.Type.LIGHT);
		if(name.equals("Swordmaster"))
			return new Class("Swordmaster",
					"A seasoned myrmidon who has reached the pinnacle of swordsmanship.",
					20,
					Arrays.asList(new Astra(), new ChargeSkillFromDamageRecieved(2), new RageEp()),
					Arrays.asList(new Shove(), new Rage()),
					3,
					Weapon.Type.SWORD);
		if(name.equals("Sorcerer"))
			return new Class("Sorcerer",
					"A sinister warlock who wields potent dark magic.",
					0,
					Arrays.asList(new Luna(true), new ChargeSkillFromDamageRecieved(1), new RageEp()),
					Arrays.asList(new Shove(), new Rage()),
					3,
					Weapon.Type.DARK, Weapon.Type.ANIMA);
		if(name.equals("Falconknight"))
			return new Class("Falconknight", 
					"Knights who control pegasi with great mastery.",
					0,
					Arrays.asList(new Crisis(), new ChargeSkillFromDamageRecieved(1), new RageEp()),
					Arrays.asList(new Rage()),
					3,
					Weapon.Type.LANCE, Weapon.Type.SWORD);
		if(name.equals("Phantom"))
			return new Class("Phantom", 
					"A phantom that fights for its summoner.",
					0,
					Arrays.asList(),
					Arrays.asList(new Shove()),
					3,
					Weapon.Type.AXE);
		
		throw new IllegalArgumentException("Unknown Class Name: " + name);
	}
}
