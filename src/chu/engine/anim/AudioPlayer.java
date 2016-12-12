package chu.engine.anim;

import org.newdawn.slick.openal.Audio;

import net.fe.FEResources;

public class AudioPlayer {

	static Camera camera;

	public static void setCamera(Camera c) {
		camera = c;
	}

	public static void playAudio(String name) {
		Audio audio = FEResources.getAudio(name);
		audio.playAsSoundEffect(1.0f, FEResources.getAudioVolume(), false);

	}
}
