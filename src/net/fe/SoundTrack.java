package net.fe;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ResourceLoader;

// TODO: Auto-generated Javadoc
/**
 * The Class SoundTrack.
 */
public class SoundTrack {
	
	/** The current. */
	private static String current;
	
	/** The enabled. */
	public static boolean enabled = true;
	
	private static Map<String, String[]> songs;
	
	/**
	 * Loop.
	 *
	 * @param name the name
	 */
	
	public static void loop(String name){
		if(!enabled) return;
		if(name.equals(current)) return;
		current = name;
		/*
		 * TODO: randomizing music
		File folder = new File("res/music");
		File[] listOfFiles = folder.listFiles();
		for(File s: listOfFiles){
			songs.get(s.getName().split("_")[0], value);
		}
		songs = new HashMap<String, String[]>();
		*/
		try{
			Audio b = AudioLoader.getAudio("OGG",
					ResourceLoader.getResourceAsStream("res/music/"+FEResources.getAudioSetting(name.toUpperCase())+".ogg"));
			b.playAsMusic(1.0f, FEResources.getAudioVolume(), true);
		} catch (Exception e){
			System.out.println("Bad sound configuration: "+name);
			try{
				Audio b = AudioLoader.getAudio("OGG",
						ResourceLoader.getResourceAsStream("res/music/"+name+".ogg"));
				b.playAsMusic(1.0f, FEResources.getAudioVolume(), true);
			}catch(Exception f){}
		}
	}
	
	/**
	 * Restart.
	 */
	public static void restart(){
		if(!enabled) return;
		try {
			Audio a = AudioLoader.getStreamingAudio("OGG", 
					ResourceLoader.getResource("res/music/"+current+".ogg"));
			a.playAsMusic(1.0f, FEResources.getAudioVolume(), true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
