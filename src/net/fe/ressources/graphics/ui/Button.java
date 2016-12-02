package net.fe.ressources.graphics.ui;

import org.newdawn.slick.Color;

import chu.engine.anim.Renderer;
import chu.engine.entity.Entity;
import net.fe.FEResources;
import net.fe.game.fightStage.FightStage;

// TODO: Auto-generated Javadoc
/**
 * The Class Button.
 */
public abstract class Button extends Entity{
	
	/** The text. */
	protected String text;
	
	/** The color. */
	private Color color;
	
	/** The hover. */
	private boolean hover;
	
	/** The width. */
	private int width;
	
	/**
	 * Instantiates a new button.
	 *
	 * @param x the x
	 * @param y the y
	 * @param text the text
	 * @param color the color
	 * @param width the width
	 */
	public Button(float x, float y, String text, Color color, int width) {
		super(x, y);
		this.text = text;
		this.color = color;
		this.width = width;
		this.renderDepth = 0.05f;
	}
		
	/* (non-Javadoc)
	 * @see chu.engine.Entity#render()
	 */
	public void render(){
		int stringWidth = FEResources.getBitmapFont("default_med").getStringWidth(text);
		Color c = new Color(color);
		if(!hover)
			c = c.darker();
		Renderer.drawBorderedRectangle(x, y, x+width, y+20, renderDepth, c, FightStage.BORDER_LIGHT, FightStage.BORDER_DARK);
		Renderer.drawString("default_med", text, x+width/2-stringWidth/2, y + 4, renderDepth);
		
	}
	
	/**
	 * Sets the hover.
	 *
	 * @param hover the new hover
	 */
	public void setHover(boolean hover){
		this.hover = hover;
	}
	
	/**
	 * Hovered.
	 *
	 * @return true, if successful
	 */
	public boolean hovered(){
		return hover;
	}
	
	/**
	 * Execute.
	 */
	public abstract void execute();
}
