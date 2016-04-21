package net.fe.fightStage.anim;

import net.fe.FEResources;
import net.fe.fightStage.FightStage;
import chu.engine.Entity;
import chu.engine.anim.Animation;
import chu.engine.anim.AudioPlayer;

// TODO: Auto-generated Javadoc
/**
 * The Class MissEffect.
 */
public class MissEffect extends Entity {

	/** The left. */
	private boolean left;
	
	/**
	 * Instantiates a new miss effect.
	 *
	 * @param left the left
	 */
	public MissEffect(boolean left) {
		super(0, 0);
		AudioPlayer.playAudio("miss");
		Animation anim = new Animation(FEResources.getTexture("miss"), 38, 26, 20, 5, 0, 0, .015f) {
			@Override
			public void increment() {
				super.increment();
				if(getFrame() == 17)
					setSpeed(.08f);
			}
			@Override
			public void done() {
				destroy();
			}
		};
		sprite.addAnimation("default", anim);
		this.left = left;
		
		renderDepth = FightStage.EFFECT_DEPTH;
	}
	
	/* (non-Javadoc)
	 * @see chu.engine.Entity#render()
	 */
	@Override
	public void render() {
		if(left)
			sprite.render(FightStage.CENTRAL_AXIS-60, FightStage.FLOOR-80, 0.0f);
		else
			sprite.render(FightStage.CENTRAL_AXIS+22, FightStage.FLOOR-80, 0.0f);
	}
	
	

}
