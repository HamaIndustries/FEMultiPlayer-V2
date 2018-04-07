package net.fe.overworldStage;

import static net.fe.overworldStage.Zone.RangeIndicator.RangeType.ATTACK_DARK;
import static net.fe.overworldStage.Zone.RangeIndicator.RangeType.ATTACK_LIGHT;
import static net.fe.overworldStage.Zone.RangeIndicator.RangeType.HEAL_DARK;
import static net.fe.overworldStage.Zone.RangeIndicator.RangeType.HEAL_LIGHT;
import static net.fe.overworldStage.Zone.RangeIndicator.RangeType.MOVE_DARK;
import static net.fe.overworldStage.Zone.RangeIndicator.RangeType.MOVE_LIGHT;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import org.newdawn.slick.Color;

import chu.engine.Entity;
import chu.engine.Game;
import chu.engine.anim.Renderer;
import chu.engine.anim.Tileset;
import chu.engine.anim.Transform;
import net.fe.FEMultiplayer;
import net.fe.FEResources;
import net.fe.Party;
import net.fe.overworldStage.ClientOverworldStage.FogOption;
import net.fe.unit.Unit;

// TODO: Auto-generated Javadoc
/**
 * An Entity that draws an overlay over a set of tiles
 */
public abstract class Zone extends Entity {
	
	/** The zone. */
	private Set<Node> zone;
	
	/**
	 * Instantiates a new zone.
	 *
	 * @param zone the zone
	 * @param type the type
	 */
	public Zone(Set<Node> zone) {
		super(0,0);
		this.zone = zone;
	}
	
	/* (non-Javadoc)
	 * @see chu.engine.Entity#beginStep()
	 */
	public void beginStep() {
		super.beginStep();
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
	
	public boolean contains(Node node) {
		return zone.contains(node);
	}
	
	public static Set<Node> all(Grid grid) {
		Set<Node> nodes = new HashSet<Node>();
		for(int i = 0; i < grid.width; i++)
			for(int j = 0; j < grid.height; j++)
				nodes.add(new Node(i, j));
		return nodes;
	}
	
	public static final class RangeIndicator extends Zone {
		private final RangeType type;
		private static int frame;
		private static float timer;
	
		/** The texture that holds the appearance of a range indicator */
		private static Tileset tiles = new Tileset(FEResources.getTexture("zone_colors"), 15, 15);
		
		public RangeIndicator(Set<Node> zone, RangeType type) {
			super(zone);
			this.type = type;
			frame = 0;
			renderDepth = ClientOverworldStage.ZONE_DEPTH;
		}
		
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
			}
			for(Node n: this.getNodes())
				tiles.renderTransformed(n.x * 16, n.y * 16, frame, ty, renderDepth, t);
			
			Renderer.removeClip();
			Renderer.translate(cs.camX, cs.camY);
		}
		
		@Override public void beginStep() {
			super.beginStep();
			timer += Game.getDeltaSeconds();
			if(timer > 0.3f) {
				frame++;
				timer = 0;
				if(frame >= 16) frame = 0;
			}
		}
		
		/**
		 * Return a new RangeIndicator with the same RangeType as `this`, but
		 * with a set of nodes which includes the nodes that `this` contains and
		 * which `b` does not contain
		 */
		public RangeIndicator minus(Zone b){
			Set<Node> nodes = new HashSet<Node>(getNodes());
			nodes.removeAll(b.getNodes());
			return new RangeIndicator(nodes, type);
		}
		
		public static enum RangeType {
			MOVE_DARK(0xC04444FF),
			ATTACK_DARK(0xC0FF4444),
			HEAL_DARK(0xC044FF44),
			MOVE_LIGHT(0xC08888FF),
			ATTACK_LIGHT(0xC0FF8888),
			HEAL_LIGHT(0xC088FF88);
			
			public final Color color;
			
			private RangeType(int color) {
				this.color = new Color(color);
			}
		}
	}
	
	public static class Fog extends Zone implements DoNotDestroy {

		public Fog(Set<Node> zone) {
			super(zone);
			super.renderDepth = ClientOverworldStage.FOG_DEPTH;
		}
		
		public void render(){
			ClientOverworldStage cs = (ClientOverworldStage)stage;
			Renderer.translate(-cs.camX, -cs.camY);
			Renderer.addClip(0, 0, 368, 240, true);
			
			Color color = FEResources.getFogColor().color;
			
			for(Node n: this.getNodes()) {
				Renderer.drawRectangle(n.x * 16, n.y * 16, (1 + n.x) * 16, (1 + n.y) * 16, renderDepth, color);
			}
			
			Renderer.removeClip();
			Renderer.translate(cs.camX, cs.camY);
		}
		
		public static Set<Node> getUnitsPerception(Set<Unit> units) {
			Set<Node> nodes = new HashSet<Node>();
			units.forEach(unit -> nodes.addAll(getUnitPerception(unit)));
			return nodes;
		}
		
		public static Set<Node> getPartyPerception(Party party) {
			return getPartyPerception(party, new HashSet<Party>());
		}
		
		//This is massively overkill because the party functionality is not even used currently
		private static Set<Node> getPartyPerception(Party party, Set<Party> visitedParties) {
			Set<Node> nodes = new HashSet<Node>();
			visitedParties.add(party);
			
			//Add the perception of the units of the party
			party.getUnits().forEach(unit -> nodes.addAll(getUnitPerception(unit)));
			
			//Add the perception of the allies of the party
			party.getAllies().forEach(ally -> {
				if(!visitedParties.contains(ally))
					nodes.addAll(getPartyPerception(ally, visitedParties));
			});
			
			return nodes;
		}
		
		public static Set<Node> getUnitPerception(Unit unit) {
			Set<Node> nodes = new HashSet<Node>();
			int sight = FEMultiplayer.getSession().getSight(unit);
			if(unit.getHp() > 0 && !unit.isRescued())
				for(int i = 0; i <= sight; i++)
					for(int j = 0; j <= sight - i; j++) {
						nodes.add(new Node(unit.getOrigX() + i, unit.getOrigY() + j));
						nodes.add(new Node(unit.getOrigX() + i, unit.getOrigY() - j));
						nodes.add(new Node(unit.getOrigX() - i, unit.getOrigY() + j));
						nodes.add(new Node(unit.getOrigX() - i, unit.getOrigY() - j));
					}
			return nodes;
		}

		
	}
}
