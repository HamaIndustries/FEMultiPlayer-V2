package net.fe.overworldStage;

import chu.engine.anim.AudioPlayer;

// TODO: Auto-generated Javadoc
/**
 * The Class MenuContext.
 *
 * @param <T> the generic type
 */
public abstract class MenuContext<T> extends OverworldContext {

	/** The menu. */
	protected Menu<T> menu;

	/**
	 * Instantiates a new menu context.
	 *
	 * @param stage the stage
	 * @param prev the prev
	 * @param m the m
	 */
	public MenuContext(ClientOverworldStage stage, OverworldContext prev, Menu<T> m) {
		super(stage, prev);
		menu = m;
		m.x = ClientOverworldStage.RIGHT_AXIS - menu.getWidth() / 2;
		m.y = 75;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.fe.overworldStage.OverworldContext#startContext()
	 */
	public void startContext() {
		super.startContext();
		cursor.off();
		stage.setMenu(menu);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.fe.overworldStage.OverworldContext#cleanUp()
	 */
	public void cleanUp() {
		cursor.on();
		stage.setMenu(null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.fe.overworldStage.OverworldContext#onSelect()
	 */
	public void onSelect() {
		onSelect(menu.getSelection());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.fe.overworldStage.OverworldContext#onUp()
	 */
	@Override
	public void onUp() {
		menu.up();
		onChange();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.fe.overworldStage.OverworldContext#onDown()
	 */
	@Override
	public void onDown() {
		menu.down();
		onChange();
	}

	/**
	 * On select.
	 *
	 * @param selectedItem the selected item
	 */
	public abstract void onSelect(T selectedItem);

	/**
	 * On change.
	 */
	public void onChange() {
		AudioPlayer.playAudio("cursor2");
	}
}
