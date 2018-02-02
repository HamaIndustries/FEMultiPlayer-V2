package net.fe.overworldStage.context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import chu.engine.anim.AudioPlayer;
import net.fe.network.command.WaitCommand;
import net.fe.overworldStage.FieldSkill;
import net.fe.overworldStage.Menu;
import net.fe.overworldStage.MenuContext;
import net.fe.overworldStage.Node;
import net.fe.overworldStage.OverworldContext;
import net.fe.overworldStage.ClientOverworldStage;
import net.fe.overworldStage.Zone;
import net.fe.overworldStage.Zone.ZoneType;
import net.fe.unit.Item;
import net.fe.unit.RiseTome;
import net.fe.unit.Unit;
import net.fe.unit.Weapon;

// TODO: Auto-generated Javadoc
/**
 * The Class UnitMoved.
 */
public class UnitMoved extends MenuContext<String> {
	
	/** The unit. */
	private Unit unit;
	
	/** The zone. */
	private Zone zone;
	
	/** The from trade. */
	private boolean fromTrade;
	
	/** The from take. */
	private boolean fromTake;

	/**
	 * Instantiates a new unit moved.
	 *
	 * @param stage the stage
	 * @param prev the prev
	 * @param u the u
	 * @param fromTrade the from trade
	 * @param fromTake the from take
	 */
	public UnitMoved(ClientOverworldStage stage, OverworldContext prev, Unit u,
			boolean fromTrade, boolean fromTake) {
		super(stage, prev, new Menu<String>(0, 0));
		unit = u;
		this.fromTrade = fromTrade;
		this.fromTake = fromTake;
		for (String cmd : getCommands(unit)) {
			menu.addItem(cmd);
		}
		
	}

	/* (non-Javadoc)
	 * @see net.fe.overworldStage.MenuContext#startContext()
	 */
	public void startContext() {
		super.startContext();

		updateZones();
		cursor.setXCoord(unit.getXCoord());
		cursor.setYCoord(unit.getYCoord());
		stage.setUnitInfoUnit(unit);
		
	}
	
	/* (non-Javadoc)
	 * @see net.fe.overworldStage.MenuContext#cleanUp()
	 */
	public void cleanUp(){
		super.cleanUp();
		stage.removeEntity(zone);
	}
	
	/* (non-Javadoc)
	 * @see net.fe.overworldStage.MenuContext#onSelect(java.lang.Object)
	 */
	@Override
	public void onSelect(String selectedItem) {
		// TODO Finish this
		AudioPlayer.playAudio("select");
		performAction(selectedItem);
	}
	
	public void performAction(String action) {
		if (action.equals("Wait")) {
			stage.addCmd(new WaitCommand());
			stage.send();
			unit.setMoved(true);
			stage.reset();	
		} else if (action.equals("Attack")) {
			new AttackTarget(stage, this, zone, unit).startContext();
		} else if (action.equals("Heal")){
			new HealTarget(stage, this, zone, unit).startContext();
		} else if (action.equals("Item")){	
			new ItemCmd(stage, this, unit).startContext();
		} else if (action.equals("Trade")){
			new TradeTarget(stage, this, zone, unit).startContext();
		} else if (action.equals("Rescue")){
			new RescueTarget(stage, this, zone, unit).startContext();
		} else if (action.equals("Give")){
			new GiveTarget(stage, this, zone, unit).startContext();
		} else if (action.equals("Take")){
			new TakeTarget(stage, this, zone, unit).startContext();
		} else if (action.equals("Drop")){
			new DropTarget(stage, this, zone, unit).startContext();
		} else if (action.equals("Summon")){
			new Summon(stage, this, zone, unit).startContext();
		} else {
			for (FieldSkill f : unit.getTheClass().fieldSkills) {
				if (action.equals(f.getName())) {
					f.onSelect(stage, this, zone, unit).startContext();
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see net.fe.overworldStage.MenuContext#onChange()
	 */
	public void onChange() {
		updateZones();
	}
	

	/* (non-Javadoc)
	 * @see net.fe.overworldStage.OverworldContext#onCancel()
	 */
	@Override
	public void onCancel() {
		if (fromTrade || fromTake){
			return; // You can't cancel this.
		}
		super.onCancel();
	}

	/**
	 * Update zones.
	 */
	public void updateZones() {
		stage.removeEntity(zone);
		if (menu.getSelection().equals("Attack")) {
			zone = new Zone(grid.getRange(
					new Node(unit.getXCoord(), unit.getYCoord()),
					unit.getTotalWepRange(false)), ZoneType.ATTACK_DARK);
			stage.addEntity(zone);
		} else if (menu.getSelection().equals("Heal")) {
			zone = new Zone(grid.getRange(
					new Node(unit.getXCoord(), unit.getYCoord()),
					unit.getTotalWepRange(true)), ZoneType.HEAL_DARK);
			stage.addEntity(zone);
		} else if (Arrays.asList("Trade", "Give", "Take", "Drop", "Rescue", "Summon")
				.contains(menu.getSelection())) {
			zone = new Zone(grid.getRange(
					new Node(unit.getXCoord(), unit.getYCoord()), 1),
					ZoneType.MOVE_DARK);
			stage.addEntity(zone);
		} else {
			for (FieldSkill f : unit.getTheClass().fieldSkills) {
				if (menu.getSelection().equals(f.getName())) {
					zone = f.getZone(unit, grid);
					stage.addEntity(zone);
				}
			}
		}
	}

	/**
	 * Gets the commands.
	 *
	 * @param u the u
	 * @return the commands
	 */
	public List<String> getCommands(Unit u) {
		// TODO Rescue
		List<String> list = new ArrayList<String>();
		
		boolean attack = false;
		Set<Node> range = grid.getRange(new Node(u.getXCoord(), u.getYCoord()),
				unit.getTotalWepRange(false));
		for (Node n : range) {
			Unit p = grid.getVisibleUnit(n.x, n.y);
			if (p != null && !stage.getCurrentPlayer().getParty().isAlly(p.getParty())) {
				attack = true;
				break;
			}
		}
		if (attack && !fromTake)
			list.add("Attack");

		boolean heal = false;
		range = grid.getRange(new Node(u.getXCoord(), u.getYCoord()),
				unit.getTotalWepRange(true));
		for (Node n : range) {
			Unit p = grid.getVisibleUnit(n.x, n.y);
			if (p != null && stage.getCurrentPlayer().getParty().isAlly(p.getParty())
					&& p.getHp() != p.getStats().maxHp) {
				heal = true;
				break;
			}
		}
		if (heal && !fromTake)
			list.add("Heal");

		//TODO Give command untested
		boolean trade = false;
		boolean rescue = false;
		boolean give = false;
		boolean take = false;
		boolean drop = false;
		boolean summon = false;
		range = grid.getRange(new Node(u.getXCoord(), u.getYCoord()), 1);
		for (Node n : range) {
			Unit p = grid.getVisibleUnit(n.x, n.y);
			if (p != null && stage.getCurrentPlayer().getParty().isAlly(p.getParty())) {
				trade = true;
				if(p.rescuedUnit() == null && unit.rescuedUnit() == null && unit.canRescue(p)){
					rescue = true;
				} else if (p.rescuedUnit() == null && unit.rescuedUnit() != null && 
						p.canRescue(unit.rescuedUnit())){
					give = true;
				} else if (p.rescuedUnit() != null && unit.rescuedUnit() == null &&
						unit.canRescue(p.rescuedUnit())){
					take = true;
				}
			}
			if(p == null && unit.rescuedUnit() != null && 
					grid.getTerrain(n.x, n.y).getMoveCost(
					unit.rescuedUnit().getTheClass()) < unit
					.rescuedUnit().getStats().mov){
				drop = true;
			}
			
			//summon
			if (p == null
					&& grid.getTerrain(n.x, n.y).getMoveCost(
							net.fe.unit.Class.createClass("Phantom")) <
							unit.getStats().mov && 
							unit.getTheClass().usableWeapon.contains(Weapon.Type.DARK)) {
				for (Item i : unit.getInventory()) {
					if (i instanceof RiseTome)
						summon = true;
				}
			}
			
		}
		if (trade && !fromTrade && !fromTake)
			list.add("Trade");
		if (rescue && !fromTrade && !fromTake)
			list.add("Rescue");
		if (give && !fromTrade && !fromTake)
			list.add("Give");
		if (take && !fromTrade && !fromTake)
			list.add("Take");
		if (drop && !fromTrade)
			list.add("Drop");
		if (summon)
			list.add("Summon");
		
		for (FieldSkill f : unit.getTheClass().fieldSkills) {
			if (f.allowedWithFog(unit, this.stage.grid)) {
				list.add(f.getName());
			}
		}
		
		list.add("Item");
		list.add("Wait");

		return list;
	}

	/* (non-Javadoc)
	 * @see net.fe.overworldStage.OverworldContext#onLeft()
	 */
	@Override
	public void onLeft() {
		// Nothing
	}

	/* (non-Javadoc)
	 * @see net.fe.overworldStage.OverworldContext#onRight()
	 */
	@Override
	public void onRight() {
		// Nothing
	}

}
