package net.fe.network.command;

import java.io.Serializable;
import java.util.ArrayList;
import net.fe.fightStage.AttackRecord;
import net.fe.overworldStage.OverworldStage;
import net.fe.overworldStage.ClientOverworldStage;
import net.fe.overworldStage.Path;
import net.fe.overworldStage.Node;
import net.fe.overworldStage.SkillChargeBar;
import net.fe.unit.UnitIdentifier;
import net.fe.unit.Unit;
import java.util.Optional;

public final class ShoveCommand extends Command {
	
	private static final long serialVersionUID = 6468268282716381357L;
	private static final int RAGE_AMOUNT = 5;
	
	private final UnitIdentifier shoveeId;
	
	public ShoveCommand(UnitIdentifier shoveeId) {
		this.shoveeId = shoveeId;
	}
	
	@Override
	public ArrayList<AttackRecord> applyServer(OverworldStage stage, Unit unit) {
		final Unit shovee = stage.getUnit(shoveeId);
		int deltaX = shovee.getXCoord() - unit.getXCoord();
		int deltaY = shovee.getYCoord() - unit.getYCoord();
		
		if (! net.fe.overworldStage.fieldskill.Shove.canShove(stage.grid, unit, shovee)) {
			throw new IllegalStateException("SHOVE: Shover is not allowed to shove shovee");
		} else {
			if (! unit.getParty().isAlly(shovee.getParty())) {
				shovee.incrementSkillCharge(RAGE_AMOUNT);
			}
			stage.grid.move(shovee, shovee.getXCoord() + deltaX, shovee.getYCoord() + deltaY, false);
			return null;
		}
	}
	
	@Override
	public Runnable applyClient(ClientOverworldStage stage, Unit unit, ArrayList<AttackRecord> attackRecords, Runnable callback) {
		
		return new Runnable() {
			public void run() {
				final Unit shovee = stage.getUnit(shoveeId);
				unit.setMoved(true);
				int deltaX = shovee.getXCoord() - unit.getXCoord();
				int deltaY = shovee.getYCoord() - unit.getYCoord();
				int newX = shovee.getXCoord() + deltaX;
				int newY = shovee.getYCoord() + deltaY;
				
				final int initialSkillCharge = shovee.getSkillCharge();
				if (! unit.getParty().isAlly(shovee.getParty())) {
					shovee.incrementSkillCharge(RAGE_AMOUNT);
				}
				final int finalSkillCharge = shovee.getSkillCharge();
				
				shovee.setOrigX(newX); // Otherwise, shovee will jump back to it's inital space on select 
				shovee.setOrigY(newY); // Otherwise, shovee will jump back to it's inital space on select
				Path p = new Path();
				p.add(new Node(newX, newY));
				stage.grid.move(shovee, newX, newY, true);
				
				Runnable stack = () -> {
					stage.checkEndGame();
					callback.run();
				};
				
				if (initialSkillCharge != finalSkillCharge) {
					final Runnable previousStack = stack;
					stack = () -> {
						stage.addEntity(new SkillChargeBar(shovee, initialSkillCharge,
								finalSkillCharge, stage, previousStack));
					};
				}
				
				{
					final Runnable previousStack = stack;
					shovee.move(p, () -> {
						shovee.sprite.setAnimation("IDLE");
						previousStack.run();
					});
				}
			}
		};
	}
	
	@Override
	public String toString() {
		return "Shove[" + shoveeId + "]";
	}
}
