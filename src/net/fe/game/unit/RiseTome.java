package net.fe.game.unit;

// TODO: Auto-generated Javadoc
/**
 * The Class RiseTome.
 */
public class RiseTome extends Item {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -2L;

	/**
	 * Instantiates a new rise tome.
	 */
	public RiseTome() {
		super("Rise", 1, 3, 5000);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.fe.unit.Item#getCopy()
	 */
	public RiseTome getCopy() {
		return new RiseTome();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Item that) {
		if (that instanceof RiseTome) {
			return 0;
		} else {
			return 1;
		}
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	protected boolean canEquals(Object other) {
		return other instanceof RiseTome;
	}

	@Override
	public boolean equals(Object other) {
		if (other != null && other instanceof RiseTome) {
			RiseTome o2 = (RiseTome) other;
			if (o2.canEquals(this)) {
				return super.equals(o2);
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
}
