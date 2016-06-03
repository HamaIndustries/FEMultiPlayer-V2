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
import net.fe.unit.UnitIdentifier;
import net.fe.unit.Unit;
import net.fe.unit.HealingItem;
import net.fe.unit.Item;
import net.fe.unit.RiseTome;
import java.util.Optional;

public final class UseCommand extends Command {
	
	private static final long serialVersionUID = 6468268282716381357L;
	
	private final int itemIndex;
	
	public UseCommand(int itemIndex) {
		this.itemIndex = itemIndex;
	}
	
	@Override
	public ArrayList<AttackRecord> applyServer(OverworldStage stage, Unit unit) {
		
		if (unit.getInventory().get(itemIndex) instanceof HealingItem) {
			unit.use(itemIndex);
			return null;
		} else {
			throw new IllegalStateException("USE: not a healing item: " + unit.getInventory().get(itemIndex));
		}
	}
	
	@Override
	public Runnable applyClient(ClientOverworldStage stage, Unit unit, ArrayList<AttackRecord> attackRecords, Runnable callback) {
		
		final int oHp = unit.getHp();
		return new Runnable() {
			public void run() {
				unit.use(itemIndex);
				unit.setMoved(true);
				stage.checkEndGame();
				//TODO Positioning
				stage.addEntity(new Healthbar(unit, oHp, unit.getHp(), stage) {
					@Override
					public void done() {
						destroy();
						callback.run();
					}
				});
			}
		};
	}
	
	@Override
	public String toString() {
		return "Use[" + itemIndex + "]";
	}
}
