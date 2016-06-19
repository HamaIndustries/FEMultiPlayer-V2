package net.fe.builderStage;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

import net.fe.builderStage.ShopInventory;
import net.fe.modifier.Modifier;
import net.fe.unit.Item;
import net.fe.unit.Unit;
import net.fe.unit.Weapon;

public class ShopInventory {
	private static ShopInventory obj;

	private Map<String, Integer> inventory;

	private ShopInventory(Iterable<Modifier> mods){
		inventory = new HashMap<String, Integer>();

		for(Item i : Item.getAllItems()){
			inventory.put(i.name, -1);
		}

		if(mods != null){

			for(Modifier m : mods){
				m.setShopLimits(inventory);
			}
		}
	}

	/**
	 * Singleton constructor method
	 * 
	 * @param mods The modifiers used by this session.  Only loaded the first time this is called, but
	 * there's no harm in passing it again.
	 * 
	 * @return
	 */
	public static ShopInventory GetInstance(Iterable<Modifier> mods){
		if(obj == null){
			obj = new ShopInventory(mods);
		}

		return obj;
	}

	/**
	 * destroys the current instance of the inventory.  To be called when the battle starts to reset
	 * it for the next game.
	 */
	public void DestroyInstance(){
		obj = null;
	}

	/**
	 * Takes the item name of the item the player is trying to buy.  Returns true and decrements the remaining
	 * stock if it succeeds, false if the weapon can't be bought.
	 * 
	 * @param itemName - The name of the item to buy
	 * @return
	 */
	public boolean buyItem(String itemName){
		if(inventory.get(itemName) != 0){
			if(inventory.get(itemName) != -1){
				inventory.put(itemName, inventory.get(itemName) - 1);
			}

			return true;
		}
		return false;
	}

	/**
	 * Returns true if the item is still in stock, false otherwise.
	 * 
	 * @param itemName
	 * @return
	 */
	public boolean itemInStock(String itemName){
		return (inventory.get(itemName) != 0);

	}

	/**
	 * Returns the items that this unit has to the shop inventory.  Does not remove the items from
	 * the unit!
	 * 
	 * @param unit
	 */
	public void refundItems(Unit unit){
		System.out.println("Refunding items on: " + unit.name);
		for(Item i : unit.getInventory()){
			System.out.println("Refunding: " + i.name);
			if(!(i instanceof Weapon && ((Weapon) i).pref != null) && inventory.get(i.name) != -1){

				System.out.println("+1 : " + i.name + " available");
				inventory.put(i.name, inventory.get(i.name) + 1);
			}

		}
	}

	/**
	 * Increases the stock of a single item by 1.
	 * 
	 * @param item
	 */
	public void returnItem(String itemName){
		if(inventory.get(itemName) != -1){
			inventory.put(itemName, inventory.get(itemName) + 1);
		}
	}
	
	public int countOf(String itemName){
		return inventory.get(itemName);
	}
}
