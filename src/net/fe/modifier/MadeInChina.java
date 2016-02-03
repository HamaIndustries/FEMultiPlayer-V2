package net.fe.modifier;

import net.fe.builderStage.ShopMenu;
import net.fe.builderStage.TeamBuilderStage;
import net.fe.builderStage.TeamSelectionStage;
import net.fe.overworldStage.OverworldStage;
import net.fe.unit.Item;
import net.fe.unit.Unit;
import net.fe.unit.Weapon;

// TODO: Auto-generated Javadoc
/**
 * All weapons have 2 durability. Players are given
 * extra gold to compensate.
 * @author Shawn
 *
 */
public class MadeInChina implements Modifier {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -3929819526675171008L;

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
	 * @see net.fe.modifier.Modifier#initOverworld(net.fe.overworldStage.OverworldStage)
	 */
	@Override
	public void initOverworld(OverworldStage stage) {
		for(Unit u : stage.getAllUnits()) {
			for(Item item : u.getInventory()) {
				if(item instanceof Weapon) {
					item.setUsesDEBUGGING(2);
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see net.fe.modifier.Modifier#endOfTurn(net.fe.overworldStage.OverworldStage)
	 */
	@Override
	public void endOfTurn(OverworldStage stage) {
		
	}

	/* (non-Javadoc)
	 * @see net.fe.modifier.Modifier#modifyUnits(net.fe.builderStage.TeamSelectionStage)
	 */
	@Override
	public void modifyUnits(TeamSelectionStage stage) {
		
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Made In China";
	}
	
	/* (non-Javadoc)
	 * @see net.fe.modifier.Modifier#getDescription()
	 */
	@Override
	public String getDescription() {
		return "All weapons have greatly reduced durability. Start with extra gold.";
	}

}
