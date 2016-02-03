package net.fe.modifier;

import java.io.Serializable;

import net.fe.builderStage.ShopMenu;
import net.fe.builderStage.TeamBuilderStage;
import net.fe.builderStage.TeamSelectionStage;
import net.fe.overworldStage.OverworldStage;

// TODO: Auto-generated Javadoc
/**
 * The Interface Modifier.
 */
public interface Modifier extends Serializable{
	
	/**
	 * Modify team.
	 *
	 * @param stage the stage
	 */
	public void modifyTeam(TeamBuilderStage stage);
	
	/**
	 * Modify shop.
	 *
	 * @param shop the shop
	 */
	public void modifyShop(ShopMenu shop);
	
	/**
	 * Modify units.
	 *
	 * @param stage the stage
	 */
	public void modifyUnits(TeamSelectionStage stage);
	
	/**
	 * Inits the overworld.
	 *
	 * @param stage the stage
	 */
	public void initOverworld(OverworldStage stage);
	
	/**
	 * End of turn.
	 *
	 * @param stage the stage
	 */
	public void endOfTurn(OverworldStage stage);
	
	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription();

}
