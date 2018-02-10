package net.fe.overworldStage;
import net.fe.FEResources;
import chu.engine.GriddedEntity;
import chu.engine.anim.Animation;

// TODO: Auto-generated Javadoc
/**
 * The Class Cursor.
 */
public class Cursor extends GriddedEntity  implements DoNotDestroy{
	
	/** If true, the cursor is displayed and active */
	private boolean on;
	
	/** The distance to keep between the cursor and the edge of the screen */
	private static final int border = 64;
	
	/**
	 * Instantiates a new cursor.
	 *
	 * @param xx the initial x-coordinate
	 * @param yy the initial y-coordinate
	 */
	public Cursor(int xx, int yy) {
		super(xx, yy);
		sprite.addAnimation("default", new Animation(FEResources.getTexture("cursor"),
				24, 24, 16, 16, 4, 4, 0.05f, chu.engine.anim.BlendModeArgs.ALPHA_BLEND));
		renderDepth = ClientOverworldStage.CURSOR_DEPTH;
		on = true;
	}
	
	/* (non-Javadoc)
	 * @see chu.engine.Entity#onStep()
	 */
	public void onStep(){
		sprite.update();
	}
	
	/* (non-Javadoc)
	 * @see chu.engine.Entity#render()
	 */
	public void render(){
		ClientOverworldStage c = (ClientOverworldStage)stage;
		if(c.hasControl() && on) {
			if(x - c.camX > 367 || y - c.camY > 239) return; 
			sprite.render(x - c.camX, y - c.camY, renderDepth);
		}
	}

	/**
	 * Sets the x coord.
	 *
	 * @param xcoord the new x coord
	 */
	public void setXCoord(int xcoord) {
		this.xcoord = xcoord;
		updateCamera();
	}

	/**
	 * Sets the y coord.
	 *
	 * @param ycoord the new y coord
	 */
	public void setYCoord(int ycoord) {
		this.ycoord = ycoord;
		updateCamera();
	}
	
	/**
	 * Update camera.
	 */
	public void updateCamera() {
		ClientOverworldStage c = (ClientOverworldStage)stage;
		int rX = xcoord*16 - c.camX;
		if(rX < border) {
			c.camX = Math.max(0, xcoord*16 - border);
		} else if(rX > 368 - border) {
			c.camX = Math.min(c.camMaxX, xcoord*16 - (368 - border));
		}
		int rY = ycoord*16 - c.camY;
		if(rY < border) {
			c.camY = Math.max(0, ycoord*16 - border);
		} else if(rY > 240 - border) {
			c.camY = Math.min(c.camMaxY, ycoord*16 - (240 - border));
		}
	}
	
	/**
	 * Sets the cursor to not render
	 */
	public void off(){
		on = false;
	}
	
	/**
	 * Sets the cursor to render
	 */
	public void on(){
		on = true;
	}

}
