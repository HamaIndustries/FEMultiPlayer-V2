package net.fe.unit;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

import chu.engine.Game;
import chu.engine.GriddedEntity;
import chu.engine.anim.Renderer;
import chu.engine.anim.ShaderArgs;
import chu.engine.anim.Transform;
import net.fe.FEMultiplayer;
import net.fe.FEResources;
import net.fe.PaletteSwapper;
import net.fe.Party;
import net.fe.fightStage.CombatTrigger;
import net.fe.overworldStage.ClientOverworldStage;
import net.fe.overworldStage.Corpse;
import net.fe.overworldStage.DoNotDestroy;
import net.fe.overworldStage.Grid;
import net.fe.overworldStage.Node;
import net.fe.overworldStage.OverworldStage;
import net.fe.overworldStage.Path;
import net.fe.overworldStage.Terrain;
import net.fe.overworldStage.Zone;

// TODO: Auto-generated Javadoc
/**
 * The Class Unit.
 */
public final class Unit extends GriddedEntity implements Serializable, DoNotDestroy{
	
	/** The bases. */
	public final Statistics bases;
	
	/** The growths. */
	public final Statistics growths;
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -5101031417704315547L;
	
	/** The unit's current level */
	private int level;
	
	/** The unit's current stats. */
	private Statistics stats;
	
	/** The skills. */
	private final ArrayList<CombatTrigger> skills;
	
	/** The hp. */
	private int hp;
	
	/** A resource that is expended to use trigger skills */
	private int skillCharge;
	
	/** The clazz. */
	private final Class clazz;
	
	/** The gender. */
	public final char gender;
	
	/** True if the unit does not have an equipped weapon, even if it is possible for it to do so. */
	private boolean isUnequipped;
	
	/** The inventory. */
	private final ArrayList<Item> inventory;
	
	/** The name. */
	public final String name;
	
	/** The team. */
	private Party team;
	
	/** The temp mods. */
	private transient HashMap<String, Integer> tempMods;
	
	/** The battle contributions record */
	private BattleStats battleStats;
	
	/** The assist. */
	private transient Set<Unit> assist;
	
	/** The rescued unit. */
	private transient Unit rescuedUnit;
	
	/** The moved. */
	private transient boolean moved;
	
	/** The path. */
	private transient Path path;
	
	/** The r y. */
	private transient float rX, rY;
	
	/** The callback. */
	private transient Runnable callback;
	
	/** The rescued. */
	private boolean rescued;
	
	/** The counter. */
	private float counter;

	/** The orig y. */
	private int origX, origY;

	private Node[] move;
	
	/** The Constant MAP_ANIM_SPEED. */
	public static final float MAP_ANIM_SPEED = 0.2f;
	
	/** The Constant MOVE_SPEED. */
	public static final int MOVE_SPEED = 250;
	
	public static final int MAX_SKILL_CHARGE = 50;
	
	/** The rescue. */
	public static Texture rescue;
	
	static {
		if(Game.glContextExists())
			rescue = FEResources.getTexture("rescue");
	}

	/**
	 * Instantiates a new unit.
	 *
	 * @param name the name
	 * @param c the c
	 * @param gender the gender
	 * @param bases the bases
	 * @param growths the growths
	 */
	public Unit(String name, Class c, char gender, Statistics bases,
			Statistics growths) {
		super(0, 0);
		this.name = name;
		this.clazz = c;
		this.bases = bases;
		this.growths = growths;
		this.gender = gender;
		this.isUnequipped = true;
		inventory = new ArrayList<Item>();
		tempMods = new HashMap<String, Integer>();
		assist = new HashSet<Unit>();
		skills = new ArrayList<CombatTrigger>();
		battleStats = new BattleStats();
		this.setLevel(1);
		fillHp();
		
		renderDepth = ClientOverworldStage.UNIT_DEPTH;
	}
	
	/**
	 * Load map sprites.
	 */
	public void loadMapSprites(){
		sprite.addAnimation("IDLE", new MapAnimation(functionalClassName() + 
				"_map_idle", false));
		sprite.addAnimation("SELECTED", new MapAnimation(functionalClassName() + 
				"_map_selected", false));
		sprite.addAnimation("LEFT", new MapAnimation(functionalClassName() + 
				"_map_side", true));
		sprite.addAnimation("RIGHT", new MapAnimation(functionalClassName() + 
				"_map_side", true));
		sprite.addAnimation("UP", new MapAnimation(functionalClassName() + 
				"_map_up", true));
		sprite.addAnimation("DOWN", new MapAnimation(functionalClassName() + 
				"_map_down", true));
		sprite.setAnimation("IDLE");
	}
	
	/**
	 * Read object.
	 *
	 * @param in the in
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ClassNotFoundException the class not found exception
	 */
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        tempMods = new HashMap<String, Integer>();
        assist = new HashSet<Unit>();
        battleStats = new BattleStats();
    }
	
	/**
	 * Functional class name.
	 *
	 * @return the string
	 */
	public String functionalClassName(){
		String prefix = clazz.name;
		if(prefix.equals("Lord")){
			prefix = name;
		}
		if(gender != '-'){
			prefix += gender;
		}
		return prefix.toLowerCase();
	}
	
	/**
	 * No gender name.
	 *
	 * @return the string
	 */
	public String noGenderName() {
		String prefix = clazz.name;
		if(prefix.equals("Lord")){
			prefix = name;
		}
		return prefix;
	}

	/**
	 * Move.
	 *
	 * @param p the p
	 * @param callback the callback
	 */
	public void move(Path p, Runnable callback) {
		this.path = p.getCopy();
		move = p.getAllNodes();
		this.callback = callback;
	}
	
	/**
	 * Rescue.
	 *
	 * @param u the u
	 */
	public void rescue(Unit u){
		final int oldX = u.xcoord;
		final int oldY = u.ycoord;
		rescuedUnit = u;
		rescuedUnit.rescued = true;
		final OverworldStage grid = (OverworldStage) stage;
		Path p = new Path();
		p.add(new Node(this.xcoord, this.ycoord));
		rescuedUnit.move(p, new Runnable(){
			@Override
			public void run() {
				rescuedUnit.xcoord = oldX;
				rescuedUnit.ycoord = oldY;
				grid.removeUnit(rescuedUnit);
			}
		});
	}

	public static boolean isRider(Unit u){
		List<String> mounts = WeaponFactory.riding;
		return mounts.contains(u.name) || mounts.contains(u.clazz.name);
	}
	
	public static boolean isRider(Class c){
		List<String> mounts = WeaponFactory.riding;
		return mounts.contains(c.name);
	}
	
	public static boolean isRider(String n){
		List<String> mounts = WeaponFactory.riding;
		return mounts.contains(n);
	}
	
	public boolean canRescue(Unit u){
		if(u == null)
			return false;
		return this.getStats().aid >= u.getStats().con;
	}

	
	/**
	 * Drop.
	 *
	 * @param x the x
	 * @param y the y
	 */
	public void drop(int x, int y){
		if(rescuedUnit == null) throw new IllegalStateException("rescuedUnit == null");
		rescuedUnit.rescued = false;
		rescuedUnit.setMoved(true);
		final OverworldStage grid = (OverworldStage) stage;
		grid.addUnit(rescuedUnit, x, y);
		rescuedUnit.rX = this.x - x * 16;
		rescuedUnit.rY = this.y - y * 16;
		rescuedUnit = null;
	}
	
	
	/**
	 * Give.
	 *
	 * @param u the u
	 */
	public void give(Unit u){
		if(rescuedUnit == null) throw new IllegalStateException("rescuedUnit == null");
		if(u.rescuedUnit() != null) throw new IllegalStateException(u.name + ".rescuedUnit() != null");
		u.rescuedUnit = rescuedUnit;
		rescuedUnit = null;
	}
	
	
	
	/* (non-Javadoc)
	 * @see chu.engine.GriddedEntity#beginStep()
	 */
	public void beginStep(){
		super.beginStep();
		if(Game.glContextExists() && !sprite.hasAnimation("IDLE")) {
    		loadMapSprites();
        }
		if(path != null){
			String name;
			if(rX > 0) 		name = "left";
			else if(rX < 0) name = "right";
			else if(rY > 0) name = "up";
			else 			name = "down";
			sprite.setAnimation(name);
		}
		renderDepth = calcRenderDepth();
	}
	
	/**
	 * Calc render depth.
	 *
	 * @return the float
	 */
	private float calcRenderDepth(){
		float depth = ClientOverworldStage.UNIT_DEPTH;
		if(rescued){
			return depth-0.0001f;
		}
		float highlightDiff = (ClientOverworldStage.UNIT_DEPTH - ClientOverworldStage.UNIT_MAX_DEPTH)/2;
		Grid g = ((ClientOverworldStage) stage).grid;
		float yDiff = highlightDiff/g.width;
		float xDiff = yDiff/g.height;
		
		if(path!=null) depth -= highlightDiff;
		if(((ClientOverworldStage) stage).getHoveredUnit() == this) depth -= highlightDiff;
		depth -= ycoord*yDiff;
		depth -= (g.width-xcoord)*xDiff;
		return depth;
	}

	/* (non-Javadoc)
	 * @see chu.engine.Entity#onStep()
	 */
	public void onStep() {
		super.onStep();
		float rXOld = rX;
		float rYOld = rY;
		rX = rX - Math.signum(rX) * Game.getDeltaSeconds() * 250;
		rY = rY - Math.signum(rY) * Game.getDeltaSeconds() * 250;
		if(rXOld * rX < 0 || rYOld * rY < 0 || (rXOld == rX && rYOld == rY)){
			rX = 0;
			rY = 0;
			if (path != null) {
				if (path.size() == 0) {
					// We made it to destination					
					path = null;
					callback.run();
				} else {
					Node next = path.removeFirst();
					rX = -(next.x - xcoord) * 16;
					rY = -(next.y - ycoord) * 16;
					xcoord = next.x;
					ycoord = next.y;
					x = xcoord * 16;
					y = ycoord * 16;
				}
			}
		}		
	}

	/**
	 * Creates a clone of this unit. Transient characteristics might not be
	 * carried over to the new object. Other mutable characteristics are deep-cloned.
	 */
	public Unit getCopy() {
		Unit copy = new Unit(name, clazz, gender, bases, growths);
		copy.setLevel(this.level);
		for (Item i : inventory) {
			copy.addToInventory(i.getCopy());
		}
		this.skills.forEach((s) -> copy.addSkill(s.getCopy()));
		return copy;
	}

	/* (non-Javadoc)
	 * @see chu.engine.Entity#render()
	 */
	public void render() {
		
		ClientOverworldStage cs = (ClientOverworldStage)stage;
		
		if(!isVisible(cs))
			return;

		Renderer.translate(-cs.camX, -cs.camY);
		Renderer.addClip(0, 0, 368, 240, true);
		
		if(FEResources.hasTexture(functionalClassName().toLowerCase() + "_map_idle")){
			Transform t = new Transform();
			if(sprite.getAnimationName().equals("RIGHT")){
				t.flipHorizontal();
				t.setTranslation(14, 0); //Why do we have to do this?
			}
			Color mod = new Color(1.0f, 1.0f, 1.0f, 1.0f);
			t.setColor(mod);
			if(moved) {
				sprite.render(x+1+rX, y+1+rY, renderDepth, t, new ShaderArgs("greyscale"));
			} else {
				ShaderArgs args = PaletteSwapper.setup(this);
				sprite.render(x+1+rX, y+1+rY, renderDepth, t, args);
			}
		} else {
			Color c = !moved ? new Color(getPartyColor()) : new Color(128, 128, 128);
			Renderer.drawRectangle(x + 1 + rX, y + 1 + rY, x + 14 + rX,
					y + 14 + rY, ClientOverworldStage.UNIT_DEPTH, c);
			Renderer.drawString("default_med",
					name.charAt(0) + "" + name.charAt(1), x + 2 + rX, y + 1 + rY,
					ClientOverworldStage.UNIT_DEPTH);
			
		}
		if(rescuedUnit!=null){
			counter+=Game.getDeltaSeconds();
			counter%=1;
			if(counter > 0.5){
				Renderer.render(rescue, 
						0, 0, 1, 1, x+9, y+7, x+9+8, y+7+8, renderDepth);
			}
		}
		
		Renderer.removeClip();
		Renderer.translate(cs.camX, cs.camY);
	}

	
	
	//Skills
	
	/**
	 * Adds the skill.
	 *
	 * @param t the t
	 */
	public void addSkill(CombatTrigger t) {
		skills.add(t);
	}
	
	/**
	 * Gets the attack anims.
	 *
	 * @return the attack anims
	 */
	public List<String> getAttackAnims(){
		ArrayList<String> ans = new ArrayList<String>();
		ans.add("attack");
		ans.add("critical");
		for(CombatTrigger skill: getTriggers()){
			for(String a: skill.attackAnims){
				ans.add(a);
			}
		}
		return ans;
	}

	// Inventory

	/**
	 * Gets the inventory.
	 *
	 * @return the inventory
	 */
	public List<Item> getInventory() {
		return inventory;
	}
	
	/**
	 * Find item.
	 *
	 * @param i the i
	 * @return the int
	 */
	public int findItem(Item i){
		return inventory.indexOf(i);
	}
	
	/**
	 * Removes the from inventory.
	 *
	 * @param item the item
	 */
	public void removeFromInventory(Item item){
		inventory.remove(item);
	}
	
	/**
	 * Adds the to inventory.
	 *
	 * @param item the item
	 */
	public void addToInventory(Item item) {
		if(inventory.size() < 4)
			inventory.add(item);
	}
	
	/**
	 * Gets the total wep range.
	 *
	 * @param staff the staff
	 * @return the total wep range
	 */
	public Set<Integer> getTotalWepRange(boolean staff) {
		Set<Integer> range = new HashSet<Integer>();
		for (Item i : getInventory()) {
			if (!(i instanceof Weapon))
				continue;
			Weapon w = (Weapon) i;
			if (staff == (w.type == Weapon.Type.STAFF) && equippable(w))
				range.addAll(w.range.apply(this.getStats()));
		}
		return range;
	}
	
	/**
	 * Equips the specified item.
	 *
	 * The assumption is that w is currently in this unit's inventory.
	 * The weapon will pop into existence otherwise.
	 * 
	 * @param w the weapon
	 */
	public void equip(Weapon w) {
		if (equippable(w)) {
			this.isUnequipped = false;
			inventory.remove(w);
			inventory.add(0, w);
		}
	}
	
	/**
	 * Equip the item in the nth slot.
	 *
	 * @param i the index of the item to equip 
	 * @throws ClassCastException if the item is not a Weapon 
	 */
	// For use in command message processing only
	public void equip(int i) {
		Weapon w = (Weapon)inventory.get(i);
		if (equippable(w)) {
			this.isUnequipped = false;
			inventory.remove(w);
			inventory.add(0, w);
		}
	}
	
	/**
	 * Unequip.
	 */
	public void unequip(){
		this.isUnequipped = true;
	}

	/**
	 * Equippable.
	 *
	 * @param w the w
	 * @return true, if successful
	 */
	public boolean equippable(Weapon w) {
		if(w.pref!= null){
			return name.equals(w.pref);
		}
		return clazz.usableWeapon.contains(w.type);

	}

	/**
	 * Equippable weapons.
	 *
	 * @param range the range
	 * @return the array list
	 */
	public ArrayList<Weapon> equippableWeapons(int range) {
		ArrayList<Weapon> weps = new ArrayList<Weapon>();
		for (Item i : inventory) {
			if (i instanceof Weapon) {
				Weapon w = (Weapon) i;
				if (equippable(w) && w.type != Weapon.Type.STAFF
						&& w.range.apply(this.getStats()).contains(range)) {
					weps.add(w);
				}
			}
		}
		return weps;
	}
	
	/**
	 * Equippable staves.
	 *
	 * @param range the range
	 * @return the array list
	 */
	public ArrayList<Weapon> equippableStaves(int range) {
		ArrayList<Weapon> weps = new ArrayList<Weapon>();
		for (Item i : inventory) {
			if (i instanceof Weapon) {
				Weapon w = (Weapon) i;
				if (equippable(w) && w.type == Weapon.Type.STAFF
						&& w.range.apply(this.getStats()).contains(range)) {
					weps.add(w);
				}
			}
		}
		return weps;
	}
	
	/**
	 * Initialize equipment.
	 */
	public void initializeEquipment(){
		for(Item it: inventory){
			if(it instanceof Weapon){
				if(equippable((Weapon)it)){
					equip((Weapon) it);
					break;
				}
			}
		}
	}
	
	/**
	 * Returns an Command that, if executed, would perform the same action as a call to #reEquip()
	 */
	public net.fe.network.command.Command reEquipCommand() {
		for (int i = 0; i < inventory.size(); i++) {
			Item it = inventory.get(i);
			if (it instanceof Weapon) {
				Weapon w = (Weapon) it;
				if (equippable(w)) {
					return new net.fe.network.command.EquipCommand(new UnitIdentifier(this), i);
				}
			}
		}
		return new net.fe.network.command.WaitCommand(); /* a not-null no-op */
	}
	
	/**
	 * Remove any currently equipped weapon, then equip the top-most weapon eligible for equipping.
	 */
	public void reEquip(){
		this.unequip();
		for (int i = 0; i < inventory.size(); i++) {
			Item it = inventory.get(i);
			if (it instanceof Weapon) {
				Weapon w = (Weapon) it;
				if (equippable(w)) {
					this.equip(w);
					return;
				}
			}
		}
	}

	/**
	 * Use an item
	 *
	 * @param index the index of the item in this unit's inventory
	 */
	public void use(int index) {
		use(inventory.get(index));
	}
	
	/**
	 * Use an item
	 *
	 * @param i the item
	 */
	public void use(Item i){
		i.use(this);
		if(i.getUses() <= 0){
			inventory.remove(i);
			reEquip();
		}
	}

	/**
	 * Gets the triggers.
	 *
	 * @return the triggers
	 */
	public ArrayList<CombatTrigger> getTriggers() {
		ArrayList<CombatTrigger> triggers = new ArrayList<CombatTrigger>();
		triggers.addAll(skills);
		triggers.addAll(clazz.combatSkills);
		if(getWeapon() != null)
			triggers.addAll(getWeapon().getTriggers());
		return triggers;
	}
	
	//Development
	
	/**
	 * Sets the level.
	 *
	 * @param lv the new level
	 */
	public void setLevel(int lv) {
		if (lv > 20 || lv < 1) {
			return;
		}
		this.level = lv;
		lv--;
		stats = growths.times(lv / 100f).plus(bases)
				.min(new Statistics(60, 35,35,35, 35,35,35, 35,35,35,35));
		fillHp();
	}
	
	/**
	 * Fill hp.
	 */
	public void fillHp() {
		setHp(getStats().maxHp);
	}
	
	/**
	 * Gets the exp cost.
	 *
	 * @param level the level
	 * @return the exp cost
	 */
	public static int getExpCost(int level){
		return level * 50 + 500;
	}
	
	/**
	 * Squeeze exp.
	 *
	 * @return the int
	 */
	public int squeezeExp(){
		int exp = 0;
		while(getLevel() != 1){
			exp += getExpCost(getLevel());
			setLevel(getLevel() - 1);
		}
		return exp;
	}
	
	/**
	 * Squeeze gold.
	 *
	 * @return the int
	 */
	public int squeezeGold(){
		int gold = 0;
		ListIterator<Item> items = inventory.listIterator();
		while(items.hasNext()){
			Item i = items.next();
			boolean remove = true;
			if(i instanceof Weapon){
				Weapon w = (Weapon) i;
				if(w.pref != null){
					remove = false;
				}
			}
			if(remove){
				items.remove();
				gold+= i.getCost();
			}
		}
		return gold;
	}
	
	/**
	 * Increments this unit's contributions-to-battle record
	 * @param add the values to increment by
	 */
	public void addBattleStats(BattleStats add) {
		this.battleStats = this.battleStats.plus(add);
	}
	
	/**
	 * Gets the battle stat.
	 *
	 * @param stat the stat
	 * @return the battle stat
	 */
	public int getBattleStat(String stat) {
		/* LOOOOOPS! */
		switch (stat) {
			case "Kills": return battleStats.kills; 
			case "Assists": return battleStats.assists; 
			case "Damage": return battleStats.damage; 
			case "Healing": return battleStats.healing; 
			default: return -1; 
		}
	}
	
	/**
	 * Gets the assisters.
	 *
	 * @return the assisters
	 */
	public Set<Unit> getAssisters() {
		return assist;
	}

	// Combat statistics
	
	/**
	 * Hit.
	 *
	 * @return the int
	 */
	public int hit() {
		if(this.getWeapon() == null) return 0;
		return getWeapon().hit + 2 * getStats().skl + getStats().lck / 2
				+ (tempMods.get("Hit") != null ? tempMods.get("Hit") : 0);
	}

	/**
	 * Avoid.
	 *
	 * @return the int
	 */
	public int avoid() {
		return 2 * getStats().spd + getStats().lck / 2
				+ (tempMods.get("Avo") != null ? tempMods.get("Avo") : 0)
				+ getTerrain().getAvoidBonus(this);
	}

	/**
	 * Crit.
	 *
	 * @return the int
	 */
	public int crit() {
		if(getWeapon() == null) return 0;
		return getWeapon().crit + getStats().skl / 2 + clazz.crit
				+ (tempMods.get("Crit") != null ? tempMods.get("Crit") : 0);
	}

	/**
	 * Critical avoid
	 *
	 * @return the int
	 */
	public int dodge() {
		return getStats().lck
				+ (tempMods.get("Dodge") != null ? tempMods.get("Dodge") : 0);
	}

	/**
	 * Gets the the class.
	 *
	 * @return the the class
	 */
	// Getter/Setter
	public Class getTheClass() {
		return clazz;
	}

	/**
	 * Gets the hp.
	 *
	 * @return the hp
	 */
	public int getHp() {
		return hp;
	}

	/**
	 * Sets the hp.
	 *
	 * @param hp the new hp
	 */
	public void setHp(int hp) {
		this.hp = Math.max(hp, 0);
		if(this.hp == 0) {
			((OverworldStage) stage).removeUnit(xcoord, ycoord);
			if(rescuedUnit != null) {
				drop(xcoord, ycoord);
			}
			if(Game.glContextExists()) {
				((ClientOverworldStage) stage).setControl(false);
				stage.addEntity(new Corpse(this));
			}
		}
	}
	
	/**
	 * Sets the unit's max HP (used for Sudden Death)
	 * 
	 */
	public void setMaxHp(int mhp){
		this.stats = this.stats.copy("HP", mhp);
		if(this.getHp() > this.stats.maxHp){
			this.setHp(mhp);
		}
	}

	/** Returns the unit's current level */
	public int getLevel() {
		return this.level;
	}

	/**
	 * Return current statistics including tempMods and weapon
	 * and other non-unit things.
	 */
	public Statistics getStats() {
		Statistics retVal = this.stats;
		if (this.getWeapon() != null) {retVal = retVal.plus(this.getWeapon().modifiers);}
		retVal = retVal.plus(new Statistics(tempMods));
		retVal = retVal.plus(this.getTerrain().getDefenseBonus(this));
		if (rescuedUnit != null) {
			retVal = retVal.copy("Spd", retVal.spd / 2);
			retVal = retVal.copy("Skl", retVal.skl / 2);
		}
		return retVal;
	}

	/**
	 * Return current statistics (NOT BASES) before tempMods and weapon
	 * and other non-unit things are taken into account.
	 */
	public Statistics getBase() {
		return stats;
	}

	/**
	 * Return current statistics (NOT BASES) before tempMods and weapon
	 * and other non-unit things are taken into account.
	 */
	// this is used in too many loops to be easily refactored
	public int getBase(String stat) {
		switch (stat) {
			case "Lvl": return this.getLevel();
			case "Str" : return this.getBase().str;
			case "Mag" : return this.getBase().mag;
			case "Skl" : return this.getBase().skl;
			case "Spd" : return this.getBase().spd;
			case "Lck" : return this.getBase().lck;
			case "Def" : return this.getBase().def;
			case "Res" : return this.getBase().res;
			case "Mov" : return this.getBase().mov;
			case "Con" : return this.getBase().con;
			case "HP": return this.getBase().maxHp;
			default : throw new IllegalArgumentException("Unknown Stat: " + stat);
		}
	}

	/**
	 * Sets the temp mod.
	 *
	 * @param stat the stat
	 * @param val the val
	 */
	public void setTempMod(String stat, int val) {
		tempMods.put(stat, (tempMods.containsKey(stat) ? tempMods.get(stat) : 0) + val);
	}
	
	/**
	 * Debug stat.
	 *
	 * @param stat the stat
	 */
	public void debugStat(String stat){
		stats = stats.copy(stat, 9999);
	}
	
	/**
	 * Debug stat.
	 *
	 * @param stat the stat
	 * @param value the value
	 */
	public void debugStat(String stat, int value){
		stats = stats.copy(stat, value);
	}
	
	/**
	 * Debug crit.
	 *
	 * @param might the might
	 */
	public void debugCrit(int might){
		Weapon w = getWeapon();
		this.removeFromInventory(w);
		this.equip(w.getCopyWithNewMtHitCrit(might, 100, 100));
	}
	
	/**
	 * Debug crit.
	 */
	public void debugCrit(){
		Weapon w = getWeapon();
		this.removeFromInventory(w);
		this.equip(w.getCopyWithNewMtHitCrit(100, 100, 100));
	}

	/**
	 * Clear temp mods.
	 */
	public void clearTempMods() {
		tempMods.clear();
	}

	/**
	 * Gets the weapon.
	 *
	 * @return the weapon
	 */
	public Weapon getWeapon() {
		if (isUnequipped) {
			return null;
		} else if (inventory.size() < 1) {
			return null;
		} else {
			Item candidate = inventory.get(0);
			if (candidate instanceof Weapon) {
				Weapon candidateW = (Weapon) candidate;
				if (equippable(candidateW)) {
					return candidateW;
				} else {
					return null;
				}
			} else {
				return null;
			}
		}
	}

	/**
	 * Gets the terrain.
	 *
	 * @return the terrain
	 */
	public Terrain getTerrain() {
		if(stage == null) return Terrain.PLAIN;
		return ((OverworldStage) stage).getTerrain(xcoord, ycoord);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return name + " HP" + hp + " #" + hashCode() + " \n" + stats;
	}

	/**
	 * Gets the party color.
	 *
	 * @return the party color
	 */
	public Color getPartyColor() {
		if(team == null) return Party.TEAM_BLUE;
		return team.getColor();
	}

	/**
	 * Sets the party.
	 *
	 * @param t the new party
	 */
	public void setParty(Party t) {
		team = t;
	}

	/**
	 * Gets the party.
	 *
	 * @return the party
	 */
	public Party getParty() {
		return team;
	}

	/**
	 * Sets the moved.
	 *
	 * @param status the new moved
	 */
	public void setMoved(boolean status) {
		moved = status;
		if(moved) {
			sprite.setAnimation("IDLE");
			origX = xcoord;
			origY = ycoord;
		}
	}

	/**
	 * Checks for moved.
	 *
	 * @return true, if successful
	 */
	public boolean hasMoved() {
		return moved;
	}


	/**
	 * Gets the orig x.
	 *
	 * @return the orig x
	 */
	public int getOrigX() {
		return origX;
	}

	/**
	 * Sets the orig x.
	 *
	 * @param origX the new orig x
	 */
	public void setOrigX(int origX) {
		this.origX = origX;
	}

	/**
	 * Sets the orig y.
	 *
	 * @param origY the new orig y
	 */
	public void setOrigY(int origY) {
		this.origY = origY;
	}

	/**
	 * Gets the orig y.
	 *
	 * @return the orig y
	 */
	public int getOrigY() {
		return origY;
	}
	
	/**
	 * Rescued unit.
	 *
	 * @return the unit
	 */
	public Unit rescuedUnit(){
		return rescuedUnit;
	}

	/**
	 * Returns true if this unit has been rescued by another unit
	 */
	public boolean isRescued() {
		return this.rescued;
	}
	
	public boolean isVisible(ClientOverworldStage stage) {
		if(FEMultiplayer.getLocalPlayer().getParty().isAlly(team))
			return true;
		return !stage.getFog().getNodes().contains(new Node(xcoord, ycoord));
	}
	
	@Override public int hashCode() {
		return ((((
			this.name.hashCode()) * 31 +
			this.bases.hashCode()) * 31 +
			this.growths.hashCode()) * 31 +
			this.clazz.hashCode());
	}
	
	public Node[] getMove() {
		return move;
	}
	
	public int getSkillCharge() {
		return this.skillCharge;
	}
	public void incrementSkillCharge(int amount) {
		this.skillCharge = Math.max(0, Math.min(MAX_SKILL_CHARGE, this.skillCharge + amount));
	}
	public void emptySkillCharge() {
		this.skillCharge = 0;
	}
}
