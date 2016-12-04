package chu.engine.anim;

import java.io.IOException;
import java.util.HashMap;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

// TODO: Auto-generated Javadoc
/**
 * The Class BitmapFont.
 */
public class BitmapFont {

	/** The texture. */
	private Texture texture;

	/** The glyph height. */
	private int glyphHeight;

	/** The spacing. */
	private int spacing;

	/** The glyphs. */
	private HashMap<Character, Glyph> glyphs;

	/**
	 * Instantiates a new bitmap font.
	 *
	 * @param texName the tex name
	 */
	public BitmapFont(String texName) {
		try {
			texture = TextureLoader.getTexture("PNG",
			        ResourceLoader.getResourceAsStream("res/fonts/" + texName + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		glyphs = new HashMap<Character, Glyph>();
	}

	/**
	 * Sets the height.
	 *
	 * @param height the new height
	 */
	public void setHeight(int height) {
		glyphHeight = height;
	}

	/**
	 * Sets the spacing.
	 *
	 * @param spacing the new spacing
	 */
	public void setSpacing(int spacing) {
		this.spacing = spacing;
	}

	/**
	 * Put.
	 *
	 * @param c the c
	 * @param pos the pos
	 * @param width the width
	 */
	public void put(char c, int pos, int width) {
		glyphs.put(c, new Glyph(pos, width));
	}

	/**
	 * Render.
	 *
	 * @param string the string
	 * @param beginX the begin x
	 * @param beginY the begin y
	 * @param depth the depth
	 * @param t the t
	 */
	public void render(String string, float beginX, float beginY, float depth, Transform t) {
		int x = (int) beginX;
		int y = (int) beginY;
		for (char c : string.toCharArray()) {
			Glyph g = glyphs.get(c);
			if(c == '\n'){
				y += texture.getImageHeight();
				x = (int) beginX;
				continue;
			}
			if(g == null){
				System.err.println("I don't have this character: '" + c + "'");
				continue;
			}
			float tx0 = (float) g.pos / texture.getImageWidth();
			float tx1 = (float) (g.pos + g.width) / texture.getImageWidth();
			Renderer.render(texture, tx0, 0, tx1, 1, x, y, x + g.width, y + glyphHeight, depth, t);
			x += g.width * (t != null ? t.scaleX : 1);
			x += spacing * (t != null ? t.scaleX : 1);
		}
	}

	/**
	 * Gets the string width.
	 *
	 * @param string the string
	 * @return the string width
	 */
	public int getStringWidth(String string) {
		int width = 0;
		for (char c : string.toCharArray()) {
			width += glyphs.get(c).width;
			width += spacing;
		}
		return width;
	}

	/**
	 * Contains character.
	 *
	 * @param c the c
	 * @return true, if successful
	 */
	public boolean containsCharacter(char c) {
		return glyphs.containsKey(c);
	}

	/**
	 * The Class Glyph.
	 */
	private class Glyph {

		/** The pos. */
		int pos;

		/** The width. */
		int width;

		/**
		 * Instantiates a new glyph.
		 *
		 * @param pos the pos
		 * @param width the width
		 */
		public Glyph(int pos, int width) {
			this.pos = pos;
			this.width = width;
		}
	}
}
