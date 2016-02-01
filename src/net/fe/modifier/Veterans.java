package net.fe.modifier;

import net.fe.builderStage.ShopMenu;
import net.fe.builderStage.TeamBuilderStage;
import net.fe.builderStage.TeamSelectionStage;
import net.fe.overworldStage.OverworldStage;

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
	public void modifyTeam(TeamBuilderStage stage) {
		stage.setExp(999999999);
	}

	/* (non-Javadoc)
	 * @see net.fe.modifier.Modifier#modifyShop(net.fe.builderStage.ShopMenu)
	 */
	@Override
	public void modifyShop(ShopMenu shop) {
		
	}

	/* (non-Javadoc)
	 * @see net.fe.modifier.Modifier#modifyUnits(net.fe.builderStage.TeamSelectionStage)
	 */
	@Override
	public void modifyUnits(TeamSelectionStage stage) {
		
	}

	/* (non-Javadoc)
	 * @see net.fe.modifier.Modifier#initOverworld(net.fe.overworldStage.OverworldStage)
	 */
	@Override
	public void initOverworld(OverworldStage stage) {
		
	}

	/* (non-Javadoc)
	 * @see net.fe.modifier.Modifier#endOfTurn(net.fe.overworldStage.OverworldStage)
	 */
	@Override
	public void endOfTurn(OverworldStage stage) {
		
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
