package net.fe.fightStage.anim;

import static java.lang.System.out;

import java.util.HashMap;

import net.fe.fightStage.FightStage;
import chu.engine.AnimationData;
import chu.engine.anim.Animation;
import chu.engine.anim.AudioPlayer;

// TODO: Auto-generated Javadoc
/**
 * The Class AttackAnimation.
 */
public abstract class AttackAnimation extends Animation {
	
	/** The hitframes. */
	protected int[] hitframes;
	
	/** The head x. */
	private int headX;
	
	/** The head y. */
	private int headY;
	
	/** The prev frame. */
	private int prevFrame;
	
	/** The stop. */
	private boolean stop;
	
	/** The loop until. */
	protected int loopUntil;
	
	/** The animation args. */
	protected AnimationArgs animationArgs;
	
	/** The stage. */
	protected FightStage stage;
	
	/** The freeze. */
	protected int freeze;
	
	/** The sound map. */
	protected HashMap<Integer, String> soundMap;
	
	/** The Constant NORMAL_SPEED. */
	public static final float NORMAL_SPEED = .055f;
	
	/** The default speed. */
	private float defaultSpeed = NORMAL_SPEED;
	
	/**
	 * Instantiates a new attack animation.
	 *
	 * @param data the data
	 * @param stage the stage
	 * @param animArgs the anim args
	 */
	//TODO You can't have a hit frame on the very last frame
	public AttackAnimation(AnimationData data, FightStage stage, AnimationArgs animArgs) {
		super(data.getTexture(), data.frameWidth, data.frameHeight, data.frames,
                data.columns, data.offsetX, data.offsetY, 0);
		this.hitframes = data.hitframes;
		this.stage = stage;
		this.animationArgs = animArgs;
		this.freeze = data.freeze;
		this.loopUntil = -1;
		this.prevFrame = -1;
		this.soundMap = data.soundMap;
		this.stop = data.stop;
		if(data.speed != 0) {
			defaultSpeed = data.speed;
		}
	}
	
	/* (non-Javadoc)
	 * @see chu.engine.anim.Animation#done()
	 */
	@Override
	public void done() {
		((FightStage)stage).setCurrentEvent(FightStage.DONE);
		setSpeed(0);
		if(stop)
			setFrame(getFrame()-1);
		else
			setFrame(0);
	}
	
	/* (non-Javadoc)
	 * @see chu.engine.anim.Animation#update()
	 */
	@Override
	public void update() {
		prevFrame = getFrame();
		super.update();
		if(prevFrame != getFrame()) {
			for(int j=0; j<hitframes.length; j++) {
				int i = hitframes[j];
				if(getFrame() == i) {
					if(j == hitframes.length - 1)
						onLastHit();
					else
						onHit();
				}
			}
			if(soundMap.get(getFrame()) != null) {
				AudioPlayer.playAudio(soundMap.get(getFrame()));
			}
		}
		if(getFrame() == loopUntil) {
			setFrame(getFrame()-freeze);
		}
	}
	
	/**
	 * Loop next frames.
	 *
	 * @param frames the frames
	 */
	protected void loopNextFrames(int frames){
		if(frames == 0){
			setSpeed(0);
		} else if (frames > 0){
			loopUntil = getFrame() + frames;
		}
	}

	/**
	 * Gets the head x.
	 *
	 * @return the head x
	 */
	public int getHeadX() {
		return headX;
	}
	
	/**
	 * Gets the head y.
	 *
	 * @return the head y
	 */
	public int getHeadY() {
		return headY;
	}
	
	/* (non-Javadoc)
	 * @see chu.engine.anim.Animation#setSpeed(float)
	 */
	@Override
	public void setSpeed(float speed) {
		super.setSpeed(speed);
		loopUntil = -1;
	}
	
	
	/**
	 * Gets the default speed.
	 *
	 * @return the default speed
	 */
	public float getDefaultSpeed() {
		return defaultSpeed;
	}
	
	/**
	 * On hit.
	 */
	public abstract void onHit();
	
	/**
	 * On last hit.
	 */
	public abstract void onLastHit();
	
	/**
	 * Creates the animation.
	 *
	 * @param data the data
	 * @param stage the stage
	 * @param args the args
	 * @return the attack animation
	 */
	public static AttackAnimation createAnimation(AnimationData data, 
			FightStage stage, AnimationArgs args){
		if(args.classification.equals("normal")){
			return new NormalAttack(data, stage, args);
		} else if (args.classification.equals("ranged")){
			return new ProjectileAttack(data, stage, args);
		} else if (args.classification.equals("magic")){
			return new MagicAttack(data, stage, args);
		}
		return null;
	}

	/**
	 * Gets the next hit frame.
	 *
	 * @return the next hit frame
	 */
	public int getNextHitFrame() {
		for(int h : hitframes) {
			if(h >= getFrame()) {
				return h;
			}
		}
		return -1;
	}

}
