package net.fe.unit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import org.newdawn.slick.util.ResourceLoader;

// TODO: Auto-generated Javadoc
/**
 * A factory for creating Unit objects.
 */
public class UnitFactory {
	
	/** The units. */
	private static HashMap<String, Unit> units = new HashMap<String, Unit>();
	
	/**
	 * Load units.
	 * Fails if [[WeaponFactory#loadWeapons]] has not been called yet.
	 */
	public static void loadUnits() {
		Scanner in = new Scanner(ResourceLoader.getResourceAsStream("res/stats.txt"));
		while(in.hasNextLine()){
			String line = in.nextLine();
			if(line.equals("") || line.startsWith("#")) continue;
			String[] args = line.split("\\s+");
			String name = args[0];
			Class clazz = Class.createClass(args[1]);
			int lv = Integer.parseInt(args[2]);
			int hpBase = Integer.parseInt(args[3]);
			int strBase = Integer.parseInt(args[4]);
			int magBase = Integer.parseInt(args[5]);
			int sklBase = Integer.parseInt(args[6]);
			int spdBase = Integer.parseInt(args[7]);
			int lckBase = Integer.parseInt(args[8]);
			int defBase = Integer.parseInt(args[9]);
			int resBase = Integer.parseInt(args[10]);
			int con = Integer.parseInt(args[11]);
			int mov = Integer.parseInt(args[12]);
			int hpGrowth = Integer.parseInt(args[13]);
			int strGrowth = Integer.parseInt(args[14]);
			int magGrowth = Integer.parseInt(args[15]);
			int sklGrowth = Integer.parseInt(args[16]);
			int spdGrowth = Integer.parseInt(args[17]);
			int lckGrowth = Integer.parseInt(args[18]);
			int defGrowth = Integer.parseInt(args[19]);
			int resGrowth = Integer.parseInt(args[20]);
			char gender = args[21].charAt(0);
			
			for(int i = lv; i < 20; i++){
				hpBase += hpGrowth/100.0f;
				strBase += strGrowth/100.0f;
				magBase += magGrowth/100.0f;
				sklBase += sklGrowth/100.0f;
				spdBase += spdGrowth/100.0f;
				lckBase += lckGrowth/100.0f;
				defBase += defGrowth/100.0f;
				resBase += resGrowth/100.0f;
			}
			
			int aid;
			if(name.equals("Eirika") || 
					clazz.name.equals("Valkyrie") ||
					clazz.name.equals("Falconknight")){
				aid = 20 - con;
			} else if (Unit.isRider(clazz) || Unit.isRider(name)){
				aid = 27 - con;
			} else {
				aid = con - 1;
			}
			
			
			Statistics bases = new Statistics(
				hpBase,
				strBase,
				magBase,
				sklBase,
				spdBase,
				defBase,
				resBase,
				lckBase,
				mov,
				con,
				aid
			);
			
			Statistics growths = new Statistics(
				hpGrowth,
				strGrowth,
				magGrowth,
				sklGrowth,
				spdGrowth,
				defGrowth,
				resGrowth,
				lckGrowth,
				0,
				0,
				0
			);
			
			if(clazz == null){
				System.err.println(line);
			}
			Unit u = new Unit(name, clazz, gender, bases, growths);
			if(name.equals("Roy")){
				u.addToInventory(WeaponFactory.getWeapon("Binding Blade"));
			} else if (name.equals("Lyn")){
				u.addToInventory(WeaponFactory.getWeapon("Sol Katti"));
			} else if (name.equals("Eliwood")){
				u.addToInventory(WeaponFactory.getWeapon("Durandal"));
			} else if (name.equals("Hector")){
				u.addToInventory(WeaponFactory.getWeapon("Armads"));
			} else if(name.equals("Eirika")){
				u.addToInventory(WeaponFactory.getWeapon("Sieglinde"));
			} else if(name.equals("Ephraim")){
				u.addToInventory(WeaponFactory.getWeapon("Siegmund"));
			} else if(name.equals("Marth")){
				u.addToInventory(WeaponFactory.getWeapon("Falchion"));
			} else if(name.equals("Ike")){
				u.addToInventory(WeaponFactory.getWeapon("Ragnell"));
			}
			
			units.put(name, u);
		}
		in.close();
	}
	
	/**
	 * Gets the unit.
	 *
	 * @param name the name
	 * @return the unit
	 */
	public static Unit getUnit(String name){
		return units.get(name).getCopy();
	}
	
	/**
	 * Gets the all units.
	 *
	 * @return the all units
	 */
	public static ArrayList<Unit> getAllUnits() {
		ArrayList<Unit> ans = new ArrayList<Unit>();
		for(Unit u : units.values()) {
			ans.add(u.getCopy());
		}
		return ans;
	}
	
	/**
	 * Gets the lords.
	 *
	 * @return the lords
	 */
	public static ArrayList<Unit> getLords(){
		ArrayList<Unit> ans = new ArrayList<Unit>();
		for(Unit u : units.values()) {
			if(u.getTheClass().name.equals("Lord"))
				ans.add(u.getCopy());
		}
		return ans;
	}
	
	/**
	 * Gets the vassals.
	 *
	 * @return the vassals
	 */
	public static ArrayList<Unit> getVassals(){
		ArrayList<Unit> ans = new ArrayList<Unit>();
		for(Unit u : units.values()) {
			if(!u.getTheClass().name.equals("Lord"))
				ans.add(u.getCopy());
		}
		return ans;
	}
}
