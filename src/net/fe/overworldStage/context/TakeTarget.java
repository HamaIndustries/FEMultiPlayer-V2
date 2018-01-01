package net.fe.overworldStage.context;

import net.fe.overworldStage.ClientOverworldStage;
import net.fe.overworldStage.OverworldContext;
import net.fe.overworldStage.SelectTargetContext;
import net.fe.overworldStage.Zone;
import net.fe.unit.Unit;
import net.fe.unit.UnitIdentifier;
import net.fe.network.command.TakeCommand;

// TODO: Auto-generated Javadoc
/**
 * The Class TakeTarget.
 */
public class TakeTarget extends SelectTargetContext {

	private Unit unit;
	
	/**
	 * Instantiates a new take target.
	 *
	 * @param stage the stage
	 * @param context the context
	 * @param z the z
	 * @param u the u
	 */
	public TakeTarget(ClientOverworldStage stage, OverworldContext context, Zone z,
			Unit u) {
		super(stage, context, z, u, true);
		unit = u;
	}
	
	/* (non-Javadoc)
	 * @see net.fe.overworldStage.SelectTargetContext#validTarget(net.fe.unit.Unit)
	 */
	public boolean validTarget(Unit u){
		return super.validTarget(u) && u.rescuedUnit() != null && unit.canRescue(u.rescuedUnit());
	}

	/* (non-Javadoc)
	 * @see net.fe.overworldStage.SelectTargetContext#unitSelected(net.fe.unit.Unit)
	 */
	@Override
	public void unitSelected(Unit u) {
		TakeCommand c = new TakeCommand(new UnitIdentifier(u));
		stage.addCmd(c);
		c.applyClient(stage, unit, null, new EmptyRunnable()).run();
		cursor.setXCoord(unit.getXCoord());
		cursor.setYCoord(unit.getYCoord());
		stage.setUnitInfoUnit(unit);
		new UnitMoved(stage, this, unit, false, true).startContext();
	}

	private static final class EmptyRunnable implements Runnable {
		@Override public void run() {}
	}
}
