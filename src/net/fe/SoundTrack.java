package net.fe;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.ArrayList;
import java.util.Enumeration;

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
	
	private static Map<String, ArrayList<String>> songs;
	
	private static ArrayList<String> categories;
	
	/**
	 * Loop.
	 * 
	 * Loops the given audio according to settings.
	 * 
	 * @param name the music category
	 */
	
	public static void loop(String name){
		if(!enabled || !(FEResources.getAudioVolume()>0)) return;
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
			e.printStackTrace();
			System.err.println("Warn: Bad sound configuration: "+name);
			try{
				Audio b = AudioLoader.getAudio("OGG",
						ResourceLoader.getResourceAsStream("res/music/"+name+".ogg"));
				b.playAsMusic(1.0f, FEResources.getAudioVolume(), true);
			}catch(Exception f){}
		}
	}
	
	private static void loadAudioNames(){
		try{
			songs = new HashMap<String, ArrayList<String>>();
			final String musPath = "res/music";
			final File jarFile = new File(SoundTrack.class.getProtectionDomain().getCodeSource().getLocation().getPath());
			if(jarFile.isFile()) {  // Run with JAR file
			    final JarFile jar = new JarFile(jarFile);
			    final Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
			    while(entries.hasMoreElements()) {
			        final String name = entries.nextElement().getName();
			        if (name.startsWith(musPath + "/") && name.indexOf(".ogg")>0) { //filter according to the path
			        	String[] sPreName = name.replace(".ogg", "").replace("res/music/", "").split("_",2);
			        	String cat = sPreName[0], sFileName = sPreName.length<2?"":sPreName[1];
			        	if(!songs.containsKey(cat))
							songs.put(cat, new ArrayList<String>());
						songs.get(cat).add(sFileName);
						songs.get(cat).trimToSize();
			        }
			    }
			    jar.close();
			    
			} else { // Run with IDE
				File folder = new File(musPath);
				File[] listOfFiles = folder.listFiles();
				for(File s: listOfFiles){
					//category & filename (if any), saves memory
					String[] sPreName = s.getName().replace(".ogg", "").split("_",2);
					String cat = sPreName[0], sFileName = sPreName.length<2?"":sPreName[1];
					if(!songs.containsKey(cat))
						songs.put(cat, new ArrayList<String>());
					songs.get(cat).add(sFileName);
				}
			}
			categories = new ArrayList<String>(songs.keySet());
			categories.trimToSize();
		}catch(Exception e){throw new RuntimeException(e);}
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
