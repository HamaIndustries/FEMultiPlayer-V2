package net.fe.modifier;

import net.fe.builderStage.ShopMenu;
import net.fe.builderStage.TeamBuilderResources;
import net.fe.unit.Item;
import net.fe.unit.Unit;

// TODO: Auto-generated Javadoc
/**
 * The Class Veterans.
 */
public class Veterans implements Modifier {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 8924524348358477808L;

	/* (non-Javadoc)
	 * @see net.fe.modifier.Modifier#modifyTeam(net.fe.builderStage.TeamBuilderStage)
	 */
	@Override
	public TeamBuilderResources modifyTeamResources(TeamBuilderResources limits) {
		return limits.copyWithNewExp(999999999);
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
	}

	/* (non-Javadoc)
	 * @see net.fe.modifier.Modifier#getDescription()
	 */
	@Override
	public String getDescription() {
		return "Unlimited starting EXP.";
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "Veterans";
	}
	
}
