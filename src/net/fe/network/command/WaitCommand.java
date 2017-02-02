package net.fe.network.command;

import java.util.ArrayList;

import net.fe.game.fightStage.AttackRecord;
import net.fe.game.unit.Unit;
import net.fe.overworldStage.ClientOverworldStage;
import net.fe.overworldStage.OverworldStage;

/** a no-op */
public final class WaitCommand extends Command {

	private static final long serialVersionUID = 6468268282716381357L;

	public WaitCommand() {
	}

	@Override
	public ArrayList<AttackRecord> applyServer(OverworldStage stage, Unit primaryUnit) {
		return null;
	}

	@Override
	public Runnable applyClient(ClientOverworldStage stage, Unit primaryUnit, ArrayList<AttackRecord> attackRecords,
	        Runnable callback) {

		return callback;
	}

	@Override
	public String toString() {
		return "Wait[]";
	}
}
