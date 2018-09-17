package net.fe.fightStage;

import java.util.ArrayList;
import java.util.List;

import net.fe.rng.RNG;
import net.fe.unit.Unit;

/**
 * Multiplies a weapon's str by a given amount
 * 
 * Implemented by increasing the user's mag and str by a multiple of the
 * equipped unit's weapon's might
 * 
 * Chance: 100%
 */
public final class Effective extends CombatTrigger{
	private static final long serialVersionUID = 1L;
	
	/** The might multiplier */
	public final int multiplier;
	/** The unit classes that this triggers against */
	public final List<String> classes;
	
	/**
	 */
	public Effective(int multiplier, List<String> classes){
		super(NO_NAME_MOD, YOUR_TURN_PRE | SHOW_IN_PREVIEW);
		this.multiplier = multiplier;
		this.classes = java.util.Collections.unmodifiableList(new ArrayList<String>(classes));
	}
	
	@Override
	public boolean attempt(Unit user, int range, Unit opponent, RNG rng) {
		return classes.contains(opponent.noGenderName());
	}

	@Override
	public boolean runPreAttack(CombatCalculator stage, Unit a, Unit d) {
		// Apply the triangle modifier before multiplying weapon mt 
		final int effectiveMt = (a.getWeapon().mt + a.getWeapon().triMod(d.getWeapon()));
		final int delta = effectiveMt * (multiplier - 1);
		
		a.setTempMod("Str", delta);
		a.setTempMod("Mag", delta);
		return true;
	}
	
	public CombatTrigger getCopy(){
		return new Effective(this.multiplier, this.classes);
	}
}
