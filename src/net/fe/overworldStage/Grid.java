package net.fe.overworldStage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.newdawn.slick.Color;

import net.fe.Party;
import net.fe.unit.Unit;
import net.fe.unit.UnitIdentifier;

// TODO: Auto-generated Javadoc
/**
 * The Class Grid.
 */
public class Grid{
	
	/** The grid. */
	private Unit[][] grid;
	
	/** The terrain. */
	private Terrain[][] terrain;
	
	/** The blue throne y. */
	private int blueThroneX, blueThroneY;
	
	/** The red throne y. */
	private int redThroneX, redThroneY;
	
	/** The height. */
	public final int width, height;

	/**
	 * Instantiates a new grid.
	 *
	 * @param width the width
	 * @param height the height
	 * @param defaultTerrain the default terrain
	 */
	public Grid(int width, int height, Terrain defaultTerrain) {
		grid = new Unit[height][width];
		blueThroneX = -1;
		blueThroneY = -1;
		redThroneX = -1;
		redThroneY = -1;
		terrain = new Terrain[height][width];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				terrain[j][i] = defaultTerrain;
			}
		}
		this.width = width;
		this.height = height;
	}

	/**
	 * Adds a unit to the grid at the given coordinates.
	 *
	 * @param u the u
	 * @param x the x
	 * @param y the y
	 * @return true, if successful
	 */
	public boolean addUnit(Unit u, int x, int y) {
		if (grid[y][x] != null)
			return false;
		grid[y][x] = u;
		u.gridSetXCoord(x);
		u.gridSetYCoord(y);
		u.setOrigX(x);
		u.setOrigY(y);
		return true;
	}
	
	/**
	 * Find unit.
	 *
	 * @param u the u
	 * @return the unit
	 */
	public Unit findUnit(UnitIdentifier u){
		for(int i = 0; i < width; i++){
			for(int j = 0; j < height; j++){
				if(u.equals(new UnitIdentifier(getUnit(i,j)))){
					return getUnit(i,j);
				}
			}
		}
		return null;
	}

	/**
	 * Removes the unit.
	 *
	 * @param x the x
	 * @param y the y
	 * @return the unit
	 */
	Unit removeUnit(int x, int y) {
		if (grid[y][x] == null)
			return null;
		Unit ans = grid[y][x];
		grid[y][x] = null;
		return ans;
	}
	
	/**
	 * Move.
	 *
	 * @param u the u
	 * @param x the x
	 * @param y the y
	 * @param animated the animated
	 */
	public void move(Unit u, int x, int y, boolean animated){
		grid[u.getYCoord()][u.getXCoord()] = null;
		grid[y][x] = u;
		if(!animated){
			u.gridSetXCoord(x);
			u.gridSetYCoord(y);
		}
	}

	/**
	 * Gets the terrain.
	 *
	 * @param x the x
	 * @param y the y
	 * @return the terrain
	 */
	public Terrain getTerrain(int x, int y) {
		return terrain[y][x];
	}
	
	/**
	 * Sets the terrain.
	 *
	 * @param x the x
	 * @param y the y
	 * @param t the t
	 */
	public void setTerrain(int x, int y, Terrain t){
		terrain[y][x] = t;
	}

	/**
	 * Gets the unit.
	 *
	 * @param x the x
	 * @param y the y
	 * @return the unit
	 */
	public Unit getUnit(int x, int y) {
		return grid[y][x];
	}
	
	/**
	 * Returns true if the grid contains a space at (x,y).
	 */
	public boolean contains(int x, int y) {
		return x >= 0 && y >= 0 && x < this.width && y < this.height;
	}
	
	/**
	 * Sets the throne pos.
	 *
	 * @param c the c
	 * @param x the x
	 * @param y the y
	 */
	public void setThronePos(Color c, int x, int y) {
		if(c.equals(Party.TEAM_BLUE)) {
			blueThroneX = x;
			blueThroneY = y;
			System.out.println("Blue throne: "+x+" "+y);
		} else {
			redThroneX = x;
			redThroneY = y;
			System.out.println("Red throne: "+x+" "+y);
		}
	}
	
	/**
	 * Can seize.
	 *
	 * @param u the u
	 * @return true, if successful
	 */
	public boolean canSeize(Unit u) {
		//check if lord
		if(!u.getTheClass().name.equals("Lord"))
			return false;
		Color c = u.getPartyColor();
		if(c.equals(Party.TEAM_BLUE) 
				&& u.getXCoord() == redThroneX
				&& u.getYCoord() == redThroneY) {
			return true;
		} else if (c.equals(Party.TEAM_RED)
				&& u.getXCoord() == blueThroneX
				&& u.getYCoord() == blueThroneY){
			return true;
		}
		return false;
	}
	
	public Path improvePath(Unit unit, int x, int y, Path p) {
		if (grid[y][x] != null && grid[y][x] != unit)
			return null;
		
		if (p == null)
			return getShortestPath(unit, x, y);

		Path improved = new Path();
		
		int move = unit.getStats().mov;
		Node last = null;
		// Rebuild the current path, counting the movement used
		for (Node node : p.getAllNodes()) {
			improved.add(node);
			if (last != null) // The first node is the unit's current position
				move -= terrain[node.y][node.x].getMoveCost(unit.getTheClass());
			last = node;
			
			// If we go somewhere that is already on the path, just cut the path.
			if (node.x == x && node.y == y)
				return improved;
		}
		
		// Sanity check: we should be moving exactly one additional tile.
		if (last.distance(new Node(x, y)) != 1)
			return getShortestPath(unit, x, y);

		// Check that we can actually extend the path
		if (move < terrain[y][x].getMoveCost(unit.getTheClass()))
			return getShortestPath(unit, x, y);
		
		improved.add(new Node(x, y));
		return improved;
	}

	/**
	 * Gets the shortest path.
	 *
	 * @param unit the unit
	 * @param x the x
	 * @param y the y
	 * @return the shortest path
	 */
	public Path getShortestPath(Unit unit, int x, int y) {
		if(grid[y][x] != null && grid[y][x] != unit) return null;
		int move = unit.getStats().mov;
		Set<Node> closed = new HashSet<Node>();
		Set<Node> open = new HashSet<Node>();

		Node start = new Node(unit.getXCoord(), unit.getYCoord());
		Node goal = new Node(x, y);
		start.g = 0;
		start.f = heuristic(start, goal);
		open.add(start);
		while (!open.isEmpty()) {
			// get node in open with best f score
			Node cur = null;
			for (Node n : open) {
				if (cur == null || n.f < cur.f) {
					cur = n;
				}
			}

			if (cur.equals(goal)) {
				return getPath(cur);
			}
			
			open.remove(cur);
			closed.add(cur);
			for (Node n : cur.getNeighbors(this)) {
				for(Node o : open) {
					if(o.equals(n)) n = o;
				}
				int g = cur.g
						+ terrain[n.y][n.x].getMoveCost(unit.getTheClass());
				if(grid[n.y][n.x] != null && grid[n.y][n.x].getParty() != unit.getParty()) {
					g += 128;
				}
				int f = g + heuristic(n, goal);
				if (closed.contains(n) && f >= n.f) {
					continue;
				} else if (!open.contains(n) || f < n.f) {
					if (g > move){
						continue;
					}
					n.parent = cur;
					n.g = g;
					n.f = f;
					open.add(n);
				}
			}
		}

		// failure
		return null;
	}

	/**
	 * Gets the possible moves.
	 *
	 * @param u the u
	 * @return the possible moves
	 */
	public Set<Node> getPossibleMoves(Unit u) {
		int x = u.getXCoord();
		int y = u.getYCoord();
		Node start = new Node(x,y);
		start.d = 0;
		Set<Node> set = new HashSet<Node>();
		ArrayList<Node> q = new ArrayList<Node>();
		q.add(new Node(x, y));
		
		while(q.size() > 0){
			Node curr = q.remove(0);
			set.add(curr);
			for(Node n: curr.getNeighbors(this)){
				if(!set.contains(n)){
					n.d = curr.d + terrain[n.y][n.x].getMoveCost(u.getTheClass());
					if(grid[n.y][n.x] != null && grid[n.y][n.x].getParty() != u.getParty()) {
						n.d += 128;
					}
					if(n.d <= u.getStats().mov){
						q.add(n);
					}
				}
			}
		}
		
		
		return set;
	}
	
	/**
	 * Gets the attack range.
	 *
	 * @param u the u
	 * @return the attack range
	 */
	public Set<Node> getAttackRange(Unit u){
		Set<Node> move = getPossibleMoves(u);
		Set<Node> set = new HashSet<Node>();
		Set<Integer> range = u.getTotalWepRange(false);
		for(Node n: move){
			for(int i: range){
				set.addAll(getRange(n, i));
			}
		}
		return set;
	}
	
	/**
	 * Gets the heal range.
	 *
	 * @param u the u
	 * @return the heal range
	 */
	public Set<Node> getHealRange(Unit u){
		Set<Node> move = getPossibleMoves(u);
		Set<Node> set = new HashSet<Node>();
		Set<Integer> range = u.getTotalWepRange(true);
		for(Node n: move){
			set.addAll(getRange(n, range));
		}
		return set;
	}
	
	/**
	 * Gets the range.
	 *
	 * @param start the start
	 * @param range the range
	 * @return the range
	 */
	public Set<Node> getRange(Node start, Collection<Integer> range){
		int[] r = new int[range.size()];
		Iterator<Integer> it = range.iterator();
		for(int i = 0; i < range.size(); i++){
			r[i] = it.next();
		}
		return getRange(start, r);
	}
	
	/**
	 * Gets the range.
	 *
	 * @param start the start
	 * @param range the range
	 * @return the range
	 */
	public Set<Node> getRange(Node start, int... range){
		Set<Node> set = new HashSet<Node>();
		for(int r: range){
			for(int dx = -r; dx <=r; dx++){
				for(int dy = -r; dy <= r; dy++){
					Node n = new Node(start.x + dx, start.y + dy);
					if(n.x < 0 || n.x > width-1 ||n.y < 0 || n.y > height-1){
						continue;
					}
					if(n.distance(start) == r && !set.contains(n)){
						set.add(n);
					}
				}
			}
		}
		return set;
	}

	/**
	 * Gets the path.
	 *
	 * @param goal the goal
	 * @return the path
	 */
	private Path getPath(Node goal) {
		Path path = new Path();
		Node cur = goal;
		do {
			path.add(0,cur);
			cur = cur.parent;
		} while (cur != null);
		return path;
	}

	/**
	 * Heuristic.
	 *
	 * @param a the a
	 * @param b the b
	 * @return the int
	 */
	private int heuristic(Node a, Node b) {
		// Manhattan heuristic is pretty good because no diag mvmt
		return Math.abs(b.x - a.x) + Math.abs(b.y - a.y);
	}

	/**
	 * Gets the distance.
	 *
	 * @param x1 the x1
	 * @param y1 the y1
	 * @param x2 the x2
	 * @param y2 the y2
	 * @return the distance
	 */
	public static int getDistance(int x1, int y1, int x2, int y2) {
		return Math.abs(x1 - x2) + Math.abs(y1 - y2);
	}

	/**
	 * Gets the distance.
	 *
	 * @param a the a
	 * @param b the b
	 * @return the distance
	 */
	public static int getDistance(Unit a, Unit b) {
		return getDistance(a.getXCoord(), a.getYCoord(), b.getXCoord(), b.getYCoord());
	}
}
