package net.fe.network.command;

import java.util.ArrayList;

import net.fe.fightStage.AttackRecord;
import net.fe.fightStage.CombatCalculator;
import net.fe.fightStage.CombatTrigger;
import net.fe.fightStage.FightStage;
import net.fe.overworldStage.ClientOverworldStage;
import net.fe.overworldStage.OverworldStage;
import net.fe.transition.OverworldFightTransition;
import net.fe.unit.Unit;
import net.fe.unit.UnitIdentifier;

public final class AttackCommand extends Command {
	
	private static final long serialVersionUID = -5968025039862780623L;
	
	private final UnitIdentifier otherId;
	private final ArrayList<CombatTrigger> unitManualTriggers;
	
	public AttackCommand(UnitIdentifier otherId, Iterable<CombatTrigger> unitManualTriggers) {
		this.otherId = otherId;
		this.unitManualTriggers = new ArrayList<CombatTrigger>();
		unitManualTriggers.forEach(x -> this.unitManualTriggers.add(x));
		this.unitManualTriggers.trimToSize();
	} 
	
	@Override
	public ArrayList<AttackRecord> applyServer(OverworldStage stage, Unit unit) {
		
		//This updates HP so we're ok
		final UnitIdentifier unitId = new UnitIdentifier(unit);
		CombatCalculator calc = new CombatCalculator(unitId, otherId, this.unitManualTriggers, (ui) -> stage.getUnit(ui), stage.getHitRNG(), stage.getCritRNG(), stage.getSkillRNG());
		return calc.getAttackQueue();
	}
	
	@Override
	public Runnable applyClient(ClientOverworldStage stage, Unit unit, ArrayList<AttackRecord> attackRecords, Runnable callback) {
		
		return new Runnable() {
			public void run() {
				final Runnable callback2 = new Runnable(){@Override public void run() {callback.run(); stage.setControl(true);}};
				final UnitIdentifier unitId = new UnitIdentifier(unit);
				final Unit other = stage.getUnit(otherId);
				unit.setMoved(true);
				// play the battle animation
				stage.addEntity(new OverworldFightTransition(
					stage,
					new FightStage(unitId, otherId, attackRecords, stage, callback2),
					unitId,
					otherId
				));
			}
		};
	}
	
	@Override
	public String toString() {
		return "Attack[" + otherId + "]";
	}
}
