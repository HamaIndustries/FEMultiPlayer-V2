package net.fe;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Properties;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.util.ResourceLoader;

import chu.engine.AnimationData;
import chu.engine.anim.BitmapFont;

// TODO: Auto-generated Javadoc
/**
 * The Class FEResources.
 */
public class FEResources {

	/** The search folders. */
	private static String[] searchFolders = { "battle_anim", "battle_anim/static", "map_mugshots", "gui", "map_anim" };

	/** The audio. */
	private static HashMap<String, Audio> audio;

	/** The textures. */
	private static HashMap<String, AnimationData> textures;

	/** The bitmap fonts. */
	private static HashMap<String, BitmapFont> bitmapFonts;

	static {
		audio = new HashMap<String, Audio>();
		textures = new HashMap<String, AnimationData>();
		bitmapFonts = new HashMap<String, BitmapFont>();
	}

	/**
	 * Gets the texture.
	 *
	 * @param string the string
	 * @return the texture
	 */
	public static Texture getTexture(String string) {
		if (string.contains("phantom") && string.contains("mugshot"))
			return getTextureData("phantom_mugshot").getTexture();
		return getTextureData(string).getTexture();
	}

	/**
	 * Checks for texture.
	 *
	 * @param string the string
	 * @return true, if successful
	 */
	public static boolean hasTexture(String string) {
		return textures.containsKey(string);
	}

	/**
	 * Load resources.
	 */
	public static void loadResources() {
		try {
			// Load bitmap fonts
			loadBitmapFonts();

			// Textures
			textures.put("whoops", new AnimationData("res/whoops.png"));
			loadTextures();
			// load audio
			audio.put("miss", AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("res/sfx/miss.wav")));

		} catch (IOException e) {
			int max = GL11.glGetInteger(GL11.GL_MAX_TEXTURE_SIZE);
			System.out.println(max);
			e.printStackTrace();
		}
		System.gc();
	}

	/**
	 * Gets the map texture.
	 *
	 * @param name the name
	 * @return the map texture
	 */
	public static AnimationData getMapTexture(String name) {
		AnimationData t = textures.get(name);
		if (t != null)
			return t;
		System.out.println("Warn:" + name + " not explicitly defined");
		for (String loc : searchFolders) {
			try {
				AnimationData txt = new AnimationData("res/" + loc + "/" + name + ".png", 96, 24, 4, 4, 4, 4, 0, null,
				        null);
				textures.put(name, txt);
				return txt;
			} catch (Exception e) {

			}
		}
		return textures.get("whoops");
	}

	/**
	 * Load textures.
	 */
	private static void loadTextures() {
		long startTime = System.nanoTime();
		// TODO Load textures from JSON
		InputStream file = ResourceLoader.getResourceAsStream("res/resources.json");
		Scanner in = new Scanner(file);
		StringBuilder sb = new StringBuilder();
		while (in.hasNextLine()) {
			sb.append(in.nextLine());
		}
		String json = sb.toString();

		JSONObject resources = null;
		try {
			resources = (JSONObject) JSONValue.parseWithException(json);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		JSONArray txArray = (JSONArray) resources.get("textures");
		LoadStage.setMaximum(txArray.size());
		for (Object obj : txArray) {
			JSONObject texture = (JSONObject) obj;
			String name = (String) texture.get("name");
			String path = (String) texture.get("path");
			Number width = (Number) texture.get("width");
			Number height = (Number) texture.get("height");
			Number frames = (Number) texture.get("frames");
			Number columns = (Number) texture.get("columns");
			Number freeze = (Number) texture.get("freeze");
			Number offsetX = (Number) texture.get("offsetX");
			Number offsetY = (Number) texture.get("offsetY");
			Number speed = (Number) texture.get("speed");
			Number shakeFrames = (Number) texture.get("shakeFrames");
			Number shakeIntensity = (Number) texture.get("shakeIntensity");
			Boolean stop = (Boolean) texture.get("stop");
			JSONArray hitArray = (JSONArray) texture.get("hitframes");
			JSONArray audioArray = (JSONArray) texture.get("soundMap");
			HashMap<Integer, String> audioMap = new HashMap<Integer, String>();
			String blendModeName = (String) texture.get("blend");

			int[] hitframes;
			if (hitArray != null) {
				hitframes = new int[hitArray.size()];
				for (int i = 0; i < hitframes.length; i++) {
					hitframes[i] = ((Number) hitArray.get(i)).intValue();
				}
			} else {
				hitframes = new int[0];
			}

			if (audioArray != null) {
				for (Object obj2 : audioArray) {
					JSONObject audio = (JSONObject) obj2;
					audioMap.put(((Number) audio.get("frame")).intValue(), (String) audio.get("sound"));
				}
			}
			AnimationData data;
			if (width == null) {
				data = new AnimationData(path);
			} else {
				data = new AnimationData(path, width.intValue(), height.intValue(), frames.intValue(),
				        columns.intValue(), offsetX.intValue(), offsetY.intValue(), freeze.intValue(), hitframes,
				        audioMap);
			}
			if (speed != null)
				data.speed = speed.floatValue();
			if (shakeFrames != null)
				data.shakeFrames = shakeFrames.intValue();
			if (shakeIntensity != null)
				data.shakeIntensity = shakeIntensity.intValue();
			if (stop != null)
				data.stop = stop.booleanValue();
			data.blendModeName = blendModeName;
			textures.put(name, data);
			if ((System.nanoTime() - startTime) / 1000000.0f > 100) {
				LoadStage.update(textures.size());
				LoadStage.render();
			}
		}
		in.close();
	}

	/**
	 * Gets the bitmap font.
	 *
	 * @param name the name
	 * @return the bitmap font
	 */
	public static BitmapFont getBitmapFont(String name) {
		return bitmapFonts.get(name);
	}

	/**
	 * Load bitmap fonts.
	 */
	public static void loadBitmapFonts() {
		Scanner in = new Scanner(ResourceLoader.getResourceAsStream("res/fonts/fonts.txt"));
		while (in.hasNextLine()) {
			String line = in.nextLine();
			if (line.startsWith("#"))
				continue;
			if (line.startsWith("define")) {
				String name = line.split(":")[1];
				String texName = in.nextLine();
				char[] chars = in.nextLine().toCharArray();
				int height = Integer.parseInt(in.nextLine());
				int spacing = Integer.parseInt(in.nextLine());
				char[] widths = in.nextLine().toCharArray();

				BitmapFont font = new BitmapFont(texName);
				font.setHeight(height);
				font.setSpacing(spacing);
				int pos = 0;
				for (int i = 0; i < chars.length; i++) {
					int width = Integer.parseInt(widths[i] + "");
					font.put(chars[i], pos, width);
					pos += width;
				}
				bitmapFonts.put(name, font);
				System.out.println(name + "(bitmap font) loaded");
			}
		}
		in.close();
	}

	/**
	 * Gets the texture data.
	 *
	 * @param string the string
	 * @return the texture data
	 */
	public static AnimationData getTextureData(String string) {
		AnimationData t = textures.get(string);
		if (t != null) {
			return t;
		} else {
			// try to get it, in case we forgot
			System.err.println("Warn: " + string + " not explicitly defined");
			for (String loc : searchFolders) {
				if (ResourceLoader.resourceExists("res/" + loc + "/" + string + ".png")) {
					AnimationData txt = new AnimationData("res/" + loc + "/" + string + ".png");
					textures.put(string, txt);
					return txt;
				}

			}
			return textures.get("whoops");
		}
	}

	/**
	 * Gets the audio.
	 *
	 * @param name the name
	 * @return the audio
	 */
	public static Audio getAudio(String name) {
		Audio a = audio.get(name);
		if (a == null) {
			// System.err.println("Warn: " + name + " not explicitly defined");
			try {
				Audio b = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("res/sfx/" + name + ".wav"));
				audio.put(name, b);
				return b;
			} catch (Exception e) {
				return null;
			}
		} else {
			return a;
		}
	}

	/** Returns the default properties */
	private static Properties getDefaultProperties() {
		final Properties defaultProps = new Properties();
		// keybindings
		defaultProps.setProperty("Z", "Z");
		defaultProps.setProperty("X", "X");
		defaultProps.setProperty("RETURN", "RETURN");
		defaultProps.setProperty("LEFT", "LEFT");
		defaultProps.setProperty("RIGHT", "RIGHT");
		defaultProps.setProperty("UP", "UP");
		defaultProps.setProperty("DOWN", "DOWN");
		// other
		defaultProps.setProperty("VOLUME", "1.0");
		defaultProps.setProperty("SCALE", "1.0");
		defaultProps.setProperty("AUTOCURSOR", "START");

		// music
		defaultProps.setProperty("CURING", "curing");
		defaultProps.setProperty("DEFENSE", "defense");
		defaultProps.setProperty("END", "end");
		defaultProps.setProperty("ENEMY", "enemy");
		defaultProps.setProperty("FIGHT", "fight");
		defaultProps.setProperty("MAIN", "main");
		defaultProps.setProperty("OVERWORLD", "overworld");
		defaultProps.setProperty("PREPARATIONS", "preparations");
		return defaultProps;
	}

	/** The set of user settings. */
	private static Properties prop;

	/**
	 * Gets the properties.
	 *
	 * @return the properties
	 */
	// takes in a key such as Keyboard.KEY_Z and returns the corresponding key
	// the user presses
	private static Properties getProperties() {
		if (prop == null) {
			prop = new Properties();
			final File path = new File("app.config");
			try {
				final boolean isPatch = path.exists();
				// should probably also have a check for directory &&
				// !f.isDirectory()
				// but unless the user creates it, that won't be true. No clear
				// way of handling it.
				if (path.exists()) {
					try (InputStream in = new FileInputStream(path)) {
						prop.load(in);
					}
				} else {
					// make file and populate it
					path.createNewFile();
				}

				final Properties defaultProps = getDefaultProperties();
				for (String key : prop.stringPropertyNames()) {
					defaultProps.remove(key);
				}
				for (String key : defaultProps.stringPropertyNames()) {
					prop.setProperty(key, defaultProps.getProperty(key));
				}

				if (!defaultProps.isEmpty()) {
					try (OutputStream out = new FileOutputStream(path, isPatch)) {
						if (isPatch) {
							out.write('\n');
						}
						defaultProps.store(out, (isPatch ? "---Patch---" : "---Initial Configuration---"));
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return prop;
	}

	/**
	 * Gets the audio volume.
	 *
	 * @return the audio volume
	 */
	public static float getAudioVolume() {
		String volumeStr = getProperties().getProperty("VOLUME");
		float volume = Float.parseFloat(volumeStr);
		return volume;
	}

	public static String getAudioSetting(String setting) throws Exception {
		String audioName = "";
		try {
			audioName = getProperties().getProperty(setting);
		} catch (Exception e) {
			throw e;
		}
		return audioName;
	}

	public static enum AutoCursor {
		START(true, true), START_LOCAL(true, false), OFF(false, false);

		public final boolean applyAtStartOfLocalTurn;
		public final boolean applyAtStartOfOtherTurn;

		private AutoCursor(boolean applyAtStartOfLocalTurn, boolean applyAtStartOfOtherTurn) {
			this.applyAtStartOfLocalTurn = applyAtStartOfLocalTurn;
			this.applyAtStartOfOtherTurn = applyAtStartOfOtherTurn;
		}
	}

	/**
	 * Returns the autocursor setting; whether to move the cursor to the
	 * player's lord at the start of the turn.
	 */
	public static AutoCursor getAutoCursor() {
		String propStr = getProperties().getProperty("AUTOCURSOR");
		if ("start".equalsIgnoreCase(propStr)) {
			return AutoCursor.START;
		} else if ("startLocal".equalsIgnoreCase(propStr)) {
			return AutoCursor.START_LOCAL;
		} else if ("off".equalsIgnoreCase(propStr)) {
			return AutoCursor.OFF;
		} else {
			return AutoCursor.OFF;
		}
	}

	/**
	 * Gets the window scale.
	 *
	 * @return the window scale
	 */
	public static float getWindowScale() {
		String scaleStr = getProperties().getProperty("SCALE");
		float scale = Float.parseFloat(scaleStr);
		return scale;
	}

	/**
	 * Gets the key mapped.
	 *
	 * @param internalKey the internal key
	 * @return the key mapped
	 */
	public static int getKeyMapped(int internalKey) {
		// NOTE: LevelEditorStage does not use this. So its controls are still
		// hard-coded.
		// However the level editor is not part of the user-facing game, so
		// doesn't need them at the moment.
		// To fix that, this method should be changed so that instead of using
		// "internalKey" as a keyboard
		// it should take in an Enum or const that tells the meaning, not the
		// key
		// for example "back" instead of "key_z"
		// right now this method has no way of knowing if Z is used for
		// something other than backing out
		// therefore, right now it has to assume Z is always back out when
		// translating

		String mappedKey = getProperties().getProperty(Keyboard.getKeyName(internalKey));
		if (mappedKey != null && !mappedKey.equals("")) {
			return Keyboard.getKeyIndex(mappedKey);
		}

		// other controls:
		// left/right/up/down
		// backspace
		// delete
		// enter

		return internalKey;
	}

	/**
	 * Gets the key mapped name.
	 *
	 * @param internalKeyName the internal key name
	 * @return the key mapped name
	 */
	public static String getKeyMappedName(String internalKeyName) {
		if (internalKeyName.toUpperCase().equals("ENTER")) {
			internalKeyName = "RETURN";
		}
		int key = Keyboard.getKeyIndex(internalKeyName.toUpperCase());
		int mappedKey = getKeyMapped(key);
		String mappedName = Keyboard.getKeyName(mappedKey);
		// this might seem redundant, but it's to allow code using the string
		// "Enter" to map to the key "Return"
		// which then maps the key according to the bindings (possibly to
		// something else)
		// if it's still "return" we want this translated back to "enter"
		if (mappedName.equals("RETURN")) {
			return "ENTER";
		}
		return mappedName;
	}

}
