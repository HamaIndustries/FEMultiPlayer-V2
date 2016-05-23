package net.fe.overworldStage.context;

import java.util.ArrayList;

import chu.engine.anim.AudioPlayer;
import net.fe.network.command.EquipCommand;
import net.fe.network.command.AttackCommand;
import net.fe.overworldStage.*;
import net.fe.unit.*;

// TODO: Auto-generated Javadoc
/**
 * The Class AttackPreview.
 */
public class AttackPreview extends OverworldContext{
	
	/** The attacker. */
	private Unit attacker;
	
	/** The defender. */
	private Unit defender;
	
	/** The preview. */
	private BattlePreview preview;
	
	/** The weapons. */
	private ArrayList<Weapon> weapons;
	
	/** The index. */
	private int index;
	
	/**
	 * Instantiates a new attack preview.
	 *
	 * @param s the s
	 * @param prevContext the prev context
	 * @param a the a
	 * @param d the d
	 */
	public AttackPreview(ClientOverworldStage s, OverworldContext prevContext, Unit a, Unit d) {
		super(s, prevContext);
		attacker = a;
		defender = d;
		preview = new BattlePreview(ClientOverworldStage.RIGHT_AXIS - 44,
				76, a, d, Grid.getDistance(a, d));
		
	}
	
	/* (non-Javadoc)
	 * @see net.fe.overworldStage.OverworldContext#startContext()
	 */
	public void startContext(){
		super.startContext();
		stage.addEntity(preview);
		weapons = attacker.equippableWeapons(Grid.getDistance(attacker, defender));
	}

	/* (non-Javadoc)
	 * @see net.fe.overworldStage.OverworldContext#onSelect()
	 */
	@Override
	public void onSelect() {
		AudioPlayer.playAudio("select");
		stage.addCmd(new AttackCommand(new UnitIdentifier(defender)));
		stage.send();
		attacker.setMoved(true);
		cursor.setXCoord(attacker.getXCoord());
		cursor.setYCoord(attacker.getYCoord());
		stage.reset();
	}
	
	/* (non-Javadoc)
	 * @see net.fe.overworldStage.OverworldContext#cleanUp()
	 */
	public void cleanUp(){
		stage.removeEntity(preview);
	}

	/* (non-Javadoc)
	 * @see net.fe.overworldStage.OverworldContext#onUp()
	 */
	@Override
	public void onUp() {
		
	}

	/* (non-Javadoc)
	 * @see net.fe.overworldStage.OverworldContext#onDown()
	 */
	@Override
	public void onDown() {
		
	}

	/* (non-Javadoc)
	 * @see net.fe.overworldStage.OverworldContext#onLeft()
	 */
	@Override
	public void onLeft() {
		index--;
		if(index < 0){
			index += weapons.size();
		}
		equip();
	}

	/* (non-Javadoc)
	 * @see net.fe.overworldStage.OverworldContext#onRight()
	 */
	@Override
	public void onRight() {
		index = (index+1)%weapons.size();
		equip();
	}
	
	/**
	 * Equip.
	 */
	public void equip(){
		stage.addCmd(new EquipCommand(new UnitIdentifier(attacker), attacker.findItem(weapons.get(index))));
		attacker.equip(weapons.get(index));
	}

}
