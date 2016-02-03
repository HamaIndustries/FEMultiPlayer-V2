package chu.engine;

import chu.engine.anim.Sprite;

// TODO: Auto-generated Javadoc
/**
 * The Class Entity.
 */
public abstract class Entity implements Comparable<Entity> {
	
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
	
	/** The update priority. */
	public int updatePriority;
	
	/** The render depth. */
	public float renderDepth;
	
	/** The sprite. */
	public Sprite sprite;
	
	/** The hitbox. */
	public Hitbox hitbox;
	
	/** The stage. */
	public Stage stage;
	
	/** The will be removed. */
	public boolean willBeRemoved;
	
	/** The solid. */
	public boolean solid;
	
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
		if(sprite != null) sprite.update();
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
	 * Destroy.
	 */
	//Called when the entity is removed from the stage.
	public void destroy() {
		if(stage == null) {
			flagForRemoval();
		} else {
			stage.removeEntity(this);
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	//Lower numbers = higher priority.
	public int compareTo(Entity e) {
		return updatePriority - e.updatePriority;
	}
	
	/**
	 * Will be removed.
	 *
	 * @return true, if successful
	 */
	public boolean willBeRemoved() {
		return willBeRemoved;
	}
	
	/**
	 * Flag for removal.
	 */
	public void flagForRemoval() {
		willBeRemoved = true;
	}
	
}
