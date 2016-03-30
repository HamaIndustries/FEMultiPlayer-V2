package net.fe.overworldStage.context;

import net.fe.overworldStage.ClientOverworldStage;
import net.fe.overworldStage.OverworldContext;
import net.fe.overworldStage.SelectTargetContext;
import net.fe.overworldStage.Zone;
import net.fe.unit.Unit;
import net.fe.unit.UnitIdentifier;

public class StealTarget extends SelectTargetContext {

	public StealTarget(ClientOverworldStage stage, OverworldContext context, Zone z, Unit u) {
		super(stage, context, z, u, true);
	}
	
	public boolean validTarget(Unit u){
		return !super.validTarget(u);
	}
	
	@Override
	public void unitSelected(Unit u) {

		stage.addCmd("STEAL");
		stage.addCmd(new UnitIdentifier(u));
		stage.send();
		
		unit.setMoved(true);
		cursor.setXCoord(unit.getXCoord());
		cursor.setYCoord(unit.getYCoord());
		stage.reset();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
