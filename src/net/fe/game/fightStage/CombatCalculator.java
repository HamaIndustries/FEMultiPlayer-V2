package net.fe.game.fightStage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.function.Function;
import java.util.logging.Logger;

import net.fe.RNG;
import net.fe.game.unit.Unit;
import net.fe.game.unit.UnitIdentifier;
import net.fe.game.unit.Weapon;
import net.fe.overworldStage.Grid;

/**
 * A calculator used to find damage, drain, effect and healing within
 * fightstage.
 * 
 */
public class CombatCalculator {

	/** a logger */
	private static final Logger logger = Logger.getLogger("net.fe.fightStage.CombatCalculator");

	static {
		logger.setLevel(java.util.logging.Level.FINER);
		try {
			String file = "logs/combatcalculator_log_"
			        + LocalDateTime.now().toString().replace("T", "@").replace(":", "-") + ".log";
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

	/**
	 * Instantiates a new combat calculator.
	 *
	 * @param u1 the unit id of fighter 1
	 * @param u2 the unit id of fighter 2
	 * @param dereference A function that converts a UnitIdentifier into a Unit
	 */
	public CombatCalculator(UnitIdentifier u1, UnitIdentifier u2, Function<UnitIdentifier, Unit> dereference) {

		left = dereference.apply(u1);
		right = dereference.apply(u2);

		logger.fine("[BATL]0 BATTLESTART::");
		logger.fine("[BATL]0 HP::" + left.name + " HP" + left.getHp() + " " + right.name + " HP" + right.getHp());

		range = Grid.getDistance(left, right);
		attackQueue = new ArrayList<AttackRecord>();
		nextAttack = new LinkedList<String>();
		calculate();

		for (AttackRecord atk : attackQueue) {
			logger.fine("[BATL]0 ATTACKRECORD::" + atk.toString());
		}
		logger.fine("[BATL]0 HP::" + left.name + " HP" + left.getHp() + " " + right.name + " HP" + right.getHp());
		logger.fine("[BATL]0 BATTLEEND::");
	}

	/**
	 * Main calculation method, determines attack order, which units should
	 * attack, sets triggers, and finally runs each attack
	 */
	protected void calculate() {
		// The units will automatically equip the next weapon if the current one
		// breaks.
		// This, however, should not affect the battle. The weapon should not
		// change until
		// the round is over. So, record used weapons now, and not ask the units
		// for their weapons again.
		final Weapon leftWeap = left.getWeapon();
		final Weapon rightWeap = right.getWeapon();

		// Determine turn order
		ArrayList<Boolean> attackOrder = new ArrayList<Boolean>();
		if (shouldAttack(left, right, leftWeap, range))
			attackOrder.add(true);
		if (shouldAttack(right, left, rightWeap, range))
			attackOrder.add(false);
		if (left.getStats().spd >= right.getStats().spd + 4 && shouldAttack(left, right, leftWeap, range)) {
			attackOrder.add(true);
		}
		if (right.getStats().spd >= left.getStats().spd + 4 && shouldAttack(right, left, rightWeap, range)) {
			attackOrder.add(false);
		}
		leftTriggers = new ArrayList<CombatTrigger>();
		for (CombatTrigger t : left.getTriggers()) {
			leftTriggers.add(t.getCopy());
		}

		rightTriggers = new ArrayList<CombatTrigger>();
		for (CombatTrigger t : right.getTriggers()) {
			rightTriggers.add(t.getCopy());
		}

		for (Boolean i : attackOrder) {
			attack(i, "None", leftWeap, rightWeap);
			while (!nextAttack.isEmpty()) {
				attack(i, nextAttack.poll(), leftWeap, rightWeap);
			}
		}
	}

	/**
	 * 
	 * Determines who should attack, according to health, equipped weapon, and
	 * if the unit is healing
	 *
	 * @param a the attacker
	 * @param d the defender
	 * @param range the range
	 * @return true, if a is able to attack d
	 */
	public static boolean shouldAttack(Unit a, Unit d, Weapon aWeap, int range) {
		if (a.getHp() <= 0)
			return false;
		if (aWeap == null)
			return false;
		if (aWeap.getUses() == 0)
			return false;
		if (!aWeap.range.apply(a.getStats()).contains(range))
			return false;
		if (aWeap.type == Weapon.Type.STAFF)
			return false;
		return true;
	}

	/**
	 * Adds the attack.
	 *
	 * @param effect the effect
	 */
	public void addAttack(String effect) {
		nextAttack.add(effect);
	}

	/**
	 * Attack. Does most of the heavy lifting in this class, calculates the
	 * damage dealt and damage healed, if any.
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
	 * </pre>
	 *
	 * @param leftAttacking If the left fighter is attacking
	 * @param currentEffect the current effect
	 */
	private void attack(boolean leftAttacking, String currentEffect, Weapon leftWeap, Weapon rightWeap) {
		Unit a = leftAttacking ? left : right;
		Unit d = leftAttacking ? right : left;
		Weapon aWeap = leftAttacking ? leftWeap : rightWeap;
		List<CombatTrigger> aTriggers = leftAttacking ? leftTriggers : rightTriggers;
		List<CombatTrigger> dTriggers = leftAttacking ? rightTriggers : leftTriggers;
		if (!shouldAttack(a, d, aWeap, range))
			return;
		int damage = 0;
		int drain = 0;
		String animation = "Attack";
		boolean miss = false;
		boolean use = false;
		int crit = 1;

		if (a.getHp() == 0 || d.getHp() == 0) {
			return;
		}

		LinkedHashMap<CombatTrigger, Boolean> aSuccess = new LinkedHashMap<CombatTrigger, Boolean>();
		LinkedHashMap<CombatTrigger, Boolean> dSuccess = new LinkedHashMap<CombatTrigger, Boolean>();

		for (CombatTrigger t : aTriggers) {
			aSuccess.put(t, t.attempt(a, range, d));
		}
		for (CombatTrigger t : dTriggers) {
			dSuccess.put(t, t.attempt(d, range, a));
		}

		for (CombatTrigger t : aSuccess.keySet()) {
			if (aSuccess.get(t) && (t.turnToRun & CombatTrigger.YOUR_TURN_PRE) != 0) {
				t.runPreAttack(this, a, d);
				if (t.nameModification == CombatTrigger.REPLACE_NAME_AFTER_PRE) {
					animation = t.getName() + "(a)";
				}
			}
		}
		for (CombatTrigger t : dSuccess.keySet()) {
			if (dSuccess.get(t) && (t.turnToRun & CombatTrigger.ENEMY_TURN_PRE) != 0) {
				t.runPreAttack(this, a, d);
				if (t.nameModification == CombatTrigger.REPLACE_NAME_AFTER_PRE) {
					animation = t.getName() + "(d)";
				}
			}
		}

		if (!((RNG.get() + RNG.get()) / 2 < hitRate(a, d))) {
			miss = true;
			if (a.getWeapon().isMagic())
				use = true;
		} else {
			use = true;
		}

		if (RNG.get() < a.crit() - d.dodge() && !miss) {
			crit = 3;
			animation += " Critical(a)";
		}

		damage = calculateBaseDamage(a, d) * crit;
		for (CombatTrigger t : aSuccess.keySet()) {
			if (aSuccess.get(t) && (t.turnToRun & CombatTrigger.YOUR_TURN_MOD) != 0) {
				int oldDamage = damage;
				damage = t.runDamageMod(a, d, damage);
				if (t.nameModification == CombatTrigger.APPEND_NAME_AFTER_MOD && damage != oldDamage && !miss) {
					animation += " " + t.getName() + "(a)";
				}
			}
		}
		for (CombatTrigger t : dSuccess.keySet()) {
			if (dSuccess.get(t) && (t.turnToRun & CombatTrigger.ENEMY_TURN_MOD) != 0) {
				int oldDamage = damage;
				damage = t.runDamageMod(a, d, damage);
				if (t.nameModification == CombatTrigger.APPEND_NAME_AFTER_MOD && damage != oldDamage && !miss) {
					animation += " " + t.getName() + "(d)";
				}
			}
		}
		damage = Math.max(0, Math.min(damage, d.getHp()));

		for (CombatTrigger t : aSuccess.keySet()) {
			if (aSuccess.get(t) && (t.turnToRun & CombatTrigger.YOUR_TURN_DRAIN) != 0) {
				drain = t.runDrain(a, d, damage);
			}
		}
		for (CombatTrigger t : dSuccess.keySet()) {
			if (dSuccess.get(t) && (t.turnToRun & CombatTrigger.ENEMY_TURN_DRAIN) != 0) {
				drain = t.runDrain(a, d, damage);
			}
		}

		if (miss) {
			damage = 0;
			drain = 0;
			animation += " Miss";
		}

		damage = Math.max(0, Math.min(damage, d.getHp()));
		addToAttackQueue(a, d, animation, damage, drain);
		d.setHp(d.getHp() - damage);
		a.setHp(a.getHp() + drain);
		if (use)
			a.use(a.getWeapon());
		a.clearTempMods();
		d.clearTempMods();
		for (CombatTrigger t : aSuccess.keySet()) {
			if (aSuccess.get(t) && (t.turnToRun & CombatTrigger.YOUR_TURN_POST) != 0) {
				t.runPostAttack(this, leftAttacking, a, d, damage, currentEffect);
			}
		}
		for (CombatTrigger t : dSuccess.keySet()) {
			if (dSuccess.get(t) && (t.turnToRun & CombatTrigger.ENEMY_TURN_POST) != 0) {
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
	public ArrayList<AttackRecord> getAttackQueue() {
		return attackQueue;
	}

	/**
	 * Calculates base damage. This is damage excluding any combat triggers
	 *
	 * @param a the attacker
	 * @param d the defender
	 * @return the base damage
	 */
	public static int calculateBaseDamage(Unit a, Unit d) {
		boolean effective = a.getWeapon().effective.contains(d.noGenderName());

		int base;
		if (a.getWeapon().isMagic()) {
			base = a.getStats().mag + (a.getWeapon().mt + a.getWeapon().triMod(d.getWeapon())) * (effective ? 3 : 1)
			        - d.getStats().res;
		} else {
			base = a.getStats().str + (a.getWeapon().mt + a.getWeapon().triMod(d.getWeapon())) * (effective ? 3 : 1)
			        - d.getStats().def;
		}

		return Math.max(base, 0);
	}

	/**
	 * Calculates a damage amount that can be shown in a damage preview.
	 * Includes triggers marked as SHOW_IN_PREVIEW
	 * 
	 * @param a the attacker
	 * @param d the defender
	 * @return the base damage
	 */
	public static int calculatePreviewDamage(Unit a, Unit d) {
		// run preAttack triggers that are allowed to be shown in the preview
		for (CombatTrigger t : a.getTriggers()) {
			if (((t.turnToRun & CombatTrigger.SHOW_IN_PREVIEW) != 0)
			        && ((t.turnToRun & CombatTrigger.YOUR_TURN_PRE) != 0)) {
				t.runPreAttack(null, a, d);
			}
		}
		for (CombatTrigger t : d.getTriggers()) {
			if (((t.turnToRun & CombatTrigger.SHOW_IN_PREVIEW) != 0)
			        && ((t.turnToRun & CombatTrigger.ENEMY_TURN_PRE) != 0)) {
				t.runPreAttack(null, a, d);
			}
		}

		int base = CombatCalculator.calculateBaseDamage(a, d);

		// Run combat mods that are allowed to occur in the preview
		for (CombatTrigger t : a.getTriggers()) {
			if (((t.turnToRun & CombatTrigger.SHOW_IN_PREVIEW) != 0)
			        && ((t.turnToRun & CombatTrigger.YOUR_TURN_MOD) != 0)) {
				base = t.runDamageMod(a, d, base);
			}
		}
		for (CombatTrigger t : d.getTriggers()) {
			if (((t.turnToRun & CombatTrigger.SHOW_IN_PREVIEW) != 0)
			        && ((t.turnToRun & CombatTrigger.ENEMY_TURN_MOD) != 0)) {
				base = t.runDamageMod(a, d, base);
			}
		}

		// cleanup
		a.clearTempMods();
		d.clearTempMods();

		return Math.max(base, 0);
	}

	/**
	 * Hit rate.
	 *
	 * @param a the attacker
	 * @param d the defender
	 * @return the hit rate
	 */
	public static int hitRate(Unit a, Unit d) {
		return a.hit() - d.avoid() + a.getWeapon().triMod(d.getWeapon()) * 15;
	}

	public static int hitRate(Unit a, Unit d, Double mod) {
		return ((Double) (a.hit() * mod)).intValue() - d.avoid() + a.getWeapon().triMod(d.getWeapon()) * 15;
	}

}
