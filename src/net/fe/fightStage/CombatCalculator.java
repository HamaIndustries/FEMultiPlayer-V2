package net.fe.fightStage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.function.Function;
import java.util.logging.Logger;

import net.fe.FEMultiplayer;
import net.fe.FEResources;
import net.fe.overworldStage.Grid;
import net.fe.rng.RNG;
import net.fe.unit.Unit;
import net.fe.unit.UnitIdentifier;
import net.fe.unit.Weapon;


/**
 * A calculator used to find damage, drain, effect and healing within fightstage.
 * 
 */
public class CombatCalculator {
	
	/** a logger */
	private static final Logger logger = Logger.getLogger("net.fe.fightStage.CombatCalculator");
	static {
		logger.setLevel(java.util.logging.Level.FINER);
		try {
			String file = "logs/combatcalculator_log_" + LocalDateTime.now().toString().replace("T", "@").replace(":", "-") + ".log";
			java.util.logging.Handler h = new java.util.logging.FileHandler(file);
			h.setFormatter(new java.util.logging.SimpleFormatter());
			logger.addHandler(h);
		} catch (java.io.IOException e) {
			logger.throwing("net.fe.network.Client", "logging initializing", e);
		}
	}
	
	/** The left & right units */
	protected final Unit left, right;
	
	/** The attack queue. */
	private final ArrayList<AttackRecord> attackQueue;
	
	/** The range. */
	private final int range;
	
	/** The next attack. */
	private final Queue<String> nextAttack;
	
	/** The attack triggers, weapon and unit skills */
	private ArrayList<CombatTrigger> leftTriggers, rightTriggers;
	
	private RNG hitRNG;
	private RNG critRNG;
	private RNG skillRNG;
	
	/**
	 * Instantiates a new combat calculator.
	 *
	 * @param u1 the unit id of fighter 1
	 * @param u2 the unit id of fighter 2
	 * @param dereference A function that converts a UnitIdentifier into a Unit
	 */
	public CombatCalculator(UnitIdentifier u1, UnitIdentifier u2, Function<UnitIdentifier, Unit> dereference, RNG hitRNG, RNG critRNG, RNG skillRNG){
		
		this.hitRNG = hitRNG;
		this.critRNG = critRNG;
		this.skillRNG = skillRNG;
		
		left = dereference.apply(u1);
		right = dereference.apply(u2);
		
		logger.fine("[BATL]0 BATTLESTART::");
		logger.fine("[BATL]0 HP::" + left.name + " HP" + left.getHp() +
				" " + right.name + " HP" + right.getHp());
		
		range = Grid.getDistance(left, right);
		attackQueue = new ArrayList<AttackRecord>();
		nextAttack = new LinkedList<String>();
		calculate();
		
		for(AttackRecord atk: attackQueue){
			logger.fine("[BATL]0 ATTACKRECORD::" + atk.toString());
		}
		logger.fine("[BATL]0 HP::" + left.name + " HP" + left.getHp() +
				" " + right.name + " HP" + right.getHp());
		logger.fine("[BATL]0 BATTLEEND::");
	}
	
	/**
	 * Main calculation method, determines attack order, which units should attack, sets triggers, and finally runs each attack
	 */
	protected void calculate() {
		// The units will automatically equip the next weapon if the current one breaks.
		// This, however, should not affect the battle. The weapon should not change until
		// the round is over. So, record used weapons now, and not ask the units for their weapons again.
		final Weapon leftWeap = left.getWeapon();
		final Weapon rightWeap = right.getWeapon();
		
		// Determine turn order
		ArrayList<Boolean> attackOrder = new ArrayList<Boolean>();
		if (shouldAttack(left,right,leftWeap,range))
			attackOrder.add(true);
		if (shouldAttack(right,left,rightWeap,range))
			attackOrder.add(false);
		if (left.getStats().spd >= right.getStats().spd + 4 
				&& shouldAttack(left,right,leftWeap,range)) {
			attackOrder.add(true);
		}
		if (right.getStats().spd >= left.getStats().spd + 4
				&& shouldAttack(right,left,rightWeap,range)) {
			attackOrder.add(false);
		}
		leftTriggers = new ArrayList<CombatTrigger>();
		for(CombatTrigger t: left.getTriggers()){
			leftTriggers.add(t.getCopy());
		}
		
		rightTriggers = new ArrayList<CombatTrigger>();
		for(CombatTrigger t: right.getTriggers()){
			rightTriggers.add(t.getCopy());
		}
		
		for (Boolean i : attackOrder) {
			attack(i, "None",leftWeap,rightWeap);
			while(!nextAttack.isEmpty()){
				attack(i, nextAttack.poll(),leftWeap,rightWeap);
			}
		}
	}
	
	/**
	 * 
	 * Determines who should attack, according to health, equipped weapon, and if the unit is healing
	 *
	 * @param a the attacker
	 * @param d the defender
	 * @param range the range
	 * @return true, if a is able to attack d
	 */
	public static boolean shouldAttack(Unit a, Unit d, Weapon aWeap, int range){
		if(a.getHp() <= 0) return false;
		if(aWeap == null) return false;
		if(aWeap.getUses() == 0) return false;
		if(!aWeap.range.apply(a.getStats()).contains(range)) return false;
		if(aWeap.type == Weapon.Type.STAFF) return false;
		return true;
	}
	
	/**
	 * Adds the attack.
	 *
	 * @param effect the effect
	 */
	public void addAttack(String effect){
		nextAttack.add(effect);
	}

	/**
	 * Attack. Does most of the heavy lifting in this class, calculates the damage dealt and damage healed, if any.
	 * 
	 * <pre>
	 *Combat Calculations:
	 * 
	 *a is attacker, d is defender.
	 *
	 *crit: true if [RNG.get(This returns a number 1-100) < a.crit() - d.dodge()] and not miss.
	 *
	 *hit = weapon.hit + [2 * Skl + Lck / 2] + tempMods.get("Hit") {<- normally hit, unless a skill augments that.}
	 *
	 *avoid = 2 * Spd + Lck / 2 + tempMods.get("Avo"){0 unless a skill etc} + TerrainAvoidBonus
	 *
	 *hit rate = a.hit() - d.avoid() + a.getWeapon().triMod(d.getWeapon()) * 15}
	 *
	 *miss occurs when (RNG + RNG)/2 > hitrate
	 *</pre>
	 *
	 * @param leftAttacking If the left fighter is attacking
	 * @param currentEffect the current effect
	 */
	private void attack(boolean leftAttacking, String currentEffect, Weapon leftWeap, Weapon rightWeap) {
		Unit a = leftAttacking ? left: right;
		Unit d = leftAttacking ? right: left;
		
		Weapon aWeap = leftAttacking ? leftWeap : rightWeap;
		
		List<CombatTrigger> aTriggers = leftAttacking ? leftTriggers: rightTriggers;
		List<CombatTrigger> dTriggers = leftAttacking ? rightTriggers: leftTriggers;
		
		if(!shouldAttack(a, d, aWeap, range))
			return;
		
		int damage = 0;
		int drain = 0;
		String animation = "Attack";
		boolean miss = false;
		boolean use = false;
		int crit = 1;

		if (a.getHp() == 0 || d.getHp() == 0)
			return;

		LinkedHashMap<CombatTrigger, Boolean> aSuccess = new LinkedHashMap<CombatTrigger, Boolean>();
		LinkedHashMap<CombatTrigger, Boolean> dSuccess = new LinkedHashMap<CombatTrigger, Boolean>();
		
		for (CombatTrigger t : aTriggers)
			aSuccess.put(t, t.attempt(a, range, d, skillRNG));
		for (CombatTrigger t : dTriggers)
			dSuccess.put(t, t.attempt(d, range, a, skillRNG));
		
		

		for (CombatTrigger t : aSuccess.keySet()) {
			if (aSuccess.get(t) && (t.turnToRun & CombatTrigger.YOUR_TURN_PRE)!=0) {
				t.runPreAttack(this, a, d);
				if (t.nameModification == CombatTrigger.REPLACE_NAME_AFTER_PRE) {
					animation += " " + t.getName() + "(a)";
				}
			}
		}	
		for (CombatTrigger t : dSuccess.keySet()) {
			if (dSuccess.get(t) && (t.turnToRun & CombatTrigger.ENEMY_TURN_PRE)!=0) {
				t.runPreAttack(this, a, d);
				if (t.nameModification == CombatTrigger.REPLACE_NAME_AFTER_PRE) {
					animation += " " + t.getName() + "(d)";
				}
			}
		}
		
		
		if (!hitRNG.test(hitRate(a, d))) {
			miss = true;
			if (a.getWeapon().isMagic())
				use = true;
		} else {
			use = true;
		}
		
		if (critRNG.test(a.crit() - d.dodge()) && !miss) {
			crit = 3;
			animation += " Critical(a)";
		}
		
		
		damage = calculateBaseDamage(a, d) * crit;
		for (CombatTrigger t : aSuccess.keySet()) {
			if (aSuccess.get(t) && (t.turnToRun & CombatTrigger.YOUR_TURN_MOD)!=0) {
				int oldDamage = damage;
				damage = t.runDamageMod(a,d,damage);
				if (t.nameModification == CombatTrigger.APPEND_NAME_AFTER_MOD 
						&& damage!=oldDamage && !miss) {
					animation += " " + t.getName() + "(a)";
				}
			}
		}
		for (CombatTrigger t : dSuccess.keySet()) {
			if (dSuccess.get(t)  && (t.turnToRun & CombatTrigger.ENEMY_TURN_MOD)!=0) {
				int oldDamage = damage;
				damage = t.runDamageMod(a,d,damage);
				if (t.nameModification == CombatTrigger.APPEND_NAME_AFTER_MOD 
						&& damage!=oldDamage && !miss) {
					animation += " " + t.getName() + "(d)";
				}
			}
		}
		damage = Math.max(0, Math.min(damage, d.getHp()));
		
		for (CombatTrigger t : aSuccess.keySet()) {
			if(aSuccess.get(t) && (t.turnToRun & CombatTrigger.YOUR_TURN_DRAIN)!=0){
				drain = t.runDrain(a, d, damage);
			}
		}
		for (CombatTrigger t : dSuccess.keySet()) {
			if(dSuccess.get(t) && (t.turnToRun & CombatTrigger.ENEMY_TURN_DRAIN)!=0){
				drain = t.runDrain(a, d, damage);
			}
		}
		
		if(miss){
			damage = 0;
			drain = 0;
			animation += " Miss";
		}
		
		damage = Math.max(0, Math.min(damage, d.getHp()));
		addToAttackQueue(a, d, animation, damage, drain);
		d.setHp(d.getHp() - damage);
		a.setHp(a.getHp() + drain);
		if(use)
			a.use(a.getWeapon());
		a.clearTempMods();
		d.clearTempMods();
		for (CombatTrigger t : aSuccess.keySet()) {
			if (aSuccess.get(t) && (t.turnToRun & CombatTrigger.YOUR_TURN_POST)!=0) {
				t.runPostAttack(this, leftAttacking, a, d, damage, currentEffect);
			}
		}
		for (CombatTrigger t : dSuccess.keySet()) {
			if (dSuccess.get(t) && (t.turnToRun & CombatTrigger.ENEMY_TURN_POST)!=0) {
				t.runPostAttack(this, leftAttacking, a, d, damage, currentEffect);
			}
		}
		
	}
	
	/**
	 * Adds the attack to attack queue.
	 *
	 * @param a the attacker
	 * @param d the defender
	 * @param animation the animation
	 * @param damage the damage
	 * @param drain the damage healed
	 */
	public void addToAttackQueue(Unit a, Unit d, String animation, int damage, int drain) {
		AttackRecord rec = new AttackRecord();
		rec.attacker = new UnitIdentifier(a);
		rec.defender = new UnitIdentifier(d);
		rec.animation = animation;
		rec.damage = damage;
		rec.drain = drain;
		attackQueue.add(rec);

		logger.fine(rec.toString());
	}
	
	/**
	 * Gets the attack queue.
	 *
	 * @return the attack queue
	 */
	public ArrayList<AttackRecord> getAttackQueue(){
		return attackQueue;
	}
	
	/**
	 * Calculates base damage. This is damage excluding any combat triggers
	 *
	 * @param a the attacker
	 * @param d the defender
	 * @return the base damage
	 */
	public static int calculateBaseDamage(Unit a, Unit d){
		int base;
		if (a.getWeapon().isMagic()) {
			base = a.getStats().mag
					+ (a.getWeapon().mt + a.getWeapon().triMod(d.getWeapon()))
					- d.getStats().res;
		} else {
			base = a.getStats().str
					+ (a.getWeapon().mt + a.getWeapon().triMod(d.getWeapon()))
					- d.getStats().def;
		}
		
		return Math.max(base, 0);
	}
	
	/**
	 * Calculates the main battle stats of the attacking unit.
	 * Includes triggers marked as SHOW_IN_PREVIEW
	 * 
	 * @param a the attacker
	 * @param d the defender
	 * @return the main battle stats.
	 */
	public static MainBattleStats calculatePreviewStats(Unit a, Unit d, FightStage stage) {
		return calculatePreviewStats(a, d, CombatCalculator.shouldAttack(a, d, a.getWeapon(), stage.getRange()));
	}
	
	/**
	 * Calculates the main battle stats of the attacking unit.
	 * Includes triggers marked as SHOW_IN_PREVIEW
	 * 
	 * @param a the attacker
	 * @param d the defender
	 * @return the main battle stats.
	 */
	public static MainBattleStats calculatePreviewStats(Unit a, Unit d, boolean shouldAttack) {
		if(!shouldAttack)
			return new MainBattleStats(-1, -1, -1);
		
		// run preAttack triggers that are allowed to be shown in the preview
		for (CombatTrigger t : a.getTriggers())
			if (((t.turnToRun & CombatTrigger.SHOW_IN_PREVIEW) != 0) &&	((t.turnToRun & CombatTrigger.YOUR_TURN_PRE) != 0))
				if (t.attempt(a, -1, d, new net.fe.rng.NullRNG()))
					t.runPreAttack(null, a, d);
		
		for (CombatTrigger t : d.getTriggers())
			if (((t.turnToRun & CombatTrigger.SHOW_IN_PREVIEW) != 0) &&	((t.turnToRun & CombatTrigger.ENEMY_TURN_PRE) != 0))
				if (t.attempt(a, -1, d, new net.fe.rng.NullRNG()))
					t.runPreAttack(null, a, d);
		
		int damage = CombatCalculator.calculateBaseDamage(a, d);

		// Run combat mods that are allowed to occur in the preview
		for (CombatTrigger t : a.getTriggers())
			if (((t.turnToRun & CombatTrigger.SHOW_IN_PREVIEW) != 0) && ((t.turnToRun & CombatTrigger.YOUR_TURN_MOD) != 0))
				if (t.attempt(a, -1, d, new net.fe.rng.NullRNG()))
					damage = t.runDamageMod(a, d, damage);
		
		for (CombatTrigger t : d.getTriggers())
			if (((t.turnToRun & CombatTrigger.SHOW_IN_PREVIEW) != 0) && ((t.turnToRun & CombatTrigger.ENEMY_TURN_MOD) != 0))
				if (t.attempt(a, -1, d, new net.fe.rng.NullRNG()))
					damage = t.runDamageMod(a, d, damage);

		damage = limit(0, 100, damage);
		int hit = limit(0, 100, hitRate(a, d));
		int crit = limit(0, 100, critRate(a, d));
		
		// cleanup
		a.clearTempMods();
		d.clearTempMods();
		
		return new MainBattleStats(damage, hit, crit);
	}
	
	/**
	 * Hit rate.
	 *
	 * @param a the attacker
	 * @param d the defender
	 * @return the hit rate
	 */
	public static int hitRate(Unit a, Unit d){
		return a.hit() - d.avoid() + a.getWeapon().triMod(d.getWeapon()) * 15;
	}
	
	public static int critRate(Unit a, Unit d) {
		return a.crit() - d.dodge();
	}
	
	public static int actualHitRate(RNG rng, Unit a, Unit d) {
		return rng.actualSuccessRate(hitRate(a, d));
	}
	
	public static int actualCritRate(RNG rng, Unit a, Unit d) {
		return rng.actualSuccessRate(critRate(a, d));
	}
	
	private static int limit(int min, int max, int val) {
		if(val < min)
			return min;
		if(val > max)
			return max;
		return val;
	}
	
	/**
	 * Immutable class containing the damage, hit and crit of a unit when fighting a specific enemy.
	 * A value of -1 implies the unit can't attack the enemy.
	 * @author wellme
	 */
	public static class MainBattleStats {
		public final int damage;
		public final int hit;
		public final int crit;
		
		public MainBattleStats(int damage, int hit, int crit) {
			this.damage = damage;
			this.hit = hit;
			this.crit = crit;
		}
		
		public String formattedDamage() {
			if(damage < 0)
				return " --";
			return String.format("%3d", damage);
		}
		
		public String formattedHit() {
			if(hit < 0)
				return " --";
			if(FEResources.getActualOdds())
				return String.format("%3d", FEMultiplayer.getSession().getHitRNG().actualSuccessRate(hit));
			return String.format("%3d", hit);
		}
		
		public String formattedCrit() {
			if(crit < 0)
				return " --";
			if(FEResources.getActualOdds())
				return String.format("%3d", FEMultiplayer.getSession().getCritRNG().actualSuccessRate(crit));
			return String.format("%3d", crit);
		}
		
		public boolean canAttack() {
			return crit >= 0;
		}
	}
}
