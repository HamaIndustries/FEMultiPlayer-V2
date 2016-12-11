package net.fe.game.unit;

import org.newdawn.slick.Color;

import chu.engine.anim.Renderer;
import chu.engine.anim.ShaderArgs;
import chu.engine.anim.Transform;
import chu.engine.entity.Entity;
import net.fe.FEResources;
import net.fe.Party;

// TODO: Auto-generated Javadoc
/**
 * The Class UnitIcon.
 */
public class UnitIcon extends Entity {

	/** The c. */
	private Color c;

	/** The u. */
	private Unit u;

	/** The greyscale. */
	private boolean greyscale;

	/**
	 * Instantiates a new unit icon.
	 *
	 * @param u the u
	 * @param x the x
	 * @param y the y
	 * @param depth the depth
	 */
	public UnitIcon(Unit u, float x, float y, float depth) {
		super(x, y);
		this.u = u;
		if (u.getParty() != null)
			this.c = u.getPartyColor();
		else
			this.c = Party.TEAM_BLUE;
		sprite.addAnimation("IDLE", new MapAnimation(u.functionalClassName() + "_map_idle", false));
		renderDepth = depth;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see chu.engine.Entity#onStep()
	 */
	public void onStep() {
		super.onStep();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see chu.engine.Entity#render()
	 */
	public void render() {
		if (FEResources.hasTexture(u.functionalClassName().toLowerCase() + "_map_idle")) {
			Transform t = new Transform();
			if (greyscale) {
				sprite.render(x + 1, y + 1, renderDepth, t, new ShaderArgs("greyscale"));
			} else if (c.equals(Party.TEAM_RED)) {
				sprite.render(x + 1, y + 1, renderDepth, t, new ShaderArgs("paletteSwap"));
			} else {
				sprite.render(x + 1, y + 1, renderDepth, t, new ShaderArgs("default"));
			}
		} else {
			Color c = this.c;
			if (greyscale)
				c = Color.gray;
			Renderer.drawRectangle(x + 1, y + 1, x + 14, y + 14, renderDepth, c);
			Renderer.drawString("default_med", u.name.charAt(0) + "" + u.name.charAt(1), x + 2, y + 1, renderDepth);

		}
	}

	/**
	 * Checks if is greyscale.
	 *
	 * @return true, if is greyscale
	 */
	public boolean isGreyscale() {
		return greyscale;
	}

	/**
	 * Sets the greyscale.
	 *
	 * @param greyscale the new greyscale
	 */
	public void setGreyscale(boolean greyscale) {
		this.greyscale = greyscale;
	}

}
