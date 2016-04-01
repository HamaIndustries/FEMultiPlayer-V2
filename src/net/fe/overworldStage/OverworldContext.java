package net.fe.overworldStage;

import chu.engine.anim.AudioPlayer;
import net.fe.unit.Unit;

// TODO: Auto-generated Javadoc
/**
 * The Class OverworldContext.
 */
public abstract class OverworldContext {
	
	/** The stage. */
	protected ClientOverworldStage stage;
	
	/** The prev. */
	protected OverworldContext prev;
	
	/** The cursor. */
	protected Cursor cursor;
	
	/** The grid. */
	protected Grid grid;

	/**
	 * Instantiates a new overworld context.
	 *
	 * @param s the s
	 * @param prevContext the prev context
	 */
	public OverworldContext(ClientOverworldStage s, OverworldContext prevContext) {
		stage = s;
		prev = prevContext;
		cursor = stage.cursor;
		grid = stage.grid;
	}
	
	/**
	 * Cursor speed
	 */
	public float getCursorSpeed(boolean fast) {
		return 0.12f;
	}

	/**
	 * On select.
	 */
	public abstract void onSelect();

	/**
	 * On cancel.
	 */
	public void onCancel(){
		AudioPlayer.playAudio("cancel");
		prev.startContext();
	}

	/**
	 * On up.
	 */
	public abstract void onUp();
	
	/**
	 * On down.
	 */
	public abstract void onDown();
	
	/**
	 * On left.
	 */
	public abstract void onLeft();
	
	/**
	 * On right.
	 */
	public abstract void onRight();
	
	/**
	 * Start context.
	 */
	public void startContext(){
		stage.setContext(this);
	}
	
	/**
	 * Clean up.
	 */
	public void cleanUp(){
		
	}

	/**
	 * Gets the hovered unit.
	 *
	 * @return the hovered unit
	 */
	protected Unit getHoveredUnit() {
		return stage.getHoveredUnit();
	}
}
