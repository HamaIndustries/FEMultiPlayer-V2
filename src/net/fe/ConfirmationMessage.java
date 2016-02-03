package net.fe;

import java.util.List;

import net.fe.fightStage.FightStage;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;

import chu.engine.Entity;
import chu.engine.Game;
import chu.engine.KeyboardEvent;
import chu.engine.anim.AudioPlayer;
import chu.engine.anim.Renderer;

// TODO: Auto-generated Javadoc
/**
 * The Class ConfirmationMessage.
 */
public abstract class ConfirmationMessage extends Entity{
	
	/** The yes hover. */
	private boolean yesHover;
	
	/** The repeat timers. */
	private float[] repeatTimers = new float[4];
	
	/**
	 * Instantiates a new confirmation message.
	 */
	public ConfirmationMessage() {
		super(190, 140);
		renderDepth = 0.001f;
		yesHover = false;
	}
	
	/* (non-Javadoc)
	 * @see chu.engine.Entity#beginStep()
	 */
	public void beginStep(){
		List<KeyboardEvent> keys = Game.getKeys();
		if (Keyboard.isKeyDown(FEResources.getKeyMapped(Keyboard.KEY_LEFT)) && repeatTimers[2] == 0) {
			repeatTimers[2] = 0.12f;
			yesHover = !yesHover;
			AudioPlayer.playAudio("cursor2");
		}
		if (Keyboard.isKeyDown(FEResources.getKeyMapped(Keyboard.KEY_RIGHT)) && repeatTimers[3] == 0) {
			repeatTimers[3] = 0.12f;
			yesHover = !yesHover;
			AudioPlayer.playAudio("cursor2");
		}
		for(KeyboardEvent ke : keys) {
			if(ke.state) {
				if(ke.key == FEResources.getKeyMapped(Keyboard.KEY_RETURN) || ke.key == FEResources.getKeyMapped(Keyboard.KEY_Z)) { 
					AudioPlayer.playAudio("select");
					if(yesHover)
						confirm();
					else 
						cancel();
					destroy();
				} if(ke.key == FEResources.getKeyMapped(Keyboard.KEY_X)){
					AudioPlayer.playAudio("cancel");
					cancel();
					destroy();
				}
			}
		}
		for(int i=0; i<repeatTimers.length; i++) {
			if(repeatTimers[i] > 0) {
				repeatTimers[i] -= Game.getDeltaSeconds();
				if(repeatTimers[i] < 0) repeatTimers[i] = 0;
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see chu.engine.Entity#render()
	 */
	public void render(){
		Renderer.drawRectangle(0, 0, 480, 320, renderDepth, new Color(0,0,0,0.5f));
		Renderer.drawBorderedRectangle(x, y, x+100, y+40, renderDepth,
				FightStage.NEUTRAL, FightStage.BORDER_LIGHT, FightStage.BORDER_DARK);
		Renderer.drawString("default_med", "Are you sure?", x+2, y+2, renderDepth);
		if(yesHover) 
			Renderer.setColor(Color.green);
		Renderer.drawString("default_med", "YES", x+5, y+20, renderDepth);
		if(!yesHover) 
			Renderer.setColor(Color.green);
		else 
			Renderer.setColor(null);
		Renderer.drawString("default_med", "NO", x+55, y+20, renderDepth);
		Renderer.setColor(null);
	}
	
	/**
	 * Confirm.
	 */
	public abstract void confirm();
	
	/**
	 * Cancel.
	 */
	public void cancel(){
		
	}
	
}
