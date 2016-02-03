package net.fe.overworldStage.context;

import chu.engine.anim.AudioPlayer;
import net.fe.overworldStage.Menu;
import net.fe.overworldStage.MenuContext;
import net.fe.overworldStage.OverworldContext;
import net.fe.overworldStage.ClientOverworldStage;

// TODO: Auto-generated Javadoc
/**
 * The Class EndMenu.
 */
public class EndMenu extends MenuContext<String> {

	/**
	 * Instantiates a new end menu.
	 *
	 * @param stage the stage
	 * @param prev the prev
	 */
	public EndMenu(ClientOverworldStage stage, OverworldContext prev) {
		super(stage, prev, new Menu<String>());
		menu.addItem("End");
	}

	/* (non-Javadoc)
	 * @see net.fe.overworldStage.MenuContext#onSelect(java.lang.Object)
	 */
	@Override
	public void onSelect(String selectedItem) {
		AudioPlayer.playAudio("select");
		if(menu.getSelection().equals("End")){
			stage.end();
		}
	}

	/* (non-Javadoc)
	 * @see net.fe.overworldStage.OverworldContext#onLeft()
	 */
	@Override
	public void onLeft() {
		
	}

	/* (non-Javadoc)
	 * @see net.fe.overworldStage.OverworldContext#onRight()
	 */
	@Override
	public void onRight() {
		
	}

}
