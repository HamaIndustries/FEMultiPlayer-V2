package net.fe.builderStage;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.fe.*;
import net.fe.fightStage.FightStage;
import net.fe.lobbystage.ClientLobbyStage;
import net.fe.modifier.Modifier;
import net.fe.network.message.PartyMessage;
import net.fe.network.message.QuitMessage;
import net.fe.network.message.KickMessage;
import net.fe.network.Message;
import net.fe.unit.Item;
import net.fe.unit.MapAnimation;
import net.fe.unit.Unit;
import net.fe.unit.UnitIcon;
import net.fe.unit.Weapon;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;

import chu.engine.Entity;
import chu.engine.Game;
import chu.engine.KeyboardEvent;
import chu.engine.Stage;
import chu.engine.anim.AudioPlayer;
import chu.engine.anim.Renderer;

// TODO: Auto-generated Javadoc
/**
 * The Class TeamBuilderStage.
 */
public class TeamBuilderStage extends Stage {
	
	/** The units. */
	private List<Unit> units;
	
	/** The cursor. */
	private Cursor cursor;
	
	/** The repeat timers. */
	private final float[] repeatTimers;
	
	/** The funds. */
	private int funds;
	
	/** The exp. */
	private int exp;
	
	/** The select. */
	private final TeamSelectionStage select;
	
	/** The buttons. */
	private final List<Button> buttons;
	
	/** The curr button. */
	private int currButton;
	
	/** The session. */
	private Session session;
	
	/** The control. */
	private boolean control = true;
	
	/** The controls. */
	private final ControlsDisplay controls;
	
	/** The can edit units. */
	private final boolean canEditUnits;
	
	/** The hgap. */
	//CONFIG
	private final static int name = 30, clazz = 100, lv = 170, hgap = 30; //xvals
	
	/** The table_ystart. */
	private final static int yStart = 40, vgap = 20, table_ystart = 10;
	
	/** The default maximum funds */
	public final static int FUNDS = 48000;
	
	/** The default maximum EXP */
	public final static int EXP = 84000;
	
	
	
	/**
	 * Instantiates a new team builder stage.
	 *
	 * @param toMainMenu the to main menu
	 * @param presetUnits the preset units
	 * @param s the s
	 */
	public TeamBuilderStage(boolean toMainMenu, List<Unit> presetUnits, Session s) {
		super("preparations");
		repeatTimers = new float[4];
		addEntity(new RunesBg(new Color(0xd2b48c)));
		session = s;
		canEditUnits = (presetUnits == null);
		
		Button end;
		
		controls = new ControlsDisplay();
		controls.addControl("Z", "Items/Train");
		controls.addControl("X", "Back");
		if(toMainMenu){
			controls.addControl("Enter", "Main Menu");
		} else {
			controls.addControl("Enter", "Fight!");
		}
		addEntity(controls);
		
		if(!toMainMenu) {
			end = new Button(390, 270, "Fight!", Color.green, 80){
				@Override
				public void execute() {
					control = false;
					addEntity(new ConfirmationMessage(){
						@Override
						public void confirm() {
							// Send the server a PartyMessage
							FEMultiplayer.setCurrentStage(new ClientWaitStage(session));
							PartyMessage pm = new PartyMessage(units);
							FEMultiplayer.getClient().sendMessage(pm);
						}
						public void cancel(){
							control = true;
							refresh();
						}
					});
				}
			};
		} else {
			end = new Button(390, 270, "Exit", Color.red, 80){
				@Override
				public void execute() {
					control = false;
					addEntity(new ConfirmationMessage(){
						@Override
						public void confirm() {
							FEMultiplayer.setCurrentStage(FEMultiplayer.connect);
						}
						public void cancel(){
							control = true;
							refresh();
						}
					});
				}
			};
		}
		addEntity(end);
		
		if(canEditUnits) {
			select = new TeamSelectionStage(this, s);
			units = new ArrayList<Unit>();
			setUnits(select.getSelectedUnits());
			
			Button save = new Button(220, 270, "Save", Color.blue, 80){
				@Override
				public void execute() {
					new TeamNameInput(true).setStage(TeamBuilderStage.this);
				}
				
			};
			addEntity(save);
			
			Button load = new Button(305, 270, "Load", Color.blue, 80){
				@Override
				public void execute() {
					new TeamNameInput(false).setStage(TeamBuilderStage.this);
				}
			};
			addEntity(load);
			
			Button back = new Button(10,270, "Back to Unit Selection", Color.red, 120){
				public void execute() {
					AudioPlayer.playAudio("cancel");
					select.refresh();
					FEMultiplayer.setCurrentStage(select);
				}
			};
			addEntity(back);
			
			buttons = Arrays.asList( end, back, save, load );
		} else {
			select = null;
			units = presetUnits;
			buttons = Arrays.asList( end );
		}
		
		TeamBuilderResources res = new TeamBuilderResources(FUNDS, EXP);
		if(session != null) {
			for(Modifier m : session.getModifiers()) {
				res = m.modifyTeamResources(res);
			}
		}
		this.setFunds(res.funds);
		this.setExp(res.exp);
		
		cursor = new Cursor(9, yStart-4, 462, vgap, units.size());
		cursor.on = true;
		addEntity(cursor);
		
		int y = yStart;
		float d = 0.1f;
		for(Unit u: units){
			addEntity(new UnitIcon(u, 10, y-2, d));
			y+=vgap;
			d-=0.001f;
		}
	}
	
	
	
	/**
	 * Sets the units.
	 *
	 * @param units the new units
	 */
	public void setUnits(List<Unit> units){
		this.units.removeAll(units);
		for(Unit u: this.units){
			funds += u.squeezeGold();
			exp += u.squeezeExp();
		}
		this.units = units;
		for(Entity e: entities){
			if(e instanceof UnitIcon) e.destroy();
		}
		int y = yStart;
		float d = 0.1f;
		for(Unit u: units){
			addEntity(new UnitIcon(u, 10, y-2, d));
			y+=vgap;
			d-=0.001f;
		}
	}
	
	/* (non-Javadoc)
	 * @see chu.engine.Stage#render()
	 */
	@Override
	public void render() {
		
		List<String> stats = Arrays.asList(
			"Lvl", "HP", "Str", "Mag", "Skl", "Spd", "Lck", "Def", "Res", "Mov"
		);
		
		Renderer.drawBorderedRectangle(9, table_ystart-2, 471, table_ystart+14, 0.9f, 
				FightStage.NEUTRAL, FightStage.BORDER_LIGHT, FightStage.BORDER_DARK);
		
		Renderer.drawString("default_med", "Name", name, table_ystart, 0.5f);
		Renderer.drawString("default_med", "Class", clazz, table_ystart, 0.5f);
		int x = lv;
		for(String s: stats){
			Renderer.drawString("default_med", s, x, table_ystart, 0.5f);
			x+= hgap;
		}
		
		Renderer.drawBorderedRectangle(
				9, yStart-6, 471, yStart + vgap * units.size()-2, 0.9f, 
				FightStage.NEUTRAL, FightStage.BORDER_LIGHT, FightStage.BORDER_DARK);
		
		int y = yStart;
		for(Unit u: units){
			Renderer.drawString("default_med", u.name, name, y, 0.5f);
			Renderer.drawString("default_med", u.getTheClass().name, clazz, y, 0.5f);
			x = lv;
			for(String s: stats){
				Renderer.drawString("default_med", u.getBase(s), x, y, 0.5f);
				x+= hgap;
			}
			y+=vgap;
		}
		super.render();
	}

	/* (non-Javadoc)
	 * @see chu.engine.Stage#beginStep()
	 */
	@Override
	public void beginStep(List<Message> messages) {
		boolean capture = control;
		for (Entity e : entities) {
			e.beginStep();
		}
		this.checkForQuits(messages);
		processAddStack();
		processRemoveStack();
		MapAnimation.updateAll();
		if(capture){
			List<KeyboardEvent> keys = Game.getKeys();
			if (Keyboard.isKeyDown(FEResources.getKeyMapped(Keyboard.KEY_UP)) && repeatTimers[0] == 0) {
				repeatTimers[0] = 0.15f;
				if(control)
				if(!cursor.on){
					buttons.get(currButton).setHover(false);
					cursor.on = true;
					cursor.index = cursor.max - 1;
					cursor.instant = true;
					controls.set("Z", "Items/Train");
				}else if(cursor.index == 0){
					cursor.on = false;
					controls.set("Z", "Select");
					buttons.get(currButton).setHover(true);
				} else {
					cursor.up();
				}
				AudioPlayer.playAudio("cursor2");
			}
			if (Keyboard.isKeyDown(FEResources.getKeyMapped(Keyboard.KEY_DOWN)) && repeatTimers[1] == 0) {
				repeatTimers[1] = 0.15f;
				if(!cursor.on){
					buttons.get(currButton).setHover(false);
					cursor.on = true;
					cursor.index = 0;
					cursor.instant = true;
					controls.set("Z", "Items/Train");
				}else if(cursor.index == cursor.max -1){
					cursor.on = false;
					controls.set("Z", "Select");
					buttons.get(currButton).setHover(true);
				} else {
					cursor.down();
				}
				AudioPlayer.playAudio("cursor2");
			}
			if (Keyboard.isKeyDown(FEResources.getKeyMapped(Keyboard.KEY_LEFT)) && repeatTimers[2] == 0) {
				repeatTimers[2] = 0.15f;
				if(!cursor.on){
					buttons.get(currButton).setHover(false);
					currButton--;
					if(currButton < 0) currButton += buttons.size();
					buttons.get(currButton).setHover(true);
					AudioPlayer.playAudio("cursor2");
				}
			}
			if (Keyboard.isKeyDown(FEResources.getKeyMapped(Keyboard.KEY_RIGHT)) && repeatTimers[3] == 0) {
				repeatTimers[3] = 0.15f;
				if(!cursor.on){
					buttons.get(currButton).setHover(false);
					currButton++;
					if (currButton >= buttons.size()) {currButton = 0;}
					buttons.get(currButton).setHover(true);
					AudioPlayer.playAudio("cursor2");
				}
			}
			for(KeyboardEvent ke : keys) {
				if(ke.state) {
					if(ke.key == FEResources.getKeyMapped(Keyboard.KEY_Z)) {
						AudioPlayer.playAudio("select");
						if(cursor.on){
							FEMultiplayer.setCurrentStage(new UnitBuilderStage(units.get(cursor.getIndex()), this, session));
						} else {
							buttons.get(currButton).setHover(false);
							buttons.get(currButton).execute();
						}
						
					} else if (ke.key == FEResources.getKeyMapped(Keyboard.KEY_X)){
						if(canEditUnits) {
							AudioPlayer.playAudio("cancel");
							select.refresh();
							FEMultiplayer.setCurrentStage(select);
						}
					} else if (ke.key == FEResources.getKeyMapped(Keyboard.KEY_RETURN)){
						AudioPlayer.playAudio("select");
						buttons.get(0).execute();
					}
						
				}
			}
		
			for(int i=0; i<repeatTimers.length; i++) {
				if(repeatTimers[i] > 0) {
					repeatTimers[i] -= Game.getDeltaSeconds();
					if(repeatTimers[i] < 0) repeatTimers[i] = 0;
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see chu.engine.Stage#onStep()
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
	 * @see chu.engine.Stage#endStep()
	 */
	@Override
	public void endStep() {
		for (Entity e : entities) {
			e.onStep();
		}
		processAddStack();
		processRemoveStack();
	}

	/**
	 * Gets the funds.
	 *
	 * @return the funds
	 */
	public int getFunds() {
		return funds;
	}

	/**
	 * Sets the funds.
	 *
	 * @param funds the new funds
	 */
	public void setFunds(int funds) {
		this.funds = funds;
	}

	/**
	 * Gets the exp.
	 *
	 * @return the exp
	 */
	public int getExp() {
		return exp;
	}

	/**
	 * Sets the exp.
	 *
	 * @param exp the new exp
	 */
	public void setExp(int exp) {
		this.exp = exp;
	}

	/**
	 * Refresh.
	 */
	public void refresh() {
		cursor.destroy();
		cursor = new Cursor(9, yStart-4, 462, vgap, units.size());
		cursor.on = true;
		for(Button b: buttons){
			b.setHover(false);
		}
		addEntity(cursor);
	}
	
	/**
	 * Save team.
	 *
	 * @param teamName the team name
	 * @return true, if successful
	 */
	public boolean saveTeam(String teamName){
		String[][] teamData = new String[units.size()][6];
		for(int i = 0; i < units.size(); i++){
			Unit u = units.get(i);
			teamData[i][0] = u.name;
			teamData[i][1] = u.getLevel() + "";
			for(int j = 0; j < u.getInventory().size(); j++){
				teamData[i][2+j] = u.getInventory().get(j).name;
			}
		}
		refresh();
		try {
			ObjectOutputStream out = new ObjectOutputStream(
					new FileOutputStream(TeamNameInput.convertPath("teams/" + teamName)));
			out.writeObject(teamData);
			out.close();
			controls.setTempMessage("Team [" + teamName + "] saved.", 3.2f);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			controls.setTempMessage("Could not save team [" + teamName + "].", 3.2f);
			return false;
		}
		
		
	}
	
	/**
	 * Load team.
	 *
	 * @param teamName the team name
	 * @return true, if successful
	 */
	public boolean loadTeam(String teamName){
		String[][] teamData;
		try{
			ObjectInputStream in = new ObjectInputStream(
					new FileInputStream(TeamNameInput.convertPath("teams/" + teamName)));
			teamData = (String[][]) in.readObject();
			in.close();
		} catch (IOException e){
			e.printStackTrace();
			refresh();
			controls.setTempMessage("Could not find team [" + teamName + "].", 3.2f);
			return false;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			refresh();
			return false;
		}
		select.deselectAll();
		setUnits(new ArrayList<Unit>());
		boolean cashout = false, expout = false;
		for(int i = 0; i < select.getMaxUnits() && i < teamData.length; i++){
			Unit u = select.getUnit(teamData[i][0]);
			int lv = Integer.parseInt(teamData[i][1]);
			while(u.getLevel() != lv){
				int expCost = Unit.getExpCost(u.getLevel() + 1);
				if(expCost <= exp){
					exp -= expCost;
					u.setLevel(u.getLevel()+1);
				} else {
					expout = true;
					break;
				}
			}
			for(int j = 2; j < 6; j++){
				String itemName = teamData[i][j];
				if(itemName != null){
					Item item = Item.getItem(itemName);
					if(item instanceof Weapon && ((Weapon)item).pref != null) {
						continue;
					}
					int goldCost = item.getCost();
					if(goldCost <= funds){
						funds -= goldCost;
						u.addToInventory(item);
					} else {
						cashout=true;
						break;
					}
				} 
			}
			if(cashout && expout){
				controls.setTempMessage("Team [" + teamName + "] loaded. Ran out of gold and exp.", 3.2f);
			} else if (cashout){
				controls.setTempMessage("Team [" + teamName + "] loaded. Ran out of gold.", 3.2f);
			} else if (expout) {
				controls.setTempMessage("Team [" + teamName + "] loaded. Ran out of exp.", 3.2f);
			} else {
				controls.setTempMessage("Team [" + teamName + "] loaded.", 3.2f);
			}
			select.selectUnit(u);
		}
		setUnits(select.getSelectedUnits());
		refresh();
		
		return true;
	}

	/**
	 * Checks for control.
	 *
	 * @return true, if successful
	 */
	public boolean hasControl() {
		return control;
	}

	/**
	 * Sets the control.
	 *
	 * @param control the new control
	 */
	public void setControl(boolean control) {
		this.control = control;
	}



	/**
	 * Checks to see whether there are still enough players for the game to continue
	 * If not, resets this client to the lobby.
	 */
	void checkForQuits(List<Message> messages) {
		for (Message m : messages) {
			if (m instanceof QuitMessage || m instanceof KickMessage) {
				if (this.session.getNonSpectators().length < 2) {
					// player has left
					FEMultiplayer.setCurrentStage(new ClientLobbyStage(session));
				}
			}
		}
	}
}

class Cursor extends Entity{
	int index;
	private int width;
	private int initialY;
	private int height;
	int max;
	boolean instant;
	boolean on;
	public Cursor(int x, int y, int width, int height, int max) {
		super(x, y);
		this.width = width;
		this.height = height;
		this.initialY = y;
		renderDepth = 0.6f;
		this.max = max;
	}
	
	public void onStep(){
		int supposedY = initialY + index*height;
		if(instant){
			y = supposedY;
			instant = false;
		} else {
			float dy = supposedY - y;
			y+= Math.signum(dy) * Game.getDeltaSeconds() * 300;
			if((supposedY - y) * dy < 0){
				y = supposedY;
			}
		}
	}
	
	public void render(){
		if(on){
			if(max == 0)
				Renderer.drawString("default_med", "Press X to select units", 200, 154, renderDepth);
			else
				Renderer.drawRectangle(x, y, x+width, y+height, renderDepth, new Color(128,128,213,128));
		}
	}
	
	public void up(){
		if(max == 0) return;
		index--;
		if(index<0){
			index+= max;
			instant = true;
		}
	}
	
	public void down(){
		if(max == 0) return;
		index++;
		if(index >= max){
			index -= max;
			instant = true;
		}
	}
	
	public int getIndex(){
		return index;
	}
	
	public void setIndex(int i){
		index = i;
	}
	
}