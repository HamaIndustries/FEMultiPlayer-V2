package net.fe.lobbystage;

import java.util.List;

import net.fe.Party;
import net.fe.Player;
import net.fe.Session;
import net.fe.builderStage.WaitStage;
import net.fe.network.Chat;
import net.fe.network.FEServer;
import net.fe.network.Message;
import net.fe.network.message.ChatMessage;
import net.fe.network.message.ClientInit;
import net.fe.network.message.JoinLobby;
import net.fe.network.message.JoinTeam;
import net.fe.network.message.QuitMessage;
import net.fe.network.message.ReadyMessage;
import net.fe.network.message.StartPicking;
import chu.engine.Game;
import chu.engine.Stage;

// TODO: Auto-generated Javadoc
/**
 * Version of LobbyStage used by Server without extraneous entities.
 *
 * @author Shawn
 */
public class LobbyStage extends Stage {
	
	/** The chat. */
	protected Chat chat;
	
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
		chat = new Chat();
	}
	
	/* (non-Javadoc)
	 * @see chu.engine.Stage#beginStep()
	 */
	@Override
	public void beginStep(List<Message> messages) {
		for(Message message : messages) {
			if(message instanceof JoinLobby) {
				JoinLobby join = (JoinLobby)message;
				session.addPlayer((byte) join.origin, join.player);
			}
			else if(message instanceof JoinTeam) {
				JoinTeam join = (JoinTeam)message;
				session.getPlayer(join.origin).setTeam(join.team);
				if(join.team == Player.TEAM_BLUE) {
					session.getPlayer(join.origin).getParty().setColor(Party.TEAM_BLUE);
				} else if(join.team == Player.TEAM_RED) {
					session.getPlayer(join.origin).getParty().setColor(Party.TEAM_RED);
				}
				session.getPlayer(join.origin).ready = false;
			}
			else if(message instanceof ClientInit) {		// Only clients will get this
				ClientInit init = (ClientInit)message;
				session = init.session;
			}
			else if(message instanceof QuitMessage) {
				QuitMessage quit = (QuitMessage)message;
				session.removePlayer(quit.origin);
			}
			else if(message instanceof ChatMessage) {
				ChatMessage chatMsg = (ChatMessage)message;
				chat.add(session.getPlayer(chatMsg.origin), chatMsg.text);
			}
			else if(message instanceof ReadyMessage) {
				boolean ready = !session.getPlayer(message.origin).ready;
				session.getPlayer(message.origin).ready = ready;
				if(ready)
					chat.add(session.getPlayer(message.origin), "Ready!");
				else
					chat.add(session.getPlayer(message.origin), "Not ready!");
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
		if(session.numPlayers() <=1 ) return;
		int activePlayers = 0;
		int team = Player.TEAM_NONE;
		for(Player p : session.getPlayers()) {
			if(team == Player.TEAM_NONE){
				team = p.getTeam();
			}else{
				if(team == p.getTeam()){
					//two players are on the same team.
					return;
				}
			}
			if(!p.isSpectator()){
				activePlayers += 1;
			}
			if(!p.ready) {
				return;
			}
		}
		//to get here, might have 1 person ready, and 1 spectator.
		//need to have 2 people ready.
		//note, this is not exhaustive, since teams are not considered
		if(activePlayers<=1){
			return;
		}
		FEServer.getServer().broadcastMessage(new StartPicking((byte)0));
		FEServer.getServer().allowConnections = false;
		session.getPickMode().setUpServer(session);
	}

}
