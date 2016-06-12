package net.fe.overworldStage.context;

import net.fe.overworldStage.ClientOverworldStage;
import net.fe.overworldStage.Node;
import net.fe.overworldStage.OverworldContext;
import net.fe.overworldStage.SelectTargetContext;
import net.fe.overworldStage.Zone;
import net.fe.unit.Unit;
import net.fe.unit.UnitIdentifier;
import net.fe.network.command.RescueCommand;

// TODO: Auto-generated Javadoc
/**
 * The Class RescueTarget.
 */
public class RescueTarget extends SelectTargetContext {

	private Unit unit;
	/**
	 * Instantiates a new rescue target.
	 *
	 * @param stage the stage
	 * @param context the context
	 * @param z the z
	 * @param u the u
	 */
	public RescueTarget(ClientOverworldStage stage, OverworldContext context, Zone z,
			Unit u) {
		super(stage, context, z, u, true);
		unit = u;
	}
	
	/* (non-Javadoc)
	 * @see net.fe.overworldStage.SelectTargetContext#validTarget(net.fe.unit.Unit)
	 */
	public boolean validTarget(Unit p){
		return super.validTarget(p) && p.rescuedUnit() == null && unit.canRescue(p);
	}

	/* (non-Javadoc)
	 * @see net.fe.overworldStage.SelectTargetContext#unitSelected(net.fe.unit.Unit)
	 */
	@Override
	public void unitSelected(Unit u) {
		RescueCommand c = new RescueCommand(new UnitIdentifier(u));
		c.applyClient(stage, unit, null, new EmptyRunnable()).run();
		stage.addCmd(c);
		stage.send();
		cursor.setXCoord(unit.getXCoord());
		cursor.setYCoord(unit.getYCoord());
		stage.reset();
	}

	private static final class EmptyRunnable implements Runnable {
		@Override public void run() {}
	}
}
