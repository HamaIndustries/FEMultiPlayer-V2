package net.fe.network.command;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import net.fe.FEResources;
import net.fe.fightStage.AttackRecord;
import net.fe.fightStage.HealCalculator;
import net.fe.fightStage.FightStage;
import net.fe.transition.OverworldFightTransition;
import net.fe.overworldStage.OverworldStage;
import net.fe.overworldStage.ClientOverworldStage;
import net.fe.overworldStage.Path;
import net.fe.overworldStage.Node;
import net.fe.overworldStage.Healthbar;
import net.fe.unit.BattleStats;
import net.fe.unit.UnitIdentifier;
import net.fe.unit.Unit;
import net.fe.unit.Item;
import net.fe.unit.RiseTome;
import java.util.Optional;

public final class HealCommand extends Command {
	
	private static final long serialVersionUID = 6468268282716381357L;
	
	private final UnitIdentifier otherId;
	
	public HealCommand(UnitIdentifier otherId) {
		this.otherId = otherId;
	}
	
	@Override
	public ArrayList<AttackRecord> applyServer(OverworldStage stage, Unit unit) {
		
		//This updates HP so we're ok
		final UnitIdentifier unitId = new UnitIdentifier(unit);
		HealCalculator calc = new HealCalculator(unitId, otherId, (ui) -> stage.getUnit(ui));
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
				switch (FEResources.getShowAnimations()) {
					case FULL: {
						// play the battle animation
						stage.addEntity(new OverworldFightTransition(
							stage,
							new FightStage(unitId, otherId, attackRecords, stage, callback2),
							unitId,
							otherId
						));
					}; break;
					case ABRIDGED: {
						stage.addEntity(
							new net.fe.overworldStage.AbridgedFightScene(unitId, otherId, attackRecords, stage, callback2)
						);
					}; break;
					case OFF: {
						for (AttackRecord attackRecord : attackRecords) {
							final Unit attacker = stage.getUnit(attackRecord.attacker);
							final Unit defender = stage.getUnit(attackRecord.defender);
							attacker.setHp(attacker.getHp() + attackRecord.drain);
							defender.setHp(defender.getHp() - attackRecord.damage);
							attacker.addBattleStats(new BattleStats(
								/* kills = */ 0,
								/* assists = */ 0,
								/* damage = */ 0,
								/* healing = */ -attackRecord.damage
							));
						}
						callback2.run();
						stage.checkEndGame();
					}
				}
			}
		};
	}
	
	@Override
	public String toString() {
		return "Heal[" + otherId + "]";
	}
}
