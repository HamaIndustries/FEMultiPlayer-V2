package chu.engine.anim;

import org.lwjgl.opengl.GL11;

/**
 * An encapsulted pair of arguments to glBlendFunc
 */
public final class BlendModeArgs {
	
	private final int sfactor;
	private final int dfactor;
	
	/**
	 * 
	 */
	public BlendModeArgs(int sfactor, int dfactor) {
		this.sfactor = sfactor;
		this.dfactor = dfactor;
	}
	
	/**
	 * Applies this blend mode to the global ether.
	 */
	public void apply() {
		GL11.glBlendFunc(sfactor, dfactor);
	}
	
	
	/** The alpha blend mode */
	public static final BlendModeArgs ALPHA_BLEND = new BlendModeArgs(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	/** The screen blend mode */
	public static final BlendModeArgs SCREEN_BLEND = new BlendModeArgs(GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_COLOR);
	
	/**
	 * Returns a blend mode from a specified name
	 */
	public static BlendModeArgs fromName(String name) {
		if (null == name) { return ALPHA_BLEND; }
		if ("screen".equals(name)) { return SCREEN_BLEND; }
		if ("alpha".equals(name)) { return ALPHA_BLEND; }
		
		System.err.println("WARN: Unknown blend mode: " + name);
		return ALPHA_BLEND;
	}
}
