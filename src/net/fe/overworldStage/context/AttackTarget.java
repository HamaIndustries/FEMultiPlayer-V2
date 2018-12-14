package net.fe.overworldStage.context;

import net.fe.fightStage.CombatTrigger;
import net.fe.overworldStage.*;
import net.fe.unit.Unit;

// TODO: Auto-generated Javadoc
/**
 * The Class AttackTarget.
 */
public class AttackTarget extends SelectTargetContext {
	
	/** The Combat Triggers added by the attacker based on a field skill selection */
	private final Iterable<CombatTrigger> manualTriggers;
	
	/**
	 * Instantiates a new attack target.
	 *
	 * @param stage the stage
	 * @param context the context
	 * @param z the z
	 * @param u the u
	 * @param mt The Combat Triggers added by the attacker based on a field skill selection
	 */
	public AttackTarget(ClientOverworldStage stage, OverworldContext context, Zone z,
			Unit u, Iterable<CombatTrigger> mt) {
		super(stage, context, z, u, false);
		this.manualTriggers = mt;
	}

	/* (non-Javadoc)
	 * @see net.fe.overworldStage.SelectTargetContext#updateCursor()
	 */
	@Override
	public void updateCursor() {
		super.updateCursor();
	}

	/* (non-Javadoc)
	 * @see net.fe.overworldStage.SelectTargetContext#unitSelected(net.fe.unit.Unit)
	 */
	@Override
	public void unitSelected(Unit u) {
		new AttackPreview(stage, this, unit, u, manualTriggers).startContext();
	}

}
