package net.fe.network.command;

import java.util.ArrayList;
import java.util.List;

import net.fe.game.fightStage.AttackRecord;
import net.fe.game.unit.Item;
import net.fe.game.unit.RiseTome;
import net.fe.game.unit.Unit;
import net.fe.overworldStage.ClientOverworldStage;
import net.fe.overworldStage.OverworldStage;

public final class SummonCommand extends Command {

	private static final long serialVersionUID = 6468268282716381357L;

	private final int dropX;
	private final int dropY;

	public SummonCommand(int dropX, int dropY) {
		this.dropX = dropX;
		this.dropY = dropY;
	}

	@Override
	public ArrayList<AttackRecord> applyServer(OverworldStage stage, Unit unit) {

		final Unit summon = net.fe.overworldStage.context.Summon.generateSummon(unit);
		int tomeToUse = -1;
		List<Item> items = unit.getInventory();
		for (int z = 0; z < items.size(); z++) {
			if (items.get(z) instanceof RiseTome) {
				tomeToUse = z;
			}
		}

		if (tomeToUse == -1) {
			throw new IllegalStateException("SUMMON: Unit is not holding a Rise tome.");
		} else if ((Math.abs(unit.getXCoord() - dropX) + Math.abs(unit.getYCoord() - dropY)) != 1) {
			throw new IllegalStateException("SUMMON: Summoner is not adjacent to summon\n    " + unit.getXCoord() + " "
			        + dropX + " " + unit.getYCoord() + " " + dropY);
		} else {
			stage.addUnit(summon, dropX, dropY);
			unit.use(tomeToUse);
			stage.checkEndGame();
			return null;
		}
	}

	@Override
	public Runnable applyClient(ClientOverworldStage stage, Unit unit, ArrayList<AttackRecord> attackRecords,
	        Runnable callback) {

		return new Runnable() {
			public void run() {
				final Unit summon = net.fe.overworldStage.context.Summon.generateSummon(unit);

				int tomeToUse = 0;
				List<Item> items = unit.getInventory();
				for (int i = 0; i < items.size(); i++) {
					if (items.get(i) instanceof RiseTome) {
						tomeToUse = i;
					}
				}

				summon.loadMapSprites();
				stage.addUnit(summon, dropX, dropY);
				unit.setMoved(true);
				unit.use(tomeToUse);
				stage.checkEndGame();
				callback.run();
			}
		};
	}

	@Override
	public String toString() {
		return "Summon[" + dropX + "," + dropY + "]";
	}
}
