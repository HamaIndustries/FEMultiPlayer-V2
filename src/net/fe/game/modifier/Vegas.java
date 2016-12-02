package net.fe.game.modifier;

import net.fe.builderStage.ShopMenu;
import net.fe.builderStage.TeamBuilderResources;
import net.fe.game.unit.Item;
import net.fe.game.unit.Unit;

// TODO: Auto-generated Javadoc
/**
 * Everyone's hit rate is halved and crit rate doubled
 */
public final class Vegas implements Modifier {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 3407505862142624494L;

	/* (non-Javadoc)
	 * @see net.fe.modifier.Modifier#modifyTeam(net.fe.builderStage.TeamBuilderStage)
	 */
	@Override
	public TeamBuilderResources modifyTeamResources(TeamBuilderResources limits) {
		return limits;
	}

	/* (non-Javadoc)
	 * @see net.fe.modifier.Modifier#modifyShop(net.fe.builderStage.ShopMenu)
	 */
	@Override
	public Iterable<Item> modifyShop(Iterable<Item> shop) {
		return shop;
	}

	/* (non-Javadoc)
	 * @see net.fe.modifier.Modifier#initOverworld(net.fe.overworldStage.OverworldStage)
	 */
	@Override
	public void initOverworldUnits(Iterable<Unit> units) {
		for(Unit u : units) {
			u.addSkill(new Gamble());
		}
	}

	/* (non-Javadoc)
	 * @see net.fe.modifier.Modifier#getDescription()
	 */
	@Override
	public String getDescription() {
		return "Gamble! All units have halved hit rates and doubled crit rates.";
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "Vegas";
	}
	

}
