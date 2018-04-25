package net.fe.network.command;

import java.util.ArrayList;

import net.fe.FEMultiplayer;
import net.fe.fightStage.AttackRecord;
import net.fe.overworldStage.OverworldStage;
import net.fe.overworldStage.ClientOverworldStage;
import net.fe.overworldStage.ExclamationEmote;
import net.fe.overworldStage.Node;
import net.fe.unit.Unit;

/**
 * A command that indicates that a unit's move is cut short by an attempt to
 * walk though an enemy unit in fog-of-war.
 * 
 * It's semantically identical to WaitCommand, but will cause other clients to
 * show the ExclamationEmote.
 */
public final class InterruptedCommand extends Command {
	
	private static final long serialVersionUID = 1L;
	private final Node blockedLocation;
	
	/**
	 * @param blockedLocation the location that a unit was blocked from moving into
	 */
	public InterruptedCommand(Node blockedLocation) {
		this.blockedLocation = blockedLocation;
	}
	
	@Override
	public ArrayList<AttackRecord> applyServer(OverworldStage stage, Unit primaryUnit) {
		return null;
	}
	
	@Override
	public Runnable applyClient(ClientOverworldStage stage, Unit primaryUnit, ArrayList<AttackRecord> attackRecords, Runnable callback) {
		return () -> {
			if(!stage.getFog().contains(blockedLocation) &&
					!stage.getFog().contains(new Node(primaryUnit.getOrigX(), primaryUnit.getOrigY())) ||
					FEMultiplayer.getSession().alwaysShowInterruptions()) {
				stage.includeInView(blockedLocation);
				stage.addEntity(new ExclamationEmote(primaryUnit, stage, callback));
			}
		};
	}
	
	@Override
	public String toString() {
		return "Interrupted[" + blockedLocation + "]";
	}
}
