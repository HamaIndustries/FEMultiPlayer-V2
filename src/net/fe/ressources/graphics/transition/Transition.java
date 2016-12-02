package net.fe.ressources.graphics.transition;

import chu.engine.Stage;
import chu.engine.entity.Entity;
import net.fe.FEMultiplayer;

// TODO: Auto-generated Javadoc
/**
 * Manages a delayed transition between two stages.
 * @author Shawn
 *
 */
public abstract class Transition extends Entity {
	
	/** The to. */
	protected Stage to;
	
	/**
	 * Instantiates a new transition.
	 *
	 * @param to the to
	 */
	public Transition(Stage to) {
		super(0,0);
		this.to = to;
	}
	
	/**
	 * Done.
	 */
	public void done() {
		destroy();
		stage.processRemoveStack();
		FEMultiplayer.setCurrentStage(to);
	}
}
