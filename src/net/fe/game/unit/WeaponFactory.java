package net.fe.game.unit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;
import java.io.Serializable;

import org.newdawn.slick.util.ResourceLoader;

// TODO: Auto-generated Javadoc
/**
 * A factory for creating Weapon objects.
 */
public class WeaponFactory {
	
	/** The Constant mounted. */
	private static final List<String> mounted = 
			Arrays.asList("Paladin", "Valkyrie", /*"Falconknight",*/ 
					"Ephraim", "Eirika", "Eliwood");
	
	/** The Constant armored. */
	private static final List<String> armored =
			Arrays.asList("General");
	
	/** The Constant fliers. */
	public static final List<String> fliers =
			Arrays.asList("Falconknight");
	
	public static final List<String> riding;
	
	static {
		List<String> all = new ArrayList<String>(mounted);
		all.addAll(fliers);
		riding = new ArrayList<String>(all);
	}
	
	/** The weapons. */
	private static HashMap<String, Weapon> weapons = new HashMap<String, Weapon>();
	
	/**
	 * Load weapons.
	 */
	public static void loadWeapons() {
		Scanner in = new Scanner(ResourceLoader.getResourceAsStream("res/weapons.txt"));
		int id = 4;
		while(in.hasNextLine()){
			String line = in.nextLine();
			if(line.startsWith("#") || line.equals("")){
				continue;
			}
			String[] args = line.split("\\t+");
			String name = args[0];
			WeaponBuilder w = new WeaponBuilder();
			w.name = name;
			w.id = id++;
			w.type = Weapon.Type.valueOf(args[1].toUpperCase());
			
			
			String[] rangeArgs = args[2].split("-");
			if ("1-Mag/2".equals(args[2])) {
				w.range = new OneToHalfMagRange();
			} else if (rangeArgs.length == 1){
				w.range = new Static1Range(Integer.parseInt(rangeArgs[0]));
			} else {
				int min = Integer.parseInt(rangeArgs[0]);
				int max = Integer.parseInt(rangeArgs[1]);
				w.range = new StaticRange(min, max);
			}
			
			w.mt = Integer.parseInt(args[3]);
			w.hit = Integer.parseInt(args[4]);
			w.crit = Integer.parseInt(args[5]);
			w.maxUses = Integer.parseInt(args[6]);
			
			if(!args[7].equals("-")){
				w.cost = Integer.parseInt(args[7]);
			} else {
				w.cost = 0;
			}
			
			if(args[8].equals("Mount")){
				w.effective.addAll(mounted);
			} else if (args[8].equals("Armor")){
				w.effective.addAll(armored);
			} else if (args[8].equals("Flier")){
				w.effective.addAll(fliers);
			}
			
			if(!args[9].equals("-")){
				w.pref = args[9];
			}
			
			if(!args[10].equals("-")){
				String[] modArgs = args[10].split(" ");
				w.modifiers = w.modifiers.copy(modArgs[0], Integer.parseInt(modArgs[1]));
			}
			
			weapons.put(name, w.build());
		}
		in.close();
	}
	
	/**
	 * Gets the weapon.
	 *
	 * @param name the name
	 * @return the weapon
	 */
	public static Weapon getWeapon(String name){
		return weapons.get(name).getCopy();
	}
	
	/**
	 * Gets the all weapons.
	 *
	 * @return the all weapons
	 */
	public static Iterable<Weapon> getAllWeapons(){
		return weapons.values();
	}
	
	
	
	private static final class WeaponBuilder {
		public String name;
		public int id;
		public Weapon.Type type;
		public Statistics modifiers;
		public Function<Statistics, List<Integer>> range;
		public int mt, hit, crit;
		public int maxUses, cost;
		public final ArrayList<String> effective;
		public String pref;
		
		public WeaponBuilder() {
			modifiers = new Statistics();
			range = new StaticRange(1,1);
			effective = new ArrayList<>();
			pref = null;
		}
		
		public Weapon build() {
			return new Weapon(
				name, maxUses, id, cost,
				type, mt, hit, crit, range,
				modifiers, effective, pref
			);
		}
	}
	
	/** A range whose range does not depend on unit statistics */
	private final static class Static1Range implements Function<Statistics, List<Integer>>, Serializable {
		private final int value;
		
		/** 
		 * @param value the single value of the range
		 */
		public Static1Range(int value) {
			this.value = value;
		}
		
		@Override public List<Integer> apply(Statistics s) {
			return java.util.Arrays.asList(value);
		}
		
		@Override public String toString() {
			return "" + value;
		}
		@Override public boolean equals(Object o) {
			return o instanceof Static1Range &&
					((Static1Range) o).value == this.value;
		}
		@Override public int hashCode() {
			return value;
		}
	}
	
	/** A range whose range does not depend on unit statistics */
	private final static class StaticRange implements Function<Statistics, List<Integer>>, Serializable {
		private final int min;
		private final int max;
		
		/** 
		 * @param min the low bound of the range (inclusive)
		 * @param max the high bound of the range (inclusive)
		 */
		public StaticRange(int min, int max) {
			this.min = min;
			this.max = max;
		}
		
		@Override public List<Integer> apply(Statistics s) {
			final ArrayList<Integer> retVal = new ArrayList<>(max - min);
			for(int i = min; i <= max; i++){
				retVal.add(i);
			}
			return retVal;
		}
		
		@Override public String toString() {
			return "" + min + "-" + max;
		}
		@Override public boolean equals(Object o) {
			return o instanceof StaticRange &&
					((StaticRange) o).min == this.min &&
					((StaticRange) o).max == this.max;
		}
		@Override public int hashCode() {
			return min << 8 + max;
		}
	}
	
	/** A range whose range is between one and half the unit's mag */
	private final static class OneToHalfMagRange implements Function<Statistics, List<Integer>>, Serializable {
		
		@Override public List<Integer> apply(Statistics s) {
			final int min = 1;
			final int max = s.mag / 2;
			
			final ArrayList<Integer> retVal = new ArrayList<>();
			for(int i = min; i <= max; i++){
				retVal.add(i);
			}
			return retVal;
		}
		
		@Override public String toString() {
			return "1-Mag/2";
		}
		@Override public boolean equals(Object o) {
			return o instanceof OneToHalfMagRange;
		}
		@Override public int hashCode() {
			return 0xFF;
		}
	}
}
