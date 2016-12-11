package chu.engine.anim;

import org.newdawn.slick.openal.Audio;

import net.fe.FEResources;

// TODO: Auto-generated Javadoc
/**
 * The Class AudioPlayer.
 */
public class AudioPlayer {

	/** The camera. */
	static Camera camera;

	/**
	 * Sets the camera.
	 *
	 * @param c the new camera
	 */
	public static void setCamera(Camera c) {
		camera = c;
	}

	/**
	 * Play audio.
	 *
	 * @param name the name
	 */
	public static void playAudio(String name) {
		Audio audio = FEResources.getAudio(name);
		audio.playAsSoundEffect(1.0f, FEResources.getAudioVolume(), false);

	}
}
