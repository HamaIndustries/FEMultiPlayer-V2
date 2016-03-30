package net.fe.overworldStage.context;

import java.util.ArrayList;
import java.util.Arrays;

import net.fe.overworldStage.ClientOverworldStage;
import net.fe.overworldStage.Grid;
import net.fe.overworldStage.OverworldContext;
import net.fe.overworldStage.SelectTargetContext;
import net.fe.overworldStage.Zone;
import net.fe.unit.Unit;
import net.fe.unit.UnitIdentifier;
import net.fe.unit.Voice;
import net.fe.unit.Weapon;



public class SingTarget extends SelectTargetContext {
	
	private Zone zone;

	public SingTarget(ClientOverworldStage stage, OverworldContext context, Zone z, Unit u) {
		super(stage, context, z, u, true);
	}

	/* (non-Javadoc)
	 * @see net.fe.overworldStage.SelectTargetContext#validTarget(net.fe.unit.Unit)
	 */
	public boolean validTarget(Unit u){
		return super.validTarget(u) && u.hasMoved(); // repetitive, but safe.
	}
	
	/* (non-Javadoc)
	 * @see net.fe.overworldStage.SelectTargetContext#unitSelected(net.fe.unit.Unit)
	 */
	@Override
	public void unitSelected(Unit u) {

		stage.addCmd("SING");
		stage.addCmd(new UnitIdentifier(u));
		stage.send();
		
		unit.setMoved(true);
		cursor.setXCoord(unit.getXCoord());
		cursor.setYCoord(unit.getYCoord());
		stage.reset();
	}
}
