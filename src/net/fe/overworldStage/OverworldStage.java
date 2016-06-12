package net.fe.overworldStage;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;

import net.fe.FEMultiplayer;
import net.fe.Party;
import net.fe.Player;
import net.fe.Session;
import net.fe.editor.Level;
import net.fe.editor.SpawnPoint;
import net.fe.fightStage.AttackRecord;
import net.fe.fightStage.CombatCalculator;
import net.fe.fightStage.HealCalculator;
import net.fe.modifier.Modifier;
import net.fe.network.Chat;
import net.fe.network.FEServer;
import net.fe.network.Message;
import net.fe.network.command.Command;
import net.fe.network.message.ChatMessage;
import net.fe.network.message.CommandMessage;
import net.fe.network.message.EndGame;
import net.fe.network.message.EndTurn;
import net.fe.network.message.QuitMessage;
import net.fe.overworldStage.context.TradeContext;
import net.fe.overworldStage.objective.Objective;
import net.fe.unit.Item;
import net.fe.unit.RiseTome;
import net.fe.unit.Unit;
import net.fe.unit.UnitIdentifier;

import org.newdawn.slick.Color;
import org.newdawn.slick.util.ResourceLoader;

import chu.engine.Game;
import chu.engine.Stage;

// TODO: Auto-generated Javadoc
/**
 * The Class OverworldStage.
 */
public class OverworldStage extends Stage {
	
	/** The grid. */
	public Grid grid;
	
	/** The session. */
	protected Session session;
	
	/** The turn order. */
	private ArrayList<Player> turnOrder;
	
	/** The current player. */
	private int currentPlayer;
	
	/** The turn count. */
	private int turnCount;

	/**
	 * Instantiates a new overworld stage.
	 *
	 * @param s the s
	 */
	public OverworldStage(Session s) {
		super(null);
		this.session = s;
		System.out.println(session.getObjective().getDescription());
		turnOrder = new ArrayList<Player>();
		for(Player p : session.getNonSpectators()) {
			turnOrder.add(p);
		}
		Collections.sort(turnOrder, new Comparator<Player>() {
			@Override
			public int compare(Player arg0, Player arg1) {
				return arg0.getID() - arg1.getID();
			}
		});
		currentPlayer = 0;
		turnCount = 1;
		loadLevel(session.getMap());
		for(Modifier m : session.getModifiers()) {
			m.initOverworldUnits(this.getAllUnits());
		}
		processAddStack();
	}
	
	/**
	 * Gets the player by id.
	 *
	 * @param id the id
	 * @return the player by id
	 */
	public Player getPlayerByID(int id) {
		for(Player p : session.getPlayers()) {
			if(p.getID() == id) {
				return p;
			}
		}
		return null;
	}
	
	/**
	 * Gets the current player.
	 *
	 * @return the current player
	 */
	public Player getCurrentPlayer() {
		return turnOrder.get(currentPlayer);
	}
	
	/**
	 * Gets the next player.
	 *
	 * @return the next player
	 */
	public Player getNextPlayer() {
		int i = currentPlayer + 1;
		if(i >= turnOrder.size()) i = 0;
		return turnOrder.get(i);
	}

	/**
	 * Gets the terrain.
	 *
	 * @param x the x
	 * @param y the y
	 * @return the terrain
	 */
	public final Terrain getTerrain(int x, int y) {
		return grid.getTerrain(x, y);
	}

	/**
	 * Gets the unit.
	 *
	 * @param x the x
	 * @param y the y
	 * @return the unit
	 */
	public final Unit getUnit(int x, int y) {
		return grid.getUnit(x, y);
	}

	/**
	 * Adds the unit.
	 *
	 * @param u the u
	 * @param x the x
	 * @param y the y
	 * @return true, if successful
	 */
	public final boolean addUnit(Unit u, int x, int y) {
		if (grid.addUnit(u, x, y)) {
			this.addEntity(u);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Removes the unit.
	 *
	 * @param x the x
	 * @param y the y
	 * @return the unit
	 */
	public final Unit removeUnit(int x, int y) {
		Unit u = grid.removeUnit(x, y);
		if (u != null) {
			this.removeEntity(u);
		}
		return u;
	}
	
	/**
	 * Removes the unit.
	 *
	 * @param u the u
	 */
	public final void removeUnit(Unit u) {
		grid.removeUnit(u.getXCoord(), u.getYCoord());
		this.removeEntity(u);
	}
	
	/**
	 * Load level.
	 *
	 * @param levelName the level name
	 */
	public void loadLevel(String levelName) {
        try {
        	InputStream in = ResourceLoader.getResourceAsStream("levels/"+levelName+".lvl");
            ObjectInputStream ois = new ObjectInputStream(in);
            Level level = (Level) ois.readObject();
            Set<SpawnPoint> spawns = new HashSet<>(level.spawns);
            grid = new Grid(level.width, level.height, Terrain.NONE);
            for(int i=0; i<level.tiles.length; i++) {
            	for(int j=0; j<level.tiles[0].length; j++) {
            		grid.setTerrain(j, i, Tile.getTerrainFromID(level.tiles[i][j]));
            		if(Tile.getTerrainFromID(level.tiles[i][j]) == Terrain.THRONE) {
            			int blue = 0;
            			int red = 0;
            			for(SpawnPoint sp : spawns) {
                    		if(sp.team.equals(Party.TEAM_BLUE)) {
                				blue += Math.abs(sp.x-j) + Math.abs(sp.y-i);
                    		} else {
                    			red += Math.abs(sp.x-j) + Math.abs(sp.y-i);
                    		}
                    	}
            			if(blue < red) {
            				System.out.println(blue + " "+ red);
            				grid.setThronePos(Party.TEAM_BLUE, j, i);
            			} else {
            				System.out.println(blue + " "+ red);
            				grid.setThronePos(Party.TEAM_RED, j, i);
            			}
            		}
            	}
            }
            
            // Add units
            for(Player p : session.getPlayers()) {
            	Color team = p.getParty().getColor();
    			for(int i=0; i<p.getParty().size(); i++) {
    				SpawnPoint remove = null;
                	for(SpawnPoint sp : spawns) {
                		if(sp.team.equals(team)) {
            				Unit u = p.getParty().getUnit(i);
            				addUnit(u, sp.x, sp.y);
            				remove = sp;
            				break;
                		}
                	}
                	spawns.remove(remove);
    			}
            }
            ois.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
	}

	@Override
	public void beginStep(List<Message> messages) {
		for(Message message : messages) {
			this.executeMessage(message);
		}
	}
	
	/** Peroform an action in response to receiving the message */
	protected final void executeMessage(Message message) {
			if(message instanceof CommandMessage) {
				processCommands((CommandMessage)message);
			}
			else if(message instanceof EndTurn) {
				//Only end the turn if it is this player's turn to end. (Or, if for some reason we want to let
				//the server end turns in the future.
				System.out.println("" + message.origin + " " + currentPlayer);
				if(message.origin == getCurrentPlayer().getID() || message.origin == 0){
					((EndTurn) message).checkHp((ui) -> this.getUnit(ui));
					doEndTurn(message.origin);
					currentPlayer++;
					if(currentPlayer >= turnOrder.size()) {
						currentPlayer = 0;
					}
					doStartTurn(message.origin);
				}
			}
			else if(message instanceof QuitMessage) {
				this.checkEndGame();
			}
			else if(message instanceof EndGame) {
				this.checkEndGame();
			}
	}
	
	/**
	 * Do end turn.
	 *
	 * @param playerID the player id
	 */
	protected void doEndTurn(int playerID) {
		for(int x = 0; x < grid.width; x++){
			for(int y = 0; y < grid.height; y++){
				for(TerrainTrigger t: grid.getTerrain(x, y).getTriggers()){
					if(t.attempt(this, x, y, getCurrentPlayer()) && !t.start){
						beforeTriggerHook(t, x, y);
						t.endOfTurn(this, x, y);
					}
				}
			}
		}
		
		for(Player p : session.getPlayers()) {
			for(Unit u : p.getParty()) {
				u.setMoved(false);
			}
		}
		turnCount++;
		checkEndGame();
	}
	
	/**
	 * Do start turn.
	 *
	 * @param playerID the player id
	 */
	protected void doStartTurn(int playerID) {
		for(int x = 0; x < grid.width; x++){
			for(int y = 0; y < grid.height; y++){
				for(TerrainTrigger t: grid.getTerrain(x, y).getTriggers()){
					if(t.attempt(this, x, y, getCurrentPlayer()) && t.start){
						beforeTriggerHook(t, x, y);
						t.startOfTurn(this, x, y);
					}
				}
			}
		}
	}
	
	/**
	 * Before trigger hook.
	 *
	 * @param t the t
	 * @param x the x
	 * @param y the y
	 */
	protected void beforeTriggerHook(TerrainTrigger t, int x, int y){
		
	}

	/**
	 * Process commands.
	 *
	 * @param message the message
	 */
	public void processCommands(CommandMessage message) {
		//TODO: command validation
		// After validation, update the unit position
		// Move it instantly since this is the server stage
		final Unit unit = (message.unit == null ? null : getUnit(message.unit));
		
		for(int i=0; i<message.commands.length; i++) {
			try {
				ArrayList<AttackRecord> record = message.commands[i].applyServer(this, unit);
				if (record != null) {
					if (message.attackRecords != null) {
						throw new IllegalStateException("Two attacks in the same move");
					} else {
						message.attackRecords = record;
					}
				}
			} catch (IllegalStateException e) {
				
				throw e;
			}
		}
		if(unit != null) {
			unit.setMoved(true);
		}
		FEServer.getServer().broadcastMessage(message);
		checkEndGame();
	}

	/**
	 * Check end game.
	 */
	public void checkEndGame() {
		// Objective evaluation
		int winner = session.getObjective().evaluate(this);
		if(session.numPlayers()==1){//players have left
			winner = session.getPlayers()[0].getID();//whoever's left wins
		}else if (session.numPlayers()<1){
			FEMultiplayer.disconnectGame("All players have disconnected");
		}
		if(winner > 0 && FEServer.getServer() != null) {
			FEServer.getServer().broadcastMessage(new EndGame((byte) 0, winner));
			FEServer.resetToLobby();
		}
	}

	/**
	 * Gets the unit.
	 *
	 * @param id the id
	 * @return the unit
	 */
	public Unit getUnit(UnitIdentifier id) {
		for(Player p: session.getPlayers()){
			if(!p.isSpectator() && p.getParty().getColor().equals(id.partyColor)){
				return p.getParty().search(id.name);
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see chu.engine.Stage#onStep()
	 */
	@Override
	public void onStep() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see chu.engine.Stage#endStep()
	 */
	@Override
	public void endStep() {
		
	}

	/**
	 * Gets the players.
	 *
	 * @return the players
	 */
	public Player[] getPlayers() {
		return session.getPlayers();
	}
	
	/**
	 * Returns a list of players, filtered to not include players that are spectators
	 */
	public Player[] getNonSpectators() {
		return session.getNonSpectators();
	}
	
	/**
	 * Gets the objective.
	 *
	 * @return the objective
	 */
	public Objective getObjective(){
		return session.getObjective();
	}
	
	/**
	 * Gets the turn count.
	 *
	 * @return the turn count
	 */
	public int getTurnCount(){
		return turnCount;
	}

	/**
	 * Gets the all units.
	 *
	 * @return the all units
	 */
	public List<Unit> getAllUnits() {
		List<Unit> units = new ArrayList<Unit>();
		for(Player p : session.getPlayers()) {
			for(int i=0; i<p.getParty().size(); i++) {
				units.add(p.getParty().getUnit(i));
			}
		}
		return units;
	}

	/**
	 *  Returns a list of players in the turn order, 
	 *  with the first player being the current player.
	 *
	 * @return the turn order
	 */
	public Player[] getTurnOrder() {
		Player[] t = new Player[turnOrder.size()];
		for(int i=0; i<t.length; i++) {
			t[i] = turnOrder.get((currentPlayer+ i) % t.length);
		}
		return t;
	}

}
