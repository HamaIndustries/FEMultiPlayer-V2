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
	private Map<String, Integer> classInventory;

	public ShopInventory(Iterable<Modifier> mods){
		inventory = new HashMap<String, Integer>();
		classInventory = new HashMap<String, Integer>();

		for(Item i : Item.getAllItems()){
			inventory.put(i.name, -1);
			if(i.getItemClass() != "" && !inventory.containsKey(i.getItemClass())){
				classInventory.put(i.getItemClass(), -1);
			}
		}

		if(mods != null){

			for(Modifier m : mods){
				m.setShopLimits(inventory, classInventory);
			}
		}
	}


	/**
	 * Takes the item name of the item the player is trying to buy.  Returns true and decrements the remaining
	 * stock (both from class and item) if it succeeds, false if the weapon can't be bought.
	 * 
	 * @param itemName - The name of the item to buy
	 * @return
	 */
	public boolean buyItem(Item item){
		if(classInventory.get(item.getItemClass()) != null){

			if(inventory.get(item.name) != 0 && classInventory.get(item.getItemClass()) != 0){
				if(inventory.get(item.name) != -1){
					inventory.put(item.name, inventory.get(item.name) - 1);
				}

				if(classInventory.get(item.getItemClass()) != -1){
					classInventory.put(item.getItemClass(), classInventory.get(item.getItemClass())-1);
				}

				return true;
			}
			return false;
		}
		else{
			if(inventory.get(item.name) != 0){
				if(inventory.get(item.name) != -1){
					inventory.put(item.name, inventory.get(item.name) - 1);
				}

				return true;
			}
			return false;
		}
	}

	/**
	 * Returns true if the item is still in stock, false otherwise.
	 * 
	 * @param itemName
	 * @return
	 */
	public boolean itemInStock(Item item){
		if(classInventory.get(item.getItemClass()) != null){
			return ((inventory.get(item.name) != 0) && (classInventory.get(item.getItemClass()) != 0));

		}
		else{
			return inventory.get(item.name) != 0;
		}

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
			if(!(i instanceof Weapon && ((Weapon) i).pref != null)){
				if(inventory.get(i.name) != -1){
					System.out.println("+1 : " + i.name + " available");
					inventory.put(i.name, inventory.get(i.name) + 1);					
				}
				if(classInventory.get(i.getItemClass()) != null && classInventory.get(i.getItemClass()) != -1){
					System.out.println("+1 : " + i.getItemClass() + " available");
					classInventory.put(i.getItemClass(), classInventory.get(i.getItemClass()) + 1);
				}
			}

		}
	}

	/**
	 * Increases the stock of a single item by 1.
	 * 
	 * @param item
	 */
	public void returnItem(Item item){
		if(inventory.get(item.name) != -1){
			inventory.put(item.name, inventory.get(item.name) + 1);
		}
		if(classInventory.get(item.getItemClass()) != null && classInventory.get(item.getItemClass()) != -1){
			classInventory.put(item.getItemClass(), classInventory.get(item.getItemClass()) + 1);
		}
	}

	/**
	 * Takes in an item and returns the number currently available.  Returns -1 if there are infinitely many left.
	 * 
	 * @param itemName
	 * @return
	 */
	public int countOf(Item item){
		//If it has a class, check it.  Otherwise, return the individual value.
		if(classInventory.get(item.getItemClass()) != null){

			//If both are infinite, it's infinite
			if(inventory.get(item.name) <= -1 && classInventory.get(item.getItemClass()) <= -1){
				return -1;
			}
			//If one is -1, the other (higher) value should be returned.
			if(inventory.get(item.name) <= -1 || classInventory.get(item.getItemClass()) <= -1){
				return Math.max(inventory.get(item.name), classInventory.get(item.getItemClass()));
			}
			else{
				return Math.min(inventory.get(item.name), classInventory.get(item.getItemClass()));
			}
		}
		else{
			return inventory.get(item.name);
		}
	}
}
