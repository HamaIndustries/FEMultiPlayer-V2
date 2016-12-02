package net.fe.game.fightStage;

import net.fe.FEResources;
import net.fe.PaletteSwapper;
import net.fe.game.fightStage.anim.AnimationArgs;
import net.fe.game.fightStage.anim.AttackAnimation;
import net.fe.game.fightStage.anim.DodgeAnimation;
import net.fe.game.unit.Unit;

import static java.lang.System.out;

import org.newdawn.slick.Color;

import chu.engine.Game;
import chu.engine.anim.ShaderArgs;
import chu.engine.anim.Transform;
import chu.engine.entity.Entity;

// TODO: Auto-generated Javadoc
/**
 * The Class FightUnit.
 */
public class FightUnit extends Entity {

	/** The left. */
	private boolean left;

	/** The distance from center. */
	private int distanceFromCenter;

	/** The state. */
	public int state;

	/** The alpha. */
	private float alpha;

	/** The anim args. */
	private AnimationArgs animArgs;

	/** The flash timer. */
	private float flashTimer;

	/** The flashes. */
	private int flashes;

	/** The Constant ALIVE. */
	public static final int ALIVE = 0;

	/** The Constant FLASHING. */
	public static final int FLASHING = 1;

	/** The Constant FADING. */
	public static final int FADING = 2;

	/**
	 * Instantiates a new fight unit.
	 *
	 * @param animArgs the anim args
	 * @param s the s
	 */
	public FightUnit(AnimationArgs animArgs, FightStage s) {
		super(0, 0);
		this.left = animArgs.left;
		distanceFromCenter = FightStage.rangeToHeadDistance(animArgs.range);
		state = ALIVE;
		alpha = 1;
		flashTimer = 0;
		flashes = 0;
		this.animArgs = animArgs;
		StringBuilder filename = new StringBuilder();
		filename.append(animArgs.userclass);
		filename.append("_");
		filename.append(animArgs.wepAnimName);
		filename.append("_");

		String base = filename.toString().toLowerCase();

		for (String anim : animArgs.unit.getAttackAnims()) {
			if (FEResources.hasTexture(base + anim)) {
				AttackAnimation a = AttackAnimation.createAnimation(FEResources.getTextureData(base + anim), s,
				        animArgs);
				sprite.addAnimation(anim.toUpperCase(), a);
			}
		}

		DodgeAnimation dodge = new DodgeAnimation(FEResources.getTextureData(base + "dodge"));
		sprite.addAnimation("DODGE", dodge);
		sprite.setAnimation("ATTACK");

		renderDepth = FightStage.UNIT_DEPTH;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see chu.engine.Entity#beginStep()
	 */
	@Override
	public void beginStep() {
		if (state == FLASHING) {
			flashTimer += Game.getDeltaSeconds();
			if (flashTimer > 0.05f) {
				alpha = 1 - alpha;
				flashTimer -= 0.05f;
				flashes++;
				if (flashes >= 12)
					state = FADING;
			}
		} else if (state == FADING && alpha > 0) {
			alpha -= .75 * Game.getDeltaSeconds();
			if (alpha < 0) {
				((FightStage) stage).setCurrentEvent(FightStage.RETURNING);
				alpha = 0;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see chu.engine.Entity#render()
	 */
	@Override
	public void render() {
		Transform t = new Transform();
		if (state != ALIVE) {
			t.setColor(new Color(255, 255, 255, alpha));
		}
		ShaderArgs args = PaletteSwapper.setup(this);
		if (left) {
			t.flipHorizontal();
			sprite.render(FightStage.CENTRAL_AXIS - distanceFromCenter, FightStage.FLOOR, renderDepth, t, args);
		} else {
			sprite.render(FightStage.CENTRAL_AXIS + distanceFromCenter, FightStage.FLOOR, renderDepth, t, args);
		}
	}

	/**
	 * Gets the anim args.
	 *
	 * @return the anim args
	 */
	public AnimationArgs getAnimArgs() {
		return animArgs;
	}

	/**
	 * Sets the animation.
	 *
	 * @param animation the new animation
	 */
	public void setAnimation(String animation) {
		for (String s : FightStage.analyzeAnimation(animation, "(a)", false)) {
			s = s.toUpperCase();
			if (sprite.hasAnimation(s)) {
				sprite.setAnimation(s);
				return;
			}
		}
		if (animation.contains("Critical")) {
			sprite.setAnimation("CRITICAL");
		} else {
			sprite.setAnimation("ATTACK");
		}
	}

	/**
	 * Gets the unit.
	 *
	 * @return the unit
	 */
	public Unit getUnit() {
		return animArgs.unit;
	}

}
