package net.fe;

import java.util.List;

import net.fe.fightStage.FightStage;
import net.fe.builderStage.TeamBuilderStage;
import net.fe.network.Message;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.Color;

import chu.engine.Entity;
import chu.engine.Game;
import chu.engine.MouseEvent;
import chu.engine.Stage;
import chu.engine.anim.AudioPlayer;
import chu.engine.anim.BitmapFont;
import chu.engine.anim.Renderer;
import chu.engine.anim.ShaderArgs;
import chu.engine.menu.MenuButton;
import chu.engine.menu.TextInputBox;

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
	private final ConnectInputBox ip;
	
	/**
	 * Instantiates a new connect stage.
	 */
	public ConnectStage() {
		super("main");
		name = new ConnectInputBox(180,136,153,20);
		ip = new ConnectInputBox(180,166,100,20);
		addEntity(name);
		addEntity(ip);
		addEntity(new RunesBg(Color.gray));
		addEntity(new ConnectButton(286,166,47,20));
		addEntity(new MenuButton(180,196,128,32) {
			{
				sprite.addAnimation("default", FEResources.getTexture("team_builder_button"));
			}
			@Override
			public void onClick() {
				FEMultiplayer.setCurrentStage(new TeamBuilderStage(true, null, null));
			}
			@Override
			public void render() {
				if(hover) {
					sprite.render(x, y, renderDepth, null, new ShaderArgs("lighten", 0.5f));
				} else {
					sprite.render(x, y, renderDepth);
				}
			}
		});
		addEntity(new MenuButton(180,232,128,32) {
			private int x = 180, y = 232, width = 128, height = 32; 
			
			@Override
			public void onClick() {
				FEMultiplayer.setCurrentStage(new OptionsStage());
			}
			@Override
			public void render() {
				final String text = "Options";
				final int stringWidth = FEResources.getBitmapFont("default_med").getStringWidth(text);
				Color c = Color.blue;
				if(!hover)
					c = c.darker();
				Renderer.drawBorderedRectangle(x + 2, y + 2, x + width - 4, y + height - 4, renderDepth,
						c, FightStage.BORDER_LIGHT, FightStage.BORDER_DARK);
				Renderer.drawString("default_med", text, x+width/2-stringWidth/2, y + 8, renderDepth);
			}
		});
		processAddStack();
	}

	/* (non-Javadoc)
	 * @see chu.engine.Stage#beginStep()
	 */
	@Override
	public void beginStep(List<Message> messages) {
		for(Entity e : entities) {
			e.beginStep();
		}
	}

	/* (non-Javadoc)
	 * @see chu.engine.Stage#onStep()
	 */
	@Override
	public void onStep() {
		for(Entity e : entities) {
			e.onStep();
		}
	}

	/* (non-Javadoc)
	 * @see chu.engine.Stage#endStep()
	 */
	@Override
	public void endStep() {
		for(Entity e : entities) {
			e.endStep();
		}
	}
	
	/* (non-Javadoc)
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
		
		/* (non-Javadoc)
		 * @see chu.engine.menu.TextInputBox#beginStep()
		 */
		public void beginStep() {
			List<MouseEvent> mouseEvents = Game.getMouseEvents();
			for(MouseEvent event : mouseEvents) {
				if(event.button == 0) {
					int mX = Math.round(event.x / Game.getScaleX());
					int mY = Math.round((Game.getWindowHeight() - event.y) / Game.getScaleY());
					boolean newHover = (mX >= x && mX < x + width && mY >= y && mY < y + height);
					hasFocus = newHover;
				}
			}
			super.beginStep();
		}
		
		/* (non-Javadoc)
		 * @see chu.engine.Entity#render()
		 */
		public void render() {
			BitmapFont font = FEResources.getBitmapFont("default_med");
			Renderer.drawRectangle(x-1, y-1, x+width+1, y+height+1, renderDepth, Color.black);
			if(hasFocus) {
				Renderer.drawRectangle(x, y, x+width, y+height, renderDepth, FOCUSED);
				float linepos = x + font.getStringWidth(input.substring(0, cursorPos)) + 2;
				Renderer.drawRectangle(linepos, y+1, linepos+1, y+height-1, renderDepth-0.02f, CURSOR);
			} else {
				Renderer.drawRectangle(x, y, x+width, y+height, renderDepth, UNFOCUSED);
			}
			Renderer.drawString("default_med", input.toString(), x+2, y+5, renderDepth-0.01f);
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
		
		/* (non-Javadoc)
		 * @see chu.engine.menu.MenuButton#onClick()
		 */
		@Override
		public void onClick() {
			AudioPlayer.playAudio("select");
			FEMultiplayer.connect(name.getInput(), ip.getInput());
		}
		
		/* (non-Javadoc)
		 * @see chu.engine.Entity#render()
		 */
		@Override
		public void render() {
			if(hover) {
				sprite.render(x, y, renderDepth, null, new ShaderArgs("lighten", 0.5f));
			} else {
				sprite.render(x, y, renderDepth);
			}
		}
		
	}

}
