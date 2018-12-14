package net.fe.overworldStage;

import net.fe.FEResources;
import net.fe.fightStage.FightStage;
import net.fe.unit.Unit;

import org.newdawn.slick.Color;

import chu.engine.Entity;
import chu.engine.Game;
import chu.engine.anim.AudioPlayer;
import chu.engine.anim.Renderer;

/**
 * A component and animation that displays a change in SkillCharge while in the overworld
 */
public final class SkillChargeBar extends Entity {
	
	private static final Color outlineColor = new Color(88, 0, 0);
	private static final Color emptyColor = new Color(160, 0, 40);
	private static final Color filledColor = new Color(248, 184, 224);
	private static final Color filledColor2 = new Color(248, 136, 144);
	
	private final Color teamColor;
	
	private final int totalValue;
	private final int endValue;
	private final float direction;
	
	private float currentValue;
	private final Runnable onComplete;
	
	private float timeSinceAnimationEnd;
	
	/**
	 * Instantiates a new healthbar.
	 *
	 * @param u the affected unit
	 * @param startValue the starting value
	 * @param endValue the ending value
	 * @param stage the stage
	 */
	public SkillChargeBar(Unit u, int startValue, int endValue, ClientOverworldStage stage, Runnable onComplete){
		super(0,0);
		x = u.x + 8 - 30 - stage.camX;
		y = u.y - 32 - stage.camY;
		this.timeSinceAnimationEnd = 0;
		
		this.direction = Math.signum(endValue - startValue);
		this.endValue = endValue;
		this.currentValue = startValue;
		this.onComplete = onComplete;
		
		this.totalValue = Unit.MAX_SKILL_CHARGE;
		this.teamColor = u.getPartyColor();
	}
	
	public void onStep(){
		final int previousValue = (int) currentValue;
		
		if (Math.signum(endValue - currentValue) == this.direction) {
			currentValue += this.direction * 6 * Game.getDeltaSeconds();
			if (previousValue != (int) currentValue) {
				AudioPlayer.playAudio("hp_recovery");
			}
		} else {
			this.currentValue = endValue;
			timeSinceAnimationEnd += Game.getDeltaSeconds();
			if (timeSinceAnimationEnd >= 1) {
				this.done();
			}
		}
	}
	
	private void done() {
		this.destroy();
		this.onComplete.run();
	}
	
	public void render(){
		Renderer.drawRectangle(x-24, y-6, x+85, y+20, renderDepth, FightStage.BORDER_DARK);
		Renderer.drawRectangle(x-23, y-5, x+84, y+19, renderDepth, FightStage.BORDER_LIGHT);
		Renderer.drawRectangle(x-22, y-4, x+83, y+18, renderDepth, teamColor);
		int offY = 0;
		int offX = 0;
		int width = FEResources.getBitmapFont("stat_numbers").getStringWidth((int)currentValue + "");
		if(totalValue <= 40) {
			Renderer.drawString("stat_numbers", (int)currentValue + "", x-5-width, y-2, renderDepth);
		} else {
			Renderer.drawString("stat_numbers", (int)currentValue + "", x-5-width, y+2, renderDepth);
		}
		for (int hp = 1; hp <= totalValue; hp++) {
			Renderer.drawRectangle(x + offX, y + offY, x + offX + 2, y + offY + 6, renderDepth, outlineColor);
			if (hp <= currentValue) {
				Renderer.drawRectangle(x + offX + 1, y + offY + 1, x + offX + 2, y + offY + 3, renderDepth, filledColor);
				Renderer.drawRectangle(x + offX + 1, y + offY + 3, x + offX + 2, y + offY + 5, renderDepth, filledColor2);
			} else {
				Renderer.drawRectangle(x + offX + 1, y + offY + 1, x + offX + 2, y + offY + 5, renderDepth, emptyColor);
			}
			
			if(hp == 40){
				offY = 8;
				offX = 0;
			} else {
				offX +=2;
			}
		}
	}

}
