package net.fe.overworldStage.context;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import chu.engine.anim.AudioPlayer;
import net.fe.FEMultiplayer;
import net.fe.Party;
import net.fe.Player;
import net.fe.SoundTrack;
import net.fe.editor.SpawnPoint;
import net.fe.network.command.SwapCommand;
import net.fe.overworldStage.ClientOverworldStage;
import net.fe.overworldStage.Menu;
import net.fe.overworldStage.MenuContext;
import net.fe.overworldStage.Node;
import net.fe.overworldStage.OverworldContext;
import net.fe.overworldStage.TurnDisplay;
import net.fe.overworldStage.Zone;

public class RepositionContext extends OverworldInspectionContext implements FormationContext {
	
	/**
	 * The list of players currently doing their formation.
	 */
	private TreeSet<Byte> formingPlayers;
	private Node first;
	private Zone allied = new Zone(new HashSet<>(), Zone.ZoneType.MOVE_DARK);
	private Zone enemy = new Zone(new HashSet<>(), Zone.ZoneType.ATTACK_DARK); //TODO change to fog maybe?
	
	private ArrayList<Node> swaps = new ArrayList<Node>();

	public RepositionContext(ClientOverworldStage s, OverworldContext prevContext) {
		super(s, prevContext);
		SoundTrack.loop("preparations");
		Set<SpawnPoint> spawns = s.grid.getSpawns();
		for(SpawnPoint spawn : spawns)
			if(spawn.team.equals(FEMultiplayer.getLocalPlayer().getParty().getColor()))
				allied.add(new Node(spawn.x, spawn.y));
			else
				enemy.add(new Node(spawn.x, spawn.y));
		stage.addEntity(allied);
		stage.addEntity(enemy);
		formingPlayers = new TreeSet<>();
		for(Player player : stage.getNonSpectators())
			formingPlayers.add(player.getID());
	}

	@Override
	public void onCancel() {
		if(first != null)
			AudioPlayer.playAudio("cancel");
		first = null;
	}

	@Override
	public void onSelect() {
		Node selected = new Node(cursor.getXCoord(), cursor.getYCoord());
		if(!allied.getNodes().contains(selected))
			new EndFormationContext(stage, this).startContext();
		else {
			AudioPlayer.playAudio("select");
			if(first == null)
				first = selected;
			else {
				swaps.add(first);
				swaps.add(selected);
				stage.grid.swap(first, selected);
				first = null;
			}
		}
	}
	

	@Override
	public void removePlayer(byte id) {
		formingPlayers.remove(id);
	}

	private class EndFormationContext extends MenuContext<Option> implements FormationContext {

		public EndFormationContext(ClientOverworldStage stage, OverworldContext prev) {
			super(stage, prev, new Menu<Option>());
			menu.addItem(new Option("End formation", () -> {
				stage.removeEntity(allied);
				stage.removeEntity(enemy);
				stage.addCmd(new SwapCommand(swaps, FEMultiplayer.getLocalPlayer().getID()));
				stage.send();
				formingPlayers.remove(FEMultiplayer.getLocalPlayer().getID());
				new WaitForFormationContext(stage, this).startContext();
			}));
		}
		
		@Override
		public void onSelect(Option selectedItem) {
			selectedItem.action.run();
		}

		@Override
		public void onLeft() {
			
		}

		@Override
		public void onRight() {
			
		}

		@Override
		public void removePlayer(byte id) {
			formingPlayers.remove(id);
		}
	}
	
	private class WaitForFormationContext extends OverworldInspectionContext implements FormationContext {

		public WaitForFormationContext(ClientOverworldStage s, OverworldContext prevContext) {
			super(s, prevContext);
			if(formingPlayers.isEmpty())
				startGame();
		}

		@Override
		public void onSelect() {
			//Nothing to do
		}
		
		@Override
		public void onCancel() {
			//Can't cancel
		}
		
		private void startGame() {
			if(stage.getCurrentPlayer().equals(FEMultiplayer.getLocalPlayer())) {
				new Idle(stage, stage.getCurrentPlayer()).startContext();
				stage.addEntity(new TurnDisplay(true, Party.TEAM_BLUE));
				SoundTrack.loop("overworld");
			} else {
				new WaitForMessages(stage).startContext();
				if(FEMultiplayer.getLocalPlayer().isSpectator())
					stage.addEntity(new TurnDisplay(false, stage.getCurrentPlayer().getParty().getColor()));
				else
					stage.addEntity(new TurnDisplay(false, Party.TEAM_RED));
				SoundTrack.loop("enemy");
			}
		}

		@Override
		public void removePlayer(byte id) {
			formingPlayers.remove(id);
			if(formingPlayers.isEmpty())
				startGame();
		}
	}
	
	
	
	private static class Option {
		String name;
		Runnable action;
		
		public Option(String name, Runnable action) {
			this.name = name;
			this.action = action;
		}
		
		@Override
		public String toString() {
			return name;
		}
	}

}
