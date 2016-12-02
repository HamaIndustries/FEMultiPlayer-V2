package net.fe.overworldStage;

import java.util.Collections;
import java.util.Comparator;

import chu.engine.anim.Renderer;
import net.fe.FEResources;
import net.fe.game.unit.ItemDisplay;

// TODO: Auto-generated Javadoc
/**
 * The Class ItemMenu.
 */
public class ItemMenu extends Menu<ItemDisplay> {

	/** The draw uses. */
	protected boolean drawUses;

	/** The draw cost. */
	protected boolean drawCost;

	/**
	 * Instantiates a new item menu.
	 *
	 * @param x the x
	 * @param y the y
	 */
	public ItemMenu(float x, float y) {
		super(x, y);
		setWidth(98);
		height = 17;
		drawUses = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.fe.overworldStage.Menu#renderItem(java.lang.Object, int)
	 */
	public void renderItem(ItemDisplay w, int offsetY) {
		if (w == null)
			return;
		w.x = this.x;
		w.y = this.y + offsetY;
		w.renderDepth = renderDepth;
		w.render();
		int uses = w.getItem().getUses();
		int offX = uses < 10 ? 7 : 0;
		offX += 80;
		if (drawUses) {
			Renderer.drawString("default_med", uses + "", x + offX, y + offsetY + 2, renderDepth);
		}
		if (drawCost) {
			String cost;
			if (w.getItem().getCost() != 0)
				cost = w.getItem().getCost() + "";
			else
				cost = "--";
			int width = FEResources.getBitmapFont("default_med").getStringWidth(cost);
			Renderer.drawString("default_med", cost, x + 133 - width, y + offsetY + 2, renderDepth);
		}
	}

	/**
	 * Sort items.
	 */
	public void sortItems() {
		Collections.sort(items, new Comparator<ItemDisplay>() {
			@Override
			public int compare(ItemDisplay i1, ItemDisplay i2) {
				return i1.getItem().compareTo(i2.getItem());
			}
		});
	}

}
