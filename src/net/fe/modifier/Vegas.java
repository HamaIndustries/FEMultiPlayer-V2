package net.fe.modifier;

import net.fe.builderStage.ShopMenu;
import net.fe.builderStage.TeamBuilderStage;
import net.fe.builderStage.TeamSelectionStage;
import net.fe.overworldStage.OverworldStage;
import net.fe.unit.Unit;

// TODO: Auto-generated Javadoc
/**
 * The Class Vegas.
 */
// Everyone's hit rate is halved and crit rate doubled
public class Vegas implements Modifier {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 3407505862142624494L;

	/* (non-Javadoc)
	 * @see net.fe.modifier.Modifier#modifyTeam(net.fe.builderStage.TeamBuilderStage)
	 */
	@Override
	public void modifyTeam(TeamBuilderStage stage) {
		
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
		for(Unit u : stage.getAllUnits()) {
			u.addSkill(new Gamble());
		}
	}

	/* (non-Javadoc)
	 * @see net.fe.modifier.Modifier#endOfTurn(net.fe.overworldStage.OverworldStage)
	 */
	@Override
	public void endOfTurn(OverworldStage stage) {
		// TODO Auto-generated method stub
		
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
