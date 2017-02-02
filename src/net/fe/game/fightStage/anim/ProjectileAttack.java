package net.fe.game.fightStage.anim;

import chu.engine.AnimationData;
import net.fe.game.fightStage.FightStage;

public class ProjectileAttack extends AttackAnimation {

	private boolean ididit;

	public ProjectileAttack(AnimationData data, FightStage stage, AnimationArgs animArgs) {
		super(data, stage, animArgs);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.fe.fightStage.anim.AttackAnimation#update()
	 */
	public void update() {
		super.update();
		if (getFrame() >= hitframes[0] && !ididit) {
			ididit = true;
			((FightStage) stage).moveCamera(animationArgs.left);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.fe.fightStage.anim.AttackAnimation#done()
	 */
	public void done() {
		try {
			super.done();
		} catch (IllegalArgumentException e) {
			// Drats! We'll try again next frame.
			setFrame(getLength() - 1);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.fe.fightStage.anim.AttackAnimation#onLastHit()
	 */
	@Override
	public void onLastHit() {
		stage.setCurrentEvent(FightStage.HIT_EFFECT);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.fe.fightStage.anim.AttackAnimation#onHit()
	 */
	@Override
	public void onHit() {

	}

}
