package chu.engine.anim;

import static org.lwjgl.opengl.GL11.glTranslatef;

import chu.engine.Game;
import chu.engine.entity.Entity;

// TODO: Auto-generated Javadoc
/**
 * The Class Camera.
 */
public class Camera {

	/** The center. */
	Entity center;

	/** The offset x. */
	int offsetX;

	/** The offset y. */
	int offsetY;

	/**
	 * Instantiates a new camera.
	 *
	 * @param e the e
	 * @param oX the o x
	 * @param oY the o y
	 */
	public Camera(Entity e, int oX, int oY) {
		set(e, oX, oY);
	}

	/**
	 * Sets the.
	 *
	 * @param e the e
	 * @param oX the o x
	 * @param oY the o y
	 */
	public void set(Entity e, int oX, int oY) {
		center = e;
		offsetX = oX;
		offsetY = oY;
	}

	/**
	 * Look through.
	 */
	public void lookThrough() {
		if (center != null) {
			glTranslatef(-(center.x + offsetX - Game.getWindowWidth() / 2),
			        -(center.y + offsetY - Game.getWindowHeight() / 2), 0);
		}
	}

	/**
	 * Look back.
	 */
	public void lookBack() {
		if (center != null) {
			glTranslatef(center.x + offsetX - Game.getWindowWidth() / 2,
			        center.y + offsetY - Game.getWindowHeight() / 2, 0);
		}
	}

	/**
	 * Gets the x.
	 *
	 * @return the x
	 */
	public float getX() {
		if (center != null)
			return center.x + offsetX;
		else
			return Game.getWindowWidth() / 2;
	}

	/**
	 * Gets the y.
	 *
	 * @return the y
	 */
	public float getY() {
		if (center != null)
			return center.y + offsetY;
		else
			return Game.getWindowHeight() / 2;
	}

	/**
	 * Gets the screen x.
	 *
	 * @return the screen x
	 */
	public float getScreenX() {
		if (center != null)
			return center.x + offsetX - Game.getWindowWidth() / 2;
		else
			return 0;
	}

	/**
	 * Gets the screen y.
	 *
	 * @return the screen y
	 */
	public float getScreenY() {
		if (center != null)
			return center.y + offsetY - Game.getWindowHeight() / 2;
		else
			return 0;
	}

}
