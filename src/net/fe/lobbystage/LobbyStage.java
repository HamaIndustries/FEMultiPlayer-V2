package net.fe.lobbystage;

import java.util.List;

import chu.engine.Stage;
import net.fe.Player;
import net.fe.game.Session;
import net.fe.network.Message;
import net.fe.network.message.ClientInit;
import net.fe.network.message.ReadyMessage;
import net.fe.network.message.StartPicking;
import net.fe.network.server.FEServer;

// TODO: Auto-generated Javadoc
/**
 * Version of LobbyStage used by Server without extraneous entities.
 *
 * @author Shawn
 */
public class LobbyStage extends Stage {

	/** The session. */
	protected Session session;

	/**
	 * Instantiates a new lobby stage.
	 *
	 * @param s the s
	 */
	public LobbyStage(Session s) {
		super("main");
		session = s;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see chu.engine.Stage#beginStep()
	 */
	@Override
	public void beginStep(List<Message> messages) {
		for (Message message : messages) {
			if (message instanceof ClientInit) { // Only clients will get this
				ClientInit init = (ClientInit) message;
				session = init.session;
			} else if (message instanceof ReadyMessage) {
				boolean ready = !session.getPlayer(message.getOrigin()).ready;
				session.getPlayer(message.getOrigin()).ready = ready;
				if (ready)
					session.getChatlog().add(session.getPlayer(message.getOrigin()), "Ready!");
				else
					session.getChatlog().add(session.getPlayer(message.getOrigin()), "Not ready!");
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see chu.engine.Stage#onStep()
	 */
	@Override
	public void onStep() {

	}

	/**
	 * <p>
	 * Determines whether or not players are ready to start a game. The game
	 * will start when every player assigned to a team is ready and there's at
	 * least one player in either team.
	 * </p>
	 * <p>
	 * This will automatically change the FEServer's current stage
	 * </p>
	 */
	@Override
	public void endStep() {
		/*
		 * 0 => no team has a player 1 => blue team has at least a player 2 =>
		 * red team has at least a player 3 (= 1 | 2) => both teams has at least
		 * 1 player
		 */
		byte activePlayers = 0;
		for (Player p : session.getPlayers()) {
			activePlayers |= p.getTeam().ordinal();
			if (p.getTeam().ordinal() > 0 && !p.ready)
				return;
		}
		if (activePlayers != 3)
			return;

		FEServer.getServer().broadcastMessage(new StartPicking((byte) 0));
		FEServer.getServer().allowConnections = false;
		session.getPickMode().setUpServer(session);
	}

}
