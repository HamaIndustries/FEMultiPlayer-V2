package chu.engine;

import chu.engine.entity.Entity;

// TODO: Auto-generated Javadoc
/**
 * The Interface Collidable.
 */
public interface Collidable {
	
	/**
	 * Method that deals with any collisions with the entities in
	 * the array.
	 *
	 * @param entity the entity
	 */

	public void doCollisionWith(Entity entity);
}
