package chu.engine.entity;

import chu.engine.Stage;
import chu.engine.anim.Sprite;
import chu.engine.hitbox.Hitbox;

// TODO: Auto-generated Javadoc
/**
 * The Class Entity.
 */
public abstract class Entity {
	
	/** The x. */
	public float x;
	
	/** The y. */
	public float y;
	
	/** The width. */
	public float width;
	
	/** The height. */
	public float height;
	
	/** The prev x. */
	public float prevX;
	
	/** The prev y. */
	public float prevY;
	
	/** The render depth. */
	public float renderDepth;
	
	/** The sprite. */
	public final Sprite sprite;
	
	/** The hitbox. */
	public Hitbox hitbox;
	
	/** The stage. */
	public Stage stage;
	
	/** The will be removed. */
	public boolean willBeRemoved;
	
	/** The solid. */
	public boolean solid;
	

	public boolean hasRun = false;
	
	/**
	 * Instantiates a new entity.
	 *
	 * @param x the x
	 * @param y the y
	 */
	public Entity(float x, float y) {
		this.x = x;
		this.y = y;
		this.prevX = x;
		this.prevY = y;
		sprite = new Sprite();
		willBeRemoved = false;
		solid = false;
		width = 0;
		height = 0;
	}
	
	
	/**
	 * On step.
	 */
	public void onStep() {
		if(sprite != null)
			sprite.update();
		prevX = x;
		prevY = y;
	}
	
	/**
	 * Begin step.
	 */
	public void beginStep() {
		
	}
	
	/**
	 * End step.
	 */
	public void endStep() {
		
	}
	
	/**
	 * Render.
	 */
	public void render() {
		sprite.render(x, y, renderDepth);
	}
	
	/**
	 * Destroy. Called when the entity is removed from the stage.
	 */
	public final void destroy() {
		if(stage == null) {
			flagForRemoval();
		} else {
			stage.removeEntity(this);
		}
	}
	
	/**
	 * Will be removed.
	 *
	 * @return true, if successful
	 */
	public final boolean willBeRemoved() {
		return willBeRemoved;
	}
	
	/**
	 * Flag for removal.
	 */
	public final void flagForRemoval() {
		willBeRemoved = true;
	}
	
}
