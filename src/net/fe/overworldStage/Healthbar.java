package net.fe.overworldStage;

import net.fe.FEResources;
import net.fe.fightStage.FightStage;
import net.fe.unit.Unit;

import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

import chu.engine.Entity;
import chu.engine.Game;
import chu.engine.anim.AudioPlayer;
import chu.engine.anim.Renderer;

// TODO: Auto-generated Javadoc
/**
 * The Class Healthbar.
 */
public abstract class Healthbar extends Entity {
	
	/** The hp1. */
	private int hp1;
	
	/** The total health. */
	private float displayedHealth, totalHealth;
	
	/** The time. */
	private float time;
	
	/** The color. */
	private Color color;
	
	/** The tick filled. */
	private Texture tickFilled = FEResources.getTexture("gui_tickFilled");
	
	/** The tick empty. */
	private Texture tickEmpty = FEResources.getTexture("gui_tickEmpty");
	
	/**
	 * Instantiates a new healthbar.
	 *
	 * @param u the u
	 * @param hp0 the hp0
	 * @param hp1 the hp1
	 * @param stage the stage
	 */
	public Healthbar(Unit u, int hp0, int hp1, ClientOverworldStage stage){
		super(0,0);
		this.displayedHealth = hp0;
		this.hp1 = hp1;
		this.totalHealth = u.get("HP");
		x = u.x + 8 - 30 - stage.camX;
		y = u.y - 32 - stage.camY;
		color = u.getPartyColor();
	}
	
	/* (non-Javadoc)
	 * @see chu.engine.Entity#onStep()
	 */
	public void onStep(){
		int oldH = (int) displayedHealth;
		if(Math.abs(displayedHealth-hp1) >= 1){
			displayedHealth += Math.signum(hp1 - displayedHealth)
					*6*Game.getDeltaSeconds();
			if(oldH != (int) displayedHealth){
				AudioPlayer.playAudio("hp_recovery");
			}
		} else if(displayedHealth != hp1){
			displayedHealth = hp1;
			
		} else {
			time+= Game.getDeltaSeconds();
			if(time > 1)
				done();
		}
	}
	
	/**
	 * Done.
	 */
	public abstract void done();
	
	/* (non-Javadoc)
	 * @see chu.engine.Entity#render()
	 */
	public void render(){
		Renderer.drawRectangle(x-24, y-6, x+85, y+20, renderDepth, FightStage.BORDER_DARK);
		Renderer.drawRectangle(x-23, y-5, x+84, y+19, renderDepth, FightStage.BORDER_LIGHT);
		Renderer.drawRectangle(x-22, y-4, x+83, y+18, renderDepth, color);
		int offY = 0;
		int offX = 0;
		int width = FEResources.getBitmapFont("stat_numbers").getStringWidth((int)displayedHealth + "");
		if(totalHealth <= 40) {
			Renderer.drawString("stat_numbers", (int)displayedHealth + "", x-5-width, y-2, renderDepth);
		} else {
			Renderer.drawString("stat_numbers", (int)displayedHealth + "", x-5-width, y+2, renderDepth);
		}
		for (int hp = 1; hp <= totalHealth; hp++) {
			Texture t = hp <= displayedHealth ? tickFilled : tickEmpty;
			Renderer.render(t, 0, 0, 1, 1, x + offX, y + offY, x + offX + 2, y
					+ offY + 6, renderDepth);
			
			if(hp == 40){
				offY = 8;
				offX = 0;
			} else {
				offX +=2;
			}
		}
	}

}
