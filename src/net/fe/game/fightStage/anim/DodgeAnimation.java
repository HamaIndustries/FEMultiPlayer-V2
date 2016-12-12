package net.fe.game.fightStage.anim;

import chu.engine.AnimationData;
import chu.engine.Game;
import chu.engine.anim.Animation;

public class DodgeAnimation extends Animation {

	public static final float DODGE_DURATION = 0.25f;

	public static final float NORMAL_SPEED = .06f;

	private float defaultSpeed = NORMAL_SPEED;

	private float dodgeTimer;

	private boolean dodging;

	public DodgeAnimation(AnimationData data) {
		super(data.getTexture(), data.frameWidth, data.frameHeight, data.frames, data.columns, data.offsetX,
		        data.offsetY, DodgeAnimation.NORMAL_SPEED, data.getBlendMode());
		dodgeTimer = 0;
		if (data.speed != 0) {
			defaultSpeed = data.speed;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see chu.engine.anim.Animation#update()
	 */
	@Override
	public void update() {
		super.update();
		if (dodging) {
			dodgeTimer += Game.getDeltaSeconds();
			if (dodgeTimer > DODGE_DURATION) {
				dodging = false;
				setSpeed(-DodgeAnimation.NORMAL_SPEED);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see chu.engine.anim.Animation#done()
	 */
	@Override
	public void done() {
		if (speed > 0) {
			dodging = true;
			setSpeed(0);
			setFrame(getLength() - 1);
		} else if (speed < 0) {
			getSprite().setAnimation("ATTACK");
			getSprite().setSpeed(0);
			dodging = false;
			dodgeTimer = 0;
		}
	}

	public float getDefaultSpeed() {
		return defaultSpeed;
	}

}
