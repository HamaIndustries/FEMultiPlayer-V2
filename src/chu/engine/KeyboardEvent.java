package chu.engine;

// TODO: Auto-generated Javadoc
/**
 * The Class KeyboardEvent.
 */
public class KeyboardEvent {
	
	/** The key. */
	public int key;
	
	/** The event char. */
	public char eventChar;
	
	/** The is repeat event. */
	public boolean isRepeatEvent;
	
	/** The state. */
	public boolean state;
	
	/**
	 * Instantiates a new keyboard event.
	 *
	 * @param k the k
	 * @param c the c
	 * @param r the r
	 * @param s the s
	 */
	public KeyboardEvent(int k, char c, boolean r, boolean s) {
		key = k;
		eventChar = c;
		isRepeatEvent = r;
		state = s;
	}
}
