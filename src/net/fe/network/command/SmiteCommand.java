package net.fe.network.command;

import java.io.Serializable;
import java.util.ArrayList;
import net.fe.fightStage.AttackRecord;
import net.fe.overworldStage.OverworldStage;
import net.fe.overworldStage.ClientOverworldStage;
import net.fe.overworldStage.Path;
import net.fe.overworldStage.Node;
import net.fe.unit.UnitIdentifier;
import net.fe.unit.Unit;
import java.util.Optional;

public final class SmiteCommand extends Command {
	
	private static final long serialVersionUID = 6468268282716381357L;
	
	private final UnitIdentifier shoveeId;
	
	public SmiteCommand(UnitIdentifier shoveeId) {
		this.shoveeId = shoveeId;
	}
	
	public ArrayList<AttackRecord> applyServer(OverworldStage stage, Unit unit) {
		final Unit shovee = stage.getUnit(shoveeId);
		int deltaX = 2 * (shovee.getXCoord() - unit.getXCoord());
		int deltaY = 2 * (shovee.getYCoord() - unit.getYCoord());
		
		if (! net.fe.overworldStage.fieldskill.Smite.canSmite(stage.grid, unit, shovee)) {
			throw new IllegalStateException("SMITE: Shover is not allowed to shove shovee");
		} else {
			stage.grid.move(shovee, shovee.getXCoord() + deltaX, shovee.getYCoord() + deltaY, false);
			return null;
		}
	}
	
	public Runnable applyClient(ClientOverworldStage stage, Unit unit, ArrayList<AttackRecord> attackRecords) {
		
		return new Runnable() {
			public void run() {
				final Unit shovee = stage.getUnit(shoveeId);
				unit.setMoved(true);
				int deltaX = shovee.getXCoord() - unit.getXCoord();
				int deltaY = shovee.getYCoord() - unit.getYCoord();
				int newX = shovee.getXCoord() + 2 * deltaX;
				int newY = shovee.getYCoord() + 2 * deltaY;
				
				shovee.setOrigX(newX); // Otherwise, shovee will jump back to it's inital space on select
				shovee.setOrigY(newY); // Otherwise, shovee will jump back to it's inital space on select
				Path p = new Path();
				p.add(new Node(newX, newY));
				stage.grid.move(shovee, newX, newY, true);
				shovee.move(p, new Runnable() {
					public void run() {
						shovee.sprite.setAnimation("IDLE");
						stage.checkEndGame();
					}
				});
			}
		};
	}
	
	@Override
	public String toString() {
		return "Smite[" + shoveeId + "]";
	}
}
