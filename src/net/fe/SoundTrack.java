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
public final class SoundTrack {
	
	/** The name of the currently-loaded audio. */
	private static String currentName;
	
	/** The currently-loaded audio */
	private static Audio current = new org.newdawn.slick.openal.NullAudio();
	
	/**
	 * Loops the given audio according to settings.
	 * 
	 * @param name the music category
	 */
	public static void loop(String name){
		if (FEResources.getAudioVolume() <= 0) return;
		if (name.equals(currentName) && current.isPlaying()) return;
		
		current.stop();
		currentName = name;
		Map<String, ArrayList<String>> songs = loadAudioNames();
		
		try{
			String setting = FEResources.getAudioSetting(name.toUpperCase());
			if(setting.equals("random")){
				Random r = new Random();
				setting = name + "_" + songs.get(name).get(r.nextInt(songs.get(name).size()));
				if(setting.split("_").length<2)
					setting = name;
			}
			current = AudioLoader.getAudio("WAV",
					ResourceLoader.getResourceAsStream("res/music/"+setting+".wav"));
			current.playAsMusic(1.0f, FEResources.getAudioVolume(), true);
		} catch (Exception e){
			e.printStackTrace();
			System.err.println("Warn: Bad sound configuration: "+name);
			try{
				Audio b = AudioLoader.getAudio("WAV",
						ResourceLoader.getResourceAsStream("res/music/"+name+".wav"));
				b.playAsMusic(1.0f, FEResources.getAudioVolume(), true);
			}catch(Exception f){}
		}
	}
	
	private static Map<String, ArrayList<String>> loadAudioNames(){
		Map<String, ArrayList<String>> songs = new HashMap<>();
		try{
			final String musPath = "res/music";
			final File jarFile = new File(SoundTrack.class.getProtectionDomain().getCodeSource().getLocation().toURI());
			if(jarFile.isFile()) {  // Run with JAR file
			    final JarFile jar = new JarFile(jarFile);
			    final Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
			    while(entries.hasMoreElements()) {
			        final String name = entries.nextElement().getName();
			        if (name.startsWith(musPath + "/") && name.indexOf(".wav")>0) { //filter according to the path
			        	String[] sPreName = name.replace(".wav", "").replace("res/music/", "").split("_",2);
			        	String cat = sPreName[0], sFileName = sPreName.length<2?"":sPreName[1];
			        	if(!songs.containsKey(cat))
							songs.put(cat, new ArrayList<String>());
						songs.get(cat).add(sFileName);
						songs.get(cat).trimToSize();
			        }
			    }
			    jar.close();
			    
			} else if (new File(musPath).isDirectory()) { // Run with IDE
				File folder = new File(musPath);
				File[] listOfFiles = folder.listFiles();
				for(File s: listOfFiles){
					//category & filename (if any), saves memory
					String[] sPreName = s.getName().replace(".wav", "").split("_",2);
					String cat = sPreName[0], sFileName = sPreName.length<2?"":sPreName[1];
					if(!songs.containsKey(cat))
						songs.put(cat, new ArrayList<String>());
					songs.get(cat).add(sFileName);
				}
			}
		}catch(Exception e){throw new RuntimeException(e);}
		return songs;
	}
	
	/**
	 * Restart the currently-playing audio loop
	 */
	public static void restart(){
		current.setPosition(0.0f);
	}
	
	/**
	 * Stop the currently-playing audio loop
	 */
	public static void stop() {
		current.stop();
	}
	
	/**
	 * Change the volume of the currently-playing audio loop.
	 * 
	 * As usual, the volume is retrieved statically from FEResources's configurations.
	 * This attempts to have the audio continue from where it left off.
	 */
	public static void updateVolume() {
		final float pos = current.getPosition();
		current.stop();
		current.playAsMusic(1.0f, FEResources.getAudioVolume(), true);
		current.setPosition(pos);
	}
}
