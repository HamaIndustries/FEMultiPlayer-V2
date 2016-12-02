package net.fe.game.modifier;

import net.fe.builderStage.ShopMenu;
import net.fe.builderStage.TeamBuilderResources;
import net.fe.builderStage.TeamSelectionStage;
import net.fe.game.modifier.Pavise;
import net.fe.game.unit.Item;
import net.fe.game.unit.Unit;
import net.fe.overworldStage.OverworldStage;

public class ProTactics implements Modifier {

	@Override
	public TeamBuilderResources modifyTeamResources(TeamBuilderResources limits) {
		return limits;
	}

	@Override
	public Iterable<Item> modifyShop(Iterable<Item> shop) {
		return shop;
	}

	@Override
	public void initOverworldUnits(Iterable<Unit> units) {
		for (Unit u : units) {
			u.addSkill(new Pavise());
		}

	}

	@Override
	public String toString() {
		return "Pro Tactics";
	}

	@Override
	public String getDescription() {
		return "Halves damage to better emulate traditional GBA games. All units get 100%Pavise.";
	}

}
