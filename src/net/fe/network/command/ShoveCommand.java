package net.fe.network.command;

import java.util.ArrayList;

import net.fe.game.fightStage.AttackRecord;
import net.fe.game.unit.Unit;
import net.fe.game.unit.UnitIdentifier;
import net.fe.overworldStage.ClientOverworldStage;
import net.fe.overworldStage.Node;
import net.fe.overworldStage.OverworldStage;
import net.fe.overworldStage.Path;

public final class ShoveCommand extends Command {

	private static final long serialVersionUID = 6468268282716381357L;

	private final UnitIdentifier shoveeId;

	public ShoveCommand(UnitIdentifier shoveeId) {
		this.shoveeId = shoveeId;
	}

	@Override
	public ArrayList<AttackRecord> applyServer(OverworldStage stage, Unit unit) {
		final Unit shovee = stage.getUnit(shoveeId);
		int deltaX = shovee.getXCoord() - unit.getXCoord();
		int deltaY = shovee.getYCoord() - unit.getYCoord();

		if (!net.fe.overworldStage.fieldskill.Shove.canShove(stage.grid, unit, shovee)) {
			throw new IllegalStateException("SHOVE: Shover is not allowed to shove shovee");
		} else {
			stage.grid.move(shovee, shovee.getXCoord() + deltaX, shovee.getYCoord() + deltaY, false);
			return null;
		}
	}

	@Override
	public Runnable applyClient(ClientOverworldStage stage, Unit unit, ArrayList<AttackRecord> attackRecords,
	        Runnable callback) {

		return new Runnable() {
			public void run() {
				final Unit shovee = stage.getUnit(shoveeId);
				unit.setMoved(true);
				int deltaX = shovee.getXCoord() - unit.getXCoord();
				int deltaY = shovee.getYCoord() - unit.getYCoord();
				int newX = shovee.getXCoord() + deltaX;
				int newY = shovee.getYCoord() + deltaY;

				shovee.setOrigX(newX); // Otherwise, shovee will jump back to
		                               // it's inital space on select
				shovee.setOrigY(newY); // Otherwise, shovee will jump back to
		                               // it's inital space on select
				Path p = new Path();
				p.add(new Node(newX, newY));
				stage.grid.move(shovee, newX, newY, true);
				shovee.move(p, new Runnable() {
					public void run() {
						shovee.sprite.setAnimation("IDLE");
						stage.checkEndGame();
						callback.run();
					}
				});
			}
		};
	}

	@Override
	public String toString() {
		return "Shove[" + shoveeId + "]";
	}
}
