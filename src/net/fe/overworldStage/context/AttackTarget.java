package net.fe.overworldStage.context;

import net.fe.game.unit.Unit;
import net.fe.overworldStage.*;

// TODO: Auto-generated Javadoc
/**
 * The Class AttackTarget.
 */
public class AttackTarget extends SelectTargetContext {
	
	/**
	 * Instantiates a new attack target.
	 *
	 * @param stage the stage
	 * @param context the context
	 * @param z the z
	 * @param u the u
	 */
	public AttackTarget(ClientOverworldStage stage, OverworldContext context, Zone z,
			Unit u) {
		super(stage, context, z, u, false);
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
		new AttackPreview(stage, this, unit, u).startContext();
	}

}
