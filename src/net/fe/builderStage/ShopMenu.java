package net.fe.builderStage;

import net.fe.FEResources;
import net.fe.Session;
import net.fe.modifier.Modifier;
import net.fe.overworldStage.ItemMenu;
import net.fe.unit.HealingItem;
import net.fe.unit.Item;
import net.fe.unit.ItemDisplay;
import net.fe.unit.RiseTome;
import net.fe.unit.Weapon;
import net.fe.unit.WeaponFactory;

import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

import chu.engine.Entity;
import chu.engine.Game;
import chu.engine.anim.Renderer;

// TODO: Auto-generated Javadoc
/**
 * The Class ShopMenu.
 */
public class ShopMenu extends Entity {
	
	/** The shops. */
	private ItemMenu[] shops;
	
	/** The shop icons. */
	private Texture[] shopIcons;
	
	/** The selected. */
	private int selected;
	
	/** The camera x. */
	private int cameraX;
	
	/** The instant. */
	private boolean instant;
	
	/** The Constant HEIGHT. */
	public static final int HEIGHT = 200;
	
	/** The Constant WIDTH. */
	public static final int WIDTH = 135;
	
	/**
	 * Instantiates a new shop menu.
	 *
	 * @param x the x
	 * @param y the y
	 * @param s the s
	 */
	public ShopMenu(float x, float y, Session s) {
		super(x, y);
		shops = new ItemMenu[9];
		shopIcons = new Texture[9];
		for(int i = 0; i < shops.length; i++){
			shops[i] = new ItemMenu(x + 140*i,y){{
				drawCost = true;
				setWidth(135);
			}};
			shopIcons[i] = FEResources.getTexture("shop" + i);
		}
		for(Weapon w: WeaponFactory.getAllWeapons()){
			ItemDisplay i = new ItemDisplay(0, 0, w.getCopy(), false);
			if(w.pref != null || w.name.startsWith("Debug") || w.getCost() == 1) continue;
			switch(w.type){
			case SWORD: shops[0].addItem(i); break;
			case LANCE: shops[1].addItem(i); break;
			case AXE: shops[2].addItem(i); break;
			case BOW: shops[3].addItem(i); break;
			case CROSSBOW: shops[3].addItem(i); break;
			case LIGHT: shops[4].addItem(i); break;
			case ANIMA: shops[5].addItem(i); break;
			case DARK: shops[6].addItem(i); break;
			case STAFF: shops[7].addItem(i); break;
			}
		}
		shops[8].addItem(new ItemDisplay(0,0,HealingItem.VULNERARY.getCopy(), false));
		shops[8].addItem(new ItemDisplay(0,0,HealingItem.CONCOCTION.getCopy(), false));
		shops[8].addItem(new ItemDisplay(0,0,HealingItem.ELIXIR.getCopy(), false));
		shops[6].addItem(new ItemDisplay(0,0,new RiseTome(), false));
		
		for(ItemMenu shop: shops){
			shop.sortItems();
		}
		
		if(s != null) {
			for(Modifier m : s.getModifiers()) {
				m.modifyShop(this);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see chu.engine.Entity#onStep()
	 */
	public void onStep(){
		int shouldX = selected*140;
		float dx = Math.signum(shouldX - cameraX) * Game.getDeltaSeconds() * 600;
		cameraX+= dx;
		if(dx * (shouldX - cameraX) < 0 || instant){
			cameraX = shouldX;
			instant = false;
		}
	}
	
	/* (non-Javadoc)
	 * @see chu.engine.Entity#render()
	 */
	public void render(){
		Renderer.addClip(x, y-16, getSelectedShop().getWidth(), HEIGHT+16, true);
		for(int i = 0; i < shops.length; i++){
			shops[i].x = x + 140*i - cameraX;
			if(shops[i].x >= x - 140 && shops[i].x <= x + 140)
				shops[i].render();
			
			Color c = new Color(0.5f,0.5f,0.5f);
			if(i == selected) c = new Color(Color.white);
			float iconX = x + i*15;
			Renderer.setColor(c);
			Renderer.render(shopIcons[i], 0, 0, 1, 1, iconX, y-15, iconX + 15, y, renderDepth);
			Renderer.setColor(Color.white);
		}
		Renderer.removeClip();
		
	}
	
	/**
	 * Gets the selected shop.
	 *
	 * @return the selected shop
	 */
	private ItemMenu getSelectedShop(){
		return shops[selected];
	}
	
	/**
	 * Gets the item.
	 *
	 * @return the item
	 */
	public Item getItem(){
		return getSelectedShop().getSelection().getItem().getCopy();
	}
	
	/**
	 * Left.
	 */
	public void left(){
		selected--;
		if(selected< 0){
			selected+= shops.length;
			instant = true;
		}
		getSelectedShop().setSelection(0);
	}
	
	/**
	 * Right.
	 */
	public void right(){
		selected++;
		if(selected >= shops.length){
			selected -= shops.length;
			instant = true;
		}
		getSelectedShop().setSelection(0);
	}
	
	/**
	 * Up.
	 */
	public void up(){
		getSelectedShop().up();
	}
	
	/**
	 * Down.
	 */
	public void down(){
		getSelectedShop().down();
	}
	
	/**
	 * Clear selection.
	 */
	public void clearSelection(){
		for(ItemMenu shop: shops){
			shop.clearSelection();
		}
	}
	
	/**
	 * Restore selection.
	 */
	public void restoreSelection(){
		for(ItemMenu shop: shops){
			shop.restoreSelection();
		}
	}
	
	/**
	 * Gets the shops.
	 *
	 * @return the shops
	 */
	public ItemMenu[] getShops() {
		return shops;
	}

}
