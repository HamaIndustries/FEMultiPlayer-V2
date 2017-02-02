package net.fe.overworldStage;

import org.newdawn.slick.Color;

import chu.engine.Game;
import chu.engine.anim.ShaderArgs;
import chu.engine.anim.Transform;
import chu.engine.entity.Entity;
import net.fe.game.unit.Unit;

// TODO: Auto-generated Javadoc
/**
 * The Class Corpse.
 */
public final class Corpse extends Entity {

	/** The alpha. */
	private transient float alpha;

	/**
	 * Instantiates a new corpse.
	 *
	 * @param u the u
	 */
	public Corpse(Unit u) {
		super(u.x, u.y);
		sprite.addAnimation("DYING", u.sprite.getAnimation("IDLE"));
		alpha = 1.0f;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see chu.engine.Entity#beginStep()
	 */
	public void beginStep() {
		alpha -= Game.getDeltaSeconds();
		if (alpha < 0) {
			((ClientOverworldStage) stage).setControl(true);
			destroy();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see chu.engine.Entity#render()
	 */
	public void render() {
		Transform t = new Transform();
		t.color = new Color(1f, 1f, 1f, alpha);
		sprite.render(x - ((ClientOverworldStage) stage).camX, y - ((ClientOverworldStage) stage).camY, renderDepth, t,
		        new ShaderArgs("greyscale"));
	}

}
