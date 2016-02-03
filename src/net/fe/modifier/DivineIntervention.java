package net.fe.modifier;

import net.fe.builderStage.ShopMenu;
import net.fe.builderStage.TeamBuilderStage;
import net.fe.builderStage.TeamSelectionStage;
import net.fe.overworldStage.OverworldStage;
import net.fe.unit.Unit;

// TODO: Auto-generated Javadoc
/**
 * All units have Miracle, with 100% chance to proc.
 *
 * @author Shawn
 */
public class DivineIntervention implements Modifier {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -7509901063099817137L;

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
			u.addSkill(new Miracle());
		}
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
		return "Divine Intervention";
	}
	
	/* (non-Javadoc)
	 * @see net.fe.modifier.Modifier#getDescription()
	 */
	@Override
	public String getDescription() {
		return "All units have a version of Miracle that is guarenteed to activate.";
	}
}
