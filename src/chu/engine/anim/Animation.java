package chu.engine.anim;

import org.newdawn.slick.opengl.Texture;

import chu.engine.Game;

// TODO: Auto-generated Javadoc
/**
 * The Class Animation.
 */
public class Animation {
	
	private boolean hasRun = false;
	
	/** The texture. */
	private Texture texture;
	
	/** The width. */
	private int width;
	
	/** The height. */
	private int height;
	
	/** The rows. */
	private int rows;
	
	/** The columns. */
	private int columns;
	
	/** The current frame. */
	private int currentFrame;
	
	/** The counter. */
	private float counter;
	
	/** The length. */
	private int length;
	
	/** The offset x. */
	private int offsetX;
	
	/** The offset y. */
	private int offsetY;
	
	/** The sprite. */
	protected Sprite sprite;
	
	/** Time for each frame in seconds */
	protected float speed;
	
	private BlendModeArgs blend;
	
	/**
	 * Instantiates a new single-frame animation.
	 *
	 * @param t the texture
	 * @param blend the blend mode to use when drawing this animation
	 */
	public Animation(Texture t, BlendModeArgs blend) {
		texture = t;
		width = t.getImageWidth();
		height = t.getImageHeight();
		length = 1;
		rows = 1;
		columns = 1;
		speed = 0;
		currentFrame = 0;
		counter = 0;
		this.blend = blend;
	}
	
	/**
	 * Instantiates a new animation.
	 *
	 * @param t the t
	 * @param width the width
	 * @param height the height
	 * @param length the length
	 * @param columns the columns
	 * @param speed the speed
	 * @param blend the blend mode to use when drawing this animation
	 */
	public Animation(Texture t, int width, int height, int length, int columns, float speed, BlendModeArgs blend) {
		this(t, blend);
		this.width = width;
		this.height = height;
		this.columns = columns;
		this.rows = (length/columns)+1;
		this.length = length;
		this.speed = speed;
		this.offsetX = 0;
		this.offsetY = 0;
		this.currentFrame = 0;
		counter = 0;
	}
	
	/**
	 * Instantiates a new animation.
	 *
	 * @param t the t
	 * @param width the width
	 * @param height the height
	 * @param length the length
	 * @param columns the columns
	 * @param offsetX the offset x
	 * @param offsetY the offset y
	 * @param speed the speed
	 * @param blend the blend mode to use when drawing this animation
	 */
	public Animation(Texture t, int width, int height, int length, int columns,
			int offsetX, int offsetY, float speed, BlendModeArgs blend) {
		this(t, blend);
		this.width = width;
		this.height = height;
		this.columns = columns;
		this.rows = (length/columns)+1;
		this.length = length;
		this.speed = speed;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.currentFrame = 0;
		counter = 0;
	}
	
	/**
	 * Gets the length.
	 *
	 * @return the length
	 */
	public int getLength() { return length; }
	
	/**
	 * Gets the frame.
	 *
	 * @return the frame
	 */
	public int getFrame() { return currentFrame; }
	
	/**
	 * Gets the width.
	 *
	 * @return the width
	 */
	public int getWidth() { return width; }
	
	/**
	 * Gets the height.
	 *
	 * @return the height
	 */
	public int getHeight() { return height; }
	
	/**
	 * Gets the rows.
	 *
	 * @return the rows
	 */
	public int getRows() { return rows; }
	
	/**
	 * Gets the columns.
	 *
	 * @return the columns
	 */
	public int getColumns() { return columns; }
	
	/**
	 * Gets the image height.
	 *
	 * @return the image height
	 */
	public int getImageHeight() { return texture.getImageHeight(); }
	
	/**
	 * Gets the image width.
	 *
	 * @return the image width
	 */
	public int getImageWidth() { return texture.getImageWidth(); }
	
	/**
	 * Gets the texture.
	 *
	 * @return the texture
	 */
	public Texture getTexture() { return texture; }
	
	
	/**
	 * Update.
	 */
	public void update() {
		if(speed != 0) counter += Game.getDeltaSeconds();
		if(speed != 0 && counter > Math.abs(speed)) {
			increment();
			counter = 0;
		}
	}
	
	public boolean updateRunOnce() {
		if(speed != 0) counter += Game.getDeltaSeconds();
		if(speed != 0 && counter > Math.abs(speed) && !hasRun) {
			increment();
			counter = 0;
		}
		return hasRun;
	}
	
	
	/**
	 * Increment.
	 */
	public void increment() {
		if(speed > 0) {
			currentFrame += 1;
			if(currentFrame >= length) done();
		} else {
			currentFrame -= 1;
			if(currentFrame < 0) done();
		}
	}
	
	/**
	 * Done.
	 */
	public void done() {
		if(speed > 0)
			currentFrame = 0;
		else
			currentFrame = columns-1;
		hasRun = true;
	}

	/**
	 * Sets the frame.
	 *
	 * @param i the new frame
	 */
	public void setFrame(int i) {
		this.currentFrame = i;
	}

	/**
	 * Sets the speed.
	 *
	 * @param newSpeed the new speed
	 */
	public void setSpeed(float newSpeed) {
		speed = newSpeed;
	}

	/**
	 * Sets the sprite.
	 *
	 * @param sprite2 the new sprite
	 */
	public void setSprite(Sprite sprite2) {
		sprite = sprite2;
	}
	
	/**
	 * Gets the sprite.
	 *
	 * @return the sprite
	 */
	public Sprite getSprite() {
		return sprite;
	}
	
	/**
	 * Gets the offset x.
	 *
	 * @return the offset x
	 */
	public int getOffsetX() {
		return offsetX;
	}
	
	/**
	 * Gets the offset y.
	 *
	 * @return the offset y
	 */
	public int getOffsetY() {
		return offsetY;
	}

	/**
	 * Gets the speed.
	 *
	 * @return the speed
	 */
	public float getSpeed() {
		return speed;
	}
	
	public BlendModeArgs getBlendMode() {
		return blend;
	}

}
