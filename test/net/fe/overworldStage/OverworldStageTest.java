package net.fe.overworldStage;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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

public final class OverworldStageTest {
	
	@Before
	public void globalPlayerBefore() {
		Player p = new Player("null pointers", (byte) 0);
		p.setTeam(Player.TEAM_BLUE);
		p.getParty().setColor(net.fe.Party.TEAM_BLUE);
		FEMultiplayer.setLocalPlayer(p);
	}

	@Test
	public void testCommand_whenShove_thenMovesTarget() {
		
		// given a stage and a unit
		new net.fe.network.FEServer(); // ...
		Session session = new Session();
		session.setMap("test"); // Must be a valid name, despite the bypass `stage.grid = ` later
		session.addPlayer(FEMultiplayer.getLocalPlayer());
		OverworldStage stage = new OverworldStage(session);
		stage.grid = new Grid(6,6, Terrain.PLAIN);
		
		HashMap<String, Integer> vals = new HashMap<String, Integer>();
		vals.put("HP", 20);
		vals.put("Mov", 5);
		vals.put("Con", 8);
		Unit shover = new Unit("shover", Class.createClass("Sorcerer"), '-', vals, vals);
		Unit shovee = new Unit("shovee", Class.createClass("Sorcerer"), '-', vals, vals);
		FEMultiplayer.getLocalPlayer().getParty().addUnit(shover); // processCommands cannot find the unit without this
		FEMultiplayer.getLocalPlayer().getParty().addUnit(shovee); // processCommands cannot find the unit without this 
		assertTrue("Failed to add unit to grid", stage.addUnit(shover, 3, 3));
		assertTrue("Failed to add unit to grid", stage.addUnit(shovee, 4, 3));
		
		// when processing a shove command
		CommandMessage command = new CommandMessage(
				new UnitIdentifier(shover), 0,0,
				new java.util.ArrayList<net.fe.fightStage.AttackRecord>(),
				"SHOVE", new UnitIdentifier(shovee));
		stage.processCommands(command);
		
		// then the shovee has a new position
		assertEquals("x", 5, shovee.getXCoord());
		assertEquals("y", 3, shovee.getYCoord());
	}

	@Test
	public void testCommand_whenSmite_thenMovesTarget() {
		
		// given a stage and a unit
		new net.fe.network.FEServer(); // ...
		Session session = new Session();
		session.setMap("test"); // Must be a valid name, despite the bypass `stage.grid = ` later
		session.addPlayer(FEMultiplayer.getLocalPlayer());
		OverworldStage stage = new OverworldStage(session);
		stage.grid = new Grid(6,6, Terrain.PLAIN);
		
		HashMap<String, Integer> vals = new HashMap<String, Integer>();
		vals.put("HP", 20);
		vals.put("Mov", 5);
		vals.put("Con", 8);
		Unit shover = new Unit("shover", Class.createClass("Sorcerer"), '-', vals, vals);
		Unit shovee = new Unit("shovee", Class.createClass("Sorcerer"), '-', vals, vals);
		FEMultiplayer.getLocalPlayer().getParty().addUnit(shover); // processCommands cannot find the unit without this
		FEMultiplayer.getLocalPlayer().getParty().addUnit(shovee); // processCommands cannot find the unit without this 
		assertTrue("Failed to add unit to grid", stage.addUnit(shover, 1, 1));
		assertTrue("Failed to add unit to grid", stage.addUnit(shovee, 1, 2));
		
		// when processing a shove command
		CommandMessage command = new CommandMessage(
				new UnitIdentifier(shover), 0,0,
				new java.util.ArrayList<net.fe.fightStage.AttackRecord>(),
				"SMITE", new UnitIdentifier(shovee));
		stage.processCommands(command);
		
		// then the shovee has a new position
		assertEquals("x", 1, shovee.getXCoord());
		assertEquals("y", 4, shovee.getYCoord());
	}
	
}
