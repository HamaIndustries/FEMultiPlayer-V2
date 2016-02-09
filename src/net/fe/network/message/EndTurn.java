package net.fe.network.message;

import java.util.HashMap;

import net.fe.FEMultiplayer;
import net.fe.network.FEServer;
import net.fe.network.Message;
import net.fe.unit.Unit;
import net.fe.unit.UnitIdentifier;

// TODO: Auto-generated Javadoc
/**
 * The Class EndTurn.
 */
public class EndTurn extends Message {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 390213251720606794L;
	
	/** The unit hp. */
	private HashMap<UnitIdentifier, Integer> unitHp;
	
	/**
	 * Instantiates a new end turn.
	 */
	public EndTurn(){
		unitHp = new HashMap<UnitIdentifier, Integer>();
		for(Unit u: FEMultiplayer.getLocalPlayer().getParty()){
			unitHp.put(new UnitIdentifier(u), u.getHp());
		}
	}
	
	/**
	 * Check hp.
	 *
	 * @param server the server
	 */
	public void checkHp(boolean server){
		if(server){
			for(UnitIdentifier u: unitHp.keySet()){
				if(FEServer.getUnit(u).getHp() != unitHp.get(u)){
					throw new RuntimeException("Desynced HP: " + u.name);
				}
			}
		} else {
			for(UnitIdentifier u: unitHp.keySet()){
				if(FEMultiplayer.getUnit(u).getHp() != unitHp.get(u)){
					throw new RuntimeException("Desynced HP: " + u.name);
				}
			}
		}
	}
}
