package net.fe.game.unit;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import static java.util.Collections.emptyList;

import net.fe.game.fightStage.*;
import net.fe.game.fightStage.skill.Aether;
import net.fe.game.fightStage.skill.Astra;
import net.fe.game.fightStage.skill.Colossus;
import net.fe.game.fightStage.skill.Crisis;
import net.fe.game.fightStage.skill.Deadeye;
import net.fe.game.fightStage.skill.Lethality;
import net.fe.game.fightStage.skill.Luna;
import net.fe.game.fightStage.skill.Miracle;
import net.fe.game.fightStage.skill.Pavise;
import net.fe.game.fightStage.skill.Sol;
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
	public final CombatTrigger masterSkill;
	
	public final List<FieldSkill> fieldSkills;
	
	/** The usable weapon. */
	public final List<Weapon.Type> usableWeapon;
	
	/** The name. */
	public final String name;
	
	/** The description. */
	public final String description;
	
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
	private Class(String name, String desc, int c, CombatTrigger m, List<? extends FieldSkill> fs, Weapon.Type... types){
		this.crit = c;
		this.masterSkill = m;
		this.usableWeapon = java.util.Collections.unmodifiableList(Arrays.asList(types));
		this.name = name;
		this.description = desc;
		this.fieldSkills = java.util.Collections.unmodifiableList(new java.util.ArrayList<FieldSkill>(fs));
	}
	
	@Override
	public String toString() {
		return "Class [" + name + ", " + description + ", ...]";
	}
	
	@Override
	public int hashCode() {
		return (((this.crit * 31 +
				this.masterSkill.hashCode()) * 31 +
				this.name.hashCode()) * 31 + 
				this.description.hashCode()) * 31 +
				this.usableWeapon.stream().mapToInt((x) -> 1 << x.ordinal()).sum();
	}
	
	@Override
	public boolean equals(Object other) {
		if (other != null && other instanceof Class) {
			Class o2 = (Class) other;
			
			return this.crit == o2.crit &&
				this.masterSkill.equals(o2.masterSkill) &&
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
		List<FieldSkill> emptyFieldSkillList = emptyList();
		
		//Lords
		if(name.equals("Roy"))
			return new Class("Lord", 
					"A noble youth who commands armies.",
					0, new Aether(), Arrays.asList(new Shove()),
					Weapon.Type.SWORD);
		if(name.equals("Eliwood"))
			return new Class("Lord", 
					"A courageous royal who commands armies.",
					 0, new Sol(false), emptyFieldSkillList,
					 Weapon.Type.SWORD, Weapon.Type.LANCE);
		if(name.equals("Lyn"))
			return new Class("Lord", 
					"A serene youth who commands armies.",
					 0, new Astra(), Arrays.asList(new Shove()),
					 Weapon.Type.SWORD, Weapon.Type.BOW, Weapon.Type.CROSSBOW);
		if(name.equals("Hector"))
			return new Class("Lord", 
					"A mighty noble who commands armies.",
					 0, new Luna(false), Arrays.asList(new Shove(), new Smite()),
					 Weapon.Type.AXE, Weapon.Type.SWORD);
		if(name.equals("Eirika"))
			return new Class("Lord", 
					"A brave princess who commands armies.",
					 0, new Luna(false), emptyFieldSkillList,
					 Weapon.Type.SWORD);
		if(name.equals("Ephraim"))
			return new Class("Lord", 
					"A skilled prince who commands armies.",
					 0, new Sol(false), emptyFieldSkillList,
					 Weapon.Type.LANCE);
		if(name.equals("Marth"))
			return new Class("Lord", 
					"A legendary prince who commands armies.",
					 0, new Aether(), Arrays.asList(new Shove()),
					 Weapon.Type.SWORD);
		if(name.equals("Ike"))
			return new Class("Lord", 
					"A radiant hero who commands armies.",
					 0, new Aether(), Arrays.asList(new Shove(), new Smite()),
					 Weapon.Type.SWORD, Weapon.Type.AXE);
		
		//Other
		if(name.equals("Sniper"))
			return new Class("Sniper", 
					"An expert archer who has mastered the bow.",
					10, new Deadeye(), Arrays.asList(new Shove()),
					Weapon.Type.BOW, Weapon.Type.CROSSBOW);
		if(name.equals("Hero"))
			return new Class("Hero", 
					"Battle-hardened warriors who possess exceptional skill.",
					0, new Luna(false), Arrays.asList(new Shove()),
					Weapon.Type.SWORD, Weapon.Type.AXE);
		if(name.equals("Berserker"))
			return new Class("Berserker",
					"A master pirate who deals devastating attacks.",
					10, new Colossus(), Arrays.asList(new Shove(), new Smite()),
					Weapon.Type.AXE);
		if(name.equals("Warrior"))
			return new Class("Warrior", 
					"An experienced fighter whose might is second to none.",
					0, new Colossus(),  Arrays.asList(new Shove(), new Smite()),
					Weapon.Type.AXE, Weapon.Type.CROSSBOW);
		if(name.equals("Assassin"))
			return new Class("Assassin", 
					"A deadly killer who lives in the shadows.",
					10, new Lethality(), Arrays.asList(new Shove()),
					Weapon.Type.SWORD);
		if(name.equals("Paladin"))
			return new Class("Paladin", 
					"An experienced and dignified knight, possessing high mobility.",
					0, new Sol(false), emptyFieldSkillList,
					Weapon.Type.LANCE, Weapon.Type.SWORD, Weapon.Type.AXE);
		if(name.equals("Sage"))
			return new Class("Sage", 
					"A powerful magic user who wields mighty tomes.",
					0, new Sol(true), Arrays.asList(new Shove()),
					Weapon.Type.ANIMA, Weapon.Type.LIGHT, Weapon.Type.STAFF);
		if(name.equals("General"))
			return new Class("General", 
					"Armoured knights who possess overpowering strength and defense.",
					0, new Pavise(),  Arrays.asList(new Shove(), new Smite()),
					Weapon.Type.AXE, Weapon.Type.LANCE);
		if(name.equals("Valkyrie"))
			return new Class("Valkyrie", 
					"A cleric who rides a horse into combat.",
					0, new Miracle(), emptyFieldSkillList,
					Weapon.Type.STAFF, Weapon.Type.LIGHT);
		if(name.equals("Swordmaster"))
			return new Class("Swordmaster",
					"A seasoned myrmidon who has reached the pinnacle of swordsmanship.",
					20, new Astra(), Arrays.asList(new Shove()),
					Weapon.Type.SWORD);
		if(name.equals("Sorcerer"))
			return new Class("Sorcerer",
					"A sinister warlock who wields potent dark magic.",
					0, new Luna(true), Arrays.asList(new Shove()),
					Weapon.Type.DARK, Weapon.Type.ANIMA);
		if(name.equals("Falconknight"))
			return new Class("Falconknight", 
					"Knights who control pegasi with great mastery.",
					0, new Crisis(), emptyFieldSkillList,
					Weapon.Type.LANCE, Weapon.Type.SWORD);
		if(name.equals("Phantom"))
			return new Class("Phantom", 
					"A phantom that fights for its summoner.",
					0, new Miracle(), Arrays.asList(new Shove()),
					Weapon.Type.AXE);
		
		throw new IllegalArgumentException("Unknown Class Name: " + name);
	}
}
