package net.fe.unit;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;

public final class UnitTest {
	
	@Test
	public void testGetWeapon_FreshUnitNonHasNoEquippedWeapon() {
		HashMap<String, Integer> vals = new HashMap<String, Integer>();
		vals.put("HP", 20);
		vals.put("Mov", 5);
		vals.put("Con", 8);
		Unit dut = new Unit("ADFASDF", Class.createClass("Phantom"), '-', vals, vals);
		
		assertEquals(null, dut.getWeapon());
		assertEquals(0, dut.getInventory().size());
	}
	
	@Test
	public void testGetWeapon_AddingItemDoesNotEquipWeapon() {
		HashMap<String, Integer> vals = new HashMap<String, Integer>();
		vals.put("HP", 20);
		vals.put("Mov", 5);
		vals.put("Con", 8);
		Unit dut = new Unit("ADFASDF", Class.createClass("Phantom"), '-', vals, vals);
		
		dut.addToInventory(new RiseTome());
		
		assertEquals(null, dut.getWeapon());
		assertEquals(1, dut.getInventory().size());
	}
	
	@Test
	public void testGetWeapon_AddingWeaponDoesNotEquipWeapon() {
		HashMap<String, Integer> vals = new HashMap<String, Integer>();
		vals.put("HP", 20);
		vals.put("Mov", 5);
		vals.put("Con", 8);
		Unit dut = new Unit("ADFASDF", Class.createClass("Phantom"), '-', vals, vals);
		
		dut.addToInventory(createAxe(1));
		
		assertEquals(null, dut.getWeapon());
		assertEquals(1, dut.getInventory().size());
	}
	
	@Test
	public void testGetWeapon_EquippingWeaponMakesWeaponEquipped() {
		HashMap<String, Integer> vals = new HashMap<String, Integer>();
		vals.put("HP", 20);
		vals.put("Mov", 5);
		vals.put("Con", 8);
		Unit dut = new Unit("ADFASDF", Class.createClass("Phantom"), '-', vals, vals);
		Weapon weap = createAxe(1);
		
		dut.equip(weap);
		
		assertEquals(1, dut.getInventory().size());
		assertEquals(weap, dut.getWeapon());
	}
	
	@Test
	public void testGetWeapon_UnequippingWeaponMakesWeaponUnequipped() {
		HashMap<String, Integer> vals = new HashMap<String, Integer>();
		vals.put("HP", 20);
		vals.put("Mov", 5);
		vals.put("Con", 8);
		Unit dut = new Unit("ADFASDF", Class.createClass("Phantom"), '-', vals, vals);
		Weapon weap = createAxe(1);
		
		dut.equip(weap);
		dut.unequip();
		
		assertEquals(null, dut.getWeapon());
		assertEquals(1, dut.getInventory().size());
	}
	
	@Test
	public void testGetWeapon_BreakingWeaponEquipsNextWeapon() {
		HashMap<String, Integer> vals = new HashMap<String, Integer>();
		vals.put("HP", 20);
		vals.put("Mov", 5);
		vals.put("Con", 8);
		Unit dut = new Unit("ADFASDF", Class.createClass("Phantom"), '-', vals, vals);
		Weapon weap1 = createAxe(1);
		Weapon weap2 = createAxe(2);
		
		dut.addToInventory(weap1);
		dut.addToInventory(weap2);
		dut.initializeEquipment();
		
		assertEquals(weap1, dut.getWeapon());
		assertEquals(2, dut.getInventory().size());
		
		dut.use(weap1, true);
		
		assertEquals(weap2, dut.getWeapon());
		assertEquals(1, dut.getInventory().size());
	}
	
	@Test
	public void testGetWeapon_BreakingAllWeaponsEquipsNull() {
		HashMap<String, Integer> vals = new HashMap<String, Integer>();
		vals.put("HP", 20);
		vals.put("Mov", 5);
		vals.put("Con", 8);
		Unit dut = new Unit("ADFASDF", Class.createClass("Phantom"), '-', vals, vals);
		Weapon weap1 = createAxe(1);
		Weapon weap2 = createAxe(2);
		
		dut.addToInventory(weap1);
		dut.initializeEquipment();
		
		assertEquals(weap1, dut.getWeapon());
		assertEquals(1, dut.getInventory().size());
		
		dut.use(weap1, true);
		
		assertEquals(null, dut.getWeapon());
		assertEquals(0, dut.getInventory().size());
	}
	
	@Test
	public void testGetWeapon_TradingAwayWeaponsResultsInUnequippedWeapon() {
		HashMap<String, Integer> vals = new HashMap<String, Integer>();
		vals.put("HP", 20);
		vals.put("Mov", 5);
		vals.put("Con", 8);
		Unit dut = new Unit("ADFASDF", Class.createClass("Phantom"), '-', vals, vals);
		Weapon weap1 = createAxe(1);
		Weapon weap2 = createAxe(2);
		
		dut.addToInventory(weap1);
		dut.addToInventory(weap2);
		dut.initializeEquipment();
		
		assertEquals(weap1, dut.getWeapon());
		assertEquals(2, dut.getInventory().size());
		
		dut.getInventory().clear();
		dut.reEquip();
		
		assertEquals(0, dut.getInventory().size());
		assertEquals(null, dut.getWeapon());
	}
	
	private Weapon createAxe(int i) {
		
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
			"baton" + i, 1, 0, 0,
			Weapon.Type.AXE, 0, 0, 0, java.util.Arrays.asList(1),
			modifiers, new java.util.ArrayList<>(), null
		);
		return retVal;
	}
}
