package net.fe.game.fightStage.anim;

import chu.engine.Game;
import chu.engine.anim.Transform;
import chu.engine.entity.Entity;
import net.fe.FEResources;
import net.fe.game.fightStage.FightStage;

public class Projectile extends Entity {

	private float destination;

	private int dir;

	private boolean destroyOnHit;

	private boolean hit;

	private String name;

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

	public FightStage getStage() {
		return (FightStage) stage;
	}

	public static int getVelocity(String name) {
		// TODO
		return 30;
	}
}
