package net.fe.fightStage.anim;

import java.util.HashMap;
import net.fe.FEResources;
import net.fe.fightStage.FightStage;
import chu.engine.Entity;
import chu.engine.AnimationData;
import chu.engine.anim.Animation;
import chu.engine.anim.AudioPlayer;
import chu.engine.anim.Transform;

/**
 * An Attack Animation for handaxe-type weapons
 */
public final class HandaxeAttack extends AttackAnimation{
	public static final String TEXTURE_NAME = "handaxe_effect";
	
	/**
	 * Instantiates a new handaxe attack.
	 *
	 * @param data the data
	 * @param stage the stage
	 * @param animArgs the anim args
	 */
	public HandaxeAttack(AnimationData data, FightStage stage,
			AnimationArgs animArgs) {
		super(data, stage, animArgs);
	}

	@Override
	public void onLastHit() {
		stage.addEntity(new HandaxeEffect(animationArgs));
		loopNextFrames(freeze);
		onHit();
	}

	@Override
	public void onHit() {
	}
	
	
	public static final class HandaxeEffect extends Entity {
		private final boolean left;
		private final int range;
		
		/**
		 * Instantiates a new handaxe effect.
		 */
		public HandaxeEffect(final AnimationArgs args) {
			super(0, 0);
			this.left = args.left;
			this.range = args.range;
			
			final AnimationData data = FEResources.getTextureData(TEXTURE_NAME);
			Animation anim = new Animation(FightStage.getPreload(TEXTURE_NAME), data.frameWidth,
					data.frameHeight, data.frames, data.columns, data.offsetX,
					data.offsetY, data.speed==0.0f?0.05f:data.speed, data.getBlendMode()) {
				HashMap<Integer, String> soundMap = data.soundMap;
				int prevFrame = -1;
				public void update() {
					super.update();
					if(soundMap.get(getFrame()) != null && prevFrame != getFrame()) {
						AudioPlayer.playAudio(soundMap.get(getFrame()));
					}
					if (getFrame() == data.hitframes[data.hitframes.length - 1] && prevFrame != getFrame()) {
						((FightStage) stage).setCurrentEvent(FightStage.HIT_EFFECT);
						((FightStage) stage).moveCamera(! HandaxeEffect.this.left);
					}
					prevFrame = getFrame();
				}
				@Override
				public void done() {
					setFrame(0);
					setSpeed(0);
					destroy();
				}
			};
			sprite.addAnimation("default", anim);
			renderDepth = FightStage.EFFECT_DEPTH;
		}
		
		public void render(){
			Transform t = new Transform();
			int offset = FightStage.rangeToHeadDistance(this.range);
			if (this.left) {
				t.flipHorizontal();
				offset *=-1;
			}
			sprite.render(FightStage.CENTRAL_AXIS + offset,
					FightStage.FLOOR, 0, t);
		}
	}
}
