package chu.engine.anim;

import java.io.IOException;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

// TODO: Auto-generated Javadoc
/**
 * The Class Tileset.
 */
public class Tileset {

	/** The tileset. */
	private Texture tileset;
	
	/** The tile width. */
	private int tileWidth;
	
	/** The tile height. */
	private int tileHeight;
	
	/** The width. */
	private int width;
	
	/** The height. */
	private int height;

	/**
	 * Instantiates a new tileset.
	 *
	 * @param t the t
	 * @param tileWidth the tile width
	 * @param tileHeight the tile height
	 */
	public Tileset(Texture t, int tileWidth, int tileHeight) {
		tileset = t;
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
		width = tileset.getImageWidth();
		height = tileset.getImageHeight();

	}

	/**
	 * Instantiates a new tileset.
	 *
	 * @param path the path
	 * @param tileWidth the tile width
	 * @param tileHeight the tile height
	 */
	public Tileset(String path, int tileWidth, int tileHeight) {
		try {
			tileset = TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream(path));
			System.out.println("Loaded: "+path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
		width = tileset.getImageWidth();
		height = tileset.getImageHeight();
	}

	/**
	 * Render.
	 *
	 * @param x the x
	 * @param y the y
	 * @param tx the tx
	 * @param ty the ty
	 * @param depth the depth
	 */
	public void render(float x, float y, int tx, int ty, float depth) {
		float tx0 = (float) tx / (width / tileWidth);
		float ty0 = (float) ty / (height / tileHeight);
		float tx1 = (float) (tx + 1) / (width / tileWidth);
		float ty1 = (float) (ty + 1) / (height / tileHeight);
		Renderer.render(tileset, tx0, ty0, tx1, ty1, (int) x, (int) y,
				(int) (x + tileWidth), (int) (y + tileHeight), depth);
	}
	
	/**
	 * Render transformed.
	 *
	 * @param x the x
	 * @param y the y
	 * @param tx the tx
	 * @param ty the ty
	 * @param depth the depth
	 * @param t the t
	 */
	public void renderTransformed(float x, float y, int tx, int ty, float depth, Transform t) {
		float tx0 = (float) tx / (width / tileWidth);
		float ty0 = (float) ty / (height / tileHeight);
		float tx1 = (float) (tx + 1) / (width / tileWidth);
		float ty1 = (float) (ty + 1) / (height / tileHeight);
		Renderer.render(tileset, tx0, ty0, tx1, ty1, (int) x, (int) y,
				(int) (x + tileWidth), (int) (y + tileHeight), depth, t);
	}

}
