package net.fe.network.command;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;
import java.util.HashMap;
import net.fe.unit.UnitIdentifier;
import net.fe.network.message.CommandMessage;
import net.fe.FEMultiplayer;
import net.fe.Player;
import net.fe.Session;
import net.fe.overworldStage.ClientOverworldStage;
import net.fe.overworldStage.Grid;
import net.fe.overworldStage.Node;
import net.fe.overworldStage.Terrain;
import net.fe.overworldStage.Zone;
import net.fe.unit.Class;
import net.fe.unit.Unit;
import net.fe.unit.HealingItem;
import net.fe.unit.RiseTome;
import net.fe.unit.Weapon;

public final class UseCommandTest {
	
	@Test
	public void testHealingItem_Server() {
		HashMap<String, Integer> vals = new HashMap<String, Integer>();
		vals.put("HP", 20);
		vals.put("Mov", 5);
		vals.put("Con", 8);
		Unit unit = new Unit("test", Class.createClass("Ike"), '-', vals, vals);
		unit.addToInventory(new HealingItem("Blarg", 5, 0, 1));
		unit.setHp(3);
		
		Object result = new UseCommand(0).applyServer(null, unit);
		
		assertEquals(5 + 3, unit.getHp());
		assertEquals(2, unit.getInventory().get(0).getUses());
		assertEquals(null, result);
	}
	
	@Test
	public void testHealingItem_HighHealth_Server() {
		HashMap<String, Integer> vals = new HashMap<String, Integer>();
		vals.put("HP", 20);
		vals.put("Mov", 5);
		vals.put("Con", 8);
		Unit unit = new Unit("test", Class.createClass("Ike"), '-', vals, vals);
		unit.addToInventory(new HealingItem("Blarg", 15, 0, 1));
		unit.setHp(15);
		
		Object result = new UseCommand(0).applyServer(null, unit);
		
		assertEquals(20, unit.getHp());
		assertEquals(2, unit.getInventory().get(0).getUses());
		assertEquals(null, result);
	}
	
	@Test
	public void testHealingItem_OneUse_Server() {
		HashMap<String, Integer> vals = new HashMap<String, Integer>();
		vals.put("HP", 20);
		vals.put("Mov", 5);
		vals.put("Con", 8);
		Unit unit = new Unit("test", Class.createClass("Ike"), '-', vals, vals);
		unit.addToInventory(new HealingItem("Blarg", 15, 0, 1));
		unit.setHp(15);
		unit.getInventory().get(0).setUsesDEBUGGING(1);
		
		Object result = new UseCommand(0).applyServer(null, unit);
		
		assertEquals(20, unit.getHp());
		assertEquals(0, unit.getInventory().size());
		assertEquals(null, result);
	}
	
	@Test
	public void testHealingItem_OneUseWithWeapons_Server() {
		HashMap<String, Integer> vals = new HashMap<String, Integer>();
		vals.put("HP", 20);
		vals.put("Mov", 5);
		vals.put("Con", 8);
		Unit unit = new Unit("test", Class.createClass("Ike"), '-', vals, vals);
		unit.addToInventory(new HealingItem("Blarg", 15, 0, 1));
		unit.setHp(15);
		unit.getInventory().get(0).setUsesDEBUGGING(1);
		unit.addToInventory(createAxe(1));
		unit.addToInventory(createAxe(2));
		unit.addToInventory(createAxe(3));
		
		// assert doesn't throw anything
		Object result = new UseCommand(0).applyServer(null, unit);
		assertEquals(null, result);
	}
	
	@Test
	public void testRise_Server() {
		HashMap<String, Integer> vals = new HashMap<String, Integer>();
		vals.put("HP", 20);
		vals.put("Mov", 5);
		vals.put("Con", 8);
		Unit unit = new Unit("test", Class.createClass("Ike"), '-', vals, vals);
		unit.addToInventory(new RiseTome());
		unit.setHp(3);
		
		try {
			new UseCommand(0).applyServer(null, unit);
			fail("No exception thrown");
		} catch (IllegalStateException e) {
			// success
		}
	}
	
	
	private Weapon createAxe(int i) {
		Weapon retVal = new Weapon("baton" + i);
		retVal.type = Weapon.Type.AXE;
		retVal.range = java.util.Arrays.asList(1);
		retVal.setMaxUses(1);
		retVal.setUsesDEBUGGING(1);
		return retVal;
	}
}
