package chu.engine;

import java.util.Comparator;

// TODO: Auto-generated Javadoc
/**
 * The Class SortByUpdate.
 */
public class SortByUpdate implements Comparator<Entity> {

	/**
	 * Instantiates a new sort by update.
	 */
	public SortByUpdate() {

	}

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(Entity arg0, Entity arg1) {
		if(arg0.updatePriority == arg1.updatePriority){ 
			return arg0.hashCode()-arg1.hashCode();
		}
		return arg0.updatePriority - arg1.updatePriority;
	}

}