package net.fe.overworldStage.context;

import net.fe.game.unit.Unit;
import net.fe.game.unit.UnitIdentifier;
import net.fe.network.command.TakeCommand;
import net.fe.overworldStage.ClientOverworldStage;
import net.fe.overworldStage.OverworldContext;
import net.fe.overworldStage.SelectTargetContext;
import net.fe.overworldStage.Zone;

// TODO: Auto-generated Javadoc
/**
 * The Class GiveTarget.
 */
public class GiveTarget extends SelectTargetContext {

	private Unit unit;

	/**
	 * Instantiates a new give target.
	 *
	 * @param stage the stage
	 * @param context the context
	 * @param z the z
	 * @param u the u
	 */
	public GiveTarget(ClientOverworldStage stage, OverworldContext context, Zone z, Unit u) {
		super(stage, context, z, u, true);
		unit = u;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.fe.overworldStage.SelectTargetContext#validTarget(net.fe.unit.Unit)
	 */
	public boolean validTarget(Unit u) {
		return super.validTarget(u) && u.rescuedUnit() == null && u.canRescue(unit.rescuedUnit());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.fe.overworldStage.SelectTargetContext#unitSelected(net.fe.unit.Unit)
	 */
	@Override
	public void unitSelected(Unit u) {
		stage.addCmd(new TakeCommand(new UnitIdentifier(u)));
		stage.send();
		unit.setMoved(true);
		unit.give(u);
		cursor.setXCoord(unit.getXCoord());
		cursor.setYCoord(unit.getYCoord());
		stage.reset();
	}

}
