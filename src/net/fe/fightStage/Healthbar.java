package net.fe.fightStage;

import net.fe.FEResources;
import net.fe.unit.Unit;

import org.newdawn.slick.opengl.Texture;

import chu.engine.Entity;
import chu.engine.Game;
import chu.engine.anim.Renderer;

// TODO: Auto-generated Javadoc
/**
 * The Class Healthbar.
 */
public class Healthbar extends Entity {
	
	/** The total health. */
	private int totalHealth;
	
	/** The current health. */
	private int currentHealth;
	
	/** The displayed health. */
	private float displayedHealth;
	
	/** The done animating. */
	public boolean doneAnimating;
	
	/** The do not set. */
	private boolean doNotSet;
	
	/** The empty. */
	private static Texture filled, empty;

	/**
	 * Instantiates a new healthbar.
	 *
	 * @param u the u
	 * @param left the left
	 */
	public Healthbar(Unit u, boolean left) {
		super(0, 0);
		doneAnimating = true;
		if(left){
			x = FightStage.CENTRAL_AXIS - 91;
		} else {
			x = FightStage.CENTRAL_AXIS + 30;
		}
		if(u.get("HP") > 40){
			y = FightStage.FLOOR + 38;
		} else {
			y = FightStage.FLOOR + 42;
		}
		totalHealth = u.get("HP");
		currentHealth = u.getHp();
		displayedHealth = u.getHp();
		renderDepth = FightStage.HP_DEPTH;
		if(filled == null){
			filled = FEResources.getTexture("gui_tickFilled");
			empty = FEResources.getTexture("gui_tickEmpty");
		}
	}

	/* (non-Javadoc)
	 * @see chu.engine.Entity#render()
	 */
	public void render() {
		int offY = 0;
		int offX = 0;
		int width = FEResources.getBitmapFont("stat_numbers").getStringWidth((int)displayedHealth + "");
		if(totalHealth <= 40) {
			Renderer.drawString("stat_numbers", (int)displayedHealth + "", x-5-width, y-2, renderDepth);
		} else {
			Renderer.drawString("stat_numbers", (int)displayedHealth + "", x-5-width, y+2, renderDepth);
		}
		for (int hp = 1; hp <= totalHealth; hp++) {
			Texture t = hp <= displayedHealth? filled: empty;
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

	/* (non-Javadoc)
	 * @see chu.engine.Entity#onStep()
	 */
	public void onStep() {
		if(Math.abs(displayedHealth-currentHealth) >= 1){
			doneAnimating = false;
			displayedHealth += Math.signum(currentHealth - displayedHealth)*15*Game.getDeltaSeconds();
		} else if(displayedHealth != currentHealth){
			displayedHealth = currentHealth;
			if(!doNotSet){
				doneAnimating = true;
			}
		}
	}
	
	/**
	 * Gets the hp.
	 *
	 * @return the hp
	 */
	public int getHp(){
		return currentHealth;
	}
	
	/**
	 * Sets the hp.
	 *
	 * @param hp the new hp
	 */
	public void setHp(int hp){
		currentHealth = hp;
		doNotSet = false;
	}
	
	/**
	 * Sets the hp.
	 *
	 * @param hp the hp
	 * @param stateShift the state shift
	 */
	public void setHp(int hp, boolean stateShift){
		doNotSet = !stateShift;
		setHp(hp);
	}

}
