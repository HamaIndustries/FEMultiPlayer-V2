package net.fe.fightStage.anim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.fe.FEResources;
import net.fe.fightStage.AttackRecord;
import net.fe.fightStage.FightStage;
import net.fe.unit.Unit;
import net.fe.unit.Weapon;
import chu.engine.Entity;
import chu.engine.AnimationData;
import chu.engine.anim.Animation;
import chu.engine.anim.AudioPlayer;
import chu.engine.anim.Transform;

/**
 * An effect that plays between the defender taking damage and the
 * attacker returning to its' standing position
 */
public final class ReturnEffect extends Entity {
	private final boolean left;
	
	public ReturnEffect(String textureName, boolean leftAttacking) {
		super(0,0);
		
		AnimationData data = FEResources.getTextureData(textureName);
		this.left = leftAttacking;
		
		Animation anim = new Animation(FightStage.getPreload(textureName), data.frameWidth,
				data.frameHeight, data.frames, data.columns, data.offsetX,
				data.offsetY, data.speed==0.0f?0.05f:data.speed, data.getBlendMode()) {
			HashMap<Integer, String> soundMap = new HashMap<Integer, String>(data.soundMap);
			int prevFrame = -1;
			public void update() {
				super.update();
				if(soundMap.get(getFrame()) != null && prevFrame != getFrame()) {
					AudioPlayer.playAudio(soundMap.remove(getFrame()));
				}
				prevFrame = getFrame();
			}
			@Override
			public void done() {
				((FightStage) stage).setCurrentEvent(FightStage.RETURNING);
				destroy();
			}
		};
		sprite.addAnimation("default", anim);
		
		renderDepth = FightStage.EFFECT_DEPTH;
	}
	
	@Override
	public void render() {
		Transform t = new Transform();
		int offset = -((FightStage) stage).distanceToHead();
		if (left) {
			t.flipHorizontal();
			offset *= -1;
		}
		sprite.render(FightStage.CENTRAL_AXIS + offset,
				FightStage.FLOOR, renderDepth, t);
	}
	
	/**
	 * Returns a list of effects that should be applied to a battle with the given arguments
	 */
	public static List<ReturnEffect> getEffects(AnimationArgs animArgs) {
		return ReturnEffect.getEffectNames(animArgs).stream()
			.map(name -> new ReturnEffect(name, animArgs.left))
			.collect(java.util.stream.Collectors.toList());
	}
	
	private static List<String> getEffectNames(AnimationArgs animArgs){
		List<String> effects = new ArrayList<String>();
		
		Unit u = animArgs.unit;
		Weapon w = u.getWeapon();
		if (w == null) return effects;
		if (w.type == Weapon.Type.AXE && w.range.apply(u.getStats()).contains(2)) {
			effects.add("handaxe_return");
		}
		return effects;
	}
}
