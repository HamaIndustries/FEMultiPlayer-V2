package net.fe.network.command;

import java.util.ArrayList;

import net.fe.game.fightStage.AttackRecord;
import net.fe.game.fightStage.FightStage;
import net.fe.game.fightStage.HealCalculator;
import net.fe.game.unit.Unit;
import net.fe.game.unit.UnitIdentifier;
import net.fe.overworldStage.ClientOverworldStage;
import net.fe.overworldStage.OverworldStage;
import net.fe.ressources.graphics.transition.OverworldFightTransition;

public final class HealCommand extends Command {

	private static final long serialVersionUID = 6468268282716381357L;

	private final UnitIdentifier otherId;

	public HealCommand(UnitIdentifier otherId) {
		this.otherId = otherId;
	}

	@Override
	public ArrayList<AttackRecord> applyServer(OverworldStage stage, Unit unit) {

		// This updates HP so we're ok
		final UnitIdentifier unitId = new UnitIdentifier(unit);
		HealCalculator calc = new HealCalculator(unitId, otherId, (ui) -> stage.getUnit(ui));
		return calc.getAttackQueue();
	}

	@Override
	public Runnable applyClient(ClientOverworldStage stage, Unit unit, ArrayList<AttackRecord> attackRecords,
	        Runnable callback) {

		return new Runnable() {
			public void run() {
				final Runnable callback2 = new Runnable() {
					@Override
					public void run() {
						callback.run();
						stage.setControl(true);
					}
				};
				final UnitIdentifier unitId = new UnitIdentifier(unit);
				final Unit other = stage.getUnit(otherId);
				unit.setMoved(true);
				// play the battle animation
				stage.addEntity(new OverworldFightTransition(stage,
		                new FightStage(unitId, otherId, attackRecords, stage, callback2), unitId, otherId));
			}
		};
	}

	@Override
	public String toString() {
		return "Heal[" + otherId + "]";
	}
}
