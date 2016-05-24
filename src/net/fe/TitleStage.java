package net.fe;

import java.util.List;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

import chu.engine.Entity;
import chu.engine.Game;
import chu.engine.KeyboardEvent;
import chu.engine.Stage;
import chu.engine.anim.AudioPlayer;
import chu.engine.anim.Renderer;
import chu.engine.anim.Sprite;
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
	
	private Texture titleTwo;
	
	private TitleAnimation titleAnim;
	
	
	/**
	 * Instantiates a new title stage.
	 */
	public TitleStage(){
		super("main");
		dAlpha = 0.9f;
		title = FEResources.getTexture("title");
		titleTwo = FEResources.getTexture("title_two");
		titleAnim = new TitleAnimation();
		entities.add(titleAnim);
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
		for(Entity e : entities) {
			e.beginStep();
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
				FEMultiplayer.setCurrentStage(new ConnectStage());
			}
		}
		for(Entity e : entities) {
			e.onStep();
		}
	}

	/* (non-Javadoc)
	 * @see chu.engine.Stage#endStep()
	 */
	@Override
	public void endStep() {
		for(Entity e : entities) {
			e.endStep();
		}
	}
	
	/* (non-Javadoc)
	 * @see chu.engine.Stage#render()
	 */
	public void render(){
		boolean hasRun = false;
		Transform t = new Transform();
		for(Entity e : entities) {
			e.render();
			if(e.hasRun)
				hasRun = true;
		}
		
		if(!hasRun)
			Renderer.render(title, 0, 0, 1, 1, 0, 0, 480, 320, 1);
		else
			Renderer.render(titleTwo, 0, 0, 1, 1, 0, 0, 480, 320, 1);
		
		t.setScale(2, 2);
		t.setColor(new Color(1-alpha,1,1-alpha,1));
		if(counter * 20 % 2 < 1)
			Renderer.drawString("default_med", "Press Enter", 189, 230, 0, t);
	}
	
}
