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
	
	/** The Constant NO_NAME_MOD. */
	public static final int NO_NAME_MOD = 0;
	
	/** The Constant REPLACE_NAME_AFTER_PRE. */
	public static final int REPLACE_NAME_AFTER_PRE = 1;
	
	/** The Constant APPEND_NAME_AFTER_MOD. */
	public static final int APPEND_NAME_AFTER_MOD = 2;
	
	/** The Constant YOUR_TURN_PRE. */
	public static final int YOUR_TURN_PRE = 0x1;
	
	/** The Constant ENEMY_TURN_PRE. */
	public static final int ENEMY_TURN_PRE = 0x2;
	
	/** The Constant YOUR_TURN_MOD. */
	public static final int YOUR_TURN_MOD = 0x4;
	
	/** The Constant ENEMY_TURN_MOD. */
	public static final int ENEMY_TURN_MOD = 0x8;
	
	/** The Constant YOUR_TURN_POST. */
	public static final int YOUR_TURN_POST = 0x10;
	
	/** The Constant ENEMY_TURN_POST. */
	public static final int ENEMY_TURN_POST = 0x20;
	
	/** The Constant YOUR_TURN_DRAIN. */
	public static final int YOUR_TURN_DRAIN = 0x40;
	
	/** The Constant ENEMY_TURN_DRAIN. */
	public static final int ENEMY_TURN_DRAIN = 0x80;
	
	/**
	 * Instantiates a new combat trigger.
	 *
	 * @param mod the mod
	 * @param turn the turn
	 * @param attacks the attacks
	 */
	public CombatTrigger(int mod, int turn, String... attacks){
		nameModification = mod;
		turnToRun = turn;
		attackAnims = attacks;
	}
	
	/**
	 * Attempt.
	 *
	 * @param user the user
	 * @param range the range
	 * @param opponent TODO
	 * @return true, if successful
	 */
	public abstract boolean attempt(Unit user, int range, Unit opponent);
	
	/**
	 * Run pre attack.
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
	 * @param damage the damage
	 * @return the int
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
}
