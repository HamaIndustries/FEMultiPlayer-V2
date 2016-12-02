package chu.engine.anim;

import org.newdawn.slick.Color;

// TODO: Auto-generated Javadoc
/**
 * The Class Transform.
 */
public class Transform {

	/** The rotation. */
	public float rotation;

	/** The translate x. */
	public float translateX;

	/** The translate y. */
	public float translateY;

	/** The scale x. */
	public float scaleX;

	/** The scale y. */
	public float scaleY;

	/** The flip horizontal. */
	public boolean flipHorizontal;

	/** The flip vertical. */
	public boolean flipVertical;

	/** The color. */
	public Color color;

	/**
	 * Instantiates a new transform.
	 */
	public Transform() {
		rotation = 0;
		translateX = 0;
		translateY = 0;
		scaleX = 1;
		scaleY = 1;
		flipHorizontal = false;
		flipVertical = false;
		color = Color.white;
	}

	/**
	 * Sets the rotation.
	 *
	 * @param angle the new rotation
	 */
	public void setRotation(float angle) {
		rotation = angle;
	}

	/**
	 * Sets the translation.
	 *
	 * @param x the x
	 * @param y the y
	 */
	public void setTranslation(float x, float y) {
		translateX = x;
		translateY = y;
	}

	/**
	 * Sets the scale.
	 *
	 * @param x the x
	 * @param y the y
	 */
	public void setScale(float x, float y) {
		scaleX = x;
		scaleY = y;
	}

	/**
	 * Flip horizontal.
	 */
	public void flipHorizontal() {
		flipHorizontal = true;
	}

	/**
	 * Flip vertical.
	 */
	public void flipVertical() {
		flipVertical = true;
	}

	/**
	 * Sets the color.
	 *
	 * @param c the new color
	 */
	public void setColor(Color c) {
		color = c;
	}
}
