package net.fe.network.command;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import net.fe.fightStage.AttackRecord;
import net.fe.fightStage.CombatCalculator;
import net.fe.fightStage.FightStage;
import net.fe.transition.OverworldFightTransition;
import net.fe.overworldStage.OverworldStage;
import net.fe.overworldStage.ClientOverworldStage;
import net.fe.overworldStage.Path;
import net.fe.overworldStage.Node;
import net.fe.overworldStage.Healthbar;
import net.fe.unit.UnitIdentifier;
import net.fe.unit.Unit;
import net.fe.unit.Item;
import net.fe.unit.RiseTome;
import java.util.Optional;

public final class AttackCommand extends Command {
	
	private static final long serialVersionUID = 6468268282716381357L;
	
	private final UnitIdentifier otherId;
	
	public AttackCommand(UnitIdentifier otherId) {
		this.otherId = otherId;
	} 
	
	@Override
	public ArrayList<AttackRecord> applyServer(OverworldStage stage, Unit unit) {
		
		//This updates HP so we're ok
		final UnitIdentifier unitId = new UnitIdentifier(unit);
		CombatCalculator calc = new CombatCalculator(unitId, otherId, (ui) -> stage.getUnit(ui));
		return calc.getAttackQueue();
	}
	
	@Override
	public Runnable applyClient(ClientOverworldStage stage, Unit unit, ArrayList<AttackRecord> attackRecords, Runnable callback) {
		
		return new Runnable() {
			public void run() {
				final UnitIdentifier unitId = new UnitIdentifier(unit);
				final Unit other = stage.getUnit(otherId);
				unit.setMoved(true);
				// play the battle animation
				stage.addEntity(new OverworldFightTransition(
					stage,
					new FightStage(unitId, otherId, attackRecords, stage),
					unitId,
					otherId
				));
				callback.run();
			}
		};
	}
	
	@Override
	public String toString() {
		return "Attack[" + otherId + "]";
	}
}
