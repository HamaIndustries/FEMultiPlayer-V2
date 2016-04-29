package net.fe.unit;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import net.fe.Command;
import net.fe.FEResources;
import net.fe.PaletteSwapper;
import net.fe.Party;
import net.fe.fightStage.CombatTrigger;
import net.fe.overworldStage.Corpse;
import net.fe.overworldStage.DoNotDestroy;
import net.fe.overworldStage.Grid;
import net.fe.overworldStage.Node;
import net.fe.overworldStage.ClientOverworldStage;
import net.fe.overworldStage.OverworldStage;
import net.fe.overworldStage.Path;
import net.fe.overworldStage.Terrain;

import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

import chu.engine.Game;
import chu.engine.GriddedEntity;
import chu.engine.anim.Renderer;
import chu.engine.anim.ShaderArgs;
import chu.engine.anim.Transform;

// TODO: Auto-generated Javadoc
/**
 * The Class Unit.
 */
public class Unit extends GriddedEntity implements Serializable, DoNotDestroy{
	
	/** The bases. */
	public HashMap<String, Integer> bases;
	
	/** The growths. */
	public HashMap<String, Integer> growths;
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -5101031417704315547L;
	
	/** The stats. */
	private HashMap<String, Float> stats;
	
	/** The skills. */
	private ArrayList<CombatTrigger> skills;
	
	/** The hp. */
	private int hp;
	
	/** The clazz. */
	private Class clazz;
	
	/** The gender. */
	public final char gender;
	
	/** The weapon. */
	private Weapon weapon;
	
	/** The inventory. */
	private ArrayList<Item> inventory;
	
	/** The name. */
	public final String name;
	
	/** The team. */
	private Party team;
	
	/** The temp mods. */
	private transient HashMap<String, Integer> tempMods;
	
	/** The battle stats. */
	private transient HashMap<String, Integer> battleStats;
	
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
	private transient Command callback;
	
	/** The rescued. */
	private boolean rescued;
	
	/** The counter. */
	private float counter;

	/** The orig y. */
	private int origX, origY;
	
	/** The Constant MAP_ANIM_SPEED. */
	public static final float MAP_ANIM_SPEED = 0.2f;
	
	/** The Constant MOVE_SPEED. */
	public static final int MOVE_SPEED = 250;
	
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
	public Unit(String name, Class c, char gender, HashMap<String, Integer> bases,
			HashMap<String, Integer> growths) {
		super(0, 0);
		this.bases = bases;
		this.growths = growths;
		this.gender = gender;
		inventory = new ArrayList<Item>();
		tempMods = new HashMap<String, Integer>();
		assist = new HashSet<Unit>();
		skills = new ArrayList<CombatTrigger>();
		battleStats = new HashMap<String, Integer>();
        battleStats.put("Kills", 0);
        battleStats.put("Assists", 0);
        battleStats.put("Damage", 0);
        battleStats.put("Healing", 0);
		this.name = name;
		clazz = c;

		stats = new HashMap<String, Float>();
		for (String s : bases.keySet()) {
			stats.put(s, bases.get(s).floatValue());
		}
		fillHp();
		
		if(name.equals("Eirika") || 
				clazz.name.equals("Valkyrie") ||
				clazz.name.equals("Falconknight")){
			bases.put("Aid", 20-bases.get("Con"));
		} else if (Unit.isRider(clazz) || Unit.isRider(name)){
			bases.put("Aid", 27-bases.get("Con"));
		} else {
			bases.put("Aid", bases.get("Con")-1);
		}
		
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
        skills = new ArrayList<CombatTrigger>();
        battleStats = new HashMap<String, Integer>();
        battleStats.put("Kills", 0);
        battleStats.put("Assists", 0);
        battleStats.put("Damage", 0);
        battleStats.put("Healing", 0);
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
	public void move(Path p, Command callback) {
		this.path = p.getCopy();
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
		rescuedUnit.move(p, new Command(){
			@Override
			public void execute() {
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
		//System.out.println(this.get("Aid") + " >= "+ u.get("Con"));
		return this.get("Aid")>=u.get("Con");
	}

	
	/**
	 * Drop.
	 *
	 * @param x the x
	 * @param y the y
	 */
	public void drop(int x, int y){
		if(rescuedUnit == null) return;
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
		if(rescuedUnit == null) return;
		if(u.rescuedUnit() != null) return;
		u.setRescuedUnit(rescuedUnit);
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
					callback.execute();
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
	 * Gets the copy.
	 *
	 * @return the copy
	 */
	Unit getCopy() {
		Unit copy = new Unit(name, clazz, gender, bases, growths);
		copy.setLevel(stats.get("Lvl").intValue());
		for (Item i : inventory) {
			copy.addToInventory(i);
		}
		return copy;
	}

	/* (non-Javadoc)
	 * @see chu.engine.Entity#render()
	 */
	public void render() {
		ClientOverworldStage cs = (ClientOverworldStage)stage;
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

	

	/**
	 * Gets the inventory.
	 *
	 * @return the inventory
	 */
	//Inventory
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
				range.addAll(w.range);
		}
		return range;
	}
	
	/**
	 * Equip.
	 *
	 * @param w the w
	 */
	public void equip(Weapon w) {
		if (equippable(w)) {
			weapon = w;
			if(stage != null){
				((ClientOverworldStage) stage).addCmd("EQUIP");
				((ClientOverworldStage) stage).addCmd(new UnitIdentifier(this));
				((ClientOverworldStage) stage).addCmd(findItem(w));
			}
			inventory.remove(w);
			inventory.add(0, w);
		}
	}
	
	/**
	 * Equip.
	 *
	 * @param i the i
	 */
	// For use in command message processing only
	public void equip(int i) {
		Weapon w = (Weapon)inventory.get(i);
		if (equippable(w)) {
			weapon = w;
			inventory.remove(w);
			inventory.add(0, w);
		}
	}
	
	/**
	 * Unequip.
	 */
	public void unequip(){
		weapon = null;
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
						&& w.range.contains(range)) {
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
						&& w.range.contains(range)) {
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
	 * Equip first weapon.
	 *
	 * @param range the range
	 * @return the int
	 */
	public int equipFirstWeapon(int range) {
		for (int i = 0; i < inventory.size(); i++) {
			Item it = inventory.get(i);
			if (it instanceof Weapon) {
				Weapon w = (Weapon) it;
				if (equippable(w) && w.type != Weapon.Type.STAFF
						&& w.range.contains(range)) {
					equip(w);
					return i;
				}
			}
		}
		return -1;
	}
	
	/**
	 * Re equip.
	 */
	public void reEquip(){
		for (int i = 0; i < inventory.size(); i++) {
			Item it = inventory.get(i);
			if (it instanceof Weapon) {
				Weapon w = (Weapon) it;
				if (equippable(w)) {
					weapon = w;
					inventory.remove(w);
					inventory.add(0, w);
					return;
				}
			}
		}
	}

	/**
	 * Use.
	 *
	 * @param index the index
	 * @return the int
	 */
	public int use(int index) {
		return use(inventory.get(index), true);
	}
	
	/**
	 * Use.
	 *
	 * @param index the index
	 * @param destroy the destroy
	 * @return the int
	 */
	public int use(int index, boolean destroy){
		return use(inventory.get(index), destroy);
	}
	
	/**
	 * Use.
	 *
	 * @param i the i
	 * @return the int
	 */
	public int use(Item i){
		return use(i, true);
	}

	/**
	 * Use.
	 *
	 * @param i the i
	 * @param destroy the destroy
	 * @return the int
	 */
	public int use(Item i, boolean destroy) {
		int ans = i.use(this);
		if(i.getUses() <= 0 && destroy){
			inventory.remove(i);
			if(i == weapon){
				weapon = null;
				reEquip();
			}
			
		}
		return ans;
	}

	/**
	 * Gets the triggers.
	 *
	 * @return the triggers
	 */
	public ArrayList<CombatTrigger> getTriggers() {
		ArrayList<CombatTrigger> triggers = new ArrayList<CombatTrigger>();
		triggers.addAll(skills);
		if (clazz.masterSkill != null)
			triggers.add(clazz.masterSkill);
		if(weapon!=null)
			triggers.addAll(weapon.getTriggers());
		return triggers;
	}
	
	/**
	 * Sets the level.
	 *
	 * @param lv the new level
	 */
	//Development
	public void setLevel(int lv) {
		if (lv > 20 || lv < 1) {
			return;
		}
		stats.put("Lvl", (float) lv);
		lv--;
		for (String stat : growths.keySet()) {
			float newStat = bases.get(stat)
					+ (float) (lv * growths.get(stat) / 100.0);
			float max = stat.equals("HP") ? 60 : 35;
			stats.put(stat, Math.min(newStat, max));
		}
		fillHp();
	}
	
	/**
	 * Fill hp.
	 */
	public void fillHp() {
		setHp(get("HP"));
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
		while(get("Lvl") != 1){
			exp += getExpCost(get("Lvl"));
			setLevel(get("Lvl") - 1);
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
	 * Adds the battle stat.
	 *
	 * @param stat the stat
	 * @param add the add
	 */
	public void addBattleStat(String stat, int add) {
		battleStats.put(stat, battleStats.get(stat) + add);
	}
	
	/**
	 * Gets the battle stat.
	 *
	 * @param stat the stat
	 * @return the battle stat
	 */
	public int getBattleStat(String stat) {
		return battleStats.get(stat);
	}
	
	/**
	 * Report battle stats.
	 */
	public void reportBattleStats() {
		for(String s : battleStats.keySet()) {
			System.out.print(s+": ");
			System.out.print(battleStats.get(s)+" ");
		}
		System.out.println();
	}
	
	/**
	 * Gets the assisters.
	 *
	 * @return the assisters
	 */
	public Set<Unit> getAssisters() {
		return assist;
	}

	/**
	 * Hit.
	 *
	 * @return the int
	 */
	// Combat statistics
	public int hit() {
		if(weapon == null) return 0;
		return weapon.hit + 2 * get("Skl") + get("Lck") / 2
				+ (tempMods.get("Hit") != null ? tempMods.get("Hit") : 0);
	}

	/**
	 * Avoid.
	 *
	 * @return the int
	 */
	public int avoid() {
		return 2 * get("Spd") + get("Lck") / 2
				+ (tempMods.get("Avo") != null ? tempMods.get("Avo") : 0)
				+ getTerrain().getAvoidBonus(this);
	}

	/**
	 * Crit.
	 *
	 * @return the int
	 */
	public int crit() {
		if(weapon == null) return 0;
		return weapon.crit + get("Skl") / 2 + clazz.crit
				+ (tempMods.get("Crit") != null ? tempMods.get("Crit") : 0);
	}

	/**
	 * Dodge.
	 *
	 * @return the int
	 */
	public int dodge() { // Critical avoid
		return get("Lck")
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
		if(hp == 0) {
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
	 * Gets the.
	 *
	 * @param stat the stat
	 * @return the int
	 */
	public int get(String stat) {
		int ans = stats.get(stat).intValue()
				+ (weapon != null ? weapon.modifiers.get(stat) : 0)
				+ (tempMods.get(stat) != null ? tempMods.get(stat) : 0);
		if (Arrays.asList("Def", "Res").contains(stat)) {
			ans += getTerrain().getDefenseBonus(this);
		}
		if((stat.equals("Spd") || stat.equals("Skl")) && rescuedUnit!=null){
			ans/=2;
		}
		return ans;
	}

	/**
	 * Gets the base.
	 *
	 * @param stat the stat
	 * @return the base
	 */
	public int getBase(String stat) {
		return stats.get(stat).intValue();
	}

	/**
	 * Sets the temp mod.
	 *
	 * @param stat the stat
	 * @param val the val
	 */
	public void setTempMod(String stat, int val) {
		tempMods.put(stat, val);
	}
	
	/**
	 * Debug stat.
	 *
	 * @param stat the stat
	 */
	public void debugStat(String stat){
		stats.put(stat, 9999f);
	}
	
	/**
	 * Debug stat.
	 *
	 * @param stat the stat
	 * @param value the value
	 */
	public void debugStat(String stat, int value){
		stats.put(stat, value*1.0f);
	}
	
	/**
	 * Debug crit.
	 *
	 * @param might the might
	 */
	public void debugCrit(int might){
		getWeapon().crit=100;
		getWeapon().hit=100;
		getWeapon().mt=might;
	}
	
	/**
	 * Debug crit.
	 */
	public void debugCrit(){
		getWeapon().crit=100;
		getWeapon().hit=100;
		getWeapon().mt=100;
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
		return weapon;
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
	 * Sets the rescued unit.
	 *
	 * @param unit the new rescued unit
	 */
	public void setRescuedUnit(Unit unit) {
		rescuedUnit = unit;
	}
}
