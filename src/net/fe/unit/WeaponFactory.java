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
		int id = 0;
		while(in.hasNextLine()){
			String line = in.nextLine();
			if(line.startsWith("#") || line.equals("")){
				continue;
			}
			String[] args = line.split("\\t+");
			String name = args[0];
			Weapon w = new Weapon(name);
			w.id = id++;
			w.type = Weapon.Type.valueOf(args[1].toUpperCase());
			
			
			List<Integer> range = new ArrayList<Integer>();
			String[] rangeArgs = args[2].split("-");
			if(rangeArgs.length == 1){
				range.add(Integer.parseInt(rangeArgs[0]));
			} else {
				int min = Integer.parseInt(rangeArgs[0]);
				int max = Integer.parseInt(rangeArgs[1]);
				for(int i = min; i <= max; i++){
					range.add(i);
				}
			}
			w.range = range;
			
			w.mt = Integer.parseInt(args[3]);
			w.hit = Integer.parseInt(args[4]);
			w.crit = Integer.parseInt(args[5]);
			w.setMaxUses(Integer.parseInt(args[6]));
			
			if(!args[7].equals("-")){
				w.setCost(Integer.parseInt(args[7]));
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
			
			weapons.put(name, w);
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
}
