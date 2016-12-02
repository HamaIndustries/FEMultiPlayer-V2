package net.fe;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_STENCIL_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearDepth;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;

import chu.engine.anim.Renderer;

// TODO: Auto-generated Javadoc
/**
 * The Class LoadStage.
 */
public class LoadStage {

	/** The percent. */
	private static float percent;

	/** The max. */
	private static int max;

	/**
	 * Sets the maximum.
	 *
	 * @param max the new maximum
	 */
	public static void setMaximum(int max) {
		LoadStage.max = max;
	}

	/**
	 * Update.
	 *
	 * @param number the number
	 */
	public static void update(int number) {
		percent = (number + 0f) / max;
	}

	/**
	 * Render.
	 */
	public static void render() {
		int width = (int) (percent * 436);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
		glClearDepth(1.0f);
		Renderer.drawString("default_med", "FE: Multiplayer is loading...", 22, 262, 0);
		String percentText = (int) (percent * 100) + "%";
		int pwidth = FEResources.getBitmapFont("default_med").getStringWidth(percentText);
		Renderer.drawString("default_med", percentText, 458 - pwidth, 263, 0);
		Renderer.drawRectangle(20, 280, 460, 300, 0, Color.gray);
		Renderer.drawRectangle(22, 282, 22 + width, 298, 0, Color.blue.darker());
		Display.update();
	}

}
