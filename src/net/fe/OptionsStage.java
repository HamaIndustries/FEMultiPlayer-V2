package net.fe;

import java.util.List;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;

import chu.engine.Game;
import chu.engine.Entity;
import chu.engine.Stage;
import chu.engine.KeyboardEvent;
import chu.engine.anim.Renderer;
import chu.engine.anim.Transform;

import net.fe.network.Message;

public final class OptionsStage extends Stage {
	
	public static final int BUTTON_Y = 270;
	public static final int CANCEL_X = 390;
	public static final int SAVE_X = 305;
	private static final int BUTTONS_OPTIONS = -1;
	
	private final OptionEntity[] options;
	private final Button[] buttons;
	/**
	 * The selected row from the `options` array; -1 if the save/cancel buttons are selected
	 */
	private int currentOptionIndex;
	private int buttonIndex;
	
	public OptionsStage() {
		super("main");
		ControlsDisplay controls = new ControlsDisplay();
		controls.addControl("X", "Cancel");
		addEntity(new RunesBg(Color.gray));
		addEntity(controls);
		
		Button[] buttons = {
			new Button(SAVE_X, BUTTON_Y, "Save", Color.green, 80) {
				public void execute() {
					OptionsStage.this.save();
					OptionsStage.this.backToMenu();
				}
			},
			new Button(CANCEL_X, BUTTON_Y, "Cancel", Color.red, 80) {
				public void execute() {
					OptionsStage.this.backToMenu();
				}
			}
		};
		this.buttons = buttons;
		
		OptionEntity[] options = {
			new OptionGroupEntity(50,
				new OptionGroup("AUTOCURSOR", java.util.Arrays.asList("START", "STARTLOCAL", "OFF"), java.util.Arrays.asList("Move cursor to team's lead at the start of each phase", "Move cursor to team's lead at the start of your phase", "Do not move the cursor at the start of the turn")),
				FEResources.getAutoCursor().ordinal()
			),
			new OptionSliderEntity(80,
				"VOLUME", FEResources.getAudioVolume(), 20,
				"Loudness of sounds produced by this game"
			),
			new OptionGroupEntity(110,
				new OptionGroup("SCALE", java.util.Arrays.asList("1.0", "2.0", "3.0"), java.util.Arrays.asList("Increase size of game window")),
				(FEResources.getWindowScale() == 3f ? 2 : (FEResources.getWindowScale() == 2f ? 1 : 0))
			)
		};
		this.options = options;
		
		for (Entity e : options) {addEntity(e);}
		for (Entity e : buttons) {addEntity(e);}
		currentOptionIndex = 0;
		buttonIndex = 0;
	}
	
	
	@Override
	public void beginStep(List<Message> messages) {
		for (Entity e : entities) { e.beginStep(); }
		List<KeyboardEvent> keys = Game.getKeys();
		
		for (KeyboardEvent ke : keys) {
			if (ke.state) {
				if(ke.key == FEResources.getKeyMapped(Keyboard.KEY_X)) {
					OptionsStage.this.backToMenu();
				}
				if(ke.key == FEResources.getKeyMapped(Keyboard.KEY_LEFT)) {
					OptionsStage.this.left();
				}
				if(ke.key == FEResources.getKeyMapped(Keyboard.KEY_RIGHT)) {
					OptionsStage.this.right();
				}
				if(ke.key == FEResources.getKeyMapped(Keyboard.KEY_UP)) {
					OptionsStage.this.up();
				}
				if(ke.key == FEResources.getKeyMapped(Keyboard.KEY_DOWN)) {
					OptionsStage.this.down();
				}
				if(ke.key == FEResources.getKeyMapped(Keyboard.KEY_RETURN)) {
					if (currentOptionIndex == BUTTONS_OPTIONS) {
						buttons[buttonIndex].execute();
					}
				}
			}
		}
	}
		
	@Override
	public void onStep() {
		for (Entity e : entities) {
			e.onStep();
		}
	}

	@Override
	public void endStep() {
		for (Entity e : entities) {
			e.onStep();
		}
	}
	
	@Override
	public void render() {
		super.render();
		Renderer.drawRectangle(10, 5, 460, 45, 1.0f, new Color(240, 220, 102));
		if (currentOptionIndex >= 0) {
			// dialogue_text (?)
			Renderer.drawString("default_med", options[currentOptionIndex].getDescription(), 15, 8, 0.2f);
		}
	}
	
	
	private void backToMenu() {
		FEMultiplayer.setCurrentStage(FEMultiplayer.connect);
	}
	
	private void save() {
		final java.util.HashMap<String, String> newProps = new java.util.HashMap<>();
		for (OptionEntity e : options) {
			newProps.put(e.getKey(), e.getValue());
		}
		FEResources.writeProperties(newProps);
		
		// update things that depend on the set options
		if (FEResources.getAudioVolume() <= 0) {
			SoundTrack.stop();
		} else {
			SoundTrack.updateVolume();
		}
		Game.updateScale();
	}
	
	private void up() {
		currentOptionIndex--;
		if (currentOptionIndex < -1) {
			currentOptionIndex = options.length - 1;
		}
		buttons[buttonIndex].setHover(currentOptionIndex == BUTTONS_OPTIONS);
	}
	private void down() {
		currentOptionIndex++;
		if (currentOptionIndex >= options.length) {
			currentOptionIndex = -1;
		}
		buttons[buttonIndex].setHover(currentOptionIndex == BUTTONS_OPTIONS);
	}
	private void left() {
		if (currentOptionIndex >= 0) {
			options[currentOptionIndex].left();
		} else {
			buttons[buttonIndex].setHover(false);
			buttonIndex = (buttonIndex - 1 + buttons.length) % buttons.length;
			buttons[buttonIndex].setHover(true);
		}
	}
	private void right() {
		if (currentOptionIndex >= 0) {
			options[currentOptionIndex].right();
		} else {
			buttons[buttonIndex].setHover(false);
			buttonIndex = (buttonIndex + 1) % buttons.length;
			buttons[buttonIndex].setHover(true);
		}
	}
	
	
	private abstract class OptionEntity extends Entity {
		public OptionEntity(int y) {
			super(10, y);
		}
		
		/** increment value */
		public abstract void left();
		/** decrement value */
		public abstract void right();
		/** A description to display for the entity's current state */
		public abstract String getDescription();
		/** The key being set */
		public abstract String getKey();
		/** The value to set to */
		public abstract String getValue();
		
		/** Return a color to use for this object */
		protected Color getColor(final boolean columnSelected) {
			final boolean rowSelected = currentOptionIndex >= 0 && options[currentOptionIndex] == this;
			
			return (columnSelected ?
					(rowSelected ? new Color(90,200,90) : new Color(67, 150, 67)) :
					(rowSelected ? Color.white : new Color(192, 192, 192))
			);

		}
	}
	
	/** Data for a group of discrete values */
	private final static class OptionGroup {
		public final String key;
		public final List<String> values;
		public final List<String> descriptions;
		
		public OptionGroup(String key, List<String> values, List<String> descriptions) {
			this.key = key;
			this.values = java.util.Collections.unmodifiableList(new java.util.ArrayList<String>(values));
			this.descriptions = java.util.Collections.unmodifiableList(new java.util.ArrayList<String>(descriptions));
		}
	}
	
	/** */
	private final class OptionGroupEntity extends OptionEntity {
		public final OptionGroup data;
		private int selectedValueIndex;
		
		/**
		 * @param y the y-coordinate to draw this entity at
		 * @param data the 
		 * @param selectedValueIndex the starting value for selectedValueIndex
		 */
		public OptionGroupEntity(int y, OptionGroup data, int selectedValueIndex) {
			super(y);
			this.data = data;
			this.selectedValueIndex = selectedValueIndex;
			if (this.selectedValueIndex >= data.values.size()) {
				this.selectedValueIndex = 0;
			}
		}
		
		public void onStep() {
		}
		
		public void render() {
			final int spaceWidth = FEResources.getBitmapFont("default_med").getStringWidth(" ");
			final Transform t = new Transform();
			final Color normalColor = this.getColor(false);
			final Color selectedColor = this.getColor(true);
			t.scaleX = 2;
			t.scaleY = 2;
			t.setColor(normalColor);
			
			float renderX = this.x;
			{
				final int keyWidth = FEResources.getBitmapFont("default_med").getStringWidth(data.key + ": ");
				Renderer.drawString("default_med", data.key + ": ", renderX, y, 0.0f, t);
				renderX += (keyWidth + spaceWidth) * t.scaleX;
			}
			for (int i = 0; i < data.values.size(); i++) {
				final String str = data.values.get(i);
				final int strWidth = FEResources.getBitmapFont("default_med").getStringWidth(str);
				t.setColor((i == selectedValueIndex ? selectedColor : normalColor));
				Renderer.drawString("default_med", str, renderX, y, 0.0f, t);
				
				renderX += (strWidth + spaceWidth) * t.scaleX;
			}
		}
		
		@Override
		public void left() {
			selectedValueIndex = (selectedValueIndex - 1 + data.values.size()) % data.values.size();
		}
		@Override
		public void right() {
			selectedValueIndex = (selectedValueIndex + 1) % data.values.size();
		}
		@Override
		public String getDescription() {
			final int usedIndex = (data.descriptions.size() <= selectedValueIndex ? 0 : selectedValueIndex);
			final String usedString = (data.descriptions.size() <= usedIndex ? "" : data.descriptions.get(usedIndex));
			return usedString;
		}
		@Override
		public String getKey() { return this.data.key; } 
		@Override
		public String getValue() { return data.values.get(selectedValueIndex); }
	}
	
	/** A selector for a value between */
	private final class OptionSliderEntity extends OptionEntity {
		private final String key;
		private final String description;
		// javax.swing.BoundedRangeModel (?)
		private int value;
		private final int max;
		private final int sliderWidth = 200;
		private final int sliderHeight = 24;
		
		/**
		 * @param value the starting value, on a scale from 0.0f to 1.0f
		 * @param steps resolution of changes
		 */
		public OptionSliderEntity(int y, String key, float value, int steps, String description) {
			super(y);
			this.description = description;
			this.key = key;
			this.max = steps;
			this.value = (int) (Math.max(0f, Math.min(1f, value)) * steps);
		}
		
		public void render() {
			final int spaceWidth = FEResources.getBitmapFont("default_med").getStringWidth(" ");
			final Transform t = new Transform();
			final Color normalColor = this.getColor(false);
			final Color selectedColor = this.getColor(true);
			t.scaleX = 2;
			t.scaleY = 2;
			t.setColor(normalColor);
			
			float renderX = this.x;
			{
				int keyWidth = FEResources.getBitmapFont("default_med").getStringWidth(key + ": ");
				Renderer.drawString("default_med", key + ": ", renderX, y, 0.0f, t);
				renderX += (keyWidth + spaceWidth) * t.scaleX;
			}
			{
				Renderer.drawRectangle(renderX - 1, y - 1, renderX + sliderWidth + 1, y + sliderHeight + 1, 0.0f, normalColor);
				Renderer.drawRectangle(renderX, y, renderX + (sliderWidth * value / max), y + sliderHeight, 0.0f, selectedColor);
			}
		}
		
		public void onStep() {
		}
		@Override
		public void left() {
			if (value > 0) {value--;}
		}
		@Override
		public void right() {
			if (value < max) {value++;}
		}
		@Override
		public String getDescription() { return this.description; }
		@Override
		public String getKey() { return this.key; } 
		@Override
		public String getValue() { return "" + (((float) value) / max); } 
		
	}
}