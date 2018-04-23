package net.fe.overworldStage.startOfPhaseEffect;

import java.util.function.UnaryOperator;
import net.fe.unit.Unit;
import net.fe.overworldStage.ClientOverworldStage;
import net.fe.overworldStage.SkillChargeBar;
import net.fe.overworldStage.StartOfPhaseEffect;

/**
 * A StartOfPhaseEffect that changes a unit's skill charge
 */
public final class ChangeSkillCharge implements StartOfPhaseEffect {
	private static final long serialVersionUID = 1L;
	
	public final int constant;
	
	/**
	 * @param constant a constant amount to change a unit's skillCharge by
	 */
	public ChangeSkillCharge(int constant) {
		this.constant = constant;
	}
	
	@Override public void apply(Unit unit) {
		unit.incrementSkillCharge(constant);
	}
	
	@Override public void animation(Unit unit, ClientOverworldStage stage) {
		final int oldMp = unit.getSkillCharge();
		final int newMp = Math.max(0, Math.min(Unit.MAX_SKILL_CHARGE, oldMp + constant));
		
		// only perform the animation if any HP was actually healed
		if (oldMp != newMp) {
			stage.addEntity(new SkillChargeBar(unit, oldMp, newMp, stage, () -> {}));
		}
	}
	
	@Override public int hashCode() { return (int) (serialVersionUID * 3100 + constant); }
	@Override public boolean equals(Object other) {
		if (other instanceof ChangeSkillCharge) {
			ChangeSkillCharge other2 = (ChangeSkillCharge) other;
			return (other2.constant == this.constant);
		} else {
			return false;
		}
	}
}
