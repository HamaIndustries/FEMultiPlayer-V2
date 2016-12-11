package net.fe.builderStage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;

import chu.engine.Game;
import chu.engine.Stage;
import chu.engine.anim.AudioPlayer;
import chu.engine.anim.Renderer;
import chu.engine.anim.Transform;
import chu.engine.entity.Entity;
import chu.engine.event.KeyboardEvent;
import net.fe.ControlsDisplay;
import net.fe.FEMultiplayer;
import net.fe.FEResources;
import net.fe.Party;
import net.fe.Player;
import net.fe.RunesBg;
import net.fe.game.Session;
import net.fe.game.unit.MapAnimation;
import net.fe.game.unit.Unit;
import net.fe.game.unit.UnitFactory;
import net.fe.game.unit.UnitIcon;
import net.fe.network.Message;
import net.fe.network.message.DraftMessage;
import net.fe.network.message.KickMessage;
import net.fe.network.message.QuitMessage;
import net.fe.ressources.graphics.ui.Button;

// TODO: Auto-generated Javadoc
/**
 * The Class TeamDraftStage.
 */
public class TeamDraftStage extends Stage {

	/** The vassal list. */
	private UnitList vassalList;

	/** The lord list. */
	private UnitList lordList;

	/** The cursor. */
	private Cursor cursor;

	/** The buttons. */
	private Button[] buttons;

	/** The class sort. */
	private Button classSort;

	/** The name sort. */
	private Button nameSort;

	/** The submit. */
	private Button submit;

	/** The controls. */
	private ControlsDisplay controls;

	/** The session. */
	private Session session;

	/** The timers. */
	private List<DraftTimer> timers;

	/** The has control. */
	private boolean hasControl;

	/** The max vassals. */
	private int maxVassals;

	/** The max lords. */
	private int maxLords;

	/** The repeat timers. */
	private float[] repeatTimers = new float[4];

	/** The draft order. */
	private String[] draftOrder;

	/** The draft orders. */
	private static String[][] draftOrders = new String[8][0];

	/** The draft turn. */
	private int draftTurn;

	/** The last action. */
	private String lastAction;

	/** The red gray. */
	private static Color BLUE_TURN = Party.TEAM_BLUE, RED_TURN = Party.TEAM_RED, BAN_COLOR = new Color(0xf57272),
	        PICK_COLOR = new Color(0x36e34b), BLUE_GRAY = new Color(0.3f, 0.3f, 0.3f),
	        RED_GRAY = new Color(0.6f, 0.6f, 0.6f);

	static {
		draftOrders[0] = new String[] { "BL1", "RL1" };
		draftOrders[1] = new String[] { "BL1", "RL1", "BP1", "RP1" };
		draftOrders[2] = new String[] { "BL1", "RL1", "BB1", "RB1", "BP1", "RP1", "BP1", "RP1" };
		draftOrders[3] = new String[] { "BL1", "RL1", "BB1", "RB1", "BP1", "RP2", "BP2", "RP1" };
		draftOrders[4] = new String[] { "BL1", "RL1", "BB1", "RB1", "BP1", "RP2", "BP1", "RB1", "BB1", "RP1", "BP2",
		        "RP1" };
		draftOrders[5] = new String[] { "BL1", "RL1", "BB1", "RB1", "BP1", "RP2", "BP1", "RB1", "BB1", "RP1", "BP2",
		        "RP1", "BP1", "RB1" };
		draftOrders[6] = new String[] { "BL1", "RL1", "BB1", "RB1", "BP1", "RP2", "BP1", "RB1", "BB1", "RP1", "BP2",
		        "RP2", "BP1", "RB1", "BB1", "BP1", "RP1" };
		draftOrders[7] = new String[] { "BL1", "RL1", "BB1", "RB1", "BP1", "RP2", "BP1", "RB1", "BB1", "RP1", "BP2",
		        "RP2", "BP2", "RP1", "BB1", "RB1", "BP1", "RP1" };
	}

	/** The Constant NS_BUTTON_X. */
	// CONFIG
	public static final int UNIT_LIST_X = 78, UNIT_LIST_Y = 100, LORD_LIST_X = 78, LORD_LIST_Y = 40, BUTTON_Y = 260,
	        SB_BUTTON_X = 300, CS_BUTTON_X = 78, NS_BUTTON_X = 188;

	/** The Constant TIME_PER_TURN. */
	public static final float TIME_PER_TURN = 6f;

	/** The Constant BASE_TIME. */
	public static final float BASE_TIME = 15f;

	/**
	 * Instantiates a new team draft stage.
	 *
	 * @param s the s
	 */
	public TeamDraftStage(Session s) {
		super("preparations");
		cursor = new Cursor();
		this.session = s;
		lastAction = "";
		controls = new ControlsDisplay();
		controls.addControl("Z", "Select");
		controls.addControl("Enter", "Done");
		addEntity(controls);
		hasControl = true;

		addEntity(new RunesBg(new Color(0xd2b48c)));
		List<Unit> vassals = UnitFactory.getVassals();
		List<Unit> lords = UnitFactory.getLords();

		lordList = new UnitList(LORD_LIST_X, LORD_LIST_Y, 2, 4);
		lordList.addUnits(lords);
		addEntity(lordList);

		vassalList = new UnitList(UNIT_LIST_X, UNIT_LIST_Y, 5, 4);
		vassalList.addUnits(vassals);
		vassalList.sort(new SortByName());
		addEntity(vassalList);
		classSort = new Button(CS_BUTTON_X, BUTTON_Y, "Sort By Class", Color.blue, 95) {
			public void execute() {
				vassalList.sort(new SortByClass());
				vassalList.refresh();
			}
		};
		nameSort = new Button(NS_BUTTON_X, BUTTON_Y, "Sort By Name", Color.blue, 95) {
			public void execute() {
				vassalList.sort(new SortByName());
				vassalList.refresh();
			}
		};
		submit = new Button(SB_BUTTON_X, BUTTON_Y, "Submit", Color.green, 95) {
			public void execute() {
				List<String> units = new ArrayList<String>();
				for (Unit u : lordList.getSelectedUnits()) {
					units.add(u.name);
				}
				for (Unit u : vassalList.getSelectedUnits()) {
					units.add(u.name);
				}
				if (units.size() < maxLords + maxVassals) {
					return;
				}
				DraftMessage msg = new DraftMessage(units.toArray(new String[units.size()]));
				FEMultiplayer.getClient().sendMessage(msg);
				hasControl = false;
			}
		};

		buttons = new Button[3];
		buttons[1] = nameSort;
		buttons[2] = classSort;
		buttons[0] = submit;
		addEntity(cursor);
		addEntity(classSort);
		addEntity(nameSort);
		addEntity(submit);

		Collections.shuffle(vassals);
		Collections.shuffle(lords);

		// Draft order initialization
		// [Blue/Red] [Lord, Ban, Pick]
		draftOrder = draftOrders[session.getMaxUnits() - 1];
		draftTurn = -1;

		// Timers
		timers = new ArrayList<DraftTimer>();
		for (Player p : session.getNonSpectators()) {
			int x = p.getParty().getColor().equals(Party.TEAM_BLUE) ? 5 : 410;
			float totalTime = Math.round(TIME_PER_TURN * draftOrder.length / 5.0f) * 5 + BASE_TIME;
			DraftTimer dt = new DraftTimer(x, 277, totalTime, Math.round(totalTime / 12), p);
			addEntity(dt);
			timers.add(dt);
		}

		resetDraft();
		refresh();
	}

	/**
	 * Gets the round id.
	 *
	 * @return the round id
	 */
	private String getRoundID() {
		if (draftTurn >= draftOrder.length)
			return "???";
		return draftOrder[draftTurn];
	}

	/**
	 * Reset draft.
	 */
	private void resetDraft() {
		draftTurn++;
		submit.setHover(false);
		if (isMyTurn()) {
			hasControl = true;
			cursor.on = true;
			for (DraftTimer t : timers) {
				if (t.player == FEMultiplayer.getLocalPlayer())
					t.start();
				else
					t.stop();
			}
		} else {
			hasControl = false;
			cursor.on = false;
			for (DraftTimer t : timers) {
				if (t.player == FEMultiplayer.getLocalPlayer())
					t.stop();
				else
					t.start();
			}
		}
		if (cursor.index < 0)
			cursor.index = 0;
		deselectAll();
		if (draftTurn >= draftOrder.length) {
			hasControl = true;
			maxLords = 0;
			maxVassals = 0;
			removeEntity(submit);
			Button go = new Button(SB_BUTTON_X, BUTTON_Y, "Go!", Color.green, 95) {
				@Override
				public void execute() {
					TeamBuilderStage stage = new TeamBuilderStage(false,
			                FEMultiplayer.getLocalPlayer().getParty().getUnits(), session);
					FEMultiplayer.setCurrentStage(stage);
				}
			};
			for (DraftTimer t : timers) {
				t.stop();
				removeEntity(t);
			}
			cursor.index = -1;
			buttons[0] = go;
			addEntity(go);
			checkFlow();
			return;
		}
		String round = getRoundID();
		if (round.charAt(1) == 'L') {
			maxLords = 1;
			maxVassals = 0;
		} else {
			maxLords = 0;
			maxVassals = Integer.parseInt(round.charAt(2) + "");
		}
	}

	/**
	 * Checks if is my turn.
	 *
	 * @return true, if is my turn
	 */
	private boolean isMyTurn() {
		if (draftTurn >= draftOrder.length)
			return false;
		String round = getRoundID();
		Color c = FEMultiplayer.getLocalPlayer().getParty().getColor();
		return c.equals(Party.TEAM_BLUE) == (round.charAt(0) == 'B');
	}

	/**
	 * Gets the unit.
	 *
	 * @param name the name
	 * @return the unit
	 */
	public Unit getUnit(String name) {
		Unit u = lordList.getUnit(name);
		if (u == null)
			u = vassalList.getUnit(name);
		return u;
	}

	/**
	 * Select unit.
	 *
	 * @param u the u
	 */
	public void selectUnit(Unit u) {
		if (u.getUnitClass().name.equals("Lord")) {
			lordList.selectUnit(u);
		} else {
			vassalList.selectUnit(u);
		}
	}

	/**
	 * Deselect all.
	 */
	public void deselectAll() {
		for (Unit u : lordList.getSelectedUnits()) {
			lordList.deSelectUnit(u);
		}
		for (Unit u : vassalList.getSelectedUnits()) {
			vassalList.deSelectUnit(u);
		}
	}

	/**
	 * Gets the selected units.
	 *
	 * @return the selected units
	 */
	public List<Unit> getSelectedUnits() {
		ArrayList<Unit> units = new ArrayList<Unit>();
		units.addAll(lordList.getSelectedUnits());
		units.addAll(vassalList.getSelectedUnits());

		return units;
	}

	/**
	 * Refresh.
	 */
	public void refresh() {
		lordList.refresh();
		vassalList.refresh();
		cursor.index = 0;
		cursor.on = true;
		checkFlow();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see chu.engine.Stage#beginStep()
	 */
	@Override
	public void beginStep(List<Message> messages) {
		for (Entity e : entities) {
			e.beginStep();
		}

		for (Message message : messages) {
			if (message instanceof DraftMessage) {
				DraftMessage dm = (DraftMessage) message;
				Player p = session.getPlayer(dm.origin);
				String round = getRoundID();
				StringBuilder action = new StringBuilder();
				if (message.origin == FEMultiplayer.getLocalPlayer().getID()) {
					action.append("You ");
				} else {
					action.append("The enemy ");
				}
				boolean verbed = false;
				for (String name : dm.unitNames) {
					if (round.charAt(1) == 'L') {
						Unit u = lordList.getUnit(name);
						p.getParty().addUnit(u);
						UnitIcon icon = new UnitIcon(u, round.charAt(0) == 'B' ? 2 : 456, 6 + p.getParty().size() * 24,
						        0.0f);
						addEntity(icon);
						lordList.draft(name);
						action.append("picked " + name + ". ");
					} else if (round.charAt(1) == 'B') {
						vassalList.ban(name);
						if (verbed) {
							action.append(" and " + name + ". ");
						} else {
							action.append("banned " + name);
							verbed = true;
						}
					} else if (round.charAt(1) == 'P') {
						p.getParty().addUnit(vassalList.getUnit(name));
						UnitIcon icon = new UnitIcon(vassalList.getUnit(name), round.charAt(0) == 'B' ? 2 : 456,
						        6 + p.getParty().size() * 24, 0.0f);
						addEntity(icon);
						vassalList.draft(name);
						if (verbed) {
							action.append(" and " + name + ". ");
						} else {
							action.append("picked " + name);
							verbed = true;
						}
					}
				}
				if (action.charAt(action.length() - 2) != '.') {
					action.append(". ");
				}
				lastAction = action.toString();
				resetDraft();
			} else if (message instanceof QuitMessage || message instanceof KickMessage) {
				if (this.session.getNonSpectators().length < 2) {
					// player has left
					FEMultiplayer.setCurrentStage(FEMultiplayer.lobby);
				}
			}
		}

		MapAnimation.updateAll();
		if (hasControl) {
			List<KeyboardEvent> keys = Game.getKeys();
			if (Keyboard.isKeyDown(FEResources.getKeyMapped(Keyboard.KEY_UP)) && repeatTimers[0] == 0) {
				repeatTimers[0] = 0.15f;
				up();
			}
			if (Keyboard.isKeyDown(FEResources.getKeyMapped(Keyboard.KEY_DOWN)) && repeatTimers[1] == 0) {
				repeatTimers[1] = 0.15f;
				down();
			}
			if (Keyboard.isKeyDown(FEResources.getKeyMapped(Keyboard.KEY_LEFT)) && repeatTimers[2] == 0) {
				repeatTimers[2] = 0.15f;
				left();
			}
			if (Keyboard.isKeyDown(FEResources.getKeyMapped(Keyboard.KEY_RIGHT)) && repeatTimers[3] == 0) {
				repeatTimers[3] = 0.15f;
				right();
			}
			for (KeyboardEvent ke : keys) {
				if (ke.state) {
					if (ke.key == FEResources.getKeyMapped(Keyboard.KEY_Z)) {
						cursor.select();
					}
					if (ke.key == FEResources.getKeyMapped(Keyboard.KEY_RETURN)) {
						AudioPlayer.playAudio("select");
						buttons[0].execute();
					}
				}
			}

			for (int i = 0; i < repeatTimers.length; i++) {
				if (repeatTimers[i] > 0) {
					repeatTimers[i] -= Game.getDeltaSeconds();
					if (repeatTimers[i] < 0)
						repeatTimers[i] = 0;
				}
			}
		}
	}

	/**
	 * Up.
	 */
	private void up() {
		if (cursor.index < 0)
			buttons[-cursor.index - 1].setHover(false);
		AudioPlayer.playAudio("cursor");
		if (cursor.on) {
			boolean below = cursor.index >= lordList.size();
			cursor.index -= lordList.unitsPerRow;
			if (cursor.index < -1)
				cursor.index = -1;
			if (cursor.index < lordList.size() && below) {
				cursor.index = lordList.size() - 1;
			}

		} else {
			cursor.index = lordList.size() + vassalList.size() - 1;
			cursor.on = true;
			cursor.instant = true;
		}
		checkFlow();
	}

	/**
	 * Down.
	 */
	private void down() {
		if (cursor.index < 0)
			buttons[-cursor.index - 1].setHover(false);
		AudioPlayer.playAudio("cursor");
		if (cursor.on) {
			boolean above = cursor.index < lordList.size();
			cursor.index += lordList.unitsPerRow;
			if (cursor.index >= lordList.size() && above) {
				cursor.index = lordList.size();
			}

		} else {
			cursor.index = 0;
			cursor.instant = true;
			cursor.on = true;
		}
		checkFlow();
	}

	/**
	 * Left.
	 */
	private void left() {
		AudioPlayer.playAudio("cursor");
		if (cursor.index < 0)
			buttons[-cursor.index - 1].setHover(false);
		cursor.index--;
		checkFlow();
	}

	/**
	 * Right.
	 */
	private void right() {
		AudioPlayer.playAudio("cursor");
		if (cursor.index < 0)
			buttons[-cursor.index - 1].setHover(false);
		cursor.index++;
		checkFlow();
		if (cursor.index == 0) {
			cursor.instant = true;
		}
	}

	/**
	 * Check flow.
	 */
	private void checkFlow() {
		if (cursor.index >= lordList.size() + vassalList.size()) {
			cursor.index = -buttons.length;
		}
		if (-cursor.index > buttons.length) {
			cursor.on = true;
			cursor.instant = true;
			cursor.index = lordList.size() + vassalList.size() - 1;
			vassalList.scrollTo(vassalList.size() - 1);
		}
		if (cursor.index < 0) {
			cursor.on = false;
			buttons[-cursor.index - 1].setHover(true);
		} else {
			cursor.on = true;
			if (cursor.index >= lordList.size()) {
				vassalList.scrollTo(cursor.index - lordList.size());
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see chu.engine.Stage#render()
	 */
	public void render() {
		super.render();
		// Draft turns
		for (int i = 0; i < draftTurn; i++) {
			// greyscale already past turns
			String round = draftOrder[i];
			int x0 = i * 480 / draftOrder.length;
			int x1 = (i + 1) * 480 / draftOrder.length;
			Renderer.drawRectangle(x0, 0, x1, 8, 0.0f, round.charAt(0) == 'B' ? BLUE_GRAY : RED_GRAY);
		}
		for (int i = draftTurn; i < draftOrder.length; i++) {
			String round = draftOrder[i];
			int x0 = i * 480 / draftOrder.length;
			int x1 = (i + 1) * 480 / draftOrder.length;
			Renderer.drawRectangle(x0, 4, x1, 8, 0.0f, round.charAt(0) == 'B' ? BLUE_TURN : RED_TURN);
			Renderer.drawRectangle(x0, 0, x1, 4, 0.0f, round.charAt(1) == 'B' ? BAN_COLOR : PICK_COLOR);
		}
		String round = getRoundID();

		// Top banner
		Renderer.drawRectangle(0, 8, 480, 28, 0.1f, new Color(0, 0, 0, 0.5f));
		StringBuilder s = new StringBuilder();
		s.append(lastAction);
		if (round.equals("???")) {
			s.append("Picking phase is over. Equip your units!");
		} else {
			if (isMyTurn()) {
				s.append("Your turn to ");
			} else {
				s.append("Enemy's turn to ");
			}
			if (round.charAt(1) == 'L') {
				s.append("pick a Lord");
			} else if (round.charAt(1) == 'B') {
				int i = Integer.parseInt(round.charAt(2) + "");
				s.append("ban " + i + " vassal" + (i > 1 ? "s" : ""));
			} else {
				int i = Integer.parseInt(round.charAt(2) + "");
				s.append("pick " + i + " vassal" + (i > 1 ? "s" : ""));
			}
		}
		int width = FEResources.getBitmapFont("default_med").getStringWidth(s.toString());
		Renderer.drawString("default_med", s.toString(), 240 - width / 2, 13, 0);

		// Triangle under current turn
		int cX = (2 * draftTurn + 1) * 240 / draftOrder.length;
		Renderer.drawTriangle(cX - 4, 8, cX, 12, cX + 4, 8, 0.0f, round.charAt(0) == 'B' ? BLUE_TURN : RED_TURN);

		// Player lists
		for (Player p : session.getNonSpectators()) {
			int y = 30;
			for (Unit u : p.getParty()) {
				int nameWidth = FEResources.getBitmapFont("default_med").getStringWidth(u.name);
				int x = p.getParty().getColor().equals(Party.TEAM_BLUE) ? 28 : 452 - nameWidth;
				Renderer.drawString("default_med", u.name, x, y, 0);
				y += 24;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see chu.engine.Stage#onStep()
	 */
	@Override
	public void onStep() {

		for (Entity e : entities) {
			e.onStep();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see chu.engine.Stage#endStep()
	 */
	@Override
	public void endStep() {
		for (Entity e : entities) {
			e.endStep();
		}
	}

	/**
	 * Gets the max units.
	 *
	 * @return the max units
	 */
	public int getMaxUnits() {
		return maxVassals;
	}

	/**
	 * Gets the all units.
	 *
	 * @return the all units
	 */
	public List<Unit> getAllUnits() {
		List<Unit> ans = new ArrayList<Unit>();
		ans.addAll(vassalList.getUnits());
		ans.addAll(lordList.getUnits());
		return ans;
	}

	/**
	 * The Class Cursor.
	 */
	private class Cursor extends Entity {

		/** The index. */
		int index;

		/** The on. */
		boolean on = true;

		/** The instant. */
		boolean instant = false;

		/**
		 * Instantiates a new cursor.
		 */
		public Cursor() {
			super(0, 0);
			renderDepth = 0.5f;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see chu.engine.Entity#onStep()
		 */
		public void onStep() {
			if (!on) {
				return;
			}
			int supposedX, supposedY;
			if (index < lordList.size()) {
				supposedX = LORD_LIST_X + (index % lordList.unitsPerRow) * UnitList.WIDTH;
				supposedY = LORD_LIST_Y + (index / lordList.unitsPerRow) * UnitList.HEIGHT
				        - lordList.getScrollPos() * UnitList.HEIGHT;
			} else {
				int index = this.index - lordList.size();
				supposedX = UNIT_LIST_X + (index % lordList.unitsPerRow) * UnitList.WIDTH;
				supposedY = UNIT_LIST_Y + (index / lordList.unitsPerRow) * UnitList.HEIGHT
				        - vassalList.getScrollPos() * UnitList.HEIGHT;
			}
			if (Math.abs(supposedX - x) > UnitList.WIDTH || Math.abs(supposedY - y) > UnitList.HEIGHT || instant) {
				instant = false;
				y = supposedY;
				x = supposedX;
			} else {
				float dy = supposedY - y;
				y += Math.signum(dy) * Game.getDeltaSeconds() * 300;
				if ((supposedY - y) * dy < 0) {
					y = supposedY;
				}
				float dX = supposedX - x;
				x += Math.signum(dX) * Game.getDeltaSeconds() * 1200;
				if ((supposedX - x) * dX < 0) {
					x = supposedX;
				}
			}
		}

		/**
		 * Select.
		 */
		public void select() {

			if (on) {
				if (index < lordList.size()) {
					if (lordList.isSelected(index)) {
						AudioPlayer.playAudio("select");
						lordList.deSelectUnit(lordList.unitAt(index));
					} else if (lordList.numberSelected() < maxLords) {
						AudioPlayer.playAudio("select");
						lordList.selectUnit(lordList.unitAt(index));
					}
				}
				if (index >= lordList.size()) {
					if (vassalList.isSelected(index - lordList.size())) {
						AudioPlayer.playAudio("select");
						vassalList.deSelectUnit(vassalList.unitAt(index - lordList.size()));
					} else if (vassalList.numberSelected() < maxVassals) {
						AudioPlayer.playAudio("select");
						vassalList.selectUnit(vassalList.unitAt(index - lordList.size()));
					}
				}
			} else {
				AudioPlayer.playAudio("select");
				buttons[-cursor.index - 1].execute();
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see chu.engine.Entity#render()
		 */
		public void render() {
			if (on)
				Renderer.drawRectangle(x + 1, y + 1, x + UnitList.WIDTH - 1, y + UnitList.HEIGHT - 1, renderDepth,
				        new Color(0.7f, 0.7f, 1, 0.4f));
		}

	}

	/**
	 * The Class SortByClass.
	 */
	private class SortByClass implements Comparator<UnitSet> {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		@Override
		public int compare(UnitSet arg0, UnitSet arg1) {
			return arg0.unit.getUnitClass().name.compareTo(arg1.unit.getUnitClass().name);
		}
	}

	/**
	 * The Class SortByName.
	 */
	private class SortByName implements Comparator<UnitSet> {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		@Override
		public int compare(UnitSet arg0, UnitSet arg1) {
			return arg0.unit.name.compareTo(arg1.unit.name);
		}
	}

	/**
	 * The Class DraftTimer.
	 */
	private class DraftTimer extends Entity {

		/** The active. */
		private boolean active;

		/** The timer. */
		private float timer;

		/** The last stop time. */
		private float lastStopTime;

		/** The bonus. */
		private float bonus;

		/** The player. */
		public Player player;

		/**
		 * Instantiates a new draft timer.
		 *
		 * @param x the x
		 * @param y the y
		 * @param initTime the init time
		 * @param bonus the bonus
		 * @param p the p
		 */
		public DraftTimer(float x, float y, float initTime, float bonus, Player p) {
			super(x, y);
			timer = initTime;
			lastStopTime = timer;
			active = false;
			this.bonus = bonus;
			renderDepth = 0.0f;
			this.player = p;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see chu.engine.Entity#beginStep()
		 */
		public void beginStep() {
			if (active)
				timer -= Game.getDeltaSeconds();
			if (timer < 0 && player == FEMultiplayer.getLocalPlayer()) {
				timer += bonus;
				lastStopTime = timer;
				lordList.selectRandom(maxLords);
				vassalList.selectRandom(maxVassals);
				submit.execute();
			}
		}

		/**
		 * Start.
		 */
		public void start() {
			active = true;
			lastStopTime = timer;
		}

		/**
		 * Stop.
		 */
		public void stop() {
			active = false;
			timer = Math.min(timer + bonus, lastStopTime);
			lastStopTime = timer;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see chu.engine.Entity#render()
		 */
		public void render() {
			Transform t = new Transform();
			if (active)
				t.color = new Color(0f, 0.8f, 0f, 1f);
			t.scaleX = 2;
			t.scaleY = 2;
			String time = String.format("%2d:%02d", (int) timer / 60, (int) timer % 60);
			Renderer.drawString("default_med", time, x, y, renderDepth, t);
		}

	}
}
