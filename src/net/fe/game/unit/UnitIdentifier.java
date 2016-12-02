package net.fe.game.unit;

import java.io.Serializable;

// TODO: Auto-generated Javadoc
/**
 * The Class UnitIdentifier.
 */
public class UnitIdentifier implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The name. */
	public String name;

	/** The party color. */
	public org.newdawn.slick.Color partyColor;

	/**
	 * Instantiates a new unit identifier.
	 *
	 * @param u the u
	 */
	public UnitIdentifier(Unit u) {
		name = u.name;
		partyColor = u.getPartyColor();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return name.hashCode() + partyColor.hashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o) {
		if (!(o instanceof UnitIdentifier))
			return false;
		UnitIdentifier other = (UnitIdentifier) o;
		return name.equals(other.name) && partyColor.equals(other.partyColor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return name;
	}
}
