package net.fe.overworldStage.context;

import java.util.HashMap;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.lwjgl.opengl.Display;

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
import net.fe.unit.Statistics;
import net.fe.overworldStage.Zone.RangeIndicator;

public class ShoveTargetTest {
	
	@Before
	public void globalDisplayBefore() throws org.lwjgl.LWJGLException {
		Display.setDisplayMode(new org.lwjgl.opengl.DisplayMode(5, 5));
		Display.create();
	}
	
	@Before
	public void globalPlayerBefore() {
		Player p = new Player("null pointers", (byte) 0);
		p.setTeam(Player.TEAM_BLUE);
		p.getParty().setColor(net.fe.Party.TEAM_BLUE);
		FEMultiplayer.setLocalPlayer(p);
	}
	
	@After
	public void globalDisplayAfter() {
		Display.destroy();
	}
	
	// I couldn't convince either either CI to initialize LWJGL
		// Appveyor : Pixel format not accelerated
		// Travis: Could not initialize class org.lwjgl.opengl.Display
	// @Test
	public void testTargetsWhenAllShoveesThenFourTargets() {
		// things that have nothing to do with the test but need to be set up anyway
		Session session = new Session();
		session.addPlayer(FEMultiplayer.getLocalPlayer());
		ClientOverworldStage stage = new ClientOverworldStage(session);
		stage.cursor.stage = stage; // this just looks wrong
		stage.grid = new Grid(6,6, Terrain.FLOOR);
		
		Statistics shoveeVals = new Statistics(20, 0, 0, 0, 0, 0, 0, 0, 5, 2, 0);
		Unit shovee1 = new Unit("Lyn", Class.createClass("Sorcerer"), '-', shoveeVals, shoveeVals);
		Unit shovee2 = new Unit("Lyn", Class.createClass("Sorcerer"), '-', shoveeVals, shoveeVals);
		Unit shovee3 = new Unit("Lyn", Class.createClass("Sorcerer"), '-', shoveeVals, shoveeVals);
		Unit shovee4 = new Unit("Lyn", Class.createClass("Sorcerer"), '-', shoveeVals, shoveeVals);
		stage.grid.addUnit(shovee1, 2,3);
		stage.grid.addUnit(shovee2, 3,2);
		stage.grid.addUnit(shovee3, 3,4);
		stage.grid.addUnit(shovee4, 4,3);
		
		Zone zone = new RangeIndicator(stage.grid.getRange(new Node(3,3), 1), null);
		Statistics shoverVals = new Statistics(20, 0, 0, 0, 0, 0, 0, 0, 5, 15, 0);
		Unit shover = new Unit("test", Class.createClass("Sorcerer"), '-', shoverVals, shoverVals);
		stage.grid.addUnit(shover, 3, 3);
		
		// the actual tests
		ShoveTarget dut = new ShoveTarget(stage, null, zone, shover);
		dut.startContext();
		
		assertEquals(shovee1, dut.getCurrentTarget());
		dut.nextTarget();
		assertEquals(shovee2, dut.getCurrentTarget());
		dut.nextTarget();
		assertEquals(shovee3, dut.getCurrentTarget());
		dut.nextTarget();
		assertEquals(shovee4, dut.getCurrentTarget());
		dut.nextTarget();
		assertEquals(shovee1, dut.getCurrentTarget());
	}
}
