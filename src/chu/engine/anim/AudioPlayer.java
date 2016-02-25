package chu.engine.anim;

import net.fe.FEResources;

import java.io.File;
import java.io.PrintWriter;

import org.newdawn.slick.openal.Audio;

import chu.engine.Game;

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
	public static void playAudio(String name){
			try{
			Audio audio = FEResources.getAudio(name);
			audio.playAsSoundEffect(1.0f, FEResources.getAudioVolume(), false);
			}catch(Exception e){
				try{
					File errLog = new File("nameLog.log");
					PrintWriter pw = new PrintWriter(errLog);
					e.printStackTrace(pw);
					pw.println(name);
					pw.close();
				}catch(Exception f){}
			}
		
	}
}
