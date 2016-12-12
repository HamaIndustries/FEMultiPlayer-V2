package net.fe.game.fightStage.anim;

import chu.engine.AnimationData;
import net.fe.game.fightStage.FightStage;

public class MagicAttack extends AttackAnimation {

	public MagicAttack(AnimationData data, FightStage stage, AnimationArgs animArgs) {
		super(data, stage, animArgs);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.fe.fightStage.anim.AttackAnimation#onLastHit()
	 */
	@Override
	public void onLastHit() {
		stage.setDarkness(0.3f);
		stage.addEntity(new MagicEffect(animationArgs));
		stage.addEntity(new BackgroundEffect(getBgEffectName(animationArgs), animationArgs.left));
		loopNextFrames(freeze);
		onHit();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.fe.fightStage.anim.AttackAnimation#onHit()
	 */
	@Override
	public void onHit() {
		// TODO: Magic sounds
	}

	public static String getBgEffectName(AnimationArgs args) {
		return "bg_effect_" + args.unit.getWeapon().name.toLowerCase();
	}
}
