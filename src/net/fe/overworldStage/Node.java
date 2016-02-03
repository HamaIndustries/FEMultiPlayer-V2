package net.fe.overworldStage;

import java.util.HashSet;
import java.util.Set;

// TODO: Auto-generated Javadoc
/**
 * The Class Node.
 */
public class Node{
	
	/** The d. */
	public int x, y, f, g, h, d;
	
	/** The parent. */
	Node parent;
	
	/**
	 * Instantiates a new node.
	 *
	 * @param x the x
	 * @param y the y
	 */
	public Node(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object other) {
		if(other == null) return false;
		if(other == this) return true;
		if(!(other instanceof Node)) return false;
		Node node = (Node) other;
		if(this.x == node.x && this.y == node.y)
			return true;
		return false;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode(){
		return x+y;
	}
	
	/**
	 * Distance.
	 *
	 * @param other the other
	 * @return the int
	 */
	public int distance(Node other){
		return Math.abs(other.x - this.x) + Math.abs(other.y - this.y);
	}
	
	/**
	 * Gets the neighbors.
	 *
	 * @param g the g
	 * @return the neighbors
	 */
	public Set<Node> getNeighbors(Grid g) {
		Set<Node> neighbors = new HashSet<Node>(4);
		if(x < g.width-1)
			neighbors.add(new Node(x+1, y));
		if(y < g.height-1)
			neighbors.add(new Node(x, y+1));
		if(x > 0)
			neighbors.add(new Node(x-1, y));
		if(y > 0)
			neighbors.add(new Node(x, y-1));
		return neighbors;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return "(" + x + ", " + y + ")";
	}
}
