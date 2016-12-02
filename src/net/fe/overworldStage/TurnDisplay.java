package net.fe.overworldStage;

import net.fe.FEMultiplayer;
import net.fe.FEResources;
import net.fe.Party;

import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

import chu.engine.Game;
import chu.engine.anim.AudioPlayer;
import chu.engine.anim.Sprite;
import chu.engine.entity.Entity;

// TODO: Auto-generated Javadoc
/**
 * The Class TurnDisplay.
 */
public class TurnDisplay extends Entity {

	/** The text. */
	private Sprite text;

	/** The flash. */
	private Sprite flash;

	/** The xpos. */
	private float xpos;

	/** The slowed. */
	private boolean slowed;

	/** The Constant FLY_IN_SPEED. */
	private static final float FLY_IN_SPEED = 2000.0f;

	/** The Constant SLOW_SPEED. */
	private static final float SLOW_SPEED = 25.0f;

	/**
	 * Instantiates a new turn display.
	 *
	 * @param yourTurn the your turn
	 * @param teamColor the team color
	 */
	public TurnDisplay(boolean yourTurn, Color teamColor) {
		super(0, 0);
		xpos = -512;
		renderDepth = 0.0f;
		Texture t, f;
		text = new Sprite();
		flash = new Sprite();
		boolean spec = false;
		if (yourTurn) {
			t = FEResources.getTexture("player_phase");
		} else {
			if (FEMultiplayer.getLocalPlayer().isSpectator()) {
				if (teamColor == Party.TEAM_BLUE) {
					t = FEResources.getTexture("blue_phase");
				} else {
					t = FEResources.getTexture("red_phase");
				}
			} else {
				t = FEResources.getTexture("enemy_phase");
			}
		}

		if (teamColor == Party.TEAM_BLUE) {
			f = FEResources.getTexture("blue_flash");
		} else {
			f = FEResources.getTexture("red_flash");
		}
		text.addAnimation("default", t);
		flash.addAnimation("default", f);
		AudioPlayer.playAudio("turn_change");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see chu.engine.Entity#render()
	 */
	public void render() {
		if (xpos < -30 || (xpos > 0 && slowed)) {
			xpos += FLY_IN_SPEED * Game.getDeltaSeconds();
		} else {
			if (!slowed)
				xpos = -30;
			slowed = true;
			xpos += SLOW_SPEED * Game.getDeltaSeconds();
		}
		flash.render(-30 - xpos, 100, renderDepth);
		text.render(xpos, 100, renderDepth);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see chu.engine.Entity#endStep()
	 */
	public void endStep() {
		if (xpos > 512) {
			destroy();
		}
	}

}
