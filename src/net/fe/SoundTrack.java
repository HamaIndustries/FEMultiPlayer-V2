package net.fe;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.ArrayList;

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
	
	/** Whether or not Audio is enabled */
	public static boolean enabled = true;
	
	private static Map<String, List<String>> songs;
	
	private static List<String> categories;
	
	/**
	 * Loop.
	 * 
	 * Loops the given audio according to settings.
	 * 
	 * @param name the music category
	 */
	
	public static void loop(String name){
		if(!enabled) return;
		if(name.equals(current)) return;
		current = name;
		loadAudioNames();
		
		try{
			String setting = FEResources.getAudioSetting(name.toUpperCase());
			Audio b;
			if(setting.equals("random")){
				Random r = new Random();
				setting = name + "_" + songs.get(name).get(r.nextInt(songs.get(name).size()));
				if(setting.split("_").length<2)
					setting = name;
			}
			b = AudioLoader.getAudio("OGG",
					ResourceLoader.getResourceAsStream("res/music/"+setting+".ogg"));
			b.playAsMusic(1.0f, FEResources.getAudioVolume(), true);
		} catch (Exception e){
			System.err.println("Warn: Bad sound configuration: "+name);
			try{
				Audio b = AudioLoader.getAudio("OGG",
						ResourceLoader.getResourceAsStream("res/music/"+name+".ogg"));
				b.playAsMusic(1.0f, FEResources.getAudioVolume(), true);
			}catch(Exception f){}
		}
	}
	
	private static void loadAudioNames(){
		File folder = new File("res/music");
		File[] listOfFiles = folder.listFiles();
		songs = new HashMap<String, List<String>>();
		for(File s: listOfFiles){
			//category & filename (if any), saves memory
			String[] sPreName = s.getName().replace(".ogg", "").split("_",2);
			String cat = sPreName[0], sFileName = sPreName.length<2?"":sPreName[1];
			if(!songs.containsKey(cat))
				songs.put(cat, new ArrayList<String>());
			songs.get(cat).add(sFileName);
		}
		categories = new ArrayList<String>(songs.keySet());
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
