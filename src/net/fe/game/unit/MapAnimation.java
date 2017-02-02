package net.fe.game.unit;

import chu.engine.Game;
import chu.engine.anim.Animation;
import net.fe.FEResources;

// TODO: Auto-generated Javadoc
/**
 * The Class MapAnimation.
 */
public class MapAnimation extends Animation {

	/** The synchro. */
	private boolean synchro;

	/** The synchro frame. */
	private static int synchroFrame;

	/** The counter. */
	private static float counter;

	/**
	 * Instantiates a new map animation.
	 *
	 * @param name the name
	 * @param walking the walking
	 */
	public MapAnimation(String name, boolean walking) {
		super(FEResources.getTexture(name.toLowerCase()), 48, name.matches("swordmaster.*selected") ? 60 : 48, 4, 4, 17,
		        name.matches("swordmaster.*selected") ? 29 : 17, 0, chu.engine.anim.BlendModeArgs.ALPHA_BLEND);
		// System.out.println(name);
		// if(name.equals("swordmaster_map_selected")){
		// System.out.println(getOffsetY());
		// }
		synchro = !walking;
		speed = 0.15f;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see chu.engine.anim.Animation#update()
	 */
	public void update() {
		if (!synchro) {
			super.update();
		} else {
			setFrame(synchroFrame);
		}
	}

	/**
	 * Update all.
	 */
	public static void updateAll() {
		counter += Game.getDeltaSeconds();
		if (counter > 0.08 && synchroFrame % 2 != 0 || counter > 0.5 && synchroFrame % 2 == 0) {
			synchroFrame = (synchroFrame + 1) % 4;
			counter = 0;
		}
	}
}
