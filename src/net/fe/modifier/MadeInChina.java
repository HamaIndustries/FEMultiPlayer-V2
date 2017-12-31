package net.fe.modifier;

import java.util.Map;

import net.fe.builderStage.ShopMenu;
import net.fe.builderStage.TeamBuilderResources;
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
	public TeamBuilderResources modifyTeamResources(TeamBuilderResources limits) {
		return limits.copyWithNewFunds((i) -> i * 2);
	}
	
	/** Modifies each weapon in `shop` to have a maximum of two uses
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
		for (Unit u : units) {
			for(Item item : u.getInventory()) {
				if(item instanceof Weapon) {
					item.setUsesDEBUGGING(2);
				}
			}
		}
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

	@Override
	public void initBuilderUnits(Iterable<Unit> units) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setShopLimits(Map<String, Integer> inventory, Map<String, Integer> classInventory) {
		// TODO Auto-generated method stub
		
	}

}
