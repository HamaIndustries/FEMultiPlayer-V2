package net.fe.builderStage;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

import net.fe.FEMultiplayer;
import net.fe.Player;
import net.fe.Session;
import net.fe.network.FEServer;
import net.fe.network.Message;
import net.fe.network.message.PartyMessage;
import net.fe.network.message.QuitMessage;
import net.fe.network.message.StartGame;
import net.fe.overworldStage.OverworldStage;
import net.fe.unit.Unit;
import chu.engine.Game;
import chu.engine.Stage;

// TODO: Auto-generated Javadoc
/**
 * Wait for all players to select.
 *
 * @author Shawn
 */
public final class WaitStage extends Stage {
	
	/** The ready status. */
	private final HashMap<Byte, Boolean> readyStatus;
	
	/** The messages. */
	private final ArrayList<PartyMessage> messages;
	
	/** The sent start message. */
	private boolean sentStartMessage;
	
	/** The session. */
	protected final Session session;
	
	/**
	 * Instantiates a new wait stage.
	 *
	 * @param s the s
	 */
	public WaitStage(Session s) {
		super("preparations");
		session = s;
		sentStartMessage = false;
		readyStatus = new HashMap<Byte, Boolean>();
		for(Player p : session.getNonSpectators()) {
			readyStatus.put(p.getID(), false);
		}
		messages = new ArrayList<PartyMessage>();
	}
	
	/* (non-Javadoc)
	 * @see chu.engine.Stage#beginStep()
	 */
	@Override
	public void beginStep(List<Message> messages) {
		for(Message message : messages) {
			if(message instanceof PartyMessage) {
				PartyMessage pm = (PartyMessage)message;
				pm.validateTeam(
					net.fe.unit.UnitFactory::getUnit,
					net.fe.unit.Item.getAllItems(),
					session.getModifiers()
				);
				for(Player p : session.getPlayers()){ 
					if(p.getID() == message.origin) {
						p.getParty().clear();
						for(Unit u : pm.teamData)
							p.getParty().addUnit(u);
						readyStatus.put(p.getID(), true);
					}
				}
				this.messages.add(pm);
			}
			else if(message instanceof QuitMessage) {
				//player has left
				FEMultiplayer.disconnectGame("Opponent has disconnected. Exiting game.");
			}
		}
		
	}

	/* (non-Javadoc)
	 * @see chu.engine.Stage#onStep()
	 */
	@Override
	public void onStep() {
		
	}

	/* (non-Javadoc)
	 * @see chu.engine.Stage#endStep()
	 */
	@Override
	public void endStep() {
		if(!sentStartMessage) {
			for(boolean b : readyStatus.values()) {
				if(!b) return;
			}
			for(PartyMessage pm : messages) {
				FEServer.getServer().broadcastMessage(pm);
			}
			FEServer.getServer().broadcastMessage(new StartGame((byte) 0));
			for(Player p : session.getPlayers()) {
				for(Unit u : p.getParty()) {
					u.initializeEquipment();
				}
			}
			FEServer.setCurrentStage(new OverworldStage(session));
			sentStartMessage = true;
		}
	}
	
}
