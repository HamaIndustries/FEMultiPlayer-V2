package net.fe.game.modifier;

import java.io.Serializable;

import net.fe.builderStage.ShopMenu;
import net.fe.builderStage.TeamBuilderResources;
import net.fe.game.unit.Item;
import net.fe.game.unit.Unit;

// TODO: Auto-generated Javadoc
/**
 * The Interface Modifier.
 */
public interface Modifier extends Serializable{
	
	/**
	 * Edit the resources availiable while building a team.
	 * 
	 * A default implementation is to return the parameter.
	 * 
	 * @param stage the initial starting resources
	 * @return the new starting resources
	 */
	public TeamBuilderResources modifyTeamResources(TeamBuilderResources limits);
	
	/**
	 * Transforms a list of items.
	 * 
	 * A default implementation is to return the parameter.
	 *
	 * @param shop the items
	 * @return the modified list
	 */
	public Iterable<Item> modifyShop(Iterable<Item> shop);
	
	/**
	 * Modifies a list of units; for use immediately before the game starts
	 *
	 * Modifies the list in-line
	 * 
	 * @param units the units
	 */
	public void initOverworldUnits(Iterable<Unit> units);
	
	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription();

}
