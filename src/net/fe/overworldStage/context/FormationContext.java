package net.fe.overworldStage.context;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import chu.engine.anim.AudioPlayer;
import net.fe.FEMultiplayer;
import net.fe.Party;
import net.fe.SoundTrack;
import net.fe.editor.SpawnPoint;
import net.fe.network.command.SwapCommand;
import net.fe.overworldStage.ClientOverworldStage;
import net.fe.overworldStage.CursorContext;
import net.fe.overworldStage.Menu;
import net.fe.overworldStage.MenuContext;
import net.fe.overworldStage.Node;
import net.fe.overworldStage.OverworldContext;
import net.fe.overworldStage.TurnDisplay;
import net.fe.overworldStage.Zone;
import net.fe.overworldStage.Zone.ZoneType;
import net.fe.unit.Unit;

public class FormationContext extends CursorContext {
	
	private Node first;
	private Zone move, attack, heal;
	private Zone allied = new Zone(new HashSet<>(), Zone.ZoneType.MOVE_DARK);
	private Zone enemy = new Zone(new HashSet<>(), Zone.ZoneType.ATTACK_DARK); //TODO change to fog maybe?
	
	private ArrayList<Node> swaps = new ArrayList<Node>();

	public FormationContext(ClientOverworldStage s, OverworldContext prevContext) {
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
	}

	@Override
	public void cursorChanged() {
		Unit u = getHoveredUnit();
		
		stage.setUnitInfoUnit(u);
		if(u != null && !u.hasMoved()) {
			addZones(u);
			if(u.getParty() == stage.getCurrentPlayer().getParty())
				u.sprite.setAnimation("SELECTED");
		}
	}

	@Override
	public void cursorWillChange() {
		removeZones();
		Unit u = getHoveredUnit();
		if(u != null && !u.hasMoved())
			u.sprite.setAnimation("IDLE");
		AudioPlayer.playAudio("cursor");
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

	private void addZones(Unit u) {
		this.move = new Zone(stage.grid.getPossibleMoves(u), ZoneType.MOVE_LIGHT);
		this.attack = new Zone(stage.grid.getAttackRange(u), ZoneType.ATTACK_LIGHT).minus(move);
		this.heal = new Zone(stage.grid.getHealRange(u), ZoneType.HEAL_LIGHT).minus(move).minus(attack);
		stage.addEntity(move);
		stage.addEntity(attack);
		stage.addEntity(heal);
	}
	
	private void removeZones() {
		stage.removeEntity(move);
		stage.removeEntity(attack);
		stage.removeEntity(heal);
	}
	
	private class EndFormationContext extends MenuContext<Option> {

		public EndFormationContext(ClientOverworldStage stage, OverworldContext prev) {
			super(stage, prev, new Menu<Option>());
			menu.addItem(new Option("End formation", () -> {
				//TODO send formation
				stage.removeEntity(allied);
				stage.removeEntity(enemy);
				stage.addCmd(new SwapCommand(swaps));
				stage.send();
				
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
			}));
		}
		
		@Override
		public void onSelect(Option selectedItem) {
			selectedItem.action.run();
		}

		@Override
		public void onLeft() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onRight() {
			// TODO Auto-generated method stub
			
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
