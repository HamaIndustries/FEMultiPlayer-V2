package net.fe.overworldStage;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import net.fe.FEResources;
import net.fe.Session;

import org.newdawn.slick.Color;

import chu.engine.Entity;
import chu.engine.Game;
import chu.engine.anim.Renderer;
import chu.engine.anim.Tileset;
import chu.engine.anim.Transform;

// TODO: Auto-generated Javadoc
/**
 * The Class Zone.
 */
public class Zone extends Entity {
	
	/** The zone. */
	private Set<Node> zone;
	
	/** The color. */
	private Color color;
	
	/** The move dark. */
	public static Color MOVE_DARK = new Color(0xC04444FF);
	
	/** The attack dark. */
	public static Color ATTACK_DARK = new Color(0xC0FF4444);
	
	/** The heal dark. */
	public static Color HEAL_DARK = new Color(0xC044FF44);
	
	/** The move light. */
	public static Color MOVE_LIGHT = new Color(0xC08888FF);
	
	/** The attack light. */
	public static Color ATTACK_LIGHT = new Color(0xC0FF8888);
	
	/** The heal light. */
	public static Color HEAL_LIGHT = new Color(0xC088FF88);
	
	public static Color FOG_LIGHT = new Color(0x80FFFFFF);
	
	/** The frame. */
	private static int frame;
	
	/** The timer. */
	private static float timer;
	
	/** The tiles. */
	private static Tileset tiles = new Tileset(FEResources.getTexture("zone_colors"), 15, 15);
	
	/**
	 * Instantiates a new zone.
	 *
	 * @param zone the zone
	 * @param c the c
	 */
	public Zone(Set<Node> zone, Color c) {
		super(0,0);
		this.zone = zone;
		this.color = c;
		frame = 0;
		renderDepth = ClientOverworldStage.ZONE_DEPTH;
	}
	
	/* (non-Javadoc)
	 * @see chu.engine.Entity#render()
	 */
	public void render(){
		ClientOverworldStage cs = (ClientOverworldStage)stage;
		Renderer.translate(-cs.camX, -cs.camY);
		Renderer.addClip(0, 0, 368, 240, true);
		
		for(Node n: zone){
			int x = n.x*16;
			int y = n.y*16;
			Color mult;
			if(color == MOVE_DARK || color == ATTACK_DARK || color == HEAL_DARK)
				mult = new Color(1f, 1f, 1f, 0.5f);
			else
				mult = new Color(1f, 1f, 1f, 0.75f);
			Transform t = new Transform();
			t.setColor(mult.multiply(color));
			if(color == MOVE_DARK || color == MOVE_LIGHT) {
				tiles.renderTransformed(x, y, frame, 0, renderDepth, t);
			} else if(color == ATTACK_DARK || color == ATTACK_LIGHT) {
				tiles.renderTransformed(x, y, frame, 1, renderDepth, t);
			} else if(color == HEAL_DARK || color == HEAL_LIGHT) {
				tiles.renderTransformed(x, y, frame, 2, renderDepth, t);
			} else if (color == FOG_LIGHT) {
				//FIXME FIXME FIXME FIXME FIXME FIXME FIXME FIXME
				//HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK
				t.setScale(16f/15, 16f/15);
				tiles.renderTransformed(x, y, frame, 3, renderDepth, t);
				
			}
		}
		
		Renderer.removeClip();
		Renderer.translate(cs.camX, cs.camY);
	}
	
	/* (non-Javadoc)
	 * @see chu.engine.Entity#beginStep()
	 */
	public void beginStep() {
		timer += Game.getDeltaSeconds();
		if(timer > 0.3f) {
			frame++;
			timer = 0;
			if(frame >= 16) frame = 0;
		}
	}
	
	/**
	 * Gets the nodes.
	 *
	 * @return the nodes
	 */
	public Set<Node> getNodes(){
		return zone;
	}
	
	/**
	 * Minus.
	 *
	 * @param a the a
	 * @param b the b
	 * @return the zone
	 */
	public static Zone minus(Zone a, Zone b){
		Set<Node> nodes = new HashSet<Node>(a.getNodes());
		nodes.removeAll(b.getNodes());
		return new Zone(nodes, a.color);
	}
	
	public static Zone add(Zone a, Zone b) {
		Set<Node> nodes = new HashSet<Node>(a.getNodes());
		nodes.addAll(b.getNodes());
		return new Zone(nodes, a.color);
	}
	
	public static Zone all(Grid grid, Color c) {
		Set<Node> nodes = new HashSet<Node>();
		for(int i = 0; i < grid.width; i++)
			for(int j = 0; j < grid.height; j++)
				nodes.add(new Node(i, j));
		return new Zone(nodes, c);
	}
	
	public static Zone filter(Zone a, Predicate<Node> predicate) {
		Set<Node> nodes = new HashSet<Node>(a.getNodes());
		for(Node node : nodes)
			if(predicate.test(node))
				nodes.remove(node);
		return new Zone(nodes, a.color);
	}
}
