package net.fe.overworldStage;

import java.util.Iterator;

import net.fe.game.unit.Item;
import net.fe.game.unit.ItemDisplay;
import net.fe.game.unit.Unit;

// TODO: Auto-generated Javadoc
/**
 * The Class InventoryMenu.
 */
public class InventoryMenu extends ItemMenu {

	/** The unit. */
	protected Unit unit;

	/**
	 * Instantiates a new inventory menu.
	 *
	 * @param u the u
	 * @param x the x
	 * @param y the y
	 */
	public InventoryMenu(Unit u, float x, float y) {
		super(x, y);
		unit = u;
	}

	/**
	 * Instantiates a new inventory menu.
	 *
	 * @param u the u
	 */
	public InventoryMenu(Unit u) {
		this(u, 0, 0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see chu.engine.Entity#onStep()
	 */
	public void onStep() {
		items.clear();
		Iterator<Item> inv = unit.getInventory().iterator();
		for (int i = 0; i < 4; i++) {
			if (inv.hasNext()) {
				Item it = inv.next();
				items.add(new ItemDisplay(0, 0, it, unit.getWeapon() == it));
			} else {
				items.add(null);
			}
		}
	}
}