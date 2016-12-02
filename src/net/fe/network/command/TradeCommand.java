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
import net.fe.overworldStage.context.TradeContext;

import java.util.Optional;

public final class TradeCommand extends Command {

	private static final long serialVersionUID = 6468268282716381357L;

	private final UnitIdentifier leftUnitId;
	private final int leftItemIndex;
	private final UnitIdentifier rightUnitId;
	private final int rightItemIndex;

	public TradeCommand(UnitIdentifier leftUnitId, int leftItemIndex, UnitIdentifier rightUnitId, int rightItemIndex) {
		this.leftUnitId = leftUnitId;
		this.leftItemIndex = leftItemIndex;
		this.rightUnitId = rightUnitId;
		this.rightItemIndex = rightItemIndex;
	}

	@Override
	public ArrayList<AttackRecord> applyServer(OverworldStage stage, Unit primaryUnit) {

		Unit leftUnit = stage.getUnit(leftUnitId);
		Unit rightUnit = stage.getUnit(rightUnitId);
		TradeContext.doTrade(leftUnit.getInventory(), rightUnit.getInventory(), leftItemIndex, rightItemIndex);
		return null;
	}

	@Override
	public Runnable applyClient(ClientOverworldStage stage, Unit primaryUnit, ArrayList<AttackRecord> attackRecords,
	        Runnable callback) {

		return new Runnable() {
			public void run() {
				Unit leftUnit = stage.getUnit(leftUnitId);
				Unit rightUnit = stage.getUnit(rightUnitId);
				TradeContext.doTrade(leftUnit.getInventory(), rightUnit.getInventory(), leftItemIndex, rightItemIndex);
				callback.run();
			}
		};
	}

	@Override
	public String toString() {
		return "Trade[" + leftUnitId + "," + leftItemIndex + ":" + rightUnitId + "," + rightItemIndex + "]";
	}
}
