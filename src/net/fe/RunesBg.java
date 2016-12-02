package net.fe;

import net.fe.overworldStage.DoNotDestroy;

import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

import chu.engine.Game;
import chu.engine.anim.Renderer;
import chu.engine.anim.Transform;
import chu.engine.entity.Entity;

// TODO: Auto-generated Javadoc
/**
 * The Class RunesBg.
 */
public class RunesBg extends Entity implements DoNotDestroy {

	/** The color. */
	private Color color;

	/** The runes. */
	private Texture runes;

	/** The position. */
	private float position;

	/**
	 * Instantiates a new runes bg.
	 *
	 * @param c the c
	 */
	public RunesBg(Color c) {
		super(0, 0);
		color = c;
		runes = FEResources.getTexture("runes");
		renderDepth = 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see chu.engine.Entity#onStep()
	 */
	public void onStep() {
		super.onStep();
		position += Game.getDeltaSeconds() * 20;
		if (position > 480) {
			position -= 480;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see chu.engine.Entity#render()
	 */
	public void render() {
		Transform t = new Transform();
		t.setColor(color);
		Renderer.render(runes, 0, 0, 2, 1, -position, 0, -position + 480 * 2, 320, 1, t);
	}

}
