package net.fe.overworldStage.fieldskill;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

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
import net.fe.unit.Weapon;
import net.fe.unit.Statistics;

public final class ShoveTest {
	
	@Before
	public void globalTimeDeltaHijack() {
		try {
			Field f = chu.engine.Game.class.getDeclaredField("timeDelta");
			f.setAccessible(true);
			f.set(null, (long) 1e8);
		} catch (NoSuchFieldException e) {
		} catch (SecurityException e) {
		} catch (IllegalArgumentException e) {
		} catch (IllegalAccessException e) {
		}
	}
	
	@Test
	public void testAllowed_whenEmptyField_thenFalse() {
		Grid grid = new Grid(6,6, Terrain.PLAIN);
		Statistics vals = new Statistics(20, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0);
		Unit unit = new Unit("test", Class.createClass("Chrom"), '-', vals, vals);
		grid.addUnit(unit, 3,3);
		
		assertFalse(new Shove().allowed(unit, grid));
	}
	
	@Test
	public void testAllowed_whenAdjacentSameConUnits_thenTrue() {
		Grid grid = new Grid(6,6, Terrain.PLAIN);
		Statistics vals = new Statistics(20, 0, 0, 0, 0, 0, 0, 0, 5, 8, 0);
		Unit shover = new Unit("test", Class.createClass("Chrom"), '-', vals, vals);
		Unit shovee = new Unit("test", Class.createClass("Chrom"), '-', vals, vals);
		grid.addUnit(shover, 3,3);
		grid.addUnit(shovee, 2,3);
		
		assertTrue(new Shove().allowed(shover, grid));
	}
	
	@Test
	public void testAllowed_whenNonadjacentSameConUnits_thenFalse() {
		Grid grid = new Grid(6,6, Terrain.PLAIN);
		Statistics vals = new Statistics(20, 0, 0, 0, 0, 0, 0, 0, 5, 8, 0);
		Unit shover = new Unit("test", Class.createClass("Chrom"), '-', vals, vals);
		Unit shovee = new Unit("test", Class.createClass("Chrom"), '-', vals, vals);
		grid.addUnit(shover, 2,4);
		grid.addUnit(shovee, 4,2);
		
		assertFalse(new Shove().allowed(shover, grid));
	}
	
	@Test
	public void testAllowed_whenTargetOffTopEdge_thenFalse() {
		Grid grid = new Grid(6,6, Terrain.PLAIN);
		Statistics vals = new Statistics(20, 0, 0, 0, 0, 0, 0, 0, 5, 8, 0);
		Unit shover = new Unit("test", Class.createClass("Chrom"), '-', vals, vals);
		Unit shovee = new Unit("test", Class.createClass("Chrom"), '-', vals, vals);
		grid.addUnit(shover, 3,1);
		grid.addUnit(shovee, 3,0);
		
		assertFalse(new Shove().allowed(shover, grid));
	}
	
	@Test
	public void testAllowed_whenTargetOffLeftEdge_thenFalse() {
		Grid grid = new Grid(6,6, Terrain.PLAIN);
		Statistics vals = new Statistics(20, 0, 0, 0, 0, 0, 0, 0, 5, 8, 0);
		Unit shover = new Unit("test", Class.createClass("Chrom"), '-', vals, vals);
		Unit shovee = new Unit("test", Class.createClass("Chrom"), '-', vals, vals);
		grid.addUnit(shover, 1,3);
		grid.addUnit(shovee, 0,3);
		
		assertFalse(new Shove().allowed(shover, grid));
	}
	
	@Test
	public void testAllowed_whenTargetOffRightEdge_thenFalse() {
		Grid grid = new Grid(6,6, Terrain.PLAIN);
		Statistics vals = new Statistics(20, 0, 0, 0, 0, 0, 0, 0, 5, 8, 0);
		Unit shover = new Unit("test", Class.createClass("Chrom"), '-', vals, vals);
		Unit shovee = new Unit("test", Class.createClass("Chrom"), '-', vals, vals);
		grid.addUnit(shover, 4,3);
		grid.addUnit(shovee, 5,3);
		
		assertFalse(new Shove().allowed(shover, grid));
	}
	
	@Test
	public void testAllowed_whenTargetOffBottomEdge_thenFalse() {
		Grid grid = new Grid(6,6, Terrain.PLAIN);
		Statistics vals = new Statistics(20, 0, 0, 0, 0, 0, 0, 0, 5, 8, 0);
		Unit shover = new Unit("test", Class.createClass("Chrom"), '-', vals, vals);
		Unit shovee = new Unit("test", Class.createClass("Chrom"), '-', vals, vals);
		grid.addUnit(shover, 3,4);
		grid.addUnit(shovee, 3,5);
		
		assertFalse(new Shove().allowed(shover, grid));
	}
	
	@Test
	public void testAllowed_whenTargetIsThirdUnit_thenFalse() {
		Grid grid = new Grid(6,6, Terrain.PLAIN);
		Statistics vals = new Statistics(20, 0, 0, 0, 0, 0, 0, 0, 5, 8, 0);
		Unit shover = new Unit("test", Class.createClass("Chrom"), '-', vals, vals);
		Unit shovee = new Unit("test", Class.createClass("Chrom"), '-', vals, vals);
		Unit third = new Unit("test", Class.createClass("Chrom"), '-', vals, vals);
		grid.addUnit(shover, 3,4);
		grid.addUnit(shovee, 3,3);
		grid.addUnit(third, 3,2);
		
		assertFalse(new Shove().allowed(shover, grid));
	}
	
	@Test
	public void testAllowed_whenTargetIsSea_thenFalse() {
		Grid grid = new Grid(6,6, Terrain.SEA);
		Statistics vals = new Statistics(20, 0, 0, 0, 0, 0, 0, 0, 5, 8, 0);
		Unit shover = new Unit("test", Class.createClass("Chrom"), '-', vals, vals);
		Unit shovee = new Unit("test", Class.createClass("Chrom"), '-', vals, vals);
		grid.addUnit(shover, 3,4);
		grid.addUnit(shovee, 3,3);
		
		assertFalse(new Shove().allowed(shover, grid));
	}
	
	@Test
	public void testAllowed_whenTargetIsSeaAndShoveeIsFlier_thenTrue() {
		Grid grid = new Grid(6,6, Terrain.SEA);
		Statistics vals = new Statistics(20, 0, 0, 0, 0, 0, 0, 0, 5, 8, 0);
		Unit shover = new Unit("test", Class.createClass("Chrom"), '-', vals, vals);
		Unit shovee = new Unit("test", Class.createClass("Falconknight"), '-', vals, vals);
		grid.addUnit(shover, 3,4);
		grid.addUnit(shovee, 3,3);
		
		assertTrue(new Shove().allowed(shover, grid));
	}
	
	@Test
	public void testAllowed_whenShoverIsTiny_thenFalse() {
		Grid grid = new Grid(6,6, Terrain.PLAIN);
		Statistics shoverVals = new Statistics(20, 0, 0, 0, 0, 0, 0, 0, 5, 2, 0);
		Statistics shoveeVals = new Statistics(20, 0, 0, 0, 0, 0, 0, 0, 5, 8, 0);
		Unit shover = new Unit("test", Class.createClass("Chrom"), '-', shoverVals, shoverVals);
		Unit shovee = new Unit("test", Class.createClass("Chrom"), '-', shoveeVals, shoveeVals);
		grid.addUnit(shover, 2,4);
		grid.addUnit(shovee, 4,2);
		
		assertFalse(new Shove().allowed(shover, grid));
	}
	
	@Test
	public void testAllowed_whenShoverIsHoldingWeapon_thenNotError() {
		Grid grid = new Grid(6,6, Terrain.PLAIN);
		Statistics shoverVals = new Statistics(20, 0, 0, 0, 0, 0, 0, 0, 5, 2, 0);
		Statistics shoveeVals = new Statistics(20, 0, 0, 0, 0, 0, 0, 0, 5, 8, 0);
		Unit shover = new Unit("test", Class.createClass("Chrom"), '-', shoverVals, shoverVals);
		Unit shovee = new Unit("test", Class.createClass("Chrom"), '-', shoveeVals, shoveeVals);
		Weapon weapon = createSword(1);
		shover.getInventory().add(weapon);
		shover.equip(0);
		grid.addUnit(shover, 2,4);
		grid.addUnit(shovee, 4,2);
		
		assertFalse(new Shove().allowed(shover, grid));
	}
	/*
	@Test
	public void testDo() {
		Grid grid = new Grid(6,6, Terrain.PLAIN);
		HashMap<String, Integer> vals = new HashMap<String, Integer>();
		vals.put("HP", 20);
		vals.put("Mov", 5);
		vals.put("Con", 8);
		Unit shover = new Unit("test", Class.createClass("Chrom"), '-', vals, vals);
		Unit shovee = new Unit("test", Class.createClass("Chrom"), '-', vals, vals);
		grid.addUnit(shover, 3,3);
		grid.addUnit(shovee, 4,3);
		
		Shove.doShove(shover, shovee);
		
		for (int i = 0; i < 200; i++) {
			// "animation"
			//shovee.beginStep();
			shovee.onStep();
			shovee.endStep();
		}
		
		assertEquals(5, shovee.getXCoord());
		assertEquals(3, shovee.getYCoord());
	}*/
	
	private Weapon createSword(int i) {
		
		Weapon retVal = new Weapon(
			"baton" + i, 1, 0, 0,
			Weapon.Type.SWORD, 0, 0, 0, (s) -> java.util.Arrays.asList(1),
			new Statistics(), new java.util.ArrayList<>(), null
		);
		return retVal;
	}
}
