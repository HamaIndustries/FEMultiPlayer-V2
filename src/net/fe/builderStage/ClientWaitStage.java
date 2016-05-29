package net.fe.builderStage;

import java.util.List;
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
import chu.engine.Stage;
import chu.engine.anim.Renderer;

// TODO: Auto-generated Javadoc
/**
 * The Class ClientWaitStage.
 */
public class ClientWaitStage extends Stage {
	
	/** The start. */
	private boolean start;
	
	/** The session. */
	protected final Session session;
	
	/**
	 * Instantiates a new client wait stage.
	 *
	 * @param s the s
	 */
	public ClientWaitStage(Session s) {
		super("preparations");
		start = false;
		session = s;
	}
	
	/* (non-Javadoc)
	 * @see net.fe.builderStage.WaitStage#beginStep()
	 */
	@Override
	public void beginStep(List<Message> messages) {
		for(Message message : messages) {
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
				if (this.session.getNonSpectators().length < 2) {
					// player has left
					FEMultiplayer.disconnectGame("Player has disconnected. Exiting game.");
				}
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see net.fe.builderStage.WaitStage#endStep()
	 */
	@Override
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
	 * @see chu.engine.Stage#onStep()
	 */
	@Override
	public void onStep() {
		
	}

	/* (non-Javadoc)
	 * @see chu.engine.Stage#render()
	 */
	@Override
	public void render() {
		Renderer.drawString("default_med", "Waiting for other players...", 200, 150, 0.0f);
	}

}
