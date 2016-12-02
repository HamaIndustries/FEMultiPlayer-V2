package net.fe.overworldStage;

// TODO: Auto-generated Javadoc
/**
 * The Class CursorContext.
 */
public abstract class CursorContext extends OverworldContext {

	/**
	 * Instantiates a new cursor context.
	 *
	 * @param s the s
	 * @param prevContext the prev context
	 */
	public CursorContext(ClientOverworldStage s, OverworldContext prevContext) {
		super(s, prevContext);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.fe.overworldStage.OverworldContext#onUp()
	 */
	public void onUp() {
		if (cursor.getYCoord() > 0) {
			cursorWillChange();
			cursor.setYCoord(cursor.getYCoord() - 1);
			cursorChanged();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.fe.overworldStage.OverworldContext#onDown()
	 */
	public void onDown() {
		if (cursor.getYCoord() < grid.height - 1) {
			cursorWillChange();
			cursor.setYCoord(cursor.getYCoord() + 1);
			cursorChanged();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.fe.overworldStage.OverworldContext#onLeft()
	 */
	public void onLeft() {
		if (cursor.getXCoord() > 0) {
			cursorWillChange();
			cursor.setXCoord(cursor.getXCoord() - 1);
			cursorChanged();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.fe.overworldStage.OverworldContext#onRight()
	 */
	public void onRight() {
		if (cursor.getXCoord() < grid.width - 1) {
			cursorWillChange();
			cursor.setXCoord(cursor.getXCoord() + 1);
			cursorChanged();
		}
	}

	/**
	 * Cursor changed.
	 */
	public abstract void cursorChanged();

	/**
	 * Cursor will change.
	 */
	public abstract void cursorWillChange();
}
