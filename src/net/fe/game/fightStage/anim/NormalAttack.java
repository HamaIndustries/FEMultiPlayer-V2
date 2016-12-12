package net.fe.game.fightStage.anim;

import chu.engine.AnimationData;
import chu.engine.anim.AudioPlayer;
import net.fe.game.fightStage.FightStage;

public class NormalAttack extends AttackAnimation {

	public NormalAttack(AnimationData data, FightStage stage, AnimationArgs args) {
		super(data, stage, args);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.fe.fightStage.anim.AttackAnimation#onLastHit()
	 */
	@Override
	public void onLastHit() {
		stage.setCurrentEvent(FightStage.HIT_EFFECT);
		loopNextFrames(freeze);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.fe.fightStage.anim.AttackAnimation#onHit()
	 */
	@Override
	public void onHit() {
		AudioPlayer.playAudio("hit_normal");
	}

}
