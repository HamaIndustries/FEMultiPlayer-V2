package net.fe.fightStage;

import java.io.Serializable;

import net.fe.unit.Unit;

// TODO: Auto-generated Javadoc
/**
 * The Class CombatTrigger.
 */
public abstract class CombatTrigger implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1328411947653920427L;
	
	/** The name modification. */
	public final int nameModification;
	
	/** The turn to run. */
	public final int turnToRun;
	
	/** The attack anims. */
	public final String[] attackAnims;
	
	/** A constant value for use in nameModification */
	public static final int NO_NAME_MOD = 0;
	
	/** A constant value for use in nameModification */
	public static final int REPLACE_NAME_AFTER_PRE = 1;
	
	/** A constant value for use in nameModification */
	public static final int APPEND_NAME_AFTER_MOD = 2;
	
	/** A constant value for use in turnToRun */
	public static final int YOUR_TURN_PRE = 0x1;
	
	/** A constant value for use in turnToRun */
	public static final int ENEMY_TURN_PRE = 0x2;
	
	/** A constant value for use in turnToRun */
	public static final int YOUR_TURN_MOD = 0x4;
	
	/** A constant value for use in turnToRun */
	public static final int ENEMY_TURN_MOD = 0x8;
	
	/** A constant value for use in turnToRun */
	public static final int YOUR_TURN_POST = 0x10;
	
	/** A constant value for use in turnToRun */
	public static final int ENEMY_TURN_POST = 0x20;
	
	/** A constant value for use in turnToRun */
	public static final int YOUR_TURN_DRAIN = 0x40;
	
	/** A constant value for use in turnToRun */
	public static final int ENEMY_TURN_DRAIN = 0x80;
	
	/**
	 * A turnToRun constant that indicates that this trigger should have its 
	 * preAttack and CombatMod effects shown in the preview pane. In general,
	 * this is true iff [[#attempt]] always returns true, runPreAttack has no
	 * side effects other than setting a or d's tempMods, and runDamageMod has
	 * no side effects. The other turnToRun constants are taken into account.
	 */
	public static final int SHOW_IN_PREVIEW = 0x100;
	
	/**
	 * Instantiates a new combat trigger.
	 *
	 * @param mod nameModification
	 * @param turn when to run the trigger
	 * @param attacks the name of animations associated with this trigger
	 */
	public CombatTrigger(int mod, int turn, String... attacks){
		nameModification = mod;
		turnToRun = turn;
		attackAnims = attacks;
	}
	
	/**
	 * Determine whether this trigger should take effect this combat
	 *
	 * @param user the user
	 * @param range the range
	 * @param opponent the enemy
	 * @return true, if successful
	 */
	public abstract boolean attempt(Unit user, int range, Unit opponent);
	
	/**
	 * Something to do before the attack occurs. Modify statistics, for example.
	 *
	 * @param calc the calc
	 * @param a the a
	 * @param d the d
	 * @return true, if successful
	 */
	public boolean runPreAttack(CombatCalculator calc, Unit a, Unit d){
		return true;
	}
	
	/**
	 * Run damage mod.
	 *
	 * @param a the a
	 * @param d the d
	 * @param damage the amount of damage dealt, before modification
	 * @return the amount of damage dealt, after modification
	 */
	public int runDamageMod(Unit a, Unit d, int damage){
		return damage;
	}
	
	/**
	 * Run drain.
	 *
	 * @param a the a
	 * @param d the d
	 * @param damage the damage
	 * @return the int
	 */
	public int runDrain(Unit a, Unit d, int damage){
		return 0;
	}
	
	/**
	 * Run post attack.
	 *
	 * @param calc the calc
	 * @param dir the dir
	 * @param a the a
	 * @param d the d
	 * @param damage the damage
	 * @param currentEffect the current effect
	 */
	public void runPostAttack(CombatCalculator calc, boolean dir, Unit a, Unit d, int damage, String currentEffect){
		
	}
	
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName(){
		return getClass().getSimpleName();
	}
	
	/**
	 * Gets the copy.
	 *
	 * @return the copy
	 */
	public abstract CombatTrigger getCopy();
	
	
	protected boolean canEquals(Object other) {
		return other instanceof CombatTrigger;
	}
	@Override
	public boolean equals(Object other) {
		return other instanceof CombatTrigger &&
			((CombatTrigger) other).canEquals(this) &&
			this.getClass().equals(other.getClass());
	}
	@Override public int hashCode() { return this.getClass().getName().hashCode(); }
	@Override public String toString() { return this.getClass().getName(); }
}
