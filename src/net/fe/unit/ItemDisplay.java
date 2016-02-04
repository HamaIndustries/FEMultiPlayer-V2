package net.fe.unit;


import org.newdawn.slick.opengl.Texture;

import net.fe.FEResources;
import chu.engine.Entity;
import chu.engine.Game;
import chu.engine.anim.Renderer;
import chu.engine.anim.ShaderArgs;
import chu.engine.anim.Transform;

// TODO: Auto-generated Javadoc
/**
 * The Class ItemDisplay.
 */
public class ItemDisplay extends Entity{
	
	/** The item. */
	private Item item;
	
	/** The equip. */
	private boolean equip;
	
	/** The weapon icon. */
	private static Texture weaponIcon = FEResources.getTexture("gui_weaponIcon");
	
	/** The e. */
	private static Texture e = FEResources.getTexture("e");
	
	/**
	 * Instantiates a new item display.
	 *
	 * @param f the f
	 * @param g the g
	 * @param i the i
	 * @param equip the equip
	 */
	public ItemDisplay(float f, float g, Item i, boolean equip){
		super(f,g);
		renderDepth = 0.05f;
		item = i;
		this.equip = equip;
	}
	
	/* (non-Javadoc)
	 * @see chu.engine.Entity#render()
	 */
	public void render(){
		render(null, false, 0);
	}
	
	/**
	 * Render.
	 *
	 * @param t the t
	 * @param effective the effective
	 * @param timer the timer
	 */
	public void render(Transform t, boolean effective, float timer) {
		if(item == null) return;
		int row = item.id/8;
		int col = item.id%8;
		ShaderArgs args;
		if(effective) {
			float exp = (float) (Math.sin(timer)/2 + .5f);
			args = new ShaderArgs("lighten", exp);
		} else {
			args = new ShaderArgs();
		}
		Renderer.render(weaponIcon, 
				col/8.0f, row/9.0f, (col+1)/8.0f, (row+1)/9.0f,
				x-1, y, x+16, y+17, renderDepth, t, args);
		FEResources.getBitmapFont("default_med").render(item.name, x+16, y+3, renderDepth, t);
		if(equip){
			Renderer.render(e, 
					0, 0, 1, 1,
					x+10, y+10, x+16, y+17, renderDepth, t);
		}
	}
	
	/**
	 * Gets the item.
	 *
	 * @return the item
	 */
	public Item getItem(){
		return item;
	}
}
