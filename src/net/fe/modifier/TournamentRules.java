package net.fe.modifier;

import java.util.Map;

import net.fe.builderStage.ShopMenu;
import net.fe.builderStage.TeamBuilderResources;
import net.fe.builderStage.TeamSelectionStage;
import net.fe.overworldStage.OverworldStage;
import net.fe.unit.Unit;
import net.fe.unit.Item;

public class TournamentRules implements Modifier {
	public final int newFunds = 48000;
	public final int newExp = 26400;
	public final int minLevel = 10;
	public final int maxLevel = 20;

	@Override
	public TeamBuilderResources modifyTeamResources(TeamBuilderResources limits) {
		return limits.copyWithNewExp(newExp).copyWithNewFunds(newFunds);
	}

	@Override
	public Iterable<Item> modifyShop(Iterable<Item> shop) {
		return shop;
	}

	@Override
	public void initOverworldUnits(Iterable<Unit> units) {

	}

	@Override
	public String toString() {
		return "Tournament ruleset";
	}

	@Override
	public String getDescription() {
		return "A balance-improving tournament ruleset (temporary while I figure out how to make this work"
				+ "customizably";
	}

	@Override
	public void initBuilderUnits(Iterable<Unit> units) {
		for(Unit u : units){
			u.setMinLv(minLevel);
			u.setMaxLv(maxLevel);
		}
		
	}

	@Override
	public void setShopLimits(Map<String, Integer> inventory, Map<String, Integer> classInventory) {
		
		//1 longbow, for demonstration purposes
		for(String key : inventory.keySet()){
			if(key.equals("Longbow")){
				inventory.put(key, 1);
			}
		}
		
		//item classes
		for(String key : classInventory.keySet()){
			//1 killing weapon, 3 slaying weapons, 2 reavers, no braves or legends
			if(key.equals("Killer")){
				classInventory.put(key, 1);
			}

			if(key.equals("Slayer")){
				classInventory.put(key, 3);
			}
			
			if(key.equals("Reaver")){
				classInventory.put(key, 2);
			}

			if(key.equals("Brave") || key.equals("Legend")){
				classInventory.put(key, 0);
			}
		}
		
	}

}
