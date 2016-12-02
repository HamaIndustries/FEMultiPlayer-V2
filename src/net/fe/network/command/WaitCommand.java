package net.fe.network.command;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

import net.fe.overworldStage.OverworldStage;
import net.fe.game.fightStage.AttackRecord;
import net.fe.game.unit.Item;
import net.fe.game.unit.RiseTome;
import net.fe.game.unit.Unit;
import net.fe.game.unit.UnitIdentifier;
import net.fe.overworldStage.ClientOverworldStage;
import net.fe.overworldStage.Path;
import net.fe.overworldStage.Node;
import net.fe.overworldStage.Healthbar;

import java.util.Optional;

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
