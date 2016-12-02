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

import java.util.Optional;

public final class TakeCommand extends Command {

	private static final long serialVersionUID = 6468268282716381357L;

	private final UnitIdentifier otherId;

	public TakeCommand(UnitIdentifier otherId) {
		this.otherId = otherId;
	}

	@Override
	public ArrayList<AttackRecord> applyServer(OverworldStage stage, Unit unit) {

		Unit other = stage.getUnit(otherId);
		other.give(unit); // throws IllegalStateException
		return null;
	}

	@Override
	public Runnable applyClient(ClientOverworldStage stage, Unit unit, ArrayList<AttackRecord> attackRecords,
	        Runnable callback) {

		return new Runnable() {
			public void run() {
				Unit other = stage.getUnit(otherId);
				other.give(unit);
				stage.checkEndGame();
				callback.run();
			}
		};
	}

	@Override
	public String toString() {
		return "Take[" + otherId + "]";
	}
}
