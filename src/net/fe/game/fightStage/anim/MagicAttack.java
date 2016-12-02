package net.fe.game.fightStage.anim;

import chu.engine.AnimationData;
import net.fe.game.fightStage.FightStage;

// TODO: Auto-generated Javadoc
/**
 * The Class MagicAttack.
 */
public class MagicAttack extends AttackAnimation{

	/**
	 * Instantiates a new magic attack.
	 *
	 * @param data the data
	 * @param stage the stage
	 * @param animArgs the anim args
	 */
	public MagicAttack(AnimationData data, FightStage stage,
			AnimationArgs animArgs) {
		super(data, stage, animArgs);
	}

	/* (non-Javadoc)
	 * @see net.fe.fightStage.anim.AttackAnimation#onLastHit()
	 */
	@Override
	public void onLastHit() {
		stage.setDarkness(0.3f);
		stage.addEntity(new MagicEffect(animationArgs));
		stage.addEntity(new BackgroundEffect(getBgEffectName(animationArgs), 
				animationArgs.left));
		loopNextFrames(freeze);
		onHit();
		
	}

	/* (non-Javadoc)
	 * @see net.fe.fightStage.anim.AttackAnimation#onHit()
	 */
	@Override
	public void onHit() {
		//TODO: Magic sounds
	}
	
	
	/**
	 * Gets the bg effect name.
	 *
	 * @param args the args
	 * @return the bg effect name
	 */
	public static String getBgEffectName(AnimationArgs args){
		return "bg_effect_" + args.unit.getWeapon().name.toLowerCase();
	}
}
