package chu.engine;

import static java.lang.System.out;

import java.io.IOException;
import java.util.HashMap;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

// TODO: Auto-generated Javadoc
/**
 * The Class AnimationData.
 */
public class AnimationData {

	/** The path. */
	public String path;

	/** The frames. */
	public int frames;

	/** The columns. */
	public int columns;

	/** The frame width. */
	public int frameWidth;

	/** The frame height. */
	public int frameHeight;

	/** The offset x. */
	public int offsetX;

	/** The offset y. */
	public int offsetY;

	/** The freeze. */
	public int freeze;

	/** The hitframes. */
	public int[] hitframes;

	/** The sound map. */
	public HashMap<Integer, String> soundMap;

	/** The speed. */
	public float speed;

	/** The shake frames. */
	public int shakeFrames;

	/** The shake intensity. */
	public int shakeIntensity;

	/** The stop. */
	public boolean stop;

	/** The blend mode to use when */
	public String blendModeName;

	/**
	 * Instantiates a new animation data.
	 *
	 * @param path the path
	 * @param w the w
	 * @param h the h
	 * @param r the r
	 * @param c the c
	 * @param x the x
	 * @param y the y
	 * @param f the f
	 * @param frames the frames
	 * @param soundMap the sound map
	 */
	public AnimationData(String path, int w, int h, int r, int c, int x, int y, int f, int[] frames,
	        HashMap<Integer, String> soundMap) {
		this.path = path;
		this.frames = r;
		columns = c;
		offsetX = x;
		offsetY = y;
		frameWidth = w;
		frameHeight = h;
		freeze = f;
		hitframes = frames;
		this.soundMap = soundMap;
	}

	/**
	 * Instantiates a new animation data.
	 *
	 * @param path the path
	 */
	public AnimationData(String path) {
		this.path = path;
		frames = 1;
		columns = 1;
		offsetX = 0;
		offsetY = 0;
		freeze = -1;
	}

	/**
	 * Gets the texture.
	 *
	 * @return the texture
	 */
	public Texture getTexture() {
		try {
			Texture t = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(path));
			System.out.println("Loaded " + path);
			return t;
		} catch (IOException e) {
			System.err.println("Texture not found: " + path);
			e.printStackTrace();
			return null;
		}
	}

	public chu.engine.anim.BlendModeArgs getBlendMode() {
		return chu.engine.anim.BlendModeArgs.fromName(blendModeName);
	}
}
