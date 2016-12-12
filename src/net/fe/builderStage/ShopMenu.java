package net.fe.builderStage;

import java.util.Set;

import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

import chu.engine.Game;
import chu.engine.anim.Renderer;
import chu.engine.entity.Entity;
import net.fe.FEResources;
import net.fe.game.modifier.Modifier;
import net.fe.game.unit.Item;
import net.fe.game.unit.ItemDisplay;
import net.fe.game.unit.RiseTome;
import net.fe.game.unit.Weapon;
import net.fe.overworldStage.ItemMenu;

public class ShopMenu extends Entity {

	private ItemMenu[] shops;

	private Texture[] shopIcons;

	private int selected;

	private int cameraX;

	private boolean instant;

	public static final int HEIGHT = 200;

	public static final int WIDTH = 135;

	public ShopMenu(float x, float y, Set<Modifier> ms) {
		super(x, y);
		shops = new ItemMenu[9];
		shopIcons = new Texture[9];
		for (int i = 0; i < shops.length; i++) {
			shops[i] = new ItemMenu(x + 140 * i, y) {
				{
					drawCost = true;
					setWidth(135);
				}
			};
			shopIcons[i] = FEResources.getTexture("shop" + i);
		}

		Iterable<Item> items = Item.getAllItems();
		for (Modifier m : ms) {
			items = m.modifyShop(items);
		}

		for (Item i : items) {
			ItemDisplay display = new ItemDisplay(0, 0, i.getCopy(), false);

			if (i instanceof Weapon) {
				Weapon w = (Weapon) i;
				if (w.pref != null || w.name.startsWith("Debug") || w.getCost() == 1)
					continue;
				switch (w.type) {
					case SWORD:
						shops[0].addItem(display);
						break;
					case LANCE:
						shops[1].addItem(display);
						break;
					case AXE:
						shops[2].addItem(display);
						break;
					case BOW:
						shops[3].addItem(display);
						break;
					case CROSSBOW:
						shops[3].addItem(display);
						break;
					case LIGHT:
						shops[4].addItem(display);
						break;
					case ANIMA:
						shops[5].addItem(display);
						break;
					case DARK:
						shops[6].addItem(display);
						break;
					case STAFF:
						shops[7].addItem(display);
						break;
				}
			} else if (i instanceof RiseTome) {
				shops[6].addItem(display);
			} else { // including HealingItems
				shops[8].addItem(display);
			}
		}

		for (ItemMenu shop : shops) {
			shop.sortItems();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see chu.engine.Entity#onStep()
	 */
	public void onStep() {
		int shouldX = selected * 140;
		float dx = Math.signum(shouldX - cameraX) * Game.getDeltaSeconds() * 600;
		cameraX += dx;
		if (dx * (shouldX - cameraX) < 0 || instant) {
			cameraX = shouldX;
			instant = false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see chu.engine.Entity#render()
	 */
	public void render() {
		Renderer.addClip(x, y - 16, getSelectedShop().getWidth(), HEIGHT + 16, true);
		for (int i = 0; i < shops.length; i++) {
			shops[i].x = x + 140 * i - cameraX;
			if (shops[i].x >= x - 140 && shops[i].x <= x + 140)
				shops[i].render();

			Color c = new Color(0.5f, 0.5f, 0.5f);
			if (i == selected)
				c = new Color(Color.white);
			float iconX = x + i * 15;
			Renderer.setColor(c);
			Renderer.render(shopIcons[i], 0, 0, 1, 1, iconX, y - 15, iconX + 15, y, renderDepth);
			Renderer.setColor(Color.white);
		}
		Renderer.removeClip();

	}

	private ItemMenu getSelectedShop() {
		return shops[selected];
	}

	public Item getItem() {
		return getSelectedShop().getSelection().getItem().getCopy();
	}

	public void left() {
		selected--;
		if (selected < 0) {
			selected += shops.length;
			instant = true;
		}
		getSelectedShop().setSelection(0);
	}

	public void right() {
		selected++;
		if (selected >= shops.length) {
			selected -= shops.length;
			instant = true;
		}
		getSelectedShop().setSelection(0);
	}

	public void up() {
		getSelectedShop().up();
	}

	public void down() {
		getSelectedShop().down();
	}

	public void clearSelection() {
		for (ItemMenu shop : shops) {
			shop.clearSelection();
		}
	}

	public void restoreSelection() {
		for (ItemMenu shop : shops) {
			shop.restoreSelection();
		}
	}

	public ItemMenu[] getShops() {
		return shops;
	}

}
