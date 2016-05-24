package net.fe.fightStage;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import net.fe.unit.Class;
import net.fe.unit.Unit;
import net.fe.unit.Weapon;

public final class CombatCalculatorTest {
	
	@Test
	public void calculateBaseDamage_MagicWeaponAndNoTriggers() {
		HashMap<String, Integer> leftVals = new HashMap<String, Integer>();
		leftVals.put("HP", 20);
		leftVals.put("Mov", 5);
		leftVals.put("Con", 8);
		leftVals.put("Mag", 10);
		Unit left = new Unit("left", Class.createClass("Sage"), '-', leftVals, leftVals);
		left.equip(createWeapon(Weapon.Type.LIGHT, 8));
		org.junit.Assert.assertTrue(null != left.getWeapon());
		
		HashMap<String, Integer> rightVals = new HashMap<String, Integer>();
		rightVals.put("HP", 20);
		rightVals.put("Mov", 5);
		rightVals.put("Con", 8);
		rightVals.put("Res", 5);
		Unit right = new Unit("right", Class.createClass("Phantom"), '-', rightVals, rightVals);
		
		assertEquals(13, CombatCalculator.calculateBaseDamage(left, right));
	}
	
	@Test
	public void calculateBaseDamage_PhysicalWeaponAndNoTriggers() {
		HashMap<String, Integer> leftVals = new HashMap<String, Integer>();
		leftVals.put("HP", 20);
		leftVals.put("Mov", 5);
		leftVals.put("Con", 8);
		leftVals.put("Str", 10);
		Unit left = new Unit("left", Class.createClass("Phantom"), '-', leftVals, leftVals);
		left.equip(createWeapon(Weapon.Type.AXE, 8));
		
		HashMap<String, Integer> rightVals = new HashMap<String, Integer>();
		rightVals.put("HP", 20);
		rightVals.put("Mov", 5);
		rightVals.put("Con", 8);
		rightVals.put("Def", 5);
		Unit right = new Unit("right", Class.createClass("Phantom"), '-', rightVals, rightVals);
		
		assertEquals(13, CombatCalculator.calculateBaseDamage(left, right));
	}
	
	@Test
	public void calculatePreviewDamage_MagicWeaponAndNoTriggers() {
		HashMap<String, Integer> leftVals = new HashMap<String, Integer>();
		leftVals.put("HP", 20);
		leftVals.put("Mov", 5);
		leftVals.put("Con", 8);
		leftVals.put("Mag", 10);
		Unit left = new Unit("left", Class.createClass("Sage"), '-', leftVals, leftVals);
		left.equip(createWeapon(Weapon.Type.LIGHT, 8));
		
		HashMap<String, Integer> rightVals = new HashMap<String, Integer>();
		rightVals.put("HP", 20);
		rightVals.put("Mov", 5);
		rightVals.put("Con", 8);
		rightVals.put("Res", 5);
		Unit right = new Unit("right", Class.createClass("Phantom"), '-', rightVals, rightVals);
		
		assertEquals(13, CombatCalculator.calculatePreviewDamage(left, right));
	}
	
	@Test
	public void calculatePreviewDamage_PhysicalWeaponAndNoTriggers() {
		HashMap<String, Integer> leftVals = new HashMap<String, Integer>();
		leftVals.put("HP", 20);
		leftVals.put("Mov", 5);
		leftVals.put("Con", 8);
		leftVals.put("Str", 10);
		Unit left = new Unit("left", Class.createClass("Phantom"), '-', leftVals, leftVals);
		left.equip(createWeapon(Weapon.Type.AXE, 8));
		
		HashMap<String, Integer> rightVals = new HashMap<String, Integer>();
		rightVals.put("HP", 20);
		rightVals.put("Mov", 5);
		rightVals.put("Con", 8);
		rightVals.put("Def", 5);
		Unit right = new Unit("right", Class.createClass("Phantom"), '-', rightVals, rightVals);
		
		assertEquals(13, CombatCalculator.calculatePreviewDamage(left, right));
	}
	
	@Test
	public void calculatePreviewDamage_PhysicalWeaponAndShowYourModTrigger() {
		HashMap<String, Integer> leftVals = new HashMap<String, Integer>();
		leftVals.put("HP", 20);
		leftVals.put("Mov", 5);
		leftVals.put("Con", 8);
		leftVals.put("Str", 10);
		Unit left = new Unit("left", Class.createClass("Phantom"), '-', leftVals, leftVals);
		left.equip(createWeapon(Weapon.Type.AXE, 8));
		left.addSkill(new CombatTrigger(CombatTrigger.NO_NAME_MOD, CombatTrigger.YOUR_TURN_MOD | CombatTrigger.SHOW_IN_PREVIEW) {
			public int runDamageMod(Unit a, Unit d, int damage) { return 2; }
			public boolean attempt(Unit user, int range, Unit opponent) { return true; }
			public CombatTrigger getCopy() {return this;}
		});
		
		HashMap<String, Integer> rightVals = new HashMap<String, Integer>();
		rightVals.put("HP", 20);
		rightVals.put("Mov", 5);
		rightVals.put("Con", 8);
		rightVals.put("Def", 5);
		Unit right = new Unit("right", Class.createClass("Phantom"), '-', rightVals, rightVals);
		
		assertEquals(2, CombatCalculator.calculatePreviewDamage(left, right));
	}
	
	@Test
	public void calculatePreviewDamage_PhysicalWeaponAndNoShowYourModTrigger() {
		HashMap<String, Integer> leftVals = new HashMap<String, Integer>();
		leftVals.put("HP", 20);
		leftVals.put("Mov", 5);
		leftVals.put("Con", 8);
		leftVals.put("Str", 10);
		Unit left = new Unit("left", Class.createClass("Phantom"), '-', leftVals, leftVals);
		left.equip(createWeapon(Weapon.Type.AXE, 8));
		left.addSkill(new CombatTrigger(CombatTrigger.NO_NAME_MOD, CombatTrigger.YOUR_TURN_MOD) {
			public int runDamageMod(Unit a, Unit d, int damage) { return 2; }
			public boolean attempt(Unit user, int range, Unit opponent) { return true; }
			public CombatTrigger getCopy() {return this;}
		});
		
		HashMap<String, Integer> rightVals = new HashMap<String, Integer>();
		rightVals.put("HP", 20);
		rightVals.put("Mov", 5);
		rightVals.put("Con", 8);
		rightVals.put("Def", 5);
		Unit right = new Unit("right", Class.createClass("Phantom"), '-', rightVals, rightVals);
		
		assertEquals(13, CombatCalculator.calculatePreviewDamage(left, right));
	}
	
	
	private Weapon createWeapon(Weapon.Type type, int might) {
		
		HashMap<String, Integer> modifiers = new HashMap<>();
			modifiers.put("Skl", 0);
			modifiers.put("Lck", 0);
			modifiers.put("HP",  0);
			modifiers.put("Str", 0);
			modifiers.put("Mag", 0);
			modifiers.put("Def", 0);
			modifiers.put("Res", 0);
			modifiers.put("Spd", 0);
			modifiers.put("Lvl", 0);
			modifiers.put("Con", 0);
			modifiers.put("Mov", 0);
			modifiers.put("Con", 0);
			modifiers.put("Aid", 0);
		
		Weapon retVal = new Weapon(
			"fork", 1, 0, 0,
			type, might, 0, 0, java.util.Arrays.asList(1),
			modifiers, new java.util.ArrayList<>(), null
		);
		return retVal;
	}
}
