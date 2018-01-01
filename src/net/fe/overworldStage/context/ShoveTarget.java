package net.fe.overworldStage.context;

import net.fe.overworldStage.OverworldContext;
import net.fe.overworldStage.ClientOverworldStage;
import net.fe.overworldStage.SelectTargetContext;
import net.fe.overworldStage.Zone;
import net.fe.unit.Unit;
import net.fe.unit.UnitIdentifier;
import net.fe.network.command.ShoveCommand;

/**
 * An overworld context in which a target for the Shove action is selected
 */
public final class ShoveTarget extends SelectTargetContext {
	
	/**
	 * Instantiates a new shove target context.
	 *
	 * @param stage the stage
	 * @param context the parent context
	 * @param z the zone
	 * @param u the selected unit which will do the shoving
	 */
	public ShoveTarget(ClientOverworldStage stage, OverworldContext context, Zone z,
			Unit u) {
		super(stage, context, z, u, true);
	}
	
	@Override
	public boolean validTarget(Unit u){
		// can shove either allies or enemies, as long as the con is sufficiently high
		return net.fe.overworldStage.fieldskill.Shove.canShove(this.stage.grid, this.unit, u);
	}
	
	@Override
	public void unitSelected(Unit u) {
		ShoveCommand c = new ShoveCommand(new UnitIdentifier(u));
		c.applyClient(stage, unit, null, new EmptyRunnable()).run();
		stage.addCmd(c);
		stage.send();
		cursor.setXCoord(unit.getXCoord());
		cursor.setYCoord(unit.getYCoord());
		stage.setUnitInfoUnit(unit);
		stage.reset();
	}

	private static final class EmptyRunnable implements Runnable {
		@Override public void run() {}
	}
}
