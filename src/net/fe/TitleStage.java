package net.fe;

import java.util.List;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

import chu.engine.Game;
import chu.engine.KeyboardEvent;
import chu.engine.Stage;
import chu.engine.anim.AudioPlayer;
import chu.engine.anim.Renderer;
import chu.engine.anim.Transform;

// TODO: Auto-generated Javadoc
/**
 * The Class TitleStage.
 */
public class TitleStage extends Stage{
	
	/** The alpha. */
	private float alpha;
	
	/** The d alpha. */
	private float dAlpha;
	
	/** The counter. */
	private float counter;
	
	/** The title. */
	private Texture title;
	
	/**
	 * Instantiates a new title stage.
	 */
	public TitleStage(){
		super("main_theme");
		dAlpha = 0.9f;
		title = FEResources.getTexture("title");
	}
	
	/* (non-Javadoc)
	 * @see chu.engine.Stage#beginStep()
	 */
	@Override
	public void beginStep() {
		List<KeyboardEvent> keys = Game.getKeys();
		for(KeyboardEvent ke : keys) {
			if(ke.state) {
				if(ke.key == FEResources.getKeyMapped(Keyboard.KEY_RETURN)){
					counter += Game.getDeltaSeconds();
					AudioPlayer.playAudio("start");
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see chu.engine.Stage#onStep()
	 */
	@Override
	public void onStep() {
		alpha += dAlpha * Game.getDeltaSeconds();
		if(alpha > 0.4){
			alpha = 0.4f;
			dAlpha *= -1;
		}
		if(alpha < 0){
			alpha = 0f;
			dAlpha *= -1;
		}
		if(counter > 0){
			counter += Game.getDeltaSeconds();
			if(counter >= 1.3f){
				FEMultiplayer.setCurrentStage(FEMultiplayer.connect);
			}
		}
	}

	/* (non-Javadoc)
	 * @see chu.engine.Stage#endStep()
	 */
	@Override
	public void endStep() {
		
	}
	
	/* (non-Javadoc)
	 * @see chu.engine.Stage#render()
	 */
	public void render(){
		Renderer.render(title, 0, 0, 1, 1, 0, 0, 480, 320, 1);
		Transform t = new Transform();
		t.setScale(2, 2);
		t.setColor(new Color(1-alpha,1,1-alpha,1));
		if(counter * 20 % 2 < 1)
			Renderer.drawString("default_med", "Press Enter", 189, 230, 0, t);
	}
	
}
