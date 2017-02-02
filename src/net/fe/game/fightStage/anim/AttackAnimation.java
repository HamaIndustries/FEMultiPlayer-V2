package net.fe.game.fightStage.anim;

import java.util.HashMap;

import chu.engine.AnimationData;
import chu.engine.anim.Animation;
import chu.engine.anim.AudioPlayer;
import net.fe.game.fightStage.FightStage;

public abstract class AttackAnimation extends Animation {

	protected int[] hitframes;

	private int headX;

	private int headY;

	private int prevFrame;

	private boolean stop;

	protected int loopUntil;

	protected AnimationArgs animationArgs;

	protected FightStage stage;

	protected int freeze;

	protected HashMap<Integer, String> soundMap;

	public static final float NORMAL_SPEED = .055f;

	private float defaultSpeed = NORMAL_SPEED;

	// TODO You can't have a hit frame on the very last frame
	public AttackAnimation(AnimationData data, FightStage stage, AnimationArgs animArgs) {
		super(data.getTexture(), data.frameWidth, data.frameHeight, data.frames, data.columns, data.offsetX,
		        data.offsetY, 0, data.getBlendMode());
		this.hitframes = data.hitframes;
		this.stage = stage;
		this.animationArgs = animArgs;
		this.freeze = data.freeze;
		this.loopUntil = -1;
		this.prevFrame = -1;
		this.soundMap = data.soundMap;
		this.stop = data.stop;
		if (data.speed != 0) {
			defaultSpeed = data.speed;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see chu.engine.anim.Animation#done()
	 */
	@Override
	public void done() {
		((FightStage) stage).setCurrentEvent(FightStage.DONE);
		setSpeed(0);
		if (stop)
			setFrame(getFrame() - 1);
		else
			setFrame(0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see chu.engine.anim.Animation#update()
	 */
	@Override
	public void update() {
		prevFrame = getFrame();
		super.update();
		if (prevFrame != getFrame()) {
			for (int j = 0; j < hitframes.length; j++) {
				int i = hitframes[j];
				if (getFrame() == i) {
					if (j == hitframes.length - 1)
						onLastHit();
					else
						onHit();
				}
			}
			if (soundMap.get(getFrame()) != null) {
				AudioPlayer.playAudio(soundMap.get(getFrame()));
			}
		}
		if (getFrame() == loopUntil) {
			setFrame(getFrame() - freeze);
		}
	}

	protected void loopNextFrames(int frames) {
		if (frames == 0) {
			setSpeed(0);
		} else if (frames > 0) {
			loopUntil = getFrame() + frames;
		}
	}

	public int getHeadX() {
		return headX;
	}

	public int getHeadY() {
		return headY;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see chu.engine.anim.Animation#setSpeed(float)
	 */
	@Override
	public void setSpeed(float speed) {
		super.setSpeed(speed);
		loopUntil = -1;
	}

	public float getDefaultSpeed() {
		return defaultSpeed;
	}

	public abstract void onHit();

	public abstract void onLastHit();

	public static AttackAnimation createAnimation(AnimationData data, FightStage stage, AnimationArgs args) {
		if (args.classification.equals("normal")) {
			return new NormalAttack(data, stage, args);
		} else if (args.classification.equals("ranged")) {
			return new ProjectileAttack(data, stage, args);
		} else if (args.classification.equals("magic")) {
			return new MagicAttack(data, stage, args);
		}
		return null;
	}

	public int getNextHitFrame() {
		for (int h : hitframes) {
			if (h >= getFrame()) {
				return h;
			}
		}
		return -1;
	}

}
