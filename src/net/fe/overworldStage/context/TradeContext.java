package net.fe.overworldStage.context;

import java.util.List;

import net.fe.network.command.TradeCommand;
import net.fe.game.unit.*;
import net.fe.network.command.EquipCommand;
import chu.engine.anim.AudioPlayer;
import net.fe.overworldStage.*;

// TODO: Auto-generated Javadoc
/**
 * The Class TradeContext.
 */
public class TradeContext extends OverworldContext {
	
	/** The u2. */
	private Unit u1, u2;
	
	/** The curr. */
	private Menu<ItemDisplay> trader, tradee, curr;
	
	/** The traded. */
	private boolean traded;
	
	/** The marked. */
	private int marked;
	
	/**
	 * Instantiates a new trade context.
	 *
	 * @param s the s
	 * @param prevContext the prev context
	 * @param u1 the u1
	 * @param u2 the u2
	 */
	public TradeContext(ClientOverworldStage s, OverworldContext prevContext, Unit u1, Unit u2) {
		super(s, prevContext);
		this.u1 = u1;
		this.u2 = u2;
		trader = new TradeMenu(u1, 72, 130, false);
		tradee = new TradeMenu(u2, 199, 130, true);
		curr = trader;
		tradee.clearSelection();
		marked = -1;
	}
	
	/* (non-Javadoc)
	 * @see net.fe.overworldStage.OverworldContext#startContext()
	 */
	public void startContext(){
		super.startContext();
		stage.addEntity(trader);
		stage.addEntity(tradee);
		u1.unequip();
		u2.unequip();
	}
	
	/* (non-Javadoc)
	 * @see net.fe.overworldStage.OverworldContext#cleanUp()
	 */
	public void cleanUp(){
		super.cleanUp();
		stage.removeEntity(trader);
		stage.removeEntity(tradee);
		stage.addCmd(u1.reEquipCommand());
		stage.addCmd(u2.reEquipCommand());
		u1.reEquip();
		u2.reEquip();
		
	}

	/* (non-Javadoc)
	 * @see net.fe.overworldStage.OverworldContext#onSelect()
	 */
	@Override
	public void onSelect() {
		if(marked == -1){
			AudioPlayer.playAudio("select");
			marked = getIndex();
			curr.mark(curr.getSelectedIndex());
			curr.down();
			
			switchMenu();
			curr.setSelection(0);
		} else {
			//Trade
			AudioPlayer.playAudio("cancel");
			if(swap(marked, getIndex())) traded = true;
			marked = -1;
			trader.unmark();
			tradee.unmark();
		}
	}

	/* (non-Javadoc)
	 * @see net.fe.overworldStage.OverworldContext#onCancel()
	 */
	public void onCancel() {
		
		if(marked == -1){
			if(!traded){
				super.onCancel();
			} else {
				AudioPlayer.playAudio("cancel");
				cursor.setXCoord(u1.getXCoord());
				cursor.setYCoord(u1.getYCoord());
				new UnitMoved(stage, this, u1, true, false).startContext();
			}
		} else {
			AudioPlayer.playAudio("cancel");
			Menu<ItemDisplay> m1, m2;
			if(marked < 4){
				m1 = trader;
				m2 = tradee;
			} else {
				m2 = trader;
				m1 = tradee;
			}
			int i = m1.getMarkedIndex();
			m1.unmark();
			m1.setSelection(i);
			curr = m1;
			m1.restoreSelection();
			m2.clearSelection();
			
			marked = -1;
			
		}
		
	}

	/* (non-Javadoc)
	 * @see net.fe.overworldStage.OverworldContext#onUp()
	 */
	@Override
	public void onUp() {
		curr.up();
		AudioPlayer.playAudio("cursor2");
	}

	/* (non-Javadoc)
	 * @see net.fe.overworldStage.OverworldContext#onDown()
	 */
	@Override
	public void onDown() {
		curr.down();
		AudioPlayer.playAudio("cursor2");
	}

	/* (non-Javadoc)
	 * @see net.fe.overworldStage.OverworldContext#onLeft()
	 */
	@Override
	public void onLeft() {
		AudioPlayer.playAudio("cursor2");
		int i = curr.getSelectedIndex();
		switchMenu();
		curr.setSelection(i);
	}

	/* (non-Javadoc)
	 * @see net.fe.overworldStage.OverworldContext#onRight()
	 */
	@Override
	public void onRight() {
		AudioPlayer.playAudio("cursor2");
		int i = curr.getSelectedIndex();
		switchMenu();
		curr.setSelection(i);
	}
	
	
	/**
	 * Gets the index.
	 *
	 * @return the index
	 */
	private int getIndex(){
		int i = 0;
		if(curr == tradee){
			i+=4;
		}
		i+= curr.getSelectedIndex();
		return i;
	}
	
	/**
	 * Switch menu.
	 */
	private void switchMenu(){
		
		if(curr == trader){
			trader.clearSelection();
			tradee.restoreSelection();
			curr = tradee;
		} else {
			tradee.clearSelection();
			trader.restoreSelection();
			curr = trader;
		}
		
	}
	
	
	/**
	 * Swap.
	 *
	 * @param i1 the i1
	 * @param i2 the i2
	 * @return true, if successful
	 */
	private boolean swap(int i1, int i2){
		List<Item> inv1 = u1.getInventory();
		List<Item> inv2 = u2.getInventory();
		List<Item> from = i1 < 4? inv1:inv2;
		List<Item> to = i2 < 4? inv1:inv2;
		int fromIndex = i1%4;
		int toIndex = i2%4;
		
		boolean result = doTrade(from, to, fromIndex, toIndex);
		if(!result) return false;
		
		//Add the message
		stage.addCmd(new TradeCommand(
			new UnitIdentifier((from == inv1 ? u1 : u2)),
			fromIndex,
			new UnitIdentifier((to == inv1 ? u1 : u2)),
			toIndex
		));
		return true;
	}
	
	/**
	 * Do trade.
	 *
	 * @param from the from
	 * @param to the to
	 * @param fromIndex the from index
	 * @param toIndex the to index
	 * @return true, if successful
	 */
	public static boolean doTrade(List<Item> from, List<Item> to, int fromIndex, int toIndex){
		if(toIndex >= to.size() && fromIndex >= from.size()) return false;
		if(toIndex >= to.size()){
			to.add(from.remove(fromIndex));
		} else if (fromIndex >= from.size()){
			from.add(to.remove(toIndex));
		} else {
			Item temp = to.get(toIndex);
			to.set(toIndex, from.get(fromIndex));
			from.set(fromIndex, temp);
		}

		return true;
	}
}
