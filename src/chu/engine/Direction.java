package chu.engine;

// TODO: Auto-generated Javadoc
/**
 * The Enum Direction.
 */
public enum Direction {

	/** The southwest. */
	SOUTHWEST(-16, 16, 180.0f),

	/** The south. */
	SOUTH(0, 16, 270.0f),

	/** The southeast. */
	SOUTHEAST(16, 16, 270.0f),

	/** The west. */
	WEST(-16, 0, 180.0f),

	/** The none. */
	NONE(0, 0, 0.0f),

	/** The east. */
	EAST(16, 0, 0.0f),

	/** The northwest. */
	NORTHWEST(-16, -16, 90.0f),

	/** The north. */
	NORTH(0, -16, 90.0f),

	/** The northeast. */
	NORTHEAST(16, -16, 0.0f);

	/** The dx. */
	private final int dx;

	/** The dy. */
	private final int dy;

	/** The angle. */
	private final float angle;

	/**
	 * Instantiates a new direction.
	 *
	 * @param x the x
	 * @param y the y
	 * @param f the f
	 */
	Direction(int x, int y, float f) {
		dx = x;
		dy = y;
		angle = f;
	}

	/**
	 * Gets the x.
	 *
	 * @return the x
	 */
	public int getX() {
		return dx;
	}

	/**
	 * Gets the y.
	 *
	 * @return the y
	 */
	public int getY() {
		return dy;
	}

	/**
	 * Gets the unit x.
	 *
	 * @return the unit x
	 */
	public int getUnitX() {
		return dx / 16;
	}

	/**
	 * Gets the unit y.
	 *
	 * @return the unit y
	 */
	public int getUnitY() {
		return dy / 16;
	}

	/**
	 * Gets the angle.
	 *
	 * @return the angle
	 */
	public float getAngle() {
		return angle;
	}

	/**
	 * Checks if is diagonal.
	 *
	 * @return true, if is diagonal
	 */
	public boolean isDiagonal() {
		if (this == NORTHEAST || this == SOUTHEAST || this == NORTHWEST || this == SOUTHWEST)
			return true;
		return false;
	}

	/**
	 * Gets the adjacent directions.
	 *
	 * @param d the d
	 * @return the adjacent directions
	 */
	public static Direction[] getAdjacentDirections(Direction d) {
		Direction[] temp = new Direction[8];
		Direction[] adjacent = new Direction[3];
		temp[0] = SOUTHWEST;
		temp[1] = SOUTH;
		temp[2] = SOUTHEAST;
		temp[3] = EAST;
		temp[4] = NORTHEAST;
		temp[5] = NORTH;
		temp[6] = NORTHWEST;
		temp[7] = WEST;
		for (int i = 0; i < 8; i++) {
			if (temp[i] == d) {
				adjacent[0] = temp[(i + 7) % 8];
				adjacent[1] = d;
				adjacent[2] = temp[(i + 9) % 8];
				return adjacent;
			}
		}
		return null;
	}

	/**
	 * Gets the opposite direction.
	 *
	 * @param d the d
	 * @return the opposite direction
	 */
	public static Direction getOppositeDirection(Direction d) {
		switch (d) {
			case SOUTHWEST:
				return NORTHEAST;
			case SOUTH:
				return NORTH;
			case SOUTHEAST:
				return NORTHWEST;
			case WEST:
				return EAST;
			case NONE:
				return NONE;
			case EAST:
				return WEST;
			case NORTHWEST:
				return SOUTHEAST;
			case NORTH:
				return SOUTH;
			case NORTHEAST:
				return NORTHWEST;
		}
		return NONE;
	}

}
