package chu.engine.entity.menu;

import java.util.List;

import org.lwjgl.input.Mouse;

import chu.engine.Game;
import chu.engine.entity.Entity;
import chu.engine.event.MouseEvent;

// TODO: Auto-generated Javadoc
/**
 * The Class MenuButton.
 */
public abstract class MenuButton extends Entity {
	
	/** The width. */
	private float width;
	
	/** The height. */
	private float height;
	
	/** The hover. */
	protected boolean hover;

	/**
	 * Instantiates a new menu button.
	 *
	 * @param x the x
	 * @param y the y
	 */
	public MenuButton(float x, float y) {
		super(x, y);
		width = 0;
		height = 0;
		hover = false;
	}
	
	/**
	 * Instantiates a new menu button.
	 *
	 * @param x the x
	 * @param y the y
	 * @param w the w
	 * @param h the h
	 */
	public MenuButton(float x, float y, float w, float h) {
		this(x, y);
		width = w;
		height = h;
	}
	
	/**
	 * On enter.
	 */
	public void onEnter(){};
	
	/**
	 * On click.
	 */
	public void onClick(){};
	
	/**
	 * On exit.
	 */
	public void onExit(){};
	
	/* (non-Javadoc)
	 * @see chu.engine.Entity#beginStep()
	 */
	public void beginStep() {
		int mX = Math.round(Mouse.getX() / Game.getScaleX());
		int mY = Math.round((Game.getWindowHeight() - Mouse.getY()) / Game.getScaleY());
		boolean newHover = (mX >= x && mX < x + width && mY >= y && mY < y + height);
		if(newHover && !hover) {
			onEnter();
			hover = true;
		} else if(!newHover && hover) {
			onExit();
			hover = false;
		}
		List<MouseEvent> mouseEvents = Game.getMouseEvents();
		for(MouseEvent event : mouseEvents) {
			if(hover && event.button == 0 && event.state) {
				onClick();
				break;
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see chu.engine.Entity#endStep()
	 */
	public void endStep() {
		
	}

}
