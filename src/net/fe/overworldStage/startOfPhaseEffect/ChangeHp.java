package net.fe.overworldStage.startOfPhaseEffect;

import java.util.function.UnaryOperator;
import net.fe.unit.Unit;
import net.fe.overworldStage.ClientOverworldStage;
import net.fe.overworldStage.Healthbar;
import net.fe.overworldStage.StartOfPhaseEffect;

/**
 * A StartOfPhaseEffect that changes a unit's hit points
 */
public final class ChangeHp implements StartOfPhaseEffect {
	private static final long serialVersionUID = 1L;
	
	public final int constant;
	public final int percent;
	
	/**
	 * @param constant a constant amount to change a unit's hp by
	 * @param percent the percent of a unit's max hp to change its current hp by
	 */
	public ChangeHp(int constant, int percent) {
		this.constant = constant;
		this.percent = percent;
	}
	
	private int newHp(Unit unit) {
		final int maxHeal = constant + unit.getStats().maxHp * percent / 100;
		final int newHp = Math.min(unit.getStats().maxHp, unit.getHp() + maxHeal);
		return newHp;
	}
	
	@Override public void apply(Unit unit) {
		unit.setHp(newHp(unit));
	}
	
	@Override public void animation(Unit unit, ClientOverworldStage stage) {
		final int oldHp = unit.getHp();
		final int newHp = newHp(unit);
		
		// only perform the animation if any HP was actually healed
		if (oldHp != newHp) {
			stage.addEntity(new Healthbar(unit, oldHp, newHp, stage){
				@Override public void done() {
					destroy();
				}
			});
		}
	}
	
	@Override public int hashCode() { return (int) (serialVersionUID * 3100 + constant * 100 + percent); }
	@Override public boolean equals(Object other) {
		if (other instanceof ChangeHp) {
			ChangeHp other2 = (ChangeHp) other;
			return (other2.percent == this.percent &&
				other2.constant == this.constant);
		} else {
			return false;
		}
	}
}
