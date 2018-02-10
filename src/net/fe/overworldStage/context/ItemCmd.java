package net.fe.overworldStage.context;

import static net.fe.fightStage.FightStage.BORDER_DARK;
import static net.fe.fightStage.FightStage.BORDER_LIGHT;
import static net.fe.overworldStage.Menu.MENU_SELECT;

import chu.engine.Entity;
import chu.engine.anim.AudioPlayer;
import chu.engine.anim.Renderer;

import net.fe.overworldStage.Healthbar;
import net.fe.overworldStage.InventoryMenu;
import net.fe.overworldStage.MenuContext;
import net.fe.overworldStage.OverworldContext;
import net.fe.overworldStage.ClientOverworldStage;
import net.fe.network.command.EquipCommand;
import net.fe.network.command.UseCommand;
import net.fe.unit.HealingItem;
import net.fe.unit.Item;
import net.fe.unit.ItemDisplay;
import net.fe.unit.ItemDetailsText;
import net.fe.unit.Unit;
import net.fe.unit.UnitIdentifier;
import net.fe.unit.Weapon;

// TODO: Auto-generated Javadoc
/**
 * The Class ItemCmd.
 */
public class ItemCmd extends MenuContext<ItemDisplay>{
	
	/** The unit. */
	private Unit unit;
	/** The entities that display the inventory item details */
	private final ItemDetailsBubble bubble;
	/** The entities that display the inventory item details */
	private final ItemDetailsText text;
	
	/**
	 * Instantiates a new item cmd.
	 *
	 * @param stage the stage
	 * @param prev the prev
	 * @param u the u
	 */
	public ItemCmd(ClientOverworldStage stage, OverworldContext prev, Unit u) {
		super(stage, prev, new InventoryMenu(u));
		unit = u;
		this.bubble = new ItemDetailsBubble();
		this.text = new ItemDetailsText(this.bubble.x, this.bubble.y);
		this.text.renderDepth = this.bubble.renderDepth - 0.01f;
	}
	
	/* (non-Javadoc)
	 * @see net.fe.overworldStage.MenuContext#onSelect(java.lang.Object)
	 */
	@Override
	public void onSelect(ItemDisplay selectedItem) {
		if(selectedItem == null) return;
		Item i = selectedItem.getItem();
		AudioPlayer.playAudio("select");
		if(i instanceof Weapon){
			if(unit.equippable((Weapon) i)){
				stage.addCmd(new EquipCommand(new UnitIdentifier(unit), unit.findItem(i)));
				unit.equip((Weapon)i);
				menu.setSelection(0);
			}
		} else if (i instanceof HealingItem){
			if(unit.getHp() == unit.getStats().maxHp) return;
			stage.setControl(false);
			stage.addCmd(new UseCommand(unit.findItem(i)));
			stage.send();
			
			stage.setMenu(null);
			
			int oHp = unit.getHp();
			unit.use(i);
			//TODO Positioning
			stage.addEntity(new Healthbar(unit, oHp, unit.getHp(), stage){
				@Override
				public void done() {
					destroy();
					unit.setMoved(true);
					ItemCmd.this.stage.reset();
					ItemCmd.this.stage.setControl(true);
				}
			});
		}
	}

	/* (non-Javadoc)
	 * @see net.fe.overworldStage.OverworldContext#onLeft()
	 */
	@Override
	public void onLeft() {
		
	}
	
	/* (non-Javadoc)
	 * @see net.fe.overworldStage.OverworldContext#onRight()
	 */
	@Override
	public void onRight() {
		
	}
	
	@Override
	public void startContext(){
		super.startContext();
		stage.addEntity(this.bubble);
		stage.addEntity(this.text);
		if (unit.getInventory().size() > 0) {
			this.text.setItem(unit.getInventory().get(0));
		}
	}
	
	@Override
	public void cleanUp(){
		super.cleanUp();
		stage.removeEntity(this.bubble);
		stage.removeEntity(this.text);
	}
	
	@Override
	public void onChange(){
		super.onChange();
		this.bubble.alignYWithMenuIndex(super.menu.getSelectedIndex());
		this.text.y = this.bubble.y;
		final ItemDisplay selection = super.menu.getSelection();
		this.text.setItem(selection == null ? null : selection.getItem());
	}
	
	private final class ItemDetailsBubble extends Entity {
		private final static int WIDTH = 260;
		private final static int HEIGHT = 52;
		
		public ItemDetailsBubble() {
			super(ItemCmd.this.menu.x - WIDTH - 2, 0);
			this.alignYWithMenuIndex(0);
			this.width = WIDTH;
			this.height = HEIGHT;
			this.renderDepth = ClientOverworldStage.MENU_DEPTH;
		}
		
		/** Moves this entity such that its top is aligned with the given menu item */
		public void alignYWithMenuIndex(int idx) {
			this.y = ItemCmd.this.menu.y + 2 + idx * 17;
		}
		
		@Override
		public void render(){
			Renderer.drawBorderedRectangle(this.x, this.y, this.x + this.width, this.y + this.height, this.renderDepth,
					MENU_SELECT, BORDER_LIGHT, BORDER_DARK);
		}
	}
}
