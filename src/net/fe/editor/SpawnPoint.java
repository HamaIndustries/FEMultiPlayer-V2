package net.fe.editor;

import java.io.Serializable;

import org.newdawn.slick.Color;

// TODO: Auto-generated Javadoc
/**
 * The Class SpawnPoint.
 */
public class SpawnPoint implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 8955139984944016201L;
	
	/** The x. */
	public int x;
	
	/** The y. */
	public int y;
	
	/** The team. */
	public Color team;
	
	/**
	 * Instantiates a new spawn point.
	 *
	 * @param x the x
	 * @param y the y
	 * @param team the team
	 */
	public SpawnPoint(int x, int y, Color team) {
		this.x = x;
		this.y = y;
		this.team = team;
	}
	
	/* (non-Javadoc)
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

	/* (non-Javadoc)
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
		return true;
	}
	

}
