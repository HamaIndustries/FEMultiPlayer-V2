package net.fe.overworldStage.context;

import net.fe.overworldStage.OverworldContext;
import net.fe.overworldStage.ClientOverworldStage;
import net.fe.overworldStage.SelectTargetContext;
import net.fe.overworldStage.Zone;
import net.fe.unit.Unit;
import net.fe.unit.UnitIdentifier;
import net.fe.network.command.SmiteCommand;

/**
 * An overworld context in which a target for the Shove action is selected
 */
public final class SmiteTarget extends SelectTargetContext {

	/**
	 * Instantiates a new shove target context.
	 *
	 * @param stage the stage
	 * @param context the parent context
	 * @param z the zone
	 * @param u the selected unit which will do the shoving
	 */
	public SmiteTarget(ClientOverworldStage stage, OverworldContext context, Zone z,
			Unit u) {
		super(stage, context, z, u, true);
	}
	
	@Override
	public boolean validTarget(Unit u){
		// can shove either allies or enemies, as long as the con is sufficiently high
		return net.fe.overworldStage.fieldskill.Smite.canSmite(this.stage.grid, this.unit, u);
	}
	
	@Override
	public void unitSelected(Unit u) {
		SmiteCommand c = new SmiteCommand(new UnitIdentifier(u));
		stage.addCmd(c);
		c.applyClient(stage, unit, null, new EmptyRunnable()).run();
		stage.send();
		cursor.setXCoord(unit.getXCoord());
		cursor.setYCoord(unit.getYCoord());
		stage.reset();
	}

	private static final class EmptyRunnable implements Runnable {
		@Override public void run() {}
	}
}
