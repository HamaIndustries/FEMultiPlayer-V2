package net.fe.overworldStage;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;
import org.newdawn.slick.util.ResourceLoader;

import chu.engine.Entity;
import chu.engine.Game;
import chu.engine.KeyboardEvent;
import chu.engine.anim.AudioPlayer;
import net.fe.FEMultiplayer;
import net.fe.FEResources;
import net.fe.Party;
import net.fe.Player;
import net.fe.RunesBg;
import net.fe.Session;
import net.fe.SoundTrack;
import net.fe.editor.Level;
import net.fe.editor.SpawnPoint;
import net.fe.network.Message;
import net.fe.network.command.AttackCommand;
import net.fe.network.command.Command;
import net.fe.network.command.HealCommand;
import net.fe.network.command.MoveCommand;
import net.fe.network.message.CommandMessage;
import net.fe.network.message.EndTurn;
import net.fe.overworldStage.Zone.Fog;
import net.fe.overworldStage.context.RepositionContext;
import net.fe.overworldStage.context.Idle;
import net.fe.overworldStage.context.WaitForMessages;
import net.fe.transition.OverworldEndTransition;
import net.fe.unit.MapAnimation;
import net.fe.unit.Unit;
import net.fe.unit.UnitIdentifier;

// TODO: Auto-generated Javadoc
/**
 * The Class ClientOverworldStage.
 */
public class ClientOverworldStage extends OverworldStage {
	
	/** The context. */
	private OverworldContext context;
	
	/** The cursor. */
	public final Cursor cursor;
	
	/** The unit info. */
	private final UnitInfo unitInfo;
	
	/** The menu. */
	private Menu<?> menu;
	
	/** If false, then this stage will ignore all key/mouse events */
	private boolean onControl;
	
	/** The repeat timers. */
	private float[] repeatTimers;
	
	/** The selected unit. */
	private Unit selectedUnit;
	
	/** The current cmd string. */
	protected final ArrayList<Command> currentCmdString;
	
	/** The cam x. */
	public int camX;
	
	/** The cam y. */
	public int camY;
	
	/** The cam max x. */
	public final int camMaxX;
	
	/** The cam max y. */
	public final int camMaxY;
	
	/** The number of currently running messages.
	 * Should be used as a semaphore to prevent messages from running in parallel
	 */
	private volatile int runningMessagesCount;
	
	/** Messages that this has recieved but not yet executed */
	private final Queue<Message> pendingMessages;
	
	private FogOption fogOption = FogOption.NONE;
	private Fog fog;
	
	public static final float TILE_DEPTH = 0.95f;
	public static final float ZONE_DEPTH = 0.9f;
	public static final float FOG_DEPTH = 0.91f;
	public static final float PATH_DEPTH = 0.8f;
	public static final float UNIT_DEPTH = 0.6f;
	public static final float UNIT_MAX_DEPTH = 0.5f;
	public static final float CHAT_DEPTH = 0.3f;
	public static final float MENU_DEPTH = 0.1f;
	public static final float CURSOR_DEPTH = 0.15f;
	
	/** The Constant RIGHT_AXIS. */
	public static final int RIGHT_AXIS = 480 - ObjectiveInfo.WIDTH/2 -2;
	
	/**
	 * Instantiates a new client overworld stage.
	 *
	 * @param s the s
	 */
	public ClientOverworldStage(Session s) {
		super(s);
		
		
		fogOption = s.getFogOption();
		fog = new Fog(new HashSet<Node>());
		addEntity(fog);
		
		camX = camY = 0;
		camMaxX = Math.max(0,grid.width*16-368);
		camMaxY = Math.max(0,grid.height*16-240);
		{
			List<Unit> units = FEMultiplayer.getLocalPlayer().getParty().getUnits();
			Node[] n = units.stream()
					.filter((Unit u) -> u.getHp() > 0)
					.map((Unit u) -> new Node(u.getXCoord(), u.getYCoord()))
					.toArray(Node[]::new);
			if (n.length > 0) {
				cursor = new Cursor(n[0].x, n[0].y);
				this.includeInView(n);
			} else {
				cursor = new Cursor(2, 2);
			}
		}
		addEntity(cursor);
		unitInfo = new UnitInfo();
		Color c= new Color(FEMultiplayer.getLocalPlayer().getParty().getColor());
		c.r = Math.max(c.r, 0.5f);
		c.g = Math.max(c.g, 0.5f);
		c.b = Math.max(c.b, 0.5f);
		addEntity(new RunesBg(c));
		addEntity(unitInfo);
		addEntity(new TerrainInfo(cursor));
		addEntity(new OverworldChat(this.session.getChatlog()));
		addEntity(new ObjectiveInfo());
		setControl(true);
		context = new RepositionContext(this, null);
		
		repeatTimers = new float[4];
		currentCmdString = new ArrayList<Command>();
		pendingMessages = new java.util.LinkedList<Message>();
		runningMessagesCount = 0;
	}
	
	/**
	 * Sets the menu.
	 *
	 * @param m the new menu
	 */
	public void setMenu(Menu<?> m){
		removeEntity(menu);
		menu = m;
		if(m!=null){
			addEntity(menu);
		}
	}
	
	/**
	 * Gets the menu.
	 *
	 * @return the menu
	 */
	public Menu<?> getMenu(){
		return menu;
	}
	
	/**
	 * Reset.
	 */
	public void reset(){
		context.cleanUp();
		removeExtraneousEntities();
		new Idle(this, getCurrentPlayer()).startContext();
	}
	
	/**
	 * Removes the extraneous entities.
	 *
	 * @param keep the keep
	 */
	public void removeExtraneousEntities(Entity... keep){
		List<Entity> keeps = Arrays.asList(keep);
		for(Entity e: entities){
			if(!(e instanceof DoNotDestroy ||
					keeps.contains(e))){
				removeEntity(e);
			}
		}
	}
	
	/**
	 * Include in view.
	 *
	 * @param coords the coords
	 */
	public void includeInView(Node... coords) {
		// If all coords are visible in the current view, don't do anything
		boolean allVisible = true;
		for(Node n : coords) {		
			if(n.x < camX/16 || n.x > camX/16+23
					|| n.y < camY/16 || n.y > camY/16+15) 
				allVisible = false;
		}
		if(allVisible) return;
		int maxUp, maxLeft, maxRight, maxDown;
		maxUp = maxLeft = Integer.MAX_VALUE;
		maxRight = maxDown = Integer.MIN_VALUE;
		for(Node n : coords) {
			if(n.x < maxLeft) maxLeft = n.x;
			if(n.x > maxRight) maxRight = n.x;
			if(n.y < maxUp) maxUp = n.y;
			if(n.y > maxDown) maxDown = n.y;
		}
		if(camX/16+23 < maxRight) {
			camX = (maxRight-23)*16;
		} else if(camX/16 > maxLeft) {
			camX = maxLeft*16;
		}
		
		if(camY/16+15 < maxDown) {
			camY = (maxDown-15)*16;
		} else if(camY/16 > maxUp) {
			camY = maxUp*16;
		}
	}
	
	/* (non-Javadoc)
	 * @see chu.engine.Stage#render()
	 */
	public void render(){
		super.render();
	}

	/* (non-Javadoc)
	 * @see net.fe.overworldStage.OverworldStage#beginStep()
	 */
	@Override
	public void beginStep(List<Message> messages) {
		
		//TODO only update if necessary
		updateFog();
		
		messages.forEach(pendingMessages::add);
		while (runningMessagesCount == 0 && pendingMessages.peek() != null) {
			super.executeMessage(pendingMessages.poll());
		}
		
		for (Entity e : entities) {
			e.beginStep();
		}
		MapAnimation.updateAll();
		if (onControl) {
			List<KeyboardEvent> keys = Game.getKeys();
			boolean moveFast = Keyboard.isKeyDown(FEResources.getKeyMapped(Keyboard.KEY_X));
			float timer = context.getCursorSpeed(moveFast);
			if (Keyboard.isKeyDown(FEResources.getKeyMapped(Keyboard.KEY_UP)) && repeatTimers[0] == 0) {
				context.onUp();
				repeatTimers[0] = timer;
			}
			if (Keyboard.isKeyDown(FEResources.getKeyMapped(Keyboard.KEY_DOWN)) && repeatTimers[1] == 0) {
				context.onDown();
				repeatTimers[1] = timer;
			}
			if (Keyboard.isKeyDown(FEResources.getKeyMapped(Keyboard.KEY_LEFT)) && repeatTimers[2] == 0) {
				context.onLeft();
				repeatTimers[2] = timer;
			}
			if (Keyboard.isKeyDown(FEResources.getKeyMapped(Keyboard.KEY_RIGHT)) && repeatTimers[3] == 0) {
				context.onRight();
				repeatTimers[3] = timer;
			}
			for(KeyboardEvent ke : keys) {
				if(ke.state) {
					if(ke.key == FEResources.getKeyMapped(Keyboard.KEY_Z)) 
						context.onSelect();
					else if (ke.key == FEResources.getKeyMapped(Keyboard.KEY_X))
						context.onCancel();
					else if (ke.key == FEResources.getKeyMapped(Keyboard.KEY_C))
						context.onNextUnit();
					else if (ke.key == FEResources.getKeyMapped(Keyboard.KEY_D))
						context.onInspectInventory();
				}
			}
		}
		for(int i=0; i<repeatTimers.length; i++) {
			if(repeatTimers[i] > 0) {
				repeatTimers[i] -= Game.getDeltaSeconds();
				if(repeatTimers[i] < 0) repeatTimers[i] = 0;
			}
		}
		processAddStack();
		processRemoveStack();
	}

	/* (non-Javadoc)
	 * @see net.fe.overworldStage.OverworldStage#onStep()
	 */
	@Override
	public void onStep() {
		for (Entity e : entities) {
			e.onStep();
		}
		processAddStack();
		processRemoveStack();
	}

	/* (non-Javadoc)
	 * @see net.fe.overworldStage.OverworldStage#endStep()
	 */
	@Override
	public void endStep() {
		for (Entity e : entities) {
			e.endStep();
		}
		processAddStack();
		processRemoveStack();
	}

	/**
	 * Sets the context.
	 *
	 * @param c the new context
	 */
	void setContext(OverworldContext c) {
		context.cleanUp();
		context = c;
	}
	
	/**
	 * Sets whether this will respond to keyboard events
	 *
	 * @see #hasControl
	 * @param c true if this should respond to keyboard and mouse events, else false
	 */
	public void setControl(boolean c){
		onControl = c;
	}
	
	/**
	 * Returns whether this responds to keyboard events
	 *
	 * @see #setControl
	 * @return true, if this is responding to keyboard and mouse events, else false
	 */
	public boolean hasControl(){
		return onControl;
	}
	
	/**
	 * End.
	 */
	public void end(){
		if (! currentCmdString.isEmpty()) {
			// It is possible for cmdString to be non empty; if, for example,
			// a unit rearanges its inventory, then cancels back to context.Idle
			// and End Turns
			send();
		}
		EndTurn message = new EndTurn();
		FEMultiplayer.getClient().sendMessage(message);
		selectedUnit = null;
		// do the turn transition stuff now to prevent fast-fingering a second end turn
		super.beginStep(java.util.Collections.singletonList(message));
	}
	
	@Override
	protected void doEndTurn() {
		removeExtraneousEntities();
		super.doEndTurn();
		context.cleanUp();
		// reset assists
		for(Player p : session.getPlayers()) {
			for(Unit u : p.getParty()) {
				u.getAssisters().clear();
			}
		}
		if(FEMultiplayer.getLocalPlayer().getID() != getCurrentPlayer().getID()){
			SoundTrack.loop("overworld");
		} else {
			SoundTrack.loop("enemy");
		}
	}
	
	@Override
	protected void doStartTurn(){
		super.doStartTurn();
		if(FEMultiplayer.getLocalPlayer().getID() == getCurrentPlayer().getID()){
			context = new Idle(this, FEMultiplayer.getLocalPlayer());
			addEntity(new TurnDisplay(true, Party.TEAM_BLUE));
			if (FEResources.getAutoCursor().applyAtStartOfLocalTurn) {
				List<Unit> units = FEMultiplayer.getLocalPlayer().getParty().getUnits();
				Node[] n = units.stream()
						.filter((Unit u) -> u.getHp() > 0 && !u.isRescued() && u.isVisible(this))
						.map((Unit u) -> new Node(u.getXCoord(), u.getYCoord()))
						.toArray(Node[]::new);
				if (n.length > 0) {
					cursor.setXCoord(n[0].x);
					cursor.setYCoord(n[0].y);
					this.setUnitInfoUnit(this.getHoveredUnit());
				}
			}
		} else {
			context = new WaitForMessages(this);
			if(FEMultiplayer.getLocalPlayer().isSpectator())
				addEntity(new TurnDisplay(false, getCurrentPlayer().getParty().getColor()));
			else
				addEntity(new TurnDisplay(false, Party.TEAM_RED));
			
			if (FEResources.getAutoCursor().applyAtStartOfOtherTurn) {
				List<Unit> units = this.getCurrentPlayer().getParty().getUnits();
				Node[] n = units.stream()
						.filter((Unit u) -> u.getHp() > 0 && !u.isRescued() && u.isVisible(this))
						.map((Unit u) -> new Node(u.getXCoord(), u.getYCoord()))
						.toArray(Node[]::new);
				if (n.length > 0) {
					cursor.setXCoord(n[0].x);
					cursor.setYCoord(n[0].y);
					this.setUnitInfoUnit(this.getHoveredUnit());
				}
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see net.fe.overworldStage.OverworldStage#beforeTriggerHook(net.fe.overworldStage.TerrainTrigger, int, int)
	 */
	protected void beforeTriggerHook(TerrainTrigger t, int x, int y){
		addEntity(t.getAnimation(this, x, y));
	}
	
	/**
	 * Clear cmd string.
	 */
	private void clearCmdString(){
		selectedUnit = null;
		currentCmdString.clear();
	}
	
	/**
	 * Queues a command to be sent to the server
	 *
	 * @param cmd the cmd
	 */
	public void addCmd(Command cmd){
		currentCmdString.add(cmd);
	}
	
	/* (non-Javadoc)
	 * @see net.fe.overworldStage.OverworldStage#processCommands(net.fe.network.message.CommandMessage)
	 */
	@Override
	public void processCommands(final CommandMessage message) {
		runningMessagesCount++;
		final boolean execute = (message.origin != FEMultiplayer.getClient().getID());
		
		// Get unit and path
		final Unit unit = (message.unit == null ? null : getUnit(message.unit));
		// Parse commands
		Runnable callback = new Runnable() {
			@Override
			public void run() {
				if(unit != null) unit.setMoved(true);
				checkEndGame();
				runningMessagesCount--;
			}
		};
		for (int i = message.commands.length - 1; i >= 0; i--) {
			Command c = message.commands[i];
			
			// only execute if originated from another server or if the command has info added from the server
			if (execute || c instanceof AttackCommand || c instanceof HealCommand) {
				callback = c.applyClient(ClientOverworldStage.this, unit, message.attackRecords, callback);
			}
		}
		if(execute && unit != null) {
			AudioPlayer.playAudio("select");
			callback.run();
		} else {
			callback.run();
		}
	}
	
	/* (non-Javadoc)
	 * @see net.fe.overworldStage.OverworldStage#checkEndGame()
	 */
	public void checkEndGame() {
		if(FEMultiplayer.getClient().winner != -1) {
			String winnerName = getPlayerByID(FEMultiplayer.getClient().winner).getName();
			FEMultiplayer.getClient().winner = -1;
			addEntity(new OverworldEndTransition(new EndGameStage(session), winnerName));
		}
	}
	
	/**
	 * Sets the selected unit.
	 *
	 * @param u the new selected unit
	 */
	public void setSelectedUnit(Unit u){
		selectedUnit = u;
	}
	
	/**
	 * Send.
	 */
	public void send(){
		UnitIdentifier uid = null;
		if(selectedUnit != null) {
			uid = new UnitIdentifier(selectedUnit);
			currentCmdString.add(0, new MoveCommand(selectedUnit.getMove()));
		}
		FEMultiplayer.send(uid, currentCmdString.toArray(new Command[0]));
		clearCmdString();
	}
	
	/* (non-Javadoc)
	 * @see net.fe.overworldStage.OverworldStage#loadLevel(java.lang.String)
	 */
	@Override
	public void loadLevel(String levelName) {
        try {
            InputStream in = ResourceLoader.getResourceAsStream("levels/"+levelName+".lvl");
            ObjectInputStream ois = new ObjectInputStream(in);
            Level level = (Level) ois.readObject();
            Set<SpawnPoint> spawns = new HashSet<>(level.spawns);
            grid = new Grid(level.width, level.height, Terrain.NONE, new HashSet<>(spawns));
            for(int i=0; i<level.tiles.length; i++) {
            	for(int j=0; j<level.tiles[0].length; j++) {
            		Tile tile = new Tile(j, i, level.tiles[i][j]);
            		grid.setTerrain(j, i, tile.getTerrain());
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
            		addEntity(tile);
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
	
	/**
	 * Gets the hovered unit.
	 *
	 * @return the hovered unit
	 */
	public Unit getHoveredUnit() {
		return getVisibleUnit(cursor.getXCoord(), cursor.getYCoord());
	}
	
	/**
	 * Gets the hovered terrain.
	 *
	 * @return the hovered terrain
	 */
	public Terrain getHoveredTerrain() {
		return grid.getTerrain(cursor.getXCoord(), cursor.getYCoord());
	}

	/**
	 * Gets the selected unit.
	 *
	 * @return the selected unit
	 */
	public Unit getSelectedUnit() {
		return selectedUnit;
	}

	/**
	 * Play sound track.
	 */
	public void playSoundTrack() {
		if(getCurrentPlayer().equals(FEMultiplayer.getLocalPlayer())) {
			SoundTrack.loop("overworld");
		} else {
			SoundTrack.loop("enemy");
		}
	}
	
	/**
	 * Set which unit's details are shown in the unit details pane
	 */
	public void setUnitInfoUnit(Unit u) {
		this.unitInfo.setUnit(u);
	}
	
	private void updateFog() {
		if (fogOption != FogOption.NONE) {
			Set<Node> nodes = Zone.all(grid);
			for(Unit unit : getAllUnits())
				if(FEMultiplayer.getLocalPlayer().getParty().isAlly(unit.getParty()) && unit.getHp() > 0)
					for(int i = 0; i <= unit.getTheClass().sight; i++)
						for(int j = 0; j <= unit.getTheClass().sight - i; j++) {
							nodes.remove(new Node(unit.getOrigX() + i, unit.getOrigY() + j));
							nodes.remove(new Node(unit.getOrigX() + i, unit.getOrigY() - j));
							nodes.remove(new Node(unit.getOrigX() - i, unit.getOrigY() + j));
							nodes.remove(new Node(unit.getOrigX() - i, unit.getOrigY() - j));
						}
			fog.setNodes(nodes);
		}
	}
	
	public Zone getFog() {
		return fog;
	}
	
	public static enum FogOption {
		NONE("Disabled"), GBA("FE 6-12"); //TODO FE5
		
		private String representation;
		
		private FogOption() {
			representation = name();
		}
		private FogOption(String representation) {
			this.representation = representation;
		}
		
		@Override
		public String toString() {
			return representation;
		}
	}

	public OverworldContext getContext() {
		return context;
	}
}
