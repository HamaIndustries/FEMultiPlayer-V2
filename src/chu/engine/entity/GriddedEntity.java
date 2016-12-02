package chu.engine.entity;

// TODO: Auto-generated Javadoc
/**
 * The Class GriddedEntity.
 */
public class GriddedEntity extends Entity {

	/** The xcoord. */
	protected int xcoord;

	/** The ycoord. */
	protected int ycoord;

	/**
	 * Instantiates a new gridded entity.
	 */
	public GriddedEntity() {
		this(0, 0);
	}

	/**
	 * Instantiates a new gridded entity.
	 *
	 * @param xx the xx
	 * @param yy the yy
	 */
	public GriddedEntity(int xx, int yy) {
		super(xx * 16, yy * 16);
		xcoord = xx;
		ycoord = yy;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see chu.engine.Entity#beginStep()
	 */
	@Override
	public void beginStep() {
		x = xcoord * 16;
		y = ycoord * 16;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see chu.engine.Entity#endStep()
	 */
	@Override
	public void endStep() {

	}

	/**
	 * Gets the x coord.
	 *
	 * @return the x coord
	 */
	public int getXCoord() {
		return xcoord;
	}

	/**
	 * Gets the y coord.
	 *
	 * @return the y coord
	 */
	public int getYCoord() {
		return ycoord;
	}

	/**
	 * Grid set x coord.
	 *
	 * @param x the x
	 */
	// ONLY THE GRID SHOULD CALL THESE METHODS.
	public void gridSetXCoord(int x) {
		xcoord = x;
	}

	/**
	 * Grid set y coord.
	 *
	 * @param y the y
	 */
	public void gridSetYCoord(int y) {
		ycoord = y;
	}
}
