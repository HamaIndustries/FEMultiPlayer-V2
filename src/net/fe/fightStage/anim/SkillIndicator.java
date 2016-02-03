package net.fe.fightStage.anim;

import net.fe.fightStage.FightStage;
import chu.engine.Entity;
import chu.engine.Game;
import chu.engine.anim.Renderer;

// TODO: Auto-generated Javadoc
/**
 * The Class SkillIndicator.
 */
public class SkillIndicator extends Entity{
	
	/** The message. */
	private String message;
	
	/** The elapsed time. */
	private float elapsedTime;
	
	/** The extended. */
	private boolean extended;
	
	/** The vx. */
	private int vx;
	
	/** The x0. */
	private int x0;
	
	/** The xt. */
	private int xt;
	
	/** The left. */
	private boolean left;
	
	/** The Constant MSG_TIME. */
	public static final int MSG_TIME = 1;
	
	/** The Constant WIDTH. */
	public static final int WIDTH = 50;
	
	/** The Constant HEIGHT. */
	public static final int HEIGHT = 12;
	
	/**
	 * Instantiates a new skill indicator.
	 *
	 * @param message the message
	 * @param left the left
	 * @param position the position
	 */
	public SkillIndicator(String message, boolean left, int position){
		super(0,29 + position*(HEIGHT+2));
		this.message = message;
		elapsedTime = 0;
		extended = false;
		vx = left? 800: -800;
		x = left? -WIDTH: FightStage.CENTRAL_AXIS*2;
		xt = left? 0: FightStage.CENTRAL_AXIS*2-WIDTH;
		x0 = (int) x;
		this.left = left;
		
		renderDepth = FightStage.HUD_DEPTH;
	}
	
	/* (non-Javadoc)
	 * @see chu.engine.Entity#onStep()
	 */
	public void onStep(){
		if(extended){
			elapsedTime += Game.getDeltaSeconds();
			if(elapsedTime > MSG_TIME){
				extended = false;
			}
		} else if(elapsedTime == 0){
			x += vx * Game.getDeltaSeconds();
			if(Math.abs(x - x0)+3 >= WIDTH){
				extended = true;
				x = xt;
			}
		} else {
			x -= vx * Game.getDeltaSeconds();
			if(Math.abs(x - x0) <= 0){
				destroy();
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see chu.engine.Entity#render()
	 */
	public void render(){
		if(left){
			Renderer.drawRectangle(x-10, y, x+WIDTH, y + HEIGHT, 0, FightStage.NEUTRAL);
		} else {
			Renderer.drawRectangle(x, y, x+WIDTH+10, y + HEIGHT, 0, FightStage.NEUTRAL);
		}
		Renderer.drawString("msg_text", message, x+4, y+2, 0.0f);
	}
}
