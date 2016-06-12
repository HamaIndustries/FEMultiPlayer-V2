package net.fe.unit;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.HashMap;

public final class UnitTest {
	
	@Test
	public void testGetWeapon_FreshUnitNonHasNoEquippedWeapon() {
		Statistics vals = new Statistics(20, 0, 0, 0, 0, 0, 0, 0, 5, 8, 0);
		Unit dut = new Unit("ADFASDF", Class.createClass("Phantom"), '-', vals, vals);
		
		assertEquals(null, dut.getWeapon());
		assertEquals(0, dut.getInventory().size());
	}
	
	@Test
	public void testGetWeapon_AddingItemDoesNotEquipWeapon() {
		Statistics vals = new Statistics(20, 0, 0, 0, 0, 0, 0, 0, 5, 8, 0);
		Unit dut = new Unit("ADFASDF", Class.createClass("Phantom"), '-', vals, vals);
		
		dut.addToInventory(new RiseTome());
		
		assertEquals(null, dut.getWeapon());
		assertEquals(1, dut.getInventory().size());
	}
	
	@Test
	public void testGetWeapon_AddingWeaponDoesNotEquipWeapon() {
		Statistics vals = new Statistics(20, 0, 0, 0, 0, 0, 0, 0, 5, 8, 0);
		Unit dut = new Unit("ADFASDF", Class.createClass("Phantom"), '-', vals, vals);
		
		dut.addToInventory(createAxe(1));
		
		assertEquals(null, dut.getWeapon());
		assertEquals(1, dut.getInventory().size());
	}
	
	@Test
	public void testGetWeapon_EquippingWeaponMakesWeaponEquipped() {
		Statistics vals = new Statistics(20, 0, 0, 0, 0, 0, 0, 0, 5, 8, 0);
		Unit dut = new Unit("ADFASDF", Class.createClass("Phantom"), '-', vals, vals);
		Weapon weap = createAxe(1);
		
		dut.equip(weap);
		
		assertEquals(1, dut.getInventory().size());
		assertEquals(weap, dut.getWeapon());
	}
	
	@Test
	public void testGetWeapon_UnequippingWeaponMakesWeaponUnequipped() {
		Statistics vals = new Statistics(20, 0, 0, 0, 0, 0, 0, 0, 5, 8, 0);
		Unit dut = new Unit("ADFASDF", Class.createClass("Phantom"), '-', vals, vals);
		Weapon weap = createAxe(1);
		
		dut.equip(weap);
		dut.unequip();
		
		assertEquals(null, dut.getWeapon());
		assertEquals(1, dut.getInventory().size());
	}
	
	@Test
	public void testGetWeapon_BreakingWeaponEquipsNextWeapon() {
		Statistics vals = new Statistics(20, 0, 0, 0, 0, 0, 0, 0, 5, 8, 0);
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
		Statistics vals = new Statistics(20, 0, 0, 0, 0, 0, 0, 0, 5, 8, 0);
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
		Statistics vals = new Statistics(20, 0, 0, 0, 0, 0, 0, 0, 5, 8, 0);
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
	
	@Test
	public void testHashcode() {
		Statistics vals = new Statistics(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11);
		Unit dut1 = new Unit("ADFASDF", Class.createClass("Phantom"), '-', vals, vals);
		Unit dut2 = new Unit("ADFASDF", Class.createClass("Phantom"), '-', vals, vals);
		
		assertEquals(dut1.hashCode(), dut2.hashCode());
	}
	
	@Test
	public void testGetCopy_isNotReferenceEqual() {
		Statistics vals = new Statistics(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11);
		Unit original = new Unit("ADFASDF", Class.createClass("Phantom"), '-', vals, vals);
		Unit copy = original.getCopy();
		
		assertFalse("top-level object", original == copy);
	}
	
	@Test
	public void testGetCopy_inventoryIsClonedTo() {
		Statistics vals = new Statistics(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11);
		Unit original = new Unit("ADFASDF", Class.createClass("Phantom"), '-', vals, vals);
		original.addToInventory(createAxe(1));
		Unit copy = original.getCopy();
		
		assertFalse(original.getInventory().get(0) == copy.getInventory().get(0));
		assertTrue(original.getInventory().get(0).equals(copy.getInventory().get(0)));
	}
	
	private Weapon createAxe(int i) {
		
		Weapon retVal = new Weapon(
			"baton" + i, 1, 0, 0,
			Weapon.Type.AXE, 0, 0, 0, (s) -> java.util.Arrays.asList(1),
			new Statistics(), new java.util.ArrayList<>(), null
		);
		return retVal;
	}
}
