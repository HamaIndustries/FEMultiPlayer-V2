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
import net.fe.overworldStage.SkillChargeBar;
import net.fe.unit.UnitIdentifier;
import net.fe.unit.Unit;
import net.fe.unit.HealingItem;
import net.fe.unit.SkillChargingItem;
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
		} else if (unit.getInventory().get(itemIndex) instanceof SkillChargingItem) {
			unit.use(itemIndex);
			return null;
		} else {
			throw new IllegalStateException("USE: not a healing/skill item: " + unit.getInventory().get(itemIndex));
		}
	}
	
	@Override
	public Runnable applyClient(ClientOverworldStage stage, Unit unit, ArrayList<AttackRecord> attackRecords, Runnable callback) {
		
		final int initialHp = unit.getHp();
		final int initialRage = unit.getSkillCharge();
		return new Runnable() {
			public void run() {
				unit.use(itemIndex);
				unit.setMoved(true);
				stage.checkEndGame();
				
				Runnable stack = callback;
				if (initialHp != unit.getHp()) {
					final Runnable previousStack = stack;
					//TODO Positioning
					stack = () -> {
						stage.addEntity(new Healthbar(unit, initialHp, unit.getHp(), stage) {
							@Override
							public void done() {
								destroy();
								previousStack.run();
							}
						});
					};
				}
				if (initialRage != unit.getSkillCharge()) {
					final Runnable previousStack = stack;
					stack = () -> {
						stage.addEntity(new SkillChargeBar(unit, initialRage,
								unit.getSkillCharge(), stage, previousStack));
					};
				}
				
				stack.run();
			}
		};
	}
	
	@Override
	public String toString() {
		return "Use[" + itemIndex + "]";
	}
}
