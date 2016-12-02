package chu.engine.event;

// TODO: Auto-generated Javadoc
/**
 * The Class MouseEvent.
 */
public class MouseEvent {

	/** The x. */
	public int x;

	/** The y. */
	public int y;

	/** The dwheel. */
	public int dwheel;

	/** The button. */
	public int button;

	/** The state. */
	public boolean state;

	/**
	 * Instantiates a new mouse event.
	 *
	 * @param x the x
	 * @param y the y
	 * @param dwheel the dwheel
	 * @param button the button
	 * @param s the s
	 */
	public MouseEvent(int x, int y, int dwheel, int button, boolean s) {
		this.x = x;
		this.y = y;
		this.dwheel = dwheel;
		this.button = button;
		this.state = s;
	}
}
