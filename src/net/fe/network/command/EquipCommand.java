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

public final class EquipCommand extends Command {
	
	private static final long serialVersionUID = 6468268282716381357L;
	
	private final UnitIdentifier unitId;
	private final int itemIndex;
	
	public EquipCommand(UnitIdentifier unitId, int itemIndex) {
		this.unitId = unitId;
		this.itemIndex = itemIndex;
	}
	
	@Override
	public ArrayList<AttackRecord> applyServer(OverworldStage stage, Unit primaryUnit) {
		
		//TODO: validate
		Unit other = stage.getUnit(unitId);
		other.equip(itemIndex);
		return null;
	}
	
	@Override
	public Runnable applyClient(ClientOverworldStage stage, Unit primaryUnit, ArrayList<AttackRecord> attackRecords, Runnable callback) {
		
		return new Runnable() {
			public void run() {
				Unit other = stage.getUnit(unitId);
				other.equip(itemIndex);
				callback.run();
			}
		};
	}
	
	@Override
	public String toString() {
		return "Equip[" + unitId + ", " + itemIndex + "]";
	}
}
