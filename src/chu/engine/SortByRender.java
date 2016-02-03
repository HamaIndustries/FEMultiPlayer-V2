package chu.engine;

import java.util.Comparator;

// TODO: Auto-generated Javadoc
/**
 * The Class SortByRender.
 */
public class SortByRender implements Comparator<Entity> {

	/**
	 * Instantiates a new sort by render.
	 */
	public SortByRender() {
		
	}

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(Entity arg0, Entity arg1) {
		if(arg0.renderDepth == arg1.renderDepth){ 
			return arg0.hashCode()-arg1.hashCode();
		} else if(arg0.renderDepth < arg1.renderDepth) {
			return 1;
		} else {
			return -1;
		}
	}

}
