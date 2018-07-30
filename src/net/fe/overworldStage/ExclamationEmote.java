package net.fe.overworldStage;

import net.fe.unit.Unit;

import org.newdawn.slick.Color;

import chu.engine.Entity;
import chu.engine.Game;
import chu.engine.anim.AudioPlayer;
import chu.engine.anim.Renderer;

/**
 * An icon shown when a unit's move is cut short by an attempt to walk though an
 * enemy unit in fog-of-war.
 */
public final class ExclamationEmote extends Entity {
	
	private static final float WIDTH = 17;
	private static final float HEIGHT = 17;
	private static final Color stroke = new Color(/* goldenrod */ 0xDAA520);
	private static final Color background = Color.white;
	private static final Color foreground = Color.red;
	
	private static final float growTime = 0.1f;
	private static final float shrinkTime = 0.1f;
	private static final float duration = 0.5f;
	private static final float oversize = 1.5f;
	
	private final Runnable onFinished;
	private float currentTime;
	
	/**
	 *
	 * @param u the unit to place this icon over
	 * @param stage the stage
	 * @param onFinished the Runnable to run when this entity is destroyed
	 */
	public ExclamationEmote(Unit u, ClientOverworldStage stage, Runnable onFinished){
		super(0,0);
		this.onFinished = onFinished;
		this.currentTime = 0;
		x = u.x - stage.camX;
		y = u.y - stage.camY - HEIGHT + 3;
	}
	
	public void onStep() {
		this.currentTime += Game.getDeltaSeconds();
		if (this.currentTime > this.duration) {
			this.destroy();
			onFinished.run();
		}
	}
	
	public void render() {
		final float sizeMultiplier = (
			currentTime < growTime ? oversize * currentTime / growTime :
			currentTime < growTime + shrinkTime ? oversize - (oversize - 1f) * (currentTime - growTime) / shrinkTime:
			1.0f
		);
		
		final float currentWidth = WIDTH * sizeMultiplier;
		final float currentHeight = HEIGHT * sizeMultiplier;
		// currentY is the bottom - not the top like most other Ys, including this.y
		final float currentY = this.y + HEIGHT;
		final float currentX = this.x - (currentWidth - WIDTH) / 2;
		
		Renderer.drawTriangle(currentX + currentWidth / 2, currentY - currentHeight,
				currentX + currentWidth / 2, currentY,
				currentX + 0, currentY - currentHeight / 2,
				renderDepth, stroke);
		Renderer.drawTriangle(currentX + currentWidth / 2, currentY - currentHeight,
				currentX + currentWidth / 2, currentY,
				currentX + currentWidth, currentY - currentHeight / 2,
				renderDepth, stroke.darker());
		
		Renderer.drawTriangle(currentX + currentWidth / 2, currentY - (currentHeight * (HEIGHT - 1) / HEIGHT),
				currentX + currentWidth / 2, currentY - (currentHeight * 1 / HEIGHT),
				currentX + 1, currentY - currentHeight / 2,
				renderDepth, background);
		Renderer.drawTriangle(currentX + currentWidth / 2, currentY - (currentHeight * (HEIGHT - 1) / HEIGHT),
				currentX + currentWidth / 2, currentY - (currentHeight * 1 / HEIGHT),
				currentX + currentWidth - 1, currentY - currentHeight / 2,
				renderDepth, background);
		
		Renderer.drawTriangle(currentX + currentWidth / 2, currentY - (currentHeight * 3 / 17),
				currentX + currentWidth / 2 - 2, currentY - (currentHeight * 6 / 17),
				currentX + currentWidth / 2 + 2, currentY - (currentHeight * 6 / 17),
				renderDepth, foreground);
		Renderer.drawTriangle(currentX + currentWidth / 2, currentY - (currentHeight * 7 / 17),
				currentX + currentWidth / 2 - 3, currentY - (currentHeight * 11 / 17),
				currentX + currentWidth / 2 + 3, currentY - (currentHeight * 11 / 17),
				renderDepth, foreground);
		Renderer.drawTriangle(currentX + currentWidth / 2, currentY - (currentHeight * 14 / 17),
				currentX + currentWidth / 2 - 3, currentY - (currentHeight * 11 / 17),
				currentX + currentWidth / 2 + 3, currentY - (currentHeight * 11 / 17),
				renderDepth, foreground);
	}
}
