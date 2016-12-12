package net.fe.editor;

import java.io.Serializable;
import java.util.Set;

/**
 * Contains a list of entities that are to be added to an OverworldStage upon
 * setup. These entities include: - tiles (id only, terrain info is generated at
 * runtime) - spawn locations - game-mode specific entities Also contains
 * metadata including: - grid dimensions - supported game modes
 * 
 * A level can support multiple game modes by including all relevant entities.
 * The game will only load the entities necessary to run that particular game.
 * 
 * @author Shawn
 *
 */
public final class Level implements Serializable {

	private static final long serialVersionUID = -3556853678338788517L;

	public final int width;

	public final int height;

	public final int[][] tiles;

	public final Set<SpawnPoint> spawns;

	public Level(int w, int h, int[][] t, Set<SpawnPoint> s) {
		width = w;
		height = h;
		tiles = t;
		spawns = java.util.Collections.unmodifiableSet(new java.util.HashSet<>(s));
	}

	@Override
	public int hashCode() {
		return ((spawns.hashCode() * 31 + java.util.Arrays.deepHashCode(tiles)) * 31 + width) * 31 + height;
	}

	@Override
	public boolean equals(Object rhs) {
		if (rhs instanceof Level) {
			Level other = (Level) rhs;

			return this.width == other.width && this.height == other.height && this.spawns.equals(other.spawns)
			        && java.util.Arrays.deepEquals(this.tiles, other.tiles);
		} else {
			return false;
		}
	}

}
