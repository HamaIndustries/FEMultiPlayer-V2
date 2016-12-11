package net.fe.lobbystage;

import java.util.List;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;

import chu.engine.Game;
import chu.engine.anim.AudioPlayer;
import chu.engine.anim.BitmapFont;
import chu.engine.anim.Renderer;
import chu.engine.entity.menu.TextInputBox;
import chu.engine.event.KeyboardEvent;
import chu.engine.event.MouseEvent;
import net.fe.FEMultiplayer;
import net.fe.FEResources;
import net.fe.network.message.ChatMessage;

// TODO: Auto-generated Javadoc
/**
 * The Class LobbyChatBox.
 */
public class LobbyChatBox extends TextInputBox {

	/** The Constant UNFOCUSED. */
	private static final Color UNFOCUSED = new Color(0x58543c);

	/** The Constant FOCUSED. */
	private static final Color FOCUSED = new Color(0x817b58);

	/** The Constant CURSOR. */
	private static final Color CURSOR = new Color(0xeeeeee);

	/**
	 * Instantiates a new lobby chat box.
	 */
	public LobbyChatBox() {
		super(6, 294, 250, 20, "default_med");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see chu.engine.menu.TextInputBox#beginStep()
	 */
	public void beginStep() {
		List<MouseEvent> mouseEvents = Game.getMouseEvents();
		for (MouseEvent event : mouseEvents) {
			if (event.button == 0) {
				int mX = Math.round(event.x / Game.getScaleX());
				int mY = Math.round((Game.getWindowHeight() - event.y) / Game.getScaleY());
				boolean newHover = (mX >= x && mX < x + width && mY >= y && mY < y + height);
				hasFocus = newHover;
			}
		}
		super.beginStep();
		if (hasFocus) {
			List<KeyboardEvent> keys = Game.getKeys();
			for (KeyboardEvent ke : keys) {
				if (ke.state) {
					if (ke.key == FEResources.getKeyMapped(Keyboard.KEY_RETURN)) {
						send();
					}
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see chu.engine.Entity#render()
	 */
	public void render() {
		BitmapFont font = FEResources.getBitmapFont("default_med");
		if (hasFocus) {
			Renderer.drawRectangle(x, y, x + width, y + height, renderDepth, FOCUSED);
			float linepos = x + font.getStringWidth(input.substring(0, cursorPos)) + 2;
			Renderer.drawRectangle(linepos, y + 1, linepos + 1, y + height - 1, renderDepth - 0.02f, CURSOR);
		} else {
			Renderer.drawRectangle(x, y, x + width, y + height, renderDepth, UNFOCUSED);
		}
		Renderer.drawString("default_med", input.toString(), x + 2, y + 5, renderDepth - 0.01f);
	}

	/**
	 * Send.
	 */
	public void send() {
		if (input.length() == 0)
			return;
		AudioPlayer.playAudio("cancel");
		int id = FEMultiplayer.getClient().getID();
		FEMultiplayer.getClient().sendMessage(new ChatMessage(id, input.toString()));
		input.delete(0, input.length());
		cursorPos = 0;
	}

}
