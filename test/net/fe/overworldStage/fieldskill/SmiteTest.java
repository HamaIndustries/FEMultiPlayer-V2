package net.fe.overworldStage.fieldskill;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

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

public final class SmiteTest {
	
	@Test
	public void testAllowed_whenEmptyField_thenFalse() {
		Grid grid = new Grid(6,6, Terrain.PLAIN);
		HashMap<String, Integer> vals = new HashMap<String, Integer>();
		vals.put("HP", 20);
		vals.put("Mov", 5);
		Unit unit = new Unit("test", Class.createClass("Ike"), '-', vals, vals);
		grid.addUnit(unit, 3,3);
		
		assertFalse(new Smite().allowed(unit, grid));
	}
	
	@Test
	public void testAllowed_whenAdjacentSameConUnits_thenTrue() {
		Grid grid = new Grid(6,6, Terrain.PLAIN);
		HashMap<String, Integer> vals = new HashMap<String, Integer>();
		vals.put("HP", 20);
		vals.put("Mov", 5);
		vals.put("Con", 8);
		Unit shover = new Unit("test", Class.createClass("Ike"), '-', vals, vals);
		Unit shovee = new Unit("test", Class.createClass("Ike"), '-', vals, vals);
		grid.addUnit(shover, 3,3);
		grid.addUnit(shovee, 2,3);
		
		assertTrue(new Smite().allowed(shover, grid));
	}
	
	@Test
	public void testAllowed_whenTargetOffTopEdge_thenFalse() {
		Grid grid = new Grid(6,6, Terrain.PLAIN);
		HashMap<String, Integer> vals = new HashMap<String, Integer>();
		vals.put("HP", 20);
		vals.put("Mov", 5);
		vals.put("Con", 8);
		Unit shover = new Unit("test", Class.createClass("Ike"), '-', vals, vals);
		Unit shovee = new Unit("test", Class.createClass("Ike"), '-', vals, vals);
		grid.addUnit(shover, 3,1);
		grid.addUnit(shovee, 3,0);
		
		assertFalse(new Smite().allowed(shover, grid));
	}
	
	@Test
	public void testAllowed_whenTargetOffTopEdge2_thenFalse() {
		Grid grid = new Grid(6,6, Terrain.PLAIN);
		HashMap<String, Integer> vals = new HashMap<String, Integer>();
		vals.put("HP", 20);
		vals.put("Mov", 5);
		vals.put("Con", 8);
		Unit shover = new Unit("test", Class.createClass("Ike"), '-', vals, vals);
		Unit shovee = new Unit("test", Class.createClass("Ike"), '-', vals, vals);
		grid.addUnit(shover, 3,2);
		grid.addUnit(shovee, 3,1);
		
		assertFalse(new Smite().allowed(shover, grid));
	}
	
	@Test
	public void testAllowed_whenTargetOffLeftEdge_thenFalse() {
		Grid grid = new Grid(6,6, Terrain.PLAIN);
		HashMap<String, Integer> vals = new HashMap<String, Integer>();
		vals.put("HP", 20);
		vals.put("Mov", 5);
		vals.put("Con", 8);
		Unit shover = new Unit("test", Class.createClass("Ike"), '-', vals, vals);
		Unit shovee = new Unit("test", Class.createClass("Ike"), '-', vals, vals);
		grid.addUnit(shover, 1,3);
		grid.addUnit(shovee, 0,3);
		
		assertFalse(new Smite().allowed(shover, grid));
	}
	
	@Test
	public void testAllowed_whenTargetOffRightEdge_thenFalse() {
		Grid grid = new Grid(6,6, Terrain.PLAIN);
		HashMap<String, Integer> vals = new HashMap<String, Integer>();
		vals.put("HP", 20);
		vals.put("Mov", 5);
		vals.put("Con", 8);
		Unit shover = new Unit("test", Class.createClass("Ike"), '-', vals, vals);
		Unit shovee = new Unit("test", Class.createClass("Ike"), '-', vals, vals);
		grid.addUnit(shover, 4,3);
		grid.addUnit(shovee, 5,3);
		
		assertFalse(new Smite().allowed(shover, grid));
	}
	
	@Test
	public void testAllowed_whenTargetOffBottomEdge_thenFalse() {
		Grid grid = new Grid(6,6, Terrain.PLAIN);
		HashMap<String, Integer> vals = new HashMap<String, Integer>();
		vals.put("HP", 20);
		vals.put("Mov", 5);
		vals.put("Con", 8);
		Unit shover = new Unit("test", Class.createClass("Ike"), '-', vals, vals);
		Unit shovee = new Unit("test", Class.createClass("Ike"), '-', vals, vals);
		grid.addUnit(shover, 3,4);
		grid.addUnit(shovee, 3,5);
		
		assertFalse(new Smite().allowed(shover, grid));
	}
	
	@Test
	public void testAllowed_whenTargetIsThirdUnit_thenFalse() {
		Grid grid = new Grid(6,6, Terrain.PLAIN);
		HashMap<String, Integer> vals = new HashMap<String, Integer>();
		vals.put("HP", 20);
		vals.put("Mov", 5);
		vals.put("Con", 8);
		Unit shover = new Unit("test", Class.createClass("Ike"), '-', vals, vals);
		Unit shovee = new Unit("test", Class.createClass("Ike"), '-', vals, vals);
		Unit third = new Unit("test", Class.createClass("Ike"), '-', vals, vals);
		grid.addUnit(shover, 3,4);
		grid.addUnit(shovee, 3,3);
		grid.addUnit(third, 3,2);
		
		assertFalse(new Smite().allowed(shover, grid));
	}
	
	@Test
	public void testAllowed_whenTargetIsSea_thenFalse() {
		Grid grid = new Grid(6,6, Terrain.SEA);
		HashMap<String, Integer> vals = new HashMap<String, Integer>();
		vals.put("HP", 20);
		vals.put("Mov", 5);
		vals.put("Con", 8);
		Unit shover = new Unit("test", Class.createClass("Ike"), '-', vals, vals);
		Unit shovee = new Unit("test", Class.createClass("Ike"), '-', vals, vals);
		grid.addUnit(shover, 3,4);
		grid.addUnit(shovee, 3,3);
		
		assertFalse(new Smite().allowed(shover, grid));
	}
	
	@Test
	public void testAllowed_whenTargetIsSeaAndSmiteeIsFlier_thenTrue() {
		Grid grid = new Grid(6,6, Terrain.SEA);
		HashMap<String, Integer> vals = new HashMap<String, Integer>();
		vals.put("HP", 20);
		vals.put("Mov", 5);
		vals.put("Con", 8);
		Unit shover = new Unit("test", Class.createClass("Ike"), '-', vals, vals);
		Unit shovee = new Unit("test", Class.createClass("Falcoknight"), '-', vals, vals);
		grid.addUnit(shover, 3,4);
		grid.addUnit(shovee, 3,3);
		
		assertFalse(new Smite().allowed(shover, grid));
	}
		
}
