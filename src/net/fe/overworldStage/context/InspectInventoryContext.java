package net.fe.overworldStage.context;

import static net.fe.fightStage.FightStage.BORDER_DARK;
import static net.fe.fightStage.FightStage.BORDER_LIGHT;
import static net.fe.fightStage.FightStage.NEUTRAL;

import chu.engine.Entity;
import chu.engine.anim.AudioPlayer;
import chu.engine.anim.Renderer;

import net.fe.overworldStage.OverworldContext;
import net.fe.overworldStage.ClientOverworldStage;
import net.fe.unit.Item;
import net.fe.unit.ItemDetailsText;
import net.fe.unit.Unit;

/**
 * A context which displays information about a unit's inventory
 */
public final class InspectInventoryContext extends OverworldContext {
	
	/** The unit whose inventory is to be inspected */
	private final Unit unit;
	/** The index of the item to display details for */
	private int itemIndex;
	/** The entities that display the inventory item details */
	private final ItemDetailsBubble bubble;
	/** The entities that display the inventory item details */
	private final ItemDetailsText text;
	
	/**
	 * A context which displays information about a unit's inventory
	 * @param stage the stage
	 * @param prev the previous context, to return to on a 'Cancel'
	 * @param u the unit whose inventory is to be inspected
	 * @pre `u` has at least one item in its inventory
	 * @pre a UnitInfo at its default location is showing the same unit as this is
	 */
	public InspectInventoryContext(ClientOverworldStage stage, OverworldContext prev, Unit u) {
		super(stage, prev);
		this.unit = u;
		this.itemIndex = 0;
		this.bubble = new ItemDetailsBubble();
		this.text = new ItemDetailsText(this.bubble.x, this.bubble.y);
		this.text.renderDepth = this.bubble.renderDepth - 0.01f;
	}
	
	@Override
	public void startContext(){
		super.startContext();
		cursor.off();
		stage.addEntity(this.bubble);
		stage.addEntity(this.text);
		this.text.setItem(unit.getInventory().get(0));
	}
	
	@Override
	public void cleanUp(){
		super.cleanUp();
		cursor.on();
		stage.removeEntity(this.bubble);
		stage.removeEntity(this.text);
	}
	
	@Override
	public void onUp() {
		if (itemIndex > 0) {
			itemIndex--;
		} else {
			itemIndex = unit.getInventory().size() - 1;
		}
		if (unit.getInventory().size() > 1) {
			this.onChange();
		}
	}
	
	@Override
	public void onDown() {
		if (itemIndex + 1 < unit.getInventory().size()) {
			itemIndex++;
		} else {
			itemIndex = 0;
		}
		if (unit.getInventory().size() > 1) {
			this.onChange();
		}
	}

	/** Do nothing */
	@Override
	public void onLeft() {
	}
	
	/** Do nothing */
	@Override
	public void onRight() {
	}
	
	/** Do nothing */
	@Override
	public void onSelect() {
	}
	
	public void onInspectInventory() {
		this.onCancel();
	}
	
	private void onChange(){
		AudioPlayer.playAudio("cursor2");
		this.text.setItem(unit.getInventory().get(itemIndex));
	}
	
	
	private final class ItemDetailsBubble extends Entity {
		public ItemDetailsBubble() {
			// These numbers try to place this entity just north of and right-aligned with UnitInfo
			super(50 + 320 - 260 - 2, 320 - 80 - 52 - 2);
			this.width = 260;
			this.height = 52;
			this.renderDepth = ClientOverworldStage.MENU_DEPTH;
		}
		
		@Override
		public void render(){
			Renderer.drawBorderedRectangle(this.x, this.y, this.x + this.width, this.y + this.height, this.renderDepth,
					NEUTRAL.darker(0.5f), BORDER_LIGHT, BORDER_DARK);
			
			// Draw highlight, ideally under the UnitInfo's text for this item
			Renderer.drawRectangle(260, 320 - 80 + 22 + 14 * itemIndex - 2,
					366, 320 - 80 + 22 + 14 + 14 * itemIndex - 2,
					this.renderDepth, NEUTRAL.brighter(0.1f));
		}
	}
}
