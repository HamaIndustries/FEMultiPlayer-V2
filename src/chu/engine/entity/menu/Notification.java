package chu.engine.entity.menu;

import org.newdawn.slick.Color;

import chu.engine.Game;
import chu.engine.anim.Renderer;
import chu.engine.anim.Transform;
import chu.engine.entity.Entity;

// TODO: Auto-generated Javadoc
/**
 * The Class Notification.
 */
public class Notification extends Entity {

	/** The text. */
	private String text;
	
	/** The transform. */
	private Transform transform;
	
	/** The font. */
	private String font;
	
	/** The timer. */
	private float timer;
	
	/** The lifetime. */
	private float lifetime;
	
	/**
	 * Instantiates a new notification.
	 *
	 * @param x the x
	 * @param y the y
	 * @param font the font
	 * @param text the text
	 * @param lifetime the lifetime
	 * @param depth the depth
	 */
	public Notification(float x, float y, String font, String text, float lifetime, float depth) {
		this(x, y, font, text, lifetime, Color.white, depth);
	}
	
	/**
	 * Instantiates a new notification.
	 *
	 * @param x the x
	 * @param y the y
	 * @param font the font
	 * @param text the text
	 * @param lifetime the lifetime
	 * @param c the c
	 * @param depth the depth
	 */
	public Notification(float x, float y, String font, String text, float lifetime, Color c, float depth) {
		super(x, y);
		this.font = font;
		this.text = text;
		this.lifetime = lifetime;
		this.timer = 0f;
		renderDepth = depth;
		transform = new Transform();
		transform.color = c;
	}
	
	/* (non-Javadoc)
	 * @see chu.engine.Entity#endStep()
	 */
	public void endStep() {
		timer += Game.getDeltaSeconds();
		if(timer > lifetime) destroy();
	}
	
	/* (non-Javadoc)
	 * @see chu.engine.Entity#render()
	 */
	public void render() {
		Renderer.drawString(font, text, x, y, renderDepth, transform);
	}

}
