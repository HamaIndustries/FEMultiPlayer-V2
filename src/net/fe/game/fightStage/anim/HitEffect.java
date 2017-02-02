package net.fe.game.fightStage.anim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import chu.engine.AnimationData;
import chu.engine.anim.Animation;
import chu.engine.anim.AudioPlayer;
import chu.engine.anim.Transform;
import chu.engine.entity.Entity;
import net.fe.FEResources;
import net.fe.game.fightStage.AttackRecord;
import net.fe.game.fightStage.FightStage;
import net.fe.game.unit.Weapon;

public class HitEffect extends Entity {

	private boolean left;

	private float shakeLength;

	private int shakeIntensity;

	public HitEffect(String name, boolean leftAttacking, final boolean crit, boolean loadTxt, boolean primary) {
		super(0, 0);
		left = leftAttacking;
		final AnimationData data = getHitTexture(name, crit);
		if (data.shakeIntensity > 0) {
			shakeIntensity = data.shakeIntensity;
		} else {
			shakeIntensity = 5;
		}

		if (data.shakeFrames > 0) {
			shakeLength = data.shakeFrames;
		} else if (data.hitframes.length > 0) {
			shakeLength = data.frames - data.hitframes[0] + (crit ? 0 : 0.8f);
		} else {
			shakeLength = data.frames + (crit ? 0 : 0.8f);
		}
		final int realHit;
		if (!primary) {
			realHit = -1;
		} else if (data.hitframes.length != 0) {
			realHit = data.hitframes[0];
		} else {
			realHit = 0;
		}
		if (loadTxt) {
			Animation anim = new Animation(FightStage.getPreload(getHitTextureName(name, crit)), data.frameWidth,
			        data.frameHeight, data.frames, data.columns, data.offsetX, data.offsetY,
			        data.speed == 0.0f ? 0.05f : data.speed, data.getBlendMode()) {
				HashMap<Integer, String> soundMap = new HashMap<Integer, String>(data.soundMap);
				int hitframe = realHit;

				public void update() {
					super.update();
					if (soundMap.get(0) != null) {
						AudioPlayer.playAudio(soundMap.remove(0));
					}
					if (soundMap.get(getFrame()) != null) {
						// System.out.println("sound effect: " +
			            // soundMap.get(getFrame()));
						AudioPlayer.playAudio(soundMap.remove(getFrame()));
					}
					if (getFrame() > hitframe && hitframe >= 0) {
						hitframe = -1;
						((FightStage) stage).setCurrentEvent(FightStage.ATTACKED);
					}
				}

				@Override
				public void done() {
					destroy();
				}
			};
			sprite.addAnimation("default", anim);
		}
		renderDepth = FightStage.EFFECT_DEPTH;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see chu.engine.Entity#render()
	 */
	@Override
	public void render() {
		Transform t = new Transform();
		int offset = -((FightStage) stage).distanceToHead();
		if (left) {
			t.flipHorizontal();
			offset *= -1;
		}
		sprite.render(FightStage.CENTRAL_AXIS + offset, FightStage.FLOOR, renderDepth, t);
	}

	public float getShakeLength() {
		return shakeLength;
	}

	public int getShakeIntensity() {
		return shakeIntensity;
	}

	public static String getHitTextureName(String name, boolean crit) {
		String critName = name + "_critical";
		if (FEResources.hasTexture("hit_effect_" + critName) && crit) {
			return "hit_effect_" + critName;
		} else {
			return "hit_effect_" + name;
		}
	}

	public static AnimationData getHitTexture(String name, boolean crit) {
		String critName = name + "_critical";
		if (FEResources.hasTexture("hit_effect_" + critName) && crit) {
			return FEResources.getTextureData("hit_effect_" + critName);
		} else {
			return FEResources.getTextureData("hit_effect_" + name);
		}
	}

	public static List<HitEffect> getEffects(AnimationArgs animArgs, AttackRecord rec, boolean loadTex) {
		boolean crit = rec.animation.contains("Critical");
		boolean primary = true;
		List<HitEffect> effects = new ArrayList<HitEffect>();

		for (String effectName : getEffectNames(animArgs, rec)) {
			effects.add(new HitEffect(effectName, animArgs.left, crit, loadTex, primary));
			primary = false;
		}

		return effects;
	}

	public static List<String> getEffectNames(AnimationArgs animArgs, AttackRecord rec) {
		List<String> effects = new ArrayList<String>();
		if (animArgs.unit.getWeapon() == null)
			return effects;
		if (animArgs.unit.getWeapon().type == Weapon.Type.STAFF) {
			effects.add("heal");
		}

		if (animArgs.unit.getWeapon().isMagic()) {
			effects.add(animArgs.unit.getWeapon().name.replaceAll(" ", "-").toLowerCase());
		}

		if (animArgs.wepAnimName.equals("rangedsword")) {
			effects.add(animArgs.unit.getWeapon().name.replaceAll(" ", "-").toLowerCase());
		}

		for (String anim : FightStage.analyzeAnimation(rec.animation, "(a)", false)) {
			if (!FEResources.hasTexture("hit_effect_" + anim.toLowerCase()) && anim.matches(".*\\d")) {
				anim = anim.substring(0, anim.length() - 1);
			}
			if (FEResources.hasTexture("hit_effect_" + anim.toLowerCase())) {
				effects.add(anim.toLowerCase());
			} else {

			}
		}

		if (effects.size() == 0 && rec.damage != 0) { // We have nothing //
		                                              // nothing.
			effects.add("attack");
		}

		for (String anim : FightStage.analyzeAnimation(rec.animation, "(d)", false)) {
			if (anim.matches(".*\\d"))
				anim = anim.substring(0, anim.length() - 1);
			if (FEResources.hasTexture("hit_effect_" + anim.toLowerCase())) {
				effects.add(anim.toLowerCase());
			}
		}

		return effects;
	}

}
