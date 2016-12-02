package net.fe.ressources.graphics.transition;

import org.newdawn.slick.Color;

import net.fe.FEResources;
import net.fe.overworldStage.EndGameStage;
import chu.engine.Game;
import chu.engine.anim.Renderer;
import chu.engine.anim.Transform;

// TODO: Auto-generated Javadoc
/**
 * The Class OverworldEndTransition.
 */
public class OverworldEndTransition extends Transition{
	
	/** The timer. */
	private float timer;
	
	/** The end pos. */
	private float endPos;
	
	/** The winner. */
	private String winner;

	/**
	 * Instantiates a new overworld end transition.
	 *
	 * @param to the to
	 * @param winner the winner
	 */
	public OverworldEndTransition(EndGameStage to, String winner) {
		super(to);
		timer = 0.0f;
		endPos = -320;
		this.winner = winner;
	}
	
	/* (non-Javadoc)
	 * @see chu.engine.Entity#render()
	 */
	public void render() {
		String string = winner+" is the winner!";
		int width = FEResources.getBitmapFont("default_med").getStringWidth(string);
		Transform t = new Transform();
		t.scaleX = 2;
		t.scaleY = 2;
		t.color = Color.green;
		Renderer.drawString("default_med", string, 240-width, 130, 0.1f, t);
		timer += Game.getDeltaSeconds();
		if(timer > 3.0f) {
			Renderer.translate(0,endPos);
			to.render();
			Renderer.translate(0, -endPos);
			endPos += Game.getDeltaSeconds()*600;
			if(endPos > 0) done();
		}
	}

}
