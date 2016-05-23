package net.fe.network.command;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import net.fe.fightStage.AttackRecord;
import net.fe.overworldStage.OverworldStage;
import net.fe.overworldStage.ClientOverworldStage;
import net.fe.overworldStage.Path;
import net.fe.overworldStage.Node;
import net.fe.overworldStage.Healthbar;
import net.fe.overworldStage.context.TradeContext;
import net.fe.unit.UnitIdentifier;
import net.fe.unit.Unit;
import net.fe.unit.Item;
import net.fe.unit.RiseTome;
import java.util.Optional;

public final class TradeCommand extends Command {
	
	private static final long serialVersionUID = 6468268282716381357L;
	
	private final UnitIdentifier leftUnitId;
	private final int leftItemIndex;
	private final UnitIdentifier rightUnitId;
	private final int rightItemIndex;
	
	public TradeCommand(
		UnitIdentifier leftUnitId,
		int leftItemIndex,
		UnitIdentifier rightUnitId,
		int rightItemIndex
	) {
		this.leftUnitId = leftUnitId;
		this.leftItemIndex = leftItemIndex;
		this.rightUnitId = rightUnitId;
		this.rightItemIndex = rightItemIndex;
	}
	
	public ArrayList<AttackRecord> applyServer(OverworldStage stage, Unit primaryUnit) {
		
		Unit leftUnit = stage.getUnit(leftUnitId);
		Unit rightUnit = stage.getUnit(rightUnitId);
		TradeContext.doTrade(leftUnit.getInventory(), rightUnit.getInventory(), leftItemIndex, rightItemIndex);
		return null;
	}
	
	public Runnable applyClient(ClientOverworldStage stage, Unit primaryUnit, ArrayList<AttackRecord> attackRecords) {
		
		return new Runnable() {
			public void run() {
				Unit leftUnit = stage.getUnit(leftUnitId);
				Unit rightUnit = stage.getUnit(rightUnitId);
				TradeContext.doTrade(leftUnit.getInventory(), rightUnit.getInventory(), leftItemIndex, rightItemIndex);
			}
		};
	}
	
	@Override
	public String toString() {
		return "Trade[" + leftUnitId + "," + leftItemIndex + ":" + rightUnitId + "," + rightItemIndex + "]";
	}
}
