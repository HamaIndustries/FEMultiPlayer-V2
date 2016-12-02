package net.fe.editor;

import java.io.Serializable;

import org.newdawn.slick.Color;

/**
 * Represents a location where a unit will be placed upon level initialization
 */
public final class SpawnPoint implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 8955139984944016201L;

	/** The x-coordinate. */
	public final int x;

	/** The y-coordinate. */
	public final int y;

	/** The team. */
	public final Color team;

	/**
	 * Instantiates a new spawn point.
	 *
	 * @param x the x-coordinate
	 * @param y the y-coordinate
	 * @param team the team
	 */
	public SpawnPoint(int x, int y, Color team) {
		this.x = x;
		this.y = y;
		this.team = team;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SpawnPoint other = (SpawnPoint) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		// LevelEditorStage depends on the team not counting in equals
		// so that both teams can't have a spawn on the same tile
		return true;
	}

}
