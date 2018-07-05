package net.fe.overworldStage;

import static net.fe.overworldStage.Zone.ZoneType.ATTACK_DARK;
import static net.fe.overworldStage.Zone.ZoneType.ATTACK_LIGHT;
import static net.fe.overworldStage.Zone.ZoneType.FOG_LIGHT;
import static net.fe.overworldStage.Zone.ZoneType.HEAL_DARK;
import static net.fe.overworldStage.Zone.ZoneType.HEAL_LIGHT;
import static net.fe.overworldStage.Zone.ZoneType.MOVE_DARK;
import static net.fe.overworldStage.Zone.ZoneType.MOVE_LIGHT;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import org.newdawn.slick.Color;

import chu.engine.Entity;
import chu.engine.Game;
import chu.engine.anim.Renderer;
import chu.engine.anim.Tileset;
import chu.engine.anim.Transform;
import net.fe.FEResources;

// TODO: Auto-generated Javadoc
/**
 * The Class ZoneType.
 */
public class Zone extends Entity {
	
	/** The zone. */
	private Set<Node> zone;
	
	/** The color. */
	private ZoneType type;
	
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
	 * @param type the type
	 */
	public Zone(Set<Node> zone, ZoneType type) {
		super(0,0);
		this.zone = zone;
		this.type = type;
		frame = 0;
		renderDepth = type == FOG_LIGHT ? ClientOverworldStage.FOG_DEPTH : ClientOverworldStage.ZONE_DEPTH;
	}
	
	/* (non-Javadoc)
	 * @see chu.engine.Entity#render()
	 */
	public void render(){
		ClientOverworldStage cs = (ClientOverworldStage)stage;
		Renderer.translate(-cs.camX, -cs.camY);
		Renderer.addClip(0, 0, 368, 240, true);
		
		Color mult;
		if(type == MOVE_DARK || type == ATTACK_DARK || type == HEAL_DARK)
			mult = new Color(1f, 1f, 1f, 0.5f);
		else
			mult = new Color(1f, 1f, 1f, 0.75f);
		Transform t = new Transform();
		t.setColor(mult.multiply(type.color));
		
		int ty = -1; // This needs to be initialized because Java is very good, it will always be defined as all cases are covered
		switch (type) {
			case MOVE_DARK:
			case MOVE_LIGHT:
				ty = 0;
				break;

			case ATTACK_DARK:
			case ATTACK_LIGHT:
				ty = 1;
				break;
				
			case HEAL_DARK:
			case HEAL_LIGHT:
				ty = 2;
				break;
			
			case FOG_LIGHT:
				//FIXME FIXME FIXME FIXME FIXME FIXME FIXME FIXME
				//HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK
				t.setScale(16f/15, 16f/15);
				ty = 3;
				break;
		}
		for(Node n: zone)
			tiles.renderTransformed(n.x * 16, n.y * 16, frame, ty, renderDepth, t);
		
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
	
	public void setNodes(Set<Node> nodes) {
		this.zone = nodes;
	}
	
	/**
	 * Minus.
	 *
	 * @param a the a
	 * @param b the b
	 * @return the zone
	 */
	public Zone minus(Zone b){
		Set<Node> nodes = new HashSet<Node>(getNodes());
		nodes.removeAll(b.getNodes());
		return new Zone(nodes, type);
	}
	
	public Zone add(Zone b) {
		Set<Node> nodes = new HashSet<Node>(getNodes());
		nodes.addAll(b.getNodes());
		return new Zone(nodes, type);
	}
	
	public void add(Node node) {
		zone.add(node);
	}
	
	public static Zone all(Grid grid, ZoneType fogLight) {
		Set<Node> nodes = new HashSet<Node>();
		for(int i = 0; i < grid.width; i++)
			for(int j = 0; j < grid.height; j++)
				nodes.add(new Node(i, j));
		return new Zone(nodes, fogLight);
	}
	
	public Zone filter(Predicate<Node> predicate) {
		Set<Node> nodes = new HashSet<Node>(getNodes());
		for(Node node : nodes)
			if(predicate.test(node))
				nodes.remove(node);
		return new Zone(nodes, type);
	}
	
	public static Set<Node> all(Grid grid) {
		Set<Node> nodes = new HashSet<Node>();
		for(int i = 0; i < grid.width; i++)
			for(int j = 0; j < grid.height; j++)
				nodes.add(new Node(i, j));
		return nodes;
	}
	
	public static class Fog extends Zone implements DoNotDestroy {

		public Fog(Set<Node> zone) {
			super(zone, FOG_LIGHT);
		}
	}
	
	public static enum ZoneType {
		MOVE_DARK(0xC04444FF),
		ATTACK_DARK(0xC0FF4444),
		HEAL_DARK(0xC044FF44),
		MOVE_LIGHT(0xC08888FF),
		ATTACK_LIGHT(0xC0FF8888),
		HEAL_LIGHT(0xC088FF88),
		FOG_LIGHT(0x80FFFFFF);
		
		public final Color color;
		
		private ZoneType(int color) {
			this.color = new Color(color);
		}
	}
}
