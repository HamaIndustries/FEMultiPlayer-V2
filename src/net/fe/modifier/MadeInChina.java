package net.fe.modifier;

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
		return limits.copyWithNewFunds(limits.funds * 2);
	}
	
	/** Modifies each weapon in `shop` to have a maximum of two uses
	 * @see net.fe.modifier.Modifier#modifyShop(net.fe.builderStage.ShopMenu)
	 */
	@Override
	public Iterable<Weapon> modifyShop(Iterable<Weapon> shop) {
		return new Iterable<Weapon>() {
			public java.util.Iterator<Weapon> iterator() {
				return new java.util.Iterator<Weapon>() {
					private java.util.Iterator<Weapon> backing = shop.iterator();
					
					public void remove() { backing.remove(); } 
					public boolean hasNext() { return backing.hasNext(); }
					public Weapon next() {
						Weapon u = backing.next();
						return new Weapon(u.name, 2, u.id, u.getCost(),
							u.type, u.mt, u.hit, u.crit, u.range,
							u.modifiers, u.effective, u.pref);
					}
				};
			}
		};
	}

	/* (non-Javadoc)
	 * @see net.fe.modifier.Modifier#initOverworld(net.fe.overworldStage.OverworldStage)
	 */
	@Override
	public void initOverworldUnits(Iterable<Unit> units) {
		
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
