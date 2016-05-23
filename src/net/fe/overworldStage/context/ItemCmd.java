package net.fe.overworldStage.context;

import chu.engine.anim.AudioPlayer;
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
			if(unit.getHp() == unit.get("HP")) return;
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
	
}
