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
import net.fe.unit.Statistics;

public final class UseCommandTest {
	
	@Test
	public void testHealingItem_Server() {
		Statistics vals = new Statistics();
		vals = vals.copy("HP", 20);
		vals = vals.copy("Mov", 5);
		vals = vals.copy("Con", 8);
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
		Statistics vals = new Statistics();
		vals = vals.copy("HP", 20);
		vals = vals.copy("Mov", 5);
		vals = vals.copy("Con", 8);
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
		Statistics vals = new Statistics();
		vals = vals.copy("HP", 20);
		vals = vals.copy("Mov", 5);
		vals = vals.copy("Con", 8);
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
		Statistics vals = new Statistics();
		vals = vals.copy("HP", 20);
		vals = vals.copy("Mov", 5);
		vals = vals.copy("Con", 8);
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
		Statistics vals = new Statistics();
		vals = vals.copy("HP", 20);
		vals = vals.copy("Mov", 5);
		vals = vals.copy("Con", 8);
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
		
		Weapon retVal = new Weapon(
			"baton" + i, 1, 0, 0,
			Weapon.Type.AXE, 0, 0, 0, java.util.Arrays.asList(1),
			new Statistics(), new java.util.ArrayList<>(), null
		);
		return retVal;
	}
}
