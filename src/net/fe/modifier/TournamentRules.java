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
	public void setShopLimits(Map<String, Integer> inventory) {
		//No Legendary Weapons
		inventory.put("Audhulma", 0);
		inventory.put("Gradivus", 0);
		inventory.put("Garm", 0);
		inventory.put("Double Bow", 0);
		inventory.put("Arbalest", 0);
		inventory.put("Aureola", 0);
		inventory.put("Excalibur", 0);
		inventory.put("Ereshkigal", 0);
		
		//No brave weapons, killer weapons 1 each
		for(String key : inventory.keySet()){
			if(key.startsWith("Brave")){
				inventory.put(key, 0);
			}
			
			if(key.startsWith("Killer") || key.startsWith("Killing") || key.equals("Wo Dao")){
				inventory.put(key, 1);
			}
		}
		
	}

}
