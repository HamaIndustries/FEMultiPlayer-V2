package net.fe.unit;

// TODO: Auto-generated Javadoc
/**
 * The Class RiseTome.
 */
public class RiseTome extends Item{
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -2L;
	
	/**
	 * Instantiates a new rise tome.
	 */
	public RiseTome(){
		super("Rise");
		this.setMaxUses(4);
		this.id = 3;
		this.setCost(8000);
	}
	
	/* (non-Javadoc)
	 * @see net.fe.unit.Item#getCopy()
	 */
	public RiseTome getCopy(){
		return new RiseTome();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Item that) {
		if (that instanceof RiseTome) {
			return 0;
		} else {
			return 1;
		}
	}
}
