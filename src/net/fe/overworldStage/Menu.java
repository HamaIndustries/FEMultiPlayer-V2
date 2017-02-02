package net.fe.overworldStage;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;

import chu.engine.anim.Renderer;
import chu.engine.entity.Entity;

// TODO: Auto-generated Javadoc
/**
 * The Class Menu.
 *
 * @param <T> the generic type
 */
public class Menu<T> extends Entity {

	/** The items. */
	protected List<T> items;

	/** The selection. */
	protected int selection;

	/** The width. */
	private int width;

	/** The height. */
	protected int height;

	/** The cleared. */
	private boolean cleared;

	/** The marked. */
	private int marked = -1;

	/** The Constant MENU. */
	public static final Color MENU = new Color(0xBBBBBB);

	/** The Constant MENU_SELECT. */
	public static final Color MENU_SELECT = new Color(0x999999);

	/** The Constant MENU_MARKED. */
	public static final Color MENU_MARKED = new Color(0x777777);

	/**
	 * Instantiates a new menu.
	 *
	 * @param x the x
	 * @param y the y
	 */
	public Menu(float x, float y) {
		super(x, y);
		this.items = new ArrayList<T>();
		renderDepth = ClientOverworldStage.MENU_DEPTH;
		width = 60;
		height = 16;
	}

	/**
	 * Instantiates a new menu.
	 */
	public Menu() {
		this(0, 0);
	}

	/**
	 * Adds the item.
	 *
	 * @param item the item
	 */
	public void addItem(T item) {
		items.add(item);
	}

	/**
	 * Removes the item.
	 *
	 * @param item the item
	 */
	public void removeItem(T item) {
		items.remove(item);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see chu.engine.Entity#render()
	 */
	public void render() {
		int oY = 0;
		for (int i = 0; i < items.size(); i++) {
			Color c = (selection == i && !cleared) ? MENU_SELECT : MENU;
			if (i == marked) {
				c = MENU_MARKED;
			}
			Renderer.drawRectangle(x, y + oY, x + width, y + oY + height, renderDepth, c);
			renderItem(items.get(i), oY);
			oY += height + 1;
		}
	}

	/**
	 * Render item.
	 *
	 * @param item the item
	 * @param offsetY the offset y
	 */
	public void renderItem(T item, int offsetY) {
		Renderer.drawString("default_med", String.valueOf(item), x + 1, y + offsetY + 1, 0);
	}

	/**
	 * Down.
	 */
	public void down() {
		selection = (selection + 1) % items.size();
		if (selection == marked)
			down();
	}

	/**
	 * Up.
	 */
	public void up() {
		selection = (selection - 1) % items.size();
		if (selection < 0) {
			selection += items.size();
		}
		if (selection == marked)
			up();
	}

	/**
	 * Size.
	 *
	 * @return the int
	 */
	public int size() {
		return items.size();
	}

	/**
	 * Gets the.
	 *
	 * @param index the index
	 * @return the t
	 */
	public T get(int index) {
		return items.get(index);
	}

	/**
	 * Sets the selection.
	 *
	 * @param index the new selection
	 */
	public void setSelection(int index) {
		selection = index;
		if (selection == marked)
			down();
	}

	/**
	 * Gets the selection.
	 *
	 * @return the selection
	 */
	public T getSelection() {
		return items.get(selection);
	}

	/**
	 * Gets the selected index.
	 *
	 * @return the selected index
	 */
	public int getSelectedIndex() {
		return selection;
	}

	/**
	 * Gets the marked.
	 *
	 * @return the marked
	 */
	public T getMarked() {
		if (marked == -1)
			return null;
		return items.get(marked);
	}

	/**
	 * Gets the marked index.
	 *
	 * @return the marked index
	 */
	public int getMarkedIndex() {
		return marked;
	}

	/**
	 * Clear selection.
	 */
	public void clearSelection() {
		cleared = true;
	}

	/**
	 * Restore selection.
	 */
	public void restoreSelection() {
		cleared = false;
	}

	/**
	 * Checks for selection.
	 *
	 * @return true, if successful
	 */
	public boolean hasSelection() {
		return !cleared;
	}

	/**
	 * Mark.
	 *
	 * @param i the i
	 */
	public void mark(int i) {
		marked = i;
	}

	/**
	 * Unmark.
	 */
	public void unmark() {
		marked = -1;
	}

	/**
	 * Gets the width.
	 *
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Sets the width.
	 *
	 * @param width the new width
	 */
	public void setWidth(int width) {
		this.width = width;
	}

}
