package net.fe.fightStage;

import java.util.ArrayList;
import java.util.List;

import net.fe.rng.RNG;
import net.fe.unit.Unit;

/**
 * Multiplies a weapon's mt by a given amount when fighting a unit with a
 * specified other class
 * 
 * Implemented by increasing the user's mag and str by a multiple of the
 * equipped unit's weapon's might
 * 
 * Condition: enemy class in contained in this's list of classes
 * Chance: 100%
 */
public final class Effective extends CombatTrigger{
	private static final long serialVersionUID = 1L;
	
	/** The might multiplier */
	public final int multiplier;
	/** The unit classes that this triggers against */
	public final List<String> classes;
	
	/**
	 * The parameters match this's public fields, modulo some defensive copying
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
	
	@Override
	public CombatTrigger getCopy(){
		return new Effective(this.multiplier, this.classes);
	}
	
	@Override
	protected boolean canEquals(Object other) {
		return other instanceof Effective;
	}
	@Override
	public boolean equals(Object other) {
		return other instanceof Effective &&
			((Effective) other).canEquals(this) &&
			((Effective) other).multiplier == this.multiplier &&
			((Effective) other).classes.equals(this.classes);
	}
	@Override public int hashCode() { return this.getName().hashCode() * 31 + multiplier * 31 + this.classes.hashCode(); }
	@Override public String toString() { return this.getName() + " x" + multiplier + " " + this.classes.toString(); }
}
