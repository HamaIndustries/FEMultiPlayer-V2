package net.fe.network.command;

import java.util.ArrayList;

import net.fe.FEResources;
import net.fe.fightStage.AttackRecord;
import net.fe.fightStage.CombatCalculator;
import net.fe.fightStage.FightStage;
import net.fe.overworldStage.ClientOverworldStage;
import net.fe.overworldStage.OverworldStage;
import net.fe.transition.OverworldFightTransition;
import net.fe.unit.BattleStats;
import net.fe.unit.Unit;
import net.fe.unit.UnitIdentifier;

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
		CombatCalculator calc = new CombatCalculator(unitId, otherId, (ui) -> stage.getUnit(ui), stage.getHitRNG(), stage.getCritRNG(), stage.getSkillRNG());
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
							if (!attackRecord.animation.contains("Miss") || attacker.getWeapon().isMagic()) {
								attacker.use(attacker.getWeapon());
							}
							if(attackRecord.damage > 0) {
								defender.getAssisters().add(attacker);
								attacker.addBattleStats(new BattleStats(
									/* kills = */ 0,
									/* assists = */ 0,
									/* damage = */ attackRecord.damage,
									/* healing = */ attackRecord.drain
								));
							}
							if (defender.getHp() == 0) {
								attacker.addBattleStats(new BattleStats(/* kills = */ 1, 0, 0, 0));
								defender.getAssisters().remove(attacker);
								for(Unit u : defender.getAssisters()) {
									u.addBattleStats(new BattleStats(0, /* assists = */ 1, 0, 0));
								}
							}
						}
						callback2.run();
						stage.checkEndGame();
					}; break;
				}
			}
		};
	}
	
	@Override
	public String toString() {
		return "Attack[" + otherId + "]";
	}
}
