package net.fe.overworldStage;

import java.io.Serializable;
import java.util.function.UnaryOperator;
import net.fe.unit.Unit;

/**
 * A skill which has an effect at the start of a unit's player's phase
 */
public interface StartOfPhaseEffect extends Serializable {
	
	/**
	 * Perform the start-of-turn effect
	 * @param unit the unit to perform the effect upon
	 */
	public void apply(Unit unit);
	
	/**
	 * Perform an animation representing this effect.
	 * It does not actually perform the effect.
	 * @param unit the unit to perform the effect upon
	 * @param stage the stage upon which to animate
	 */
	public void animation(Unit unit, ClientOverworldStage stage);
}
