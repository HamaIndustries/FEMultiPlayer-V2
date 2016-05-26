package net.fe.unit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

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
			if(rangeArgs.length == 1){
				w.range.add(Integer.parseInt(rangeArgs[0]));
			} else {
				int min = Integer.parseInt(rangeArgs[0]);
				int max = Integer.parseInt(rangeArgs[1]);
				for(int i = min; i <= max; i++){
					w.range.add(i);
				}
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
		public final ArrayList<Integer> range;
		public int mt, hit, crit;
		public int maxUses, cost;
		public final ArrayList<String> effective;
		public String pref;
		
		public WeaponBuilder() {
			modifiers = new Statistics();
			range = new ArrayList<>(3);
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
}
