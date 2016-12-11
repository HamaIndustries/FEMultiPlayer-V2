package net.fe.builderStage;

import java.io.File;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;

import chu.engine.Game;
import chu.engine.anim.BitmapFont;
import chu.engine.anim.Renderer;
import chu.engine.entity.menu.TextInputBox;
import chu.engine.event.KeyboardEvent;
import net.fe.FEResources;
import net.fe.game.fightStage.FightStage;

// TODO: Auto-generated Javadoc
/**
 * The Class TeamNameInput.
 */
public class TeamNameInput extends TextInputBox {

	/** The Constant FOCUSED. */
	private static final Color FOCUSED = new Color(0x817b58);

	/** The Constant CURSOR. */
	private static final Color CURSOR = new Color(0xeeeeee);

	/** The Constant EXT. */
	public static final String EXT = "femt";

	/** The save. */
	private boolean save;

	/**
	 * Instantiates a new team name input.
	 *
	 * @param save the save
	 */
	public TeamNameInput(boolean save) {
		super(190, 160, 100, 20, "default_med");
		this.save = save;
		renderDepth = 0f;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see chu.engine.Entity#render()
	 */
	public void render() {
		Renderer.drawRectangle(0, 0, 480, 320, renderDepth, new Color(0, 0, 0, 0.5f));
		Renderer.drawBorderedRectangle(x - 10, y - 20, x + width + 10, y + height + 5, renderDepth, FightStage.NEUTRAL,
		        FightStage.BORDER_LIGHT, FightStage.BORDER_DARK);
		Renderer.drawString("default_med", "Team Name:", x, y - 15, renderDepth);
		BitmapFont font = FEResources.getBitmapFont("default_med");
		Renderer.drawRectangle(x, y, x + width, y + height, renderDepth, FOCUSED);
		float linepos = x + font.getStringWidth(input.substring(0, cursorPos)) + 2;
		Renderer.drawRectangle(linepos, y + 1, linepos + 1, y + height - 1, renderDepth - 0.02f, CURSOR);
		Renderer.drawString("default_med", input.toString(), x + 2, y + 5, renderDepth - 0.01f);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see chu.engine.menu.TextInputBox#beginStep()
	 */
	public void beginStep() {
		super.beginStep();
		List<KeyboardEvent> keys = Game.getKeys();
		for (KeyboardEvent ke : keys) {
			if (ke.state) {
				if (ke.key == FEResources.getKeyMapped(Keyboard.KEY_RETURN)) {
					if (!new File("teams").exists()) {
						new File("teams").mkdir();
					}
					if (save) {
						save();
					} else {
						load();
					}
					destroy();
					((TeamBuilderStage) stage).setControl(true);

				}
			}
		}
	}

	/**
	 * Save.
	 */
	public void save() {
		((TeamBuilderStage) stage).saveTeam(input.toString());
	}

	/**
	 * Load.
	 */
	public void load() {
		((TeamBuilderStage) stage).loadTeam(input.toString());
	}

	/**
	 * Convert path.
	 *
	 * @param path the path
	 * @return the string
	 */
	public static String convertPath(String path) {
		if (!path.endsWith(EXT)) {
			return path + "." + EXT;
		} else {
			return path;
		}
	}

	/**
	 * Sets the stage.
	 *
	 * @param s the new stage
	 */
	public void setStage(TeamBuilderStage s) {
		hasFocus = true;
		s.addEntity(this);
		s.setControl(false);
	}

}
