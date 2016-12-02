package chu.engine.hitbox;

import chu.engine.entity.Entity;

// TODO: Auto-generated Javadoc
/**
 * The Class RectangleHitbox.
 */
public class RectangleHitbox extends Hitbox {
	
	/** The width. */
	private float width;
	
	/** The height. */
	private float height;

	/**
	 * Instantiates a new rectangle hitbox.
	 *
	 * @param p the p
	 * @param x the x
	 * @param y the y
	 * @param w the w
	 * @param h the h
	 */
	public RectangleHitbox(Entity p, int x, int y, int w, int h) {
		super(p, x, y);
		setWidth(w);
		height = h;
	}

	/**
	 * Gets the width.
	 *
	 * @return the width
	 */
	public float getWidth() {
		return width;
	}

	/**
	 * Sets the width.
	 *
	 * @param width the new width
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * Gets the height.
	 *
	 * @return the height
	 */
	public float getHeight() {
		return height;
	}
	
	/**
	 * Sets the height.
	 *
	 * @param height the new height
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * Gets the end y.
	 *
	 * @return the end y
	 */
	public float getEndY() {
		return getY() + height;
	}
	
	/**
	 * Gets the end x.
	 *
	 * @return the end x
	 */
	public float getEndX() {
		return getX() + width;
	}
	
}
