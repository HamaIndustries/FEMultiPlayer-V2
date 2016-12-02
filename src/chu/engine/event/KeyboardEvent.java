package chu.engine.event;

// TODO: Auto-generated Javadoc
/**
 * The Class KeyboardEvent.
 */
public final class KeyboardEvent {
	
	/** The key. */
	public final int key;
	
	/** The event char. */
	public final char eventChar;
	
	/** The is repeat event. */
	public final boolean isRepeatEvent;
	
	/** true if the event key was pressed or false if it was released.  */
	public final boolean state;
	
	/**
	 * Instantiates a new keyboard event.
	 *
	 * @param k the key
	 * @param c the char
	 * @param r is repeat
	 * @param s the state
	 */
	public KeyboardEvent(int k, char c, boolean r, boolean s) {
		key = k;
		eventChar = c;
		isRepeatEvent = r;
		state = s;
	}
	
	@Override
	public String toString() {
		return "String [" +
			"key: " + key + ", " +
			"char: " + eventChar + ", " +
			"repeat: " + isRepeatEvent + ", " +
			"state: " + (state ? "pressed" : "released") + "]";
	}
}
