package chu.engine.event;

public final class KeyboardEvent {

	public final int key;

	public final char eventChar;

	public final boolean isRepeatEvent;

	/** true if the event key was pressed or false if it was released. */
	public final boolean state;
	
	private int modifiers;
	
	

	public KeyboardEvent(int k, char c, boolean r, boolean s, int modifiers) {
		key = k;
		eventChar = c;
		isRepeatEvent = r;
		state = s;
		this.modifiers = modifiers;
	}
	
	public boolean isShiftDown(){
		return (modifiers & 1) != 0;
	}
	
	public boolean isControlDown(){
		return (modifiers & 2) != 0;
	}
	
	/**
	 * Determines whether or not the alt key was pressed.
	 * This method only checks for LALT, for RALT, see {@link #isAltgrDown()}.
	 * @return true if the alt key was pressed.
	 */
	public boolean isAltDown(){
		return (modifiers & 4) != 0;
	}
	
	public boolean isAltgrDown(){
		return (modifiers & 8) != 0;
	}

	
	@Override
	public String toString() {
		return "String [" + "key: " + key + ", " + "char: " + eventChar + ", " + "repeat: " + isRepeatEvent + ", "
		        + "state: " + (state ? "pressed" : "released") + ", modifiers : " + modifiers + "]";
	}
}
