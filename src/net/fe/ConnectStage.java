package net.fe;

import java.util.List;

import org.newdawn.slick.Color;

import chu.engine.Game;
import chu.engine.Stage;
import chu.engine.anim.AudioPlayer;
import chu.engine.anim.BitmapFont;
import chu.engine.anim.Renderer;
import chu.engine.anim.ShaderArgs;
import chu.engine.entity.Entity;
import chu.engine.entity.menu.MenuButton;
import chu.engine.entity.menu.Notification;
import chu.engine.entity.menu.TextInputBox;
import chu.engine.event.MouseEvent;
import net.fe.builderStage.TeamBuilderStage;
import net.fe.network.Message;

// TODO: Auto-generated Javadoc
/**
 * Player selects name and server ip.
 *
 * @author Shawn
 */
public final class ConnectStage extends Stage {

	/** The Constant UNFOCUSED. */
	private static final Color UNFOCUSED = new Color(0x58543c);

	/** The Constant FOCUSED. */
	private static final Color FOCUSED = new Color(0x817b58);

	/** The Constant CURSOR. */
	private static final Color CURSOR = new Color(0xeeeeee);

	/** The name. */
	private final ConnectInputBox name;

	/** The ip. */
	private final ConnectInputBox ipPort;

	/**
	 * Instantiates a new connect stage.
	 */
	public ConnectStage() {
		super("main");
		name = new ConnectInputBox(180, 136, 153, 20);
		ipPort = new ConnectInputBox(180, 166, 100, 20);
		addEntity(name);
		addEntity(ipPort);
		addEntity(new RunesBg(Color.gray));
		addEntity(new ConnectButton(286, 166, 47, 20));
		addEntity(new MenuButton(180, 196, 128, 32) {
			{
				sprite.addAnimation("default", FEResources.getTexture("team_builder_button"));
			}

			@Override
			public void onClick() {
				FEMultiplayer.setCurrentStage(new TeamBuilderStage(true, null, null));
			}

			@Override
			public void render() {
				if (hover) {
					sprite.render(x, y, renderDepth, null, new ShaderArgs("lighten", 0.5f));
				} else {
					sprite.render(x, y, renderDepth);
				}
			}
		});
		processAddStack();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see chu.engine.Stage#beginStep()
	 */
	@Override
	public void beginStep(List<Message> messages) {
		for (Entity e : entities) {
			e.beginStep();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see chu.engine.Stage#onStep()
	 */
	@Override
	public void onStep() {
		for (Entity e : entities) {
			e.onStep();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see chu.engine.Stage#endStep()
	 */
	@Override
	public void endStep() {
		for (Entity e : entities) {
			e.endStep();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see chu.engine.Stage#render()
	 */
	public void render() {
		// Draw and label boxes
		Renderer.drawString("default_med", "Name:", 150, 140, 0.0f);
		Renderer.drawString("default_med", "Server IP:", 133, 170, 0.0f);

		super.render();
	}

	/**
	 * The Class ConnectInputBox.
	 */
	private class ConnectInputBox extends TextInputBox {

		/**
		 * Instantiates a new connect input box.
		 *
		 * @param x the x
		 * @param y the y
		 * @param w the w
		 * @param h the h
		 */
		public ConnectInputBox(int x, int y, int w, int h) {
			super(x, y, w, h, "default_med");
			renderDepth = 0.0f;
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
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see chu.engine.Entity#render()
		 */
		public void render() {
			BitmapFont font = FEResources.getBitmapFont("default_med");
			Renderer.drawRectangle(x - 1, y - 1, x + width + 1, y + height + 1, renderDepth, Color.black);
			if (hasFocus) {
				Renderer.drawRectangle(x, y, x + width, y + height, renderDepth, FOCUSED);
				float linepos = x + font.getStringWidth(input.substring(0, cursorPos)) + 2;
				Renderer.drawRectangle(linepos, y + 1, linepos + 1, y + height - 1, renderDepth - 0.02f, CURSOR);
			} else {
				Renderer.drawRectangle(x, y, x + width, y + height, renderDepth, UNFOCUSED);
			}
			Renderer.drawString("default_med", input.toString(), x + 2, y + 5, renderDepth - 0.01f);
		}
	}

	/**
	 * The Class ConnectButton.
	 */
	private class ConnectButton extends MenuButton {

		/**
		 * Instantiates a new connect button.
		 *
		 * @param x the x
		 * @param y the y
		 * @param w the w
		 * @param h the h
		 */
		public ConnectButton(float x, float y, float w, float h) {
			super(x, y, w, h);
			sprite.addAnimation("default", FEResources.getTexture("connect_button"));
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see chu.engine.menu.MenuButton#onClick()
		 */
		
		private Entity prevEntity;
		
		@Override
		public void onClick() {
			AudioPlayer.playAudio("select");
			try {
				FEMultiplayer.connect(name.getInput(), ipPort.getInput());
			} catch (FailedToConnectException e) {
				int count = e.getMessage().replaceAll("[^\n]", "").length();
				System.out.println("count : " +count);
				if(prevEntity != null)
					removeEntity(prevEntity);
				prevEntity = new Notification(180, 120 - count * 20, "default_med", "ERROR: " + e.getMessage(), 5f,
				        new Color(255, 120, 100), 0f);
				addEntity(prevEntity);
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see chu.engine.Entity#render()
		 */
		@Override
		public void render() {
			if (hover) {
				sprite.render(x, y, renderDepth, null, new ShaderArgs("lighten", 0.5f));
			} else {
				sprite.render(x, y, renderDepth);
			}
		}

	}

}
