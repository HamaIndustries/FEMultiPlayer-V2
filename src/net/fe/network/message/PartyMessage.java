package net.fe.network.message;

import java.util.List;
import java.util.Collection;
import java.util.ArrayList;
import java.util.function.Function;

import net.fe.builderStage.TeamBuilderResources;
import net.fe.builderStage.TeamBuilderStage;
import net.fe.network.Message;
import net.fe.modifier.Modifier;
import net.fe.unit.Unit;
import net.fe.unit.Item;

// TODO: Auto-generated Javadoc
/**
 * The Class PartyMessage.
 */
public final class PartyMessage extends Message {

	/** The team data. */
	public final List<Unit> teamData;
	
	/**
	 * Instantiates a new party message.
	 *
	 * @param data the data
	 */
	public PartyMessage(List<Unit> data) {
		super();
		teamData = data;
	}
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -6657648098112938492L;
	
	/* (non-Javadoc)
	 * @see net.fe.network.Message#toString()
	 */
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append(super.toString());
		for(Unit u: teamData){
			sb.append("\n\t");
			sb.append(u.name);
			sb.append(" Lv" + u.getLevel());
			for (Item i: u.getInventory()) {
				sb.append("\n\t\t");
				sb.append(i.name);
			}
		}
		sb.append("\n");
		return sb.toString();
	}
	
	/**
	 * 
	 * @throws IllegalStateException if something is off
	 */
	public void validateTeam(Function<String,Unit> realUnits, Iterable<Item> realItems, Iterable<Modifier> mods) {
		
		TeamBuilderResources usableRes = new TeamBuilderResources(TeamBuilderStage.FUNDS, TeamBuilderStage.EXP);
		for (Modifier m : mods ) {
			usableRes = m.modifyTeamResources(usableRes);
			realItems = m.modifyShop(realItems);
		}
		ArrayList<Item> realItems2 = new ArrayList<Item>();
		realItems.forEach(realItems2::add);
		
		
		TeamBuilderResources usedRes = new TeamBuilderResources(0, 0);
		for (Unit u : this.teamData) {
			{
				Unit u2 = u.getCopy();
				usedRes = usedRes.copyWithNewFunds((i) -> i + u2.squeezeGold());
				usedRes = usedRes.copyWithNewExp((i) -> i + u2.squeezeExp());
			}
			{
				Unit u3 = realUnits.apply(u.name);
				u3.setLevel(u.getLevel());
				if (! u.getBase().equals(u3.getBase())) {
					throw new IllegalStateException("unit's stats don't match expected: " +
						"\n\tClient: " + u.getBase() +
						"\n\tServer: " + u3.getBase()
					);
				}
				if (! u.getTheClass().equals(u3.getTheClass())) {
					throw new IllegalStateException("unit's class don't match expected" +
						"\n\tClient: " + u.getTheClass() +
						"\n\tServer: " + u3.getTheClass()
					);
				}
				if (! u.getTriggers().equals(u3.getTriggers())) {
					throw new IllegalStateException("unit's skills don't match expected" +
						"\n\tClient: " + u.getTriggers() +
						"\n\tServer: " + u3.getTriggers()
					);
				}
			}
			
			for (Item i : u.getInventory()) {
				if (! realItems2.contains(i)) {
					throw new IllegalStateException("Item does not exist in list of real items: " + i);
				}
			}
		}
		
		if (usedRes.exp > usableRes.exp) {
			throw new IllegalStateException("Used EXP exceeded usable EXP: " + usedRes.exp + " > " + usableRes.exp);
		}
		if (usedRes.funds > usableRes.funds) {
			throw new IllegalStateException("Used FUNDS exceeded usable FUNDS: " + usedRes.funds + " > " + usableRes.funds);
		}
	}
	
}
