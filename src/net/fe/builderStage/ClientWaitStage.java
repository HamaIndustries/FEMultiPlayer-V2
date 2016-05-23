package net.fe.builderStage;

import net.fe.FEMultiplayer;
import net.fe.Player;
import net.fe.Session;
import net.fe.network.FEServer;
import net.fe.network.Message;
import net.fe.network.message.PartyMessage;
import net.fe.network.message.QuitMessage;
import net.fe.network.message.StartGame;
import net.fe.overworldStage.ClientOverworldStage;
import net.fe.unit.Unit;
import chu.engine.Game;
import chu.engine.anim.Renderer;

// TODO: Auto-generated Javadoc
/**
 * The Class ClientWaitStage.
 */
public class ClientWaitStage extends WaitStage {
	
	/** The start. */
	private boolean start;
	
	/**
	 * Instantiates a new client wait stage.
	 *
	 * @param s the s
	 */
	public ClientWaitStage(Session s) {
		super(s);
		start = false;
	}
	
	/* (non-Javadoc)
	 * @see net.fe.builderStage.WaitStage#init()
	 */
	protected void init() {
		
	}
	
	/* (non-Javadoc)
	 * @see net.fe.builderStage.WaitStage#beginStep()
	 */
	public void beginStep() {
		for(Message message : Game.getMessages()) {
			if(message instanceof PartyMessage) {
				PartyMessage pm = (PartyMessage)message;
				for(Player p : session.getPlayers()){ 
					if(p.getID() == message.origin) {
						p.getParty().clear();
						for(Unit u : pm.teamData)
							p.getParty().addUnit(u);
					}
				}
			}
			if(message instanceof StartGame) {
				start = true;
			}

			if(message instanceof QuitMessage) {
				// player has left
				FEMultiplayer.disconnectGame("Opponent has disconnected. Exiting game.");
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see net.fe.builderStage.WaitStage#endStep()
	 */
	public void endStep() {
		if(start) {
			for(Player p : session.getPlayers()) {
				for(Unit u : p.getParty()) {
					u.initializeEquipment();
				}
			}
			FEMultiplayer.setCurrentStage(new ClientOverworldStage(session));
		}
	}

	/* (non-Javadoc)
	 * @see chu.engine.Stage#render()
	 */
	public void render() {
		Renderer.drawString("default_med", "Waiting for other players...", 200, 150, 0.0f);
	}

}
