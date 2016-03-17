package chu.engine.anim;

import java.util.HashMap;

import org.newdawn.slick.opengl.Texture;

// TODO: Auto-generated Javadoc
/**
 * Manages a set of animations, and allows
 * the user to switch between animations.
 * @author Shawn
 *
 */
public class Sprite {
	
	/** The animations. */
	private HashMap<String, Animation> animations;
	
	/** The current animation. */
	protected Animation currentAnimation;
	
	/** The cur anim name. */
	private String curAnimName;
	
	/**
	 * Instantiates a new sprite.
	 */
	public Sprite() {
		animations = new HashMap<String, Animation>();
	}
	
	/**
	 * Add a new animation to the sprite by creating an animation with the given parameters.
	 *
	 * @param name Name of the animation
	 * @param tex Texture to use as the spritesheet
	 * @param width Width of a single frame in pixels
	 * @param height Height of a single frame in pixels
	 * @param length the length
	 * @param columns the columns
	 * @param f Speed of the animation in frames per second
	 */
	public void addAnimation(String name, Texture tex, int width, int height, int length, int columns, float f) {
		Animation anim = new Animation(tex, width, height, length, columns, f);
		animations.put(name.toUpperCase(), anim);
		currentAnimation = anim;
		curAnimName = name;
		anim.setSprite(this);
	}
	
	/**
	 * Add a new single-image animation.
	 * @param name Name of the animation
	 * @param tex Texture to use as the static image
	 */
	public void addAnimation(String name, Texture tex) {
		Animation anim = new Animation(tex);
		animations.put(name.toUpperCase(), anim);
		currentAnimation = anim;
		curAnimName = name;
		anim.setSprite(this);
	}
	
	/**
	 * Add an existing animation to the sprite.
	 *
	 * @param name Name of the animation
	 * @param anim Existing animation to add
	 */
	public void addAnimation(String name, Animation anim) {
		animations.put(name.toUpperCase(), anim);
		currentAnimation = anim;
		curAnimName = name;
		anim.setSprite(this);
	}
	
	/**
	 * Gets the animation with the given name.
	 * @param name Name of the animation
	 * @return Animation that corresponds with that name
	 */
	public Animation getAnimation(String name) {
		return animations.get(name.toUpperCase());
	}
	
	/**
	 * Gets the current animation.
	 *
	 * @return the current animation
	 */
	public Animation getCurrentAnimation(){
		return currentAnimation;
	}
	
	/**
	 * Checks for animation.
	 *
	 * @param name the name
	 * @return true, if successful
	 */
	public boolean hasAnimation(String name){
		return animations.containsKey(name.toUpperCase());
	}
	/**
	 * Sets the current, rendering animation to the animation with the
	 * given name.
	 * @param name Name of the animation
	 */
	public void setAnimation(String name) {
		if(!hasAnimation(name)) return;
		currentAnimation = animations.get(name.toUpperCase());
		curAnimName = name.toUpperCase();
	}
	
	/**
	 * Gets the frame.
	 *
	 * @return Frame the current animation is on
	 */
	public int getFrame() {
		return currentAnimation.getFrame();
	}
	
	/**
	 * Sets the current animation to the given frame.
	 *
	 * @param frame Frame to set the current animation to
	 */
	public void setFrame(int frame) {
		currentAnimation.setFrame(frame % currentAnimation.getLength());
	}
	
	/**
	 * Size.
	 *
	 * @return Number of animations in the sprite
	 */
	public int size() {
		return animations.size();
	}
	
	/**
	 * Draws the sprite at the specified coordinates.
	 *
	 * @param x the x
	 * @param y the y
	 * @param depth the depth
	 */
	public void render(float x, float y, float depth) {
		this.render(x, y, depth, null, new ShaderArgs());
	}
	
	/**
	 * Render.
	 *
	 * @param x the x
	 * @param y the y
	 * @param depth the depth
	 * @param t the t
	 */
	public void render(float x, float y, float depth, Transform t) {
		this.render(x, y, depth, t, new ShaderArgs());
	}

	/**
	 * Updates the current animation.
	 */
	public void update() {
		if(currentAnimation == null) return;
		currentAnimation.update();
	}
	
	public boolean updateRunOnce() {
		if(currentAnimation == null) return true;
		return currentAnimation.updateRunOnce();
	}

	/**
	 * Draws the sprite .
	 *
	 * @param x the x
	 * @param y the y
	 * @param depth the depth
	 * @param transform the transform
	 * @param shader the shader
	 */
	public void render(float x, float y, float depth, Transform transform, ShaderArgs shader) {
		if(currentAnimation == null) return;
		
		int width = currentAnimation.getWidth();
		int height = currentAnimation.getHeight();
		int frameX = currentAnimation.getFrame() % currentAnimation.getColumns();
		int frameY = currentAnimation.getFrame() / currentAnimation.getColumns();
		int offX = currentAnimation.getOffsetX();
		int offY = currentAnimation.getOffsetY();;
		if(transform != null) {
			if(transform.flipHorizontal) {
				offX = -offX + width;
			}
			if(transform.flipVertical) {
				offY = -offY + height;
			}
		}
		float x0 = ((float)frameX * width)/currentAnimation.getImageWidth();
		float x1 = ((float)(frameX+1) * width)/currentAnimation.getImageWidth();
		float y0 = ((float)frameY * height)/currentAnimation.getImageHeight();
		float y1 = ((float)(frameY+1) * height)/currentAnimation.getImageHeight();
		Texture texture = currentAnimation.getTexture();
		Renderer.render(texture, x0, y0, x1, y1, x - offX, y - offY,
				x + width - offX, y + height - offY, depth, transform, shader);
	}

	/**
	 * Sets the speed.
	 *
	 * @param newSpeed the new speed
	 */
	public void setSpeed(float newSpeed) {
		currentAnimation.setSpeed(newSpeed);
	}
	
	/**
	 * Gets the animation name.
	 *
	 * @return the animation name
	 */
	public String getAnimationName() {
		return curAnimName;
	}
}
