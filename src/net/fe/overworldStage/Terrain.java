package net.fe.overworldStage;

import java.util.List;
import static java.util.Collections.unmodifiableList;

import net.fe.Player;
import net.fe.unit.Class;
import net.fe.unit.Statistics;
import net.fe.unit.Unit;
import net.fe.unit.WeaponFactory;
import chu.engine.Entity;

// TODO: Auto-generated Javadoc
/**
 * The Enum Terrain.
 */
public enum Terrain {
	
	/** The plain. */
	PLAIN(1,0,0,0,0),
	
	/** The path. */
	PATH(1,0,0,0,0),
	
	/** The forest. */
	FOREST(2,1,1,20,0),
	
	/** The floor. */
	FLOOR(1,0,0,0,0),
	
	/** The pillar. */
	PILLAR(2,1,1,20,0),
	
	/** The mountain. */
	MOUNTAIN(4,2,2,30,0),
	
	/** The village. */
	VILLAGE(1,0,0,10,0),
	
	/** The peak. */
	PEAK(127,2,2,40,0),
	
	/** The fort. */
	FORT(2,1,1,15,10),
	
	/** The sea. */
	SEA(127,0,0,10,0),
	
	/** The desert. */
	DESERT(2,0,0,5,0),
	
	/** The wall. */
	WALL(127,0,0,0,0),
	
	/** The fence. */
	FENCE(127,0,0,0,0),
	
	/** The none. */
	NONE(127,0,0,0,0),
	
	/** The cliff. */
	CLIFF(127,0,0,0,0),
	
	/** The throne. */
	THRONE(1,3,5,30,10),
	
	/** The hill. */
	HILL(4,2,2,30,0),
	
	/** The house. */
	HOUSE(127,0,0,10,0),
	
	UNKNOWN(1, -1, -1, -1, -1);

	/** The base move cost. */
	private final int baseMoveCost;
	
	/** The avoid bonus. */
	private final int avoidBonus;
	
	/** The defense bonus. */
	private final Statistics defenseBonus;
	
	/** The health bonus. */
	public final int healthBonus;
	
	/** The triggers. */
	private final List<TerrainTrigger> triggers;
	
	

	/**
	 * Instantiates a new terrain.
	 *
	 * @param baseMoveCost the base move cost
	 * @param def the DEF gained by a unit while standing on this space
	 * @param res the RES gained by a unit while standing on this space
	 * @param avo the AVOID gained by a unit while standing on this space
	 * @param health the health
	 */
	Terrain(int baseMoveCost, int def, int res, int avo, int health) {
		this.baseMoveCost = baseMoveCost;
		avoidBonus = avo;
		defenseBonus = new Statistics().copy("Def", def).copy("Res", res);
		healthBonus = health;
		List<TerrainTrigger> tmpTriggers = new java.util.ArrayList<>();
		if(healthBonus > 0){
			tmpTriggers.add(new Healing(healthBonus));
		}
		triggers = unmodifiableList(tmpTriggers);
	}

	/**
	 * Gets the move cost.
	 *
	 * @param c the c
	 * @return the move cost
	 */
	public int getMoveCost(Class c) {
		if (c == null)
			return baseMoveCost;
		String name = c.name;
		if(WeaponFactory.fliers.contains(name)){
			if(this == WALL)
				return 127;
			return 1;
		}
		
		else if (this == SEA) {
			if (name.equals("Berserker")) {
				return 2;
			}
		}
		
		else if (this == FOREST || this == PILLAR) {
			if (name.equals("Sniper") || name.equals("Paladin")) {
				return 3;
			}
		}
		
		else if (this == DESERT) {
			if (name.equals("Sniper") || name.equals("General")) {
				return 3;
			}
			else if (name.equals("Paladin")) {
				return 4;
			}
		}
		
		else if (this == MOUNTAIN || this == HILL) {
			if (name.equals("Berserker")
					|| name.equals("Hero")
					|| name.equals("Sniper")
					|| name.equals("Swordmaster")) {
				return 3;
			}
			else if(name.equals("Paladin")) {
				return 6;
			}
		}
		
		return baseMoveCost;
	}
	
	/**
	 * Gets the triggers.
	 *
	 * @return the triggers
	 */
	public List<TerrainTrigger> getTriggers(){
		return triggers;
	}
	
	/**
	 * Gets the avoid bonus.
	 *
	 * @param u the unit to check, or null if a generic answer
	 * @return the avoid bonus
	 */
	public int getAvoidBonus(Unit u) {
		if(u == null) return avoidBonus;
		if(WeaponFactory.fliers.contains(u.getTheClass().name)) return 0;
		return avoidBonus;
	}
	
	/**
	 * Gets the defense bonus.
	 *
	 * @param u the unit to check, or null if a generic answer
	 * @return the defense bonus
	 */
	public Statistics getDefenseBonus(Unit u) {
		if(u == null) return defenseBonus;
		if(WeaponFactory.fliers.contains(u.getTheClass().name)) return new Statistics();
		return defenseBonus;
	}
	
	/**
	 * The Class Healing.
	 */
	public final class Healing extends TerrainTrigger{
		
		/** The percent. */
		private int percent;
		
		/** The amount. */
		private int amount;
		
		/**
		 * Instantiates a new healing.
		 *
		 * @param percent the percent
		 */
		public Healing(int percent){
			super(true);
			this.percent = percent;
		}
		
		/* (non-Javadoc)
		 * @see net.fe.overworldStage.TerrainTrigger#attempt(net.fe.overworldStage.OverworldStage, int, int, net.fe.Player)
		 */
		public boolean attempt(OverworldStage g, int x, int y, Player turnPlayer){
			Unit u = g.getUnit(x, y);
			return u != null && 
					u.getPartyColor().equals(turnPlayer.getParty().getColor()) &&
					u.getStats().maxHp > u.getHp();
		}
		
		/* (non-Javadoc)
		 * @see net.fe.overworldStage.TerrainTrigger#startOfTurn(net.fe.overworldStage.OverworldStage, int, int)
		 */
		public void startOfTurn(OverworldStage g, int x, int y){
			Unit u = g.getUnit(x, y);
			amount = Math.min(u.getStats().maxHp*percent/100, u.getStats().maxHp - u.getHp());
			if(u != null)
				u.setHp(u.getHp() + amount);
		}
		
		/* (non-Javadoc)
		 * @see net.fe.overworldStage.TerrainTrigger#getAnimation(net.fe.overworldStage.OverworldStage, int, int)
		 */
		public Entity getAnimation(OverworldStage g, int x, int y){
			Unit u = g.getVisibleUnit(x, y);
			if (u == null)
				//Returns a "do nothing" entity because the unit is not visible.
				return new Entity(0, 0) {};
			else
				return new Healthbar(u, u.getHp(), u.getHp() + amount, (ClientOverworldStage) g){
					@Override
					public void done() {
						destroy();
					}
				};
		}
	}
}
