package net.fe.modifier;

import net.fe.builderStage.ShopMenu;
import net.fe.builderStage.TeamBuilderStage;
import net.fe.builderStage.TeamSelectionStage;
import net.fe.overworldStage.OverworldStage;

// TODO: Auto-generated Javadoc
/**
 * The Class Treasury.
 */
public class Treasury implements Modifier {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 843174984852438018L;

	/* (non-Javadoc)
	 * @see net.fe.modifier.Modifier#modifyTeam(net.fe.builderStage.TeamBuilderStage)
	 */
	@Override
	public void modifyTeam(TeamBuilderStage stage) {
		stage.setFunds(99999900);
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
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Treasury";
	}
	
	/* (non-Javadoc)
	 * @see net.fe.modifier.Modifier#getDescription()
	 */
	@Override
	public String getDescription() {
		return "Start with the maximum amount of gold.";
	}

}
