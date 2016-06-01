package net.fe.overworldStage;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.HashMap;

import net.fe.Command;
import net.fe.FEMultiplayer;
import net.fe.FEResources;
import net.fe.Party;
import net.fe.Player;
import net.fe.RunesBg;
import net.fe.Session;
import net.fe.SoundTrack;
import net.fe.editor.Level;
import net.fe.editor.SpawnPoint;
import net.fe.network.message.CommandMessage;
import net.fe.network.message.EndTurn;
import net.fe.overworldStage.context.Idle;
import net.fe.overworldStage.context.TradeContext;
import net.fe.overworldStage.context.WaitForMessages;
import net.fe.transition.OverworldEndTransition;
import net.fe.unit.Item;
import net.fe.unit.MapAnimation;
import net.fe.unit.RiseTome;
import net.fe.unit.Unit;
import net.fe.unit.UnitIdentifier;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;
import org.newdawn.slick.util.ResourceLoader;

import chu.engine.Entity;
import chu.engine.Game;
import chu.engine.KeyboardEvent;
import chu.engine.anim.AudioPlayer;

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
	UnitInfo unitInfo;
	
	/** The menu. */
	private Menu<?> menu;
	
	/** The on control. */
	private boolean onControl;
	
	/** The repeat timers. */
	private float[] repeatTimers;
	
	/** The mov y. */
	private int movX, movY;
	
	/** The selected unit. */
	private Unit selectedUnit;
	
	/** The current cmd string. */
	protected ArrayList<Object> currentCmdString;
	
	/** The cam x. */
	public int camX;
	
	/** The cam y. */
	public int camY;
	
	/** The cam max x. */
	public int camMaxX;
	
	/** The cam max y. */
	public int camMaxY;
	
	/** The Constant TILE_DEPTH. */
	public static final float TILE_DEPTH = 0.95f;
	
	/** The Constant ZONE_DEPTH. */
	public static final float ZONE_DEPTH = 0.9f;
	
	/** The Constant PATH_DEPTH. */
	public static final float PATH_DEPTH = 0.8f;
	
	/** The Constant UNIT_DEPTH. */
	public static final float UNIT_DEPTH = 0.6f;
	
	/** The Constant UNIT_MAX_DEPTH. */
	public static final float UNIT_MAX_DEPTH = 0.5f;
	
	/** The Constant CHAT_DEPTH. */
	public static final float CHAT_DEPTH = 0.3f;
	
	/** The Constant MENU_DEPTH. */
	public static final float MENU_DEPTH = 0.2f;
	
	/** The Constant CURSOR_DEPTH. */
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
		camX = camY = 0;
		camMaxX = Math.max(0,grid.width*16-368);
		camMaxY = Math.max(0,grid.height*16-240);
		cursor = new Cursor(2, 2);
		addEntity(cursor);
		unitInfo = new UnitInfo();
		Color c= new Color(FEMultiplayer.getLocalPlayer().getParty().getColor());
		c.r = Math.max(c.r, 0.5f);
		c.g = Math.max(c.g, 0.5f);
		c.b = Math.max(c.b, 0.5f);
		addEntity(new RunesBg(c));
		addEntity(unitInfo);
		addEntity(new TerrainInfo(cursor));
		addEntity(new OverworldChat(this.chat));
		addEntity(new ObjectiveInfo());
		setControl(true);
		if(getCurrentPlayer().equals(FEMultiplayer.getLocalPlayer())) {
			context = new Idle(this, getCurrentPlayer());
			addEntity(new TurnDisplay(true, Party.TEAM_BLUE));
			SoundTrack.loop("overworld");
		} else {
			context = new WaitForMessages(this);
			if(FEMultiplayer.getLocalPlayer().isSpectator())
				addEntity(new TurnDisplay(false, getCurrentPlayer().getParty().getColor()));
			else
				addEntity(new TurnDisplay(false, Party.TEAM_RED));
			SoundTrack.loop("enemy");
		}
		repeatTimers = new float[4];
		currentCmdString = new ArrayList<Object>();
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
//		Renderer.scale(2, 2);
		super.render();
	}

	/* (non-Javadoc)
	 * @see net.fe.overworldStage.OverworldStage#beginStep()
	 */
	@Override
	public void beginStep() {
		super.beginStep();
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
	 * Sets the control.
	 *
	 * @param c the new control
	 */
	public void setControl(boolean c){
		onControl = c;
	}
	
	/**
	 * Checks for control.
	 *
	 * @return true, if successful
	 */
	public boolean hasControl(){
		return onControl;
	}
	
	/**
	 * End.
	 */
	public void end(){
		FEMultiplayer.getClient().sendMessage(new EndTurn());
		selectedUnit = null;
	}
	
	/* (non-Javadoc)
	 * @see net.fe.overworldStage.OverworldStage#doEndTurn(int)
	 */
	@Override
	protected void doEndTurn(int playerID) {
		removeExtraneousEntities();
		super.doEndTurn(playerID);
		context.cleanUp();
		// reset assists
		for(Player p : session.getPlayers()) {
			for(Unit u : p.getParty()) {
				u.getAssisters().clear();
			}
		}
		send();
		if(FEMultiplayer.getLocalPlayer().getID() != getCurrentPlayer().getID()){
			SoundTrack.loop("overworld");
		} else {
			SoundTrack.loop("enemy");
		}
	}
	
	/* (non-Javadoc)
	 * @see net.fe.overworldStage.OverworldStage#doStartTurn(int)
	 */
	protected void doStartTurn(int playerID){
		super.doStartTurn(playerID);
		if(FEMultiplayer.getLocalPlayer().getID() == getCurrentPlayer().getID()){
			context = new Idle(this, FEMultiplayer.getLocalPlayer());
			addEntity(new TurnDisplay(true, Party.TEAM_BLUE));
		} else {
			context = new WaitForMessages(this);
			if(FEMultiplayer.getLocalPlayer().isSpectator())
				addEntity(new TurnDisplay(false, getCurrentPlayer().getParty().getColor()));
			else
				addEntity(new TurnDisplay(false, Party.TEAM_RED));
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
		movX = 0;
		movY = 0;
		currentCmdString.clear();
	}
	
	/**
	 * Adds the cmd.
	 *
	 * @param cmd the cmd
	 */
	public void addCmd(Object cmd){
		currentCmdString.add(cmd);
	}
	
	/**
	 * Adds the cmd.
	 *
	 * @param cmd the cmd
	 */
	public void addCmd(Object... cmd){
		for(Object o: cmd)
			currentCmdString.add(o);
	}
	
	/* (non-Javadoc)
	 * @see net.fe.overworldStage.OverworldStage#processCommands(net.fe.network.message.CommandMessage)
	 */
	@Override
	public void processCommands(CommandMessage message) {
		final CommandMessage cmds = (CommandMessage) message;
		boolean execute = true;
		if(cmds.origin == FEMultiplayer.getClient().getID()) {
			execute = false;
		}
		//TODO: command validation
		// Get unit and path
		final Unit unit = (cmds.unit == null ? null : getUnit(cmds.unit));
		// Parse commands
		Command callback = new Command() {
			@Override
			public void execute() {
				if(unit != null) unit.setMoved(true);
				checkEndGame();
			}
		};
		for(int i=0; i<cmds.commands.length; i++) {
			Object obj = cmds.commands[i];
			if(obj.equals("EQUIP") && execute) {
				Unit other = getUnit((UnitIdentifier) cmds.commands[++i]);
				other.equip((Integer) cmds.commands[++i]);
			}
			else if(obj.equals("TRADE")) {
				Unit u1 = getUnit((UnitIdentifier) cmds.commands[++i]);
				int i1 = (Integer)cmds.commands[++i];
				Unit u2 = getUnit((UnitIdentifier) cmds.commands[++i]);
				int i2 = (Integer)cmds.commands[++i];
				//Swap the two items
				if(execute) {
					TradeContext.doTrade(u1.getInventory(), u2.getInventory(), i1, i2);
				}
			}
			else if(obj.equals("USE")) {
				final int oHp = unit.getHp();
				final int index = (Integer)cmds.commands[++i];
				if(execute) {
					callback = new Command() {
						public void execute() {
							unit.use(index);
							unit.setMoved(true);
							checkEndGame();
							//TODO Positioning
							addEntity(new Healthbar(unit, oHp, 
									unit.getHp(), ClientOverworldStage.this){
								@Override
								public void done() {
									destroy();
								}
							});
						}
					};
				}
			}
			else if(obj.equals("RESCUE")) {
				final Unit rescuee = getUnit((UnitIdentifier) cmds.commands[++i]);
				callback = new Command() {
					public void execute() {
						unit.setMoved(true);
						unit.rescue(rescuee);
						checkEndGame();
					}
				};
			}
			else if(obj.equals("TAKE")) {
				if(execute) {
					unit.setMoved(true);
					Unit other = getUnit((UnitIdentifier) cmds.commands[++i]);
					other.give(unit);
					checkEndGame();
				}
			}
			else if(obj.equals("DROP")) {
				final int dropX = (Integer) cmds.commands[++i];
				final int dropY = (Integer) cmds.commands[++i];
				callback = new Command() {
					public void execute() {
						unit.setMoved(true);
						unit.rescuedUnit().setMoved(true);
						unit.drop(dropX, dropY);
						checkEndGame();
					}
				};
			}
			else if(obj.equals("ATTACK") || obj.equals("HEAL")) {
				final UnitIdentifier other = (UnitIdentifier) cmds.commands[++i];
				callback = new Command() {
					public void execute() {
						unit.setMoved(true);
						FEMultiplayer.goToFightStage(cmds.unit, 
								other, cmds.attackRecords);
					}
				};
			}
			else if(obj.equals("SUMMON")) {
				final int dropX = (Integer) cmds.commands[++i];
				final int dropY = (Integer) cmds.commands[++i];
				callback = new Command() {
					public void execute() {
						final Unit summon = net.fe.overworldStage.context.Summon.generateSummon(unit);
						
						int tomeToUse = 0;
						List<Item> items = unit.getInventory();
						for(int i = 0; i < items.size(); i++){
							if (items.get(i) instanceof RiseTome){
								tomeToUse = i;
							}
						}
						
						summon.loadMapSprites();
						ClientOverworldStage.this.addUnit(summon, dropX, dropY);
						unit.setMoved(true);
						unit.use(tomeToUse);
						checkEndGame();
					}
				};
			}
			else if(obj.equals("SHOVE")) {
				final Unit shovee = getUnit((UnitIdentifier) cmds.commands[++i]);
				callback = new Command() {
					public void execute() {
						unit.setMoved(true);
						int deltaX = shovee.getXCoord() - unit.getXCoord();
						int deltaY = shovee.getYCoord() - unit.getYCoord();
						int newX = shovee.getXCoord() + deltaX;
						int newY = shovee.getYCoord() + deltaY;
						
						shovee.setOrigX(newX); // Otherwise, shovee will jump back to it's inital space on select 
						shovee.setOrigY(newY); // Otherwise, shovee will jump back to it's inital space on select
						Path p = new Path();
						p.add(new Node(newX, newY));
						grid.move(shovee, newX, newY, true);
						shovee.move(p, new Command() {
							public void execute() {
								shovee.sprite.setAnimation("IDLE");
								checkEndGame();
							}
						});
					}
				};
			}
			else if(obj.equals("SMITE")) {
				final Unit shovee = getUnit((UnitIdentifier) cmds.commands[++i]);
				callback = new Command() {
					public void execute() {
						unit.setMoved(true);
						int deltaX = shovee.getXCoord() - unit.getXCoord();
						int deltaY = shovee.getYCoord() - unit.getYCoord();
						int newX = shovee.getXCoord() + 2 * deltaX;
						int newY = shovee.getYCoord() + 2 * deltaY;
						
						shovee.setOrigX(newX); // Otherwise, shovee will jump back to it's inital space on select
						shovee.setOrigY(newY); // Otherwise, shovee will jump back to it's inital space on select
						Path p = new Path();
						p.add(new Node(newX, newY));
						grid.move(shovee, newX, newY, true);
						shovee.move(p, new Command() {
							public void execute() {
								shovee.sprite.setAnimation("IDLE");
								checkEndGame();
							}
						});
					}
				};
			}
		}
		if(execute && unit != null) {
			AudioPlayer.playAudio("select");
			Path p = grid.getShortestPath(unit, unit.getXCoord()+cmds.moveX, unit.getYCoord()+cmds.moveY);
			grid.move(unit, unit.getXCoord()+cmds.moveX, unit.getYCoord()+cmds.moveY, true);
			unit.move(p, callback);
			includeInView(p.getAllNodes());
		} else {
			callback.execute();
		}
	}
	
	/**
	 * Sets the mov x.
	 *
	 * @param x the new mov x
	 */
	public void setMovX(int x){
		movX = x;
	}
	
	/**
	 * Sets the mov y.
	 *
	 * @param y the new mov y
	 */
	public void setMovY(int y){
		movY = y;
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
		if(selectedUnit != null)
			uid = new UnitIdentifier(selectedUnit);
		FEMultiplayer.send(uid, movX, movY, currentCmdString.toArray());
		clearCmdString();
	}
	
	/* (non-Javadoc)
	 * @see net.fe.overworldStage.OverworldStage#loadLevel(java.lang.String)
	 */
	public void loadLevel(String levelName) {
        try {
            InputStream in = ResourceLoader.getResourceAsStream("levels/"+levelName+".lvl");
            ObjectInputStream ois = new ObjectInputStream(in);
            Level level = (Level) ois.readObject();
            grid = new Grid(level.width, level.height, Terrain.NONE);
            Set<SpawnPoint> spawns = level.spawns;
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
		return getUnit(cursor.getXCoord(), cursor.getYCoord());
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
}
