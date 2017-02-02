package net.fe.ressources.graphics.ui;

import java.util.ArrayList;

import chu.engine.anim.Sprite;
import chu.engine.entity.Entity;
import net.fe.FEResources;

// TODO: Auto-generated Javadoc
/**
 * The Class RunesBg.
 */
public class TitleAnimation extends Entity {

	private ArrayList<Sprite> sprites;

	private Sprite titleAnim;

	/**
	 * Instantiates a new title animation.
	 *
	 * @param c the c
	 */
	public TitleAnimation() {
		super(0, 0);
		sprites = new ArrayList<Sprite>();
		titleAnim = new Sprite();
		titleAnim.addAnimation("title_slide", FEResources.getTexture("title_slide"), 480, 320, 21, 3, 0.1f);
		titleAnim.setSpeed(.07f);
		sprites.add(titleAnim);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see chu.engine.Entity#onStep()
	 */
	public void onStep() {
		for (Sprite s : sprites) {
			if (s.updateRunOnce()) {
				hasRun = true;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see chu.engine.Entity#render()
	 */
	public void render() {
		if (!hasRun)
			titleAnim.render(0, 0, .01f);
	}

	public void setAnimation(String animation) {
		sprite.setAnimation("title_slide");
	}

}
