package net.fe.overworldStage;

import java.util.List;

import net.fe.FEMultiplayer;
import net.fe.FEResources;
import net.fe.Player;
import net.fe.RunesBg;
import net.fe.game.Session;
import net.fe.game.fightStage.FightStage;
import net.fe.game.unit.MapAnimation;
import net.fe.game.unit.Unit;
import net.fe.game.unit.UnitIcon;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;

import chu.engine.Game;
import chu.engine.Stage;
import chu.engine.anim.Renderer;
import chu.engine.entity.Entity;
import chu.engine.event.KeyboardEvent;
import net.fe.network.Message;

// TODO: Auto-generated Javadoc
/**
 * The Class EndGameStage.
 */
public class EndGameStage extends Stage {

	/** The session. */
	private final Session session;

	/**
	 * The players to render. Cached so that if a player does stuff in the
	 * lobby, then it isn't reflected here.
	 */
	private final Player[] toRender;

	/** x-coordinate of left-most display box */
	private static final int X0 = 5;

	/** y-coordinate of left-most display box */
	private static final int Y0 = 100;

	/** x-coordinate delta between display boxes */
	private static final int X_SPACING = 235;

	/** y-coordinate delta between units */
	private static final int Y_SPACING = 24;

	/**
	 * Instantiates a new end game stage.
	 *
	 * @param session the session
	 */
	public EndGameStage(Session session) {
		super("end");
		this.session = session;
		addEntity(new RunesBg(new Color(0xd2b48c)));
		toRender = session.getNonSpectators();
		for (int x = 0; x < toRender.length; x++) {
			Player p = toRender[x];
			for (int i = 0; i < p.getParty().size(); i++) {
				addEntity(new UnitIcon(p.getParty().getUnit(i), X0 + x * X_SPACING, Y0 + i * Y_SPACING, 0.5f));
			}
		}
		processAddStack();
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
		for (KeyboardEvent key : Game.getKeys()) {
			if (key.state && key.key == FEResources.getKeyMapped(Keyboard.KEY_RETURN)) {
				for (Player p : session.getPlayers()) {
					p.ready = false;
				}
				FEMultiplayer.setCurrentStage(FEMultiplayer.lobby);
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
		MapAnimation.updateAll();
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see chu.engine.Stage#render()
	 */
	public void render() {
		super.render();
		Renderer.drawString("default_med", "Press Enter to return to lobby...", 200, 5, 0.5f);
		String[] stats = { "Kills", "Assists", "Damage", "Healing" };
		for (int i = 0; i < toRender.length; i++) {
			Player p = toRender[i];
			Renderer.drawBorderedRectangle(X0 + X_SPACING * i, Y0 - 3, X0 + X_SPACING * (i + 1),
			        Y0 + Y_SPACING * p.getParty().size(), 0.9f, FightStage.NEUTRAL, FightStage.BORDER_LIGHT,
			        FightStage.BORDER_DARK);
			Renderer.drawBorderedRectangle(X0 + X_SPACING * i, Y0 - 28, X0 + X_SPACING * (i + 1), Y0 - 8, 0.9f,
			        FightStage.NEUTRAL, FightStage.BORDER_LIGHT, FightStage.BORDER_DARK);
			for (int k = 0; k < stats.length; k++) {
				Renderer.drawString("default_med", stats[k], X0 + 70 + 40 * k + X_SPACING * i, Y0 - 25, 0.5f);
			}
			Renderer.drawString("default_med", "Name", X0 + 20 + X_SPACING * i, Y0 - 25, 0.5f);
			for (int j = 0; j < p.getParty().size(); j++) {
				Unit u = p.getParty().getUnit(j);
				Renderer.drawString("default_med", u.name, X0 + 20 + X_SPACING * i, Y0 + j * Y_SPACING, 0.5f);
				for (int k = 0; k < stats.length; k++) {
					Renderer.drawString("default_med", u.getBattleStat(stats[k]), X0 + 75 + 40 * k + X_SPACING * i,
					        Y0 + j * Y_SPACING, 0.5f);
				}
			}
		}
	}

}
