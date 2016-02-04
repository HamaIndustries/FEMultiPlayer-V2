package net.fe.overworldStage;

import net.fe.FEMultiplayer;
import net.fe.FEResources;
import net.fe.Player;
import net.fe.RunesBg;
import net.fe.Session;
import net.fe.fightStage.FightStage;
import net.fe.unit.MapAnimation;
import net.fe.unit.Unit;
import net.fe.unit.UnitIcon;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;

import chu.engine.Entity;
import chu.engine.Game;
import chu.engine.KeyboardEvent;
import chu.engine.Stage;
import chu.engine.anim.Renderer;

// TODO: Auto-generated Javadoc
/**
 * The Class EndGameStage.
 */
public class EndGameStage extends Stage {
	
	/** The session. */
	private Session session;
	
	/** The Constant X0. */
	private static final int X0 = 5;
	
	/** The Constant Y0. */
	private static final int Y0 = 100;
	
	/** The Constant X_SPACING. */
	private static final int X_SPACING = 235;
	
	/** The Constant Y_SPACING. */
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
		int renderedPlayers = -1;
		for(int x=0; x<session.numPlayers(); x++) {
			Player p = session.getPlayers()[x];
			if (p.getTeam() != Player.TEAM_SPECTATOR) {
				renderedPlayers++;
				for(int i=0; i<p.getParty().size(); i++) {
					addEntity(new UnitIcon(p.getParty().getUnit(i), X0+renderedPlayers*X_SPACING, Y0+i*Y_SPACING, 0.5f));
				}
			}
		}
		processAddStack();
	}

	/* (non-Javadoc)
	 * @see chu.engine.Stage#beginStep()
	 */
	@Override
	public void beginStep() {
		for(Entity e : entities) {
			e.beginStep();
		}
		for(KeyboardEvent key : Game.getKeys()) {
			if(key.state && key.key == FEResources.getKeyMapped(Keyboard.KEY_RETURN)) {
				for(Player p : session.getPlayers()) {
					p.ready = false;
				}
				FEMultiplayer.setCurrentStage(FEMultiplayer.lobby);
			}
		}
	}

	/* (non-Javadoc)
	 * @see chu.engine.Stage#onStep()
	 */
	@Override
	public void onStep() {
		MapAnimation.updateAll();
		for(Entity e : entities) {
			e.onStep();
		}
	}

	/* (non-Javadoc)
	 * @see chu.engine.Stage#endStep()
	 */
	@Override
	public void endStep() {
		for(Entity e : entities) {
			e.endStep();
		}
	}
	
	/* (non-Javadoc)
	 * @see chu.engine.Stage#render()
	 */
	public void render() {
		super.render();
		Renderer.drawString("default_med", "Press Enter to return to lobby...", 200, 5, 0.5f);
		String[] stats = {"Kills", "Assists", "Damage", "Healing"};
		int renderedPlayers = -1;
		for(int i=0; i<session.numPlayers(); i++) {
			Player p = session.getPlayers()[i];
			if (p.getTeam() != Player.TEAM_SPECTATOR) {
				renderedPlayers++;
				Renderer.drawBorderedRectangle(X0+X_SPACING*renderedPlayers, Y0-3, X0+X_SPACING*(renderedPlayers+1), Y0+Y_SPACING*p.getParty().size(), 0.9f, 
						FightStage.NEUTRAL, FightStage.BORDER_LIGHT, FightStage.BORDER_DARK);
				Renderer.drawBorderedRectangle(X0+X_SPACING*renderedPlayers, Y0-28, X0+X_SPACING*(renderedPlayers+1), Y0-8, 0.9f, 
						FightStage.NEUTRAL, FightStage.BORDER_LIGHT, FightStage.BORDER_DARK);
				for(int k=0; k<stats.length; k++) {
					Renderer.drawString("default_med", stats[k], X0+70+40*k+X_SPACING*renderedPlayers, Y0-25, 0.5f);
				}
				Renderer.drawString("default_med", "Name", X0+20+X_SPACING*renderedPlayers, Y0-25, 0.5f);
				for(int j=0; j<p.getParty().size(); j++) {
					Unit u = p.getParty().getUnit(j);
					Renderer.drawString("default_med", u.name, X0+20+X_SPACING*renderedPlayers, Y0+j*Y_SPACING, 0.5f);
					for(int k=0; k<stats.length; k++) {
						Renderer.drawString("default_med", u.getBattleStat(stats[k]), X0+75+40*k+X_SPACING*renderedPlayers, Y0+j*Y_SPACING, 0.5f);
					}
				}
			}
		}
	}

}
