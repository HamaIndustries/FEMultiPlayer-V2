package net.fe.modifier;

import net.fe.modifier.Pavise;
import net.fe.builderStage.ShopMenu;
import net.fe.builderStage.TeamBuilderStage;
import net.fe.builderStage.TeamSelectionStage;
import net.fe.overworldStage.OverworldStage;
import net.fe.unit.Unit;

public class ProTactics implements Modifier {

	@Override
	public void modifyTeam(TeamBuilderStage stage) {

	}

	@Override
	public void modifyShop(ShopMenu shop) {

	}

	@Override
	public void modifyUnits(TeamSelectionStage stage) {
		

	}

	@Override
	public void initOverworld(OverworldStage stage) {
		for(Unit u : stage.getAllUnits()) {
			u.addSkill(new Pavise());
		}

	}

	@Override
	public void endOfTurn(OverworldStage stage) {

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
