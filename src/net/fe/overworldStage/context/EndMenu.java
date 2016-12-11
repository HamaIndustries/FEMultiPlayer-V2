package net.fe.overworldStage.context;

import chu.engine.anim.AudioPlayer;
import net.fe.Player;
import net.fe.game.unit.Unit;
import net.fe.overworldStage.ClientOverworldStage;
import net.fe.overworldStage.Menu;
import net.fe.overworldStage.MenuContext;
import net.fe.overworldStage.OverworldContext;

// TODO: Auto-generated Javadoc
/**
 * The menu that is used when there is no refreshed unit on the currently
 * selected space
 */
public final class EndMenu extends MenuContext<String> {

	private final static String next = "Next Unit";
	private final static String end = "End";

	/**
	 * Instantiates a new end menu.
	 *
	 * @param stage the stage
	 * @param prev the previous context. This expects the previous stage to be
	 *        an Idle for the "Next Unit" button to work.
	 */
	public EndMenu(ClientOverworldStage stage, OverworldContext prev) {
		super(stage, prev, new Menu<String>());
		if (hasActionableUnits(stage.getCurrentPlayer())) {
			menu.addItem(next);
		}
		menu.addItem(end);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.fe.overworldStage.MenuContext#onSelect(java.lang.Object)
	 */
	@Override
	public void onSelect(String selectedItem) {
		AudioPlayer.playAudio("select");
		switch (selectedItem) {
			case next: {
				prev.startContext();
				prev.onNextUnit();
				break;
			}
			case end: {
				stage.end();
				break;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.fe.overworldStage.OverworldContext#onLeft()
	 */
	@Override
	public void onLeft() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.fe.overworldStage.OverworldContext#onRight()
	 */
	@Override
	public void onRight() {

	}

	/**
	 * Returns true if the player has any units that may perform actions
	 */
	private static boolean hasActionableUnits(Player p) {
		boolean retVal = false;
		for (Unit u : p.getParty()) {
			retVal = retVal || (!u.hasMoved() && u.getHp() > 0);
		}
		return retVal;
	}

}
