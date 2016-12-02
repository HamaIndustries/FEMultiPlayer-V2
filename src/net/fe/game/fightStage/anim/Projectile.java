package net.fe.game.fightStage.anim;

import net.fe.FEResources;
import net.fe.game.fightStage.FightStage;
import chu.engine.Game;
import chu.engine.anim.Transform;
import chu.engine.entity.Entity;

// TODO: Auto-generated Javadoc
/**
 * The Class Projectile.
 */
public class Projectile extends Entity {

	/** The destination. */
	private float destination;

	/** The dir. */
	private int dir;

	/** The destroy on hit. */
	private boolean destroyOnHit;

	/** The hit. */
	private boolean hit;

	/** The name. */
	private String name;

	/**
	 * Instantiates a new projectile.
	 *
	 * @param name the name
	 * @param y the y
	 * @param f the f
	 * @param left the left
	 * @param destroyOnHit the destroy on hit
	 * @param args the args
	 */
	public Projectile(String name, float y, FightStage f, boolean left, boolean destroyOnHit, AnimationArgs args) {
		super(0, y);
		// TODO getTextures
		if (args.wepAnimName.equals("javelin"))
			sprite.addAnimation("default", FEResources.getTexture("proj_javelin"));
		else
			sprite.addAnimation("default", FEResources.getTexture("proj_arrow"));
		if (left) {
			x = FightStage.CENTRAL_AXIS - f.distanceToHead();
			destination = FightStage.CENTRAL_AXIS + f.distanceToHead();
			dir = 1;
		} else {
			destination = FightStage.CENTRAL_AXIS - f.distanceToHead();
			x = FightStage.CENTRAL_AXIS + f.distanceToHead();
			dir = -1;
		}
		this.destroyOnHit = destroyOnHit;
		this.name = name;
		renderDepth = FightStage.EFFECT_DEPTH;
		hit = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see chu.engine.Entity#onStep()
	 */
	public void onStep() {
		x += dir * getVelocity(name) * 40 * Game.getDeltaSeconds();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see chu.engine.Entity#render()
	 */
	public void render() {
		Transform t = new Transform();
		if (dir == 1)
			t.flipHorizontal();
		sprite.render(x, y, renderDepth, t);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see chu.engine.Entity#endStep()
	 */
	public void endStep() {
		if (dir * x > dir * destination && !hit) {
			getStage().setCurrentEvent(FightStage.ATTACKED);
			hit = true;
			if (destroyOnHit) {
				destroy();
			}
		}
		// Destroy if out of bounds
		if (x > 500 || x < -500)
			destroy();
	}

	/**
	 * Gets the stage.
	 *
	 * @return the stage
	 */
	public FightStage getStage() {
		return (FightStage) stage;
	}

	/**
	 * Gets the velocity.
	 *
	 * @param name the name
	 * @return the velocity
	 */
	public static int getVelocity(String name) {
		// TODO
		return 30;
	}
}
