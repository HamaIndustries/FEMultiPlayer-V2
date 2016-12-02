package net.fe.game.fightStage.anim;

import net.fe.FEResources;
import net.fe.game.fightStage.FightStage;
import chu.engine.AnimationData;
import chu.engine.anim.Animation;
import chu.engine.anim.Transform;
import chu.engine.entity.Entity;

// TODO: Auto-generated Javadoc
/**
 * The Class BackgroundEffect.
 */
public class BackgroundEffect extends Entity {
	
	/** The left. */
	private boolean left;
	
	/**
	 * Instantiates a new background effect.
	 *
	 * @param name the name
	 * @param left the left
	 */
	public BackgroundEffect(String name, boolean left){
		super(0,0);
		renderDepth = FightStage.BG_DEPTH;
		AnimationData data = FEResources.getTextureData(name);
		this.left = left;
		sprite.addAnimation("default", new Animation(FightStage.getPreload(name), data.frameWidth,
				data.frameHeight, data.frames, data.columns, data.offsetX,
				data.offsetY, data.speed==0.0f?0.05f:data.speed, data.getBlendMode()) {
				
			@Override
			public void done() {
				destroy();
			}
		});
	}
	
	/* (non-Javadoc)
	 * @see chu.engine.Entity#render()
	 */
	public void render(){
		Transform t = new Transform();
		if(left){
			t.flipHorizontal();
			t.translateX = 240;
		}
		if(sprite.getCurrentAnimation().getHeight()==1)
			t.setScale(240, 160);
		sprite.render(FightStage.CENTRAL_AXIS - 120,
				FightStage.FLOOR - 104, 0, t);
	}
}
