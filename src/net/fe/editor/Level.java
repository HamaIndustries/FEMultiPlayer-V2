package net.fe.editor;

import java.io.Serializable;
import java.util.Set;

// TODO: Auto-generated Javadoc
/**
 * Contains a list of entities that are to be added
 * to an OverworldStage upon setup. These entities include:
 * - tiles (id only, terrain info is generated at runtime)
 * - spawn locations
 * - game-mode specific entities
 * Also contains metadata including:
 * - grid dimensions
 * - supported game modes
 * 
 * A level can support multiple game modes by including
 * all relevant entities. The game will only load the entities
 * necessary to run that particular game.
 * @author Shawn
 *
 */
public class Level implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -3556853678338788517L;
	
	/** The width. */
	public int width;
	
	/** The height. */
	public int height;
	
	/** The tiles. */
	public int[][] tiles;
	
	/** The spawns. */
	public Set<SpawnPoint> spawns;
	
	/**
	 * Instantiates a new level.
	 *
	 * @param w the w
	 * @param h the h
	 * @param t the t
	 * @param s the s
	 */
	public Level(int w, int h, int[][] t, Set<SpawnPoint> s) {
		width = w;
		height = h;
		tiles = t;
		spawns = s;
	}
	
}
