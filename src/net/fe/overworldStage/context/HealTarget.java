package net.fe.overworldStage.context;

import java.util.ArrayList;
import java.util.List;

import chu.engine.Entity;
import chu.engine.anim.Renderer;
import chu.engine.anim.Sprite;
import chu.engine.anim.Transform;
import net.fe.FEResources;
import net.fe.network.command.EquipCommand;
import net.fe.network.command.HealCommand;
import net.fe.fightStage.FightStage;
import net.fe.overworldStage.Grid;
import net.fe.overworldStage.OverworldContext;
import net.fe.overworldStage.ClientOverworldStage;
import net.fe.overworldStage.OverworldStage;
import net.fe.overworldStage.SelectTargetContext;
import net.fe.overworldStage.Zone;
import net.fe.unit.ItemDisplay;
import net.fe.unit.Unit;
import net.fe.unit.UnitIdentifier;
import net.fe.unit.Weapon;

// TODO: Auto-generated Javadoc
/**
 * The Class HealTarget.
 */
public class HealTarget extends SelectTargetContext {
	
	/** The selector. */
	private StaffSelector selector;

	/**
	 * Instantiates a new heal target.
	 *
	 * @param stage the stage
	 * @param context the context
	 * @param z the z
	 * @param u the u
	 */
	public HealTarget(ClientOverworldStage stage, OverworldContext context, Zone z,
			Unit u) {
		super(stage, context, z, u, true);
		selector = new StaffSelector(ClientOverworldStage.RIGHT_AXIS - 45, 75,
				new ArrayList<Weapon>());
	}

	/* (non-Javadoc)
	 * @see net.fe.overworldStage.SelectTargetContext#validTarget(net.fe.unit.Unit)
	 */
	public boolean validTarget(Unit u){
		return super.validTarget(u) && u.getStats().maxHp != u.getHp();
	}
	
	/* (non-Javadoc)
	 * @see net.fe.overworldStage.SelectTargetContext#unitSelected(net.fe.unit.Unit)
	 */
	@Override
	public void unitSelected(Unit u) {
		stage.addCmd(new EquipCommand(new UnitIdentifier(unit), unit.findItem(selector.getSelected())));
		unit.equip(selector.getSelected());
		stage.addCmd(new HealCommand(new UnitIdentifier(u)));
		stage.setControl(false);
		stage.send();

		unit.setMoved(true);
		cursor.setXCoord(unit.getXCoord());
		cursor.setYCoord(unit.getYCoord());
		stage.reset();
	}

	/* (non-Javadoc)
	 * @see net.fe.overworldStage.SelectTargetContext#onLeft()
	 */
	public void onLeft() {
		selector.prev();
	}

	/* (non-Javadoc)
	 * @see net.fe.overworldStage.SelectTargetContext#onRight()
	 */
	public void onRight() {
		selector.next();
	}

	/* (non-Javadoc)
	 * @see net.fe.overworldStage.SelectTargetContext#updateCursor()
	 */
	public void updateCursor() {
		super.updateCursor();
		selector.setStaves(unit.equippableStaves(Grid.getDistance(unit,
				getCurrentTarget())));
		selector.x = getCurrentTarget().x + 8 - 45 - stage.camX;
		if(selector.x < 0) selector.x = 0;
		if(selector.x + 90 > 384) selector.x = 384-90;
		selector.y = getCurrentTarget().y - 26 - stage.camY;
	}

	/* (non-Javadoc)
	 * @see net.fe.overworldStage.SelectTargetContext#startContext()
	 */
	public void startContext() {
		super.startContext();
		stage.addEntity(selector);
	}

	/* (non-Javadoc)
	 * @see net.fe.overworldStage.SelectTargetContext#cleanUp()
	 */
	public void cleanUp() {
		super.cleanUp();
		stage.removeEntity(selector);
	}

}

class StaffSelector extends Entity {
	private List<Weapon> staves;
	private int index;
	private Sprite leftArrow, rightArrow;

	public StaffSelector(float x, float y, List<Weapon> staves) {
		super(x, y);
		this.staves = staves;
		renderDepth = ClientOverworldStage.MENU_DEPTH;
		rightArrow = new Sprite();
		rightArrow.addAnimation("default",
				FEResources.getTexture("gui_selectArrow"), 8, 8, 6, 6, 0.1f);
		leftArrow = new Sprite();
		leftArrow.addAnimation("default",
				FEResources.getTexture("gui_selectArrow"), 8, 8, 6, 6, 0.1f);
	}
	
	public void onStep(){
		super.onStep();
		leftArrow.update();
		rightArrow.update();
	}

	public void setStaves(List<Weapon> staves) {
		if (staves.size() != this.staves.size()) {
			index = 0;
		}
		this.staves = staves;
	}
	
	public Weapon getSelected(){
		return staves.get(index);
	}

	public void next() {
		index++;
		index %= staves.size();
	}

	public void prev() {
		index--;
		if (index < 0)
			index += staves.size();
	}

	public void render() {
		Renderer.drawRectangle(x, y, x + 90, y + 20, renderDepth,
				FightStage.BORDER_DARK);
		Renderer.drawRectangle(x+1, y+1, x + 89, y + 19, renderDepth,
				FightStage.BORDER_LIGHT);
		Renderer.drawRectangle(x+2, y+2, x + 88, y + 18, renderDepth,
				FightStage.NEUTRAL);
		new ItemDisplay((int) x + 12, (int) y + 1, staves.get(index), false).render();
		leftArrow.render(x+3, y+6, renderDepth);
		Transform t = new Transform();
		t.flipHorizontal();
		rightArrow.render(x + 87, y+6, renderDepth, t);
	}

}
