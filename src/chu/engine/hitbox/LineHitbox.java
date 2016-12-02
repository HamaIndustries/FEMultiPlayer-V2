package chu.engine.hitbox;

import chu.engine.entity.Entity;

// TODO: Auto-generated Javadoc
/**
 * The Class LineHitbox.
 */
public class LineHitbox extends Hitbox {

	/** The dx. */
	protected float dx;
	
	/** The dy. */
	protected float dy;
	
	/**
	 * Instantiates a new line hitbox.
	 *
	 * @param p the p
	 * @param offsetX the offset x
	 * @param offsetY the offset y
	 * @param dx the dx
	 * @param dy the dy
	 */
	public LineHitbox(Entity p, int offsetX, int offsetY, int dx, int dy) {
		super(p, offsetX, offsetY);
		this.dx = dx;
		this.dy = dy;
	}
	
	/**
	 * Gets the end x.
	 *
	 * @return the end x
	 */
	public float getEndX() {
		return parent.x + offsetX + dx;
	}
	
	/**
	 * Gets the end y.
	 *
	 * @return the end y
	 */
	public float getEndY() {
		return parent.y + offsetY + dy;
	}

}
