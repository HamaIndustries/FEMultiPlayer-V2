package net.fe.overworldStage.context;

import net.fe.overworldStage.ClientOverworldStage;
import net.fe.overworldStage.OverworldContext;
import net.fe.overworldStage.SelectTargetContext;
import net.fe.overworldStage.Zone;
import net.fe.unit.Unit;
import net.fe.unit.UnitIdentifier;

// TODO: Auto-generated Javadoc
/**
 * The Class RescueTarget.
 */
public class RescueTarget extends SelectTargetContext {

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
	}
	
	/* (non-Javadoc)
	 * @see net.fe.overworldStage.SelectTargetContext#validTarget(net.fe.unit.Unit)
	 */
	public boolean validTarget(Unit u){
		return super.validTarget(u) && u.rescuedUnit() == null;
	}

	/* (non-Javadoc)
	 * @see net.fe.overworldStage.SelectTargetContext#unitSelected(net.fe.unit.Unit)
	 */
	@Override
	public void unitSelected(Unit u) {
		stage.addCmd("RESCUE");
		stage.addCmd(new UnitIdentifier(u));
		stage.send();
		cursor.setXCoord(unit.getXCoord());
		cursor.setYCoord(unit.getYCoord());
		stage.reset();
	}

}
